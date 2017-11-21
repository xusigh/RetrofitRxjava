package baseframes.base;

import android.app.Application;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import baseframes.baselibrary.crashlib.callback.ReinstateCallback;
import baseframes.baselibrary.crashlib.rulers.Reinstate;

/**
 * Created by zhanghs on 2017/11/20/020.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //crash框架
        Reinstate.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .callback(new MyCrashCallback())
                .init(this);
        MyCrashHandler.register();
        //日志框架
        Logger.addLogAdapter(new AndroidLogAdapter());


    }
    static final class MyCrashCallback implements ReinstateCallback {
        @Override
        public void stackTrace(String exceptionMessage) {
            Log.e("zxy", "exceptionMessage:" + exceptionMessage);
        }

        @Override
        public void cause(String cause) {
            Log.e("zxy", "cause:" + cause);
        }

        @Override
        public void exception(String exceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
            Log.e("zxy", "exceptionClassName:" + exceptionType);
            Log.e("zxy", "throwClassName:" + throwClassName);
            Log.e("zxy", "throwMethodName:" + throwMethodName);
            Log.e("zxy", "throwLineNumber:" + throwLineNumber);
        }

        @Override
        public void throwable(Throwable throwable) {

        }
    }
}
