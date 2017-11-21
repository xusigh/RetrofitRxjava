package baseframes.baselibrary.crashlib.rulers;

import android.content.Intent;
import android.text.TextUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

import baseframes.baselibrary.crashlib.callback.ReinstateCallback;
import baseframes.baselibrary.crashlib.utils.DefaultHandlerUtil;
import baseframes.baselibrary.crashlib.utils.ReinstateSharedPrefsUtil;
import baseframes.baselibrary.crashlib.utils.ReinstateSilentSharedPrefsUtil;
import baseframes.baselibrary.crashlib.utils.ReinstateUtil;

/**
 * Created by zhengxiaoyong on 16/8/26.
 * 对没有捕捉的异常 进行处理的类
 */
final class ReinstateHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private ReinstateCallback mCallback;

    private ReinstateStore.ExceptionData mExceptionData;

    private String mStackTrace;

    private String mCause;

    private ReinstateHandler(Thread.UncaughtExceptionHandler defHandler) {
        mDefaultUncaughtExceptionHandler = defHandler;
    }

    static ReinstateHandler newInstance(Thread.UncaughtExceptionHandler defHandler) {
        return new ReinstateHandler(defHandler);
    }

    @Override
    public synchronized void uncaughtException(Thread t, Throwable e) {
        //是否显示crash的也页面
        if (baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isRecoverEnabled()) {
            //是否默认回复，不显示页面
            if (baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isSilentEnabled()) {
                ReinstateSilentSharedPrefsUtil.recordCrashData();
            } else {
                ReinstateSharedPrefsUtil.recordCrashData();
            }
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();

        String stackTrace = sw.toString();
        String cause = e.getMessage();
        Throwable rootTr = e;
        while (e.getCause() != null) {
            e = e.getCause();
            if (e.getStackTrace() != null && e.getStackTrace().length > 0)
                rootTr = e;
            String msg = e.getMessage();
            if (!TextUtils.isEmpty(msg))
                cause = msg;
        }

        String exceptionType = rootTr.getClass().getName();

        String throwClassName;
        String throwMethodName;
        int throwLineNumber;

        if (rootTr.getStackTrace().length > 0) {
            StackTraceElement trace = rootTr.getStackTrace()[0];
            throwClassName = trace.getClassName();
            throwMethodName = trace.getMethodName();
            throwLineNumber = trace.getLineNumber();
        } else {
            throwClassName = "unknown";
            throwMethodName = "unknown";
            throwLineNumber = 0;
        }

        mExceptionData = ReinstateStore.ExceptionData.newInstance()
                .type(exceptionType)
                .className(throwClassName)
                .methodName(throwMethodName)
                .lineNumber(throwLineNumber);

        mStackTrace = stackTrace;
        mCause = cause;

        if (mCallback != null) {
            mCallback.stackTrace(stackTrace);
            mCallback.cause(cause);
            mCallback.exception(exceptionType, throwClassName, throwMethodName, throwLineNumber);
            mCallback.throwable(e);
        }

        if (!DefaultHandlerUtil.isSystemDefaultUncaughtExceptionHandler(mDefaultUncaughtExceptionHandler)) {
            if (mDefaultUncaughtExceptionHandler == null) {
                killProcess();
                return;
            }
            recover();
            mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
        } else {
            recover();
            killProcess();
        }

    }

    ReinstateHandler setCallback(ReinstateCallback callback) {
        mCallback = callback;
        return this;
    }

    private void recover() {
        if (!baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isRecoverEnabled())
            return;

        if (ReinstateUtil.isAppInBackground(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext())
                && !baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isRecoverInBackground()) {
            killProcess();
            return;
        }

        if (baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isSilentEnabled()) {
            startRecoverService();
        } else {
            startRecoverActivity();
        }
    }

    private void startRecoverActivity() {
        Intent intent = new Intent();
        intent.setClass(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext(), ReinstateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        if (ReinstateStore.getInstance().getIntent() != null)
            intent.putExtra(ReinstateStore.RECOVERY_INTENT, ReinstateStore.getInstance().getIntent());
        if (!ReinstateStore.getInstance().getIntents().isEmpty())
            intent.putParcelableArrayListExtra(ReinstateStore.RECOVERY_INTENTS, ReinstateStore.getInstance().getIntents());
        intent.putExtra(ReinstateStore.RECOVERY_STACK, baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isRecoverStack());
        intent.putExtra(ReinstateStore.IS_DEBUG, baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().isDebug());
        if (mExceptionData != null)
            intent.putExtra(ReinstateStore.EXCEPTION_DATA, mExceptionData);
        intent.putExtra(ReinstateStore.STACK_TRACE, String.valueOf(mStackTrace));
        intent.putExtra(ReinstateStore.EXCEPTION_CAUSE, String.valueOf(mCause));
        baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext().startActivity(intent);
    }

    private void startRecoverService() {
        Intent intent = new Intent();
        intent.setClass(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext(), ReinstateService.class);
        if (ReinstateStore.getInstance().getIntent() != null)
            intent.putExtra(ReinstateStore.RECOVERY_INTENT, ReinstateStore.getInstance().getIntent());
        if (!ReinstateStore.getInstance().getIntents().isEmpty())
            intent.putParcelableArrayListExtra(ReinstateStore.RECOVERY_INTENTS, ReinstateStore.getInstance().getIntents());
        intent.putExtra(ReinstateService.RECOVERY_SILENT_MODE_VALUE, baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getSilentMode().getValue());
        ReinstateService.start(baseframes.baselibrary.crashlib.rulers.Reinstate.getInstance().getContext(), intent);
    }

    void register() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}
