package baseframes.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import baseframes.base.rxtext.Scort;
import baseframes.base.rxtext.Student;
import baseframes.baselibrary.api.BaseObserver;
import baseframes.baselibrary.api.HttpManager;
import baseframes.baselibrary.basebean.Subject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        textView.setOnClickListener( v-> System.out.println("sdaaaaaaaa"));
        testRx();
    }
    public void testRx(){
        List<Student> list=new ArrayList<>();
        Student student=new Student();
        List<Scort> scos=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Scort sc=new Scort();
            sc.setName("name1"+i);
            scos.add(sc);
        }
        student.setName("zhs1");
        student.setScorts(scos);

        Student student2=new Student();
        List<Scort> scos2=new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            Scort sc=new Scort();
            sc.setName("name2"+j);
            scos2.add(sc);
        }
        student2.setName("zhs2");
        student2.setScorts(scos2);

        Student student3=new Student();
        List<Scort> scos3=new ArrayList<>();
        for (int h = 0; h < 5; h++) {
            Scort sc=new Scort();
            sc.setName("name"+h);
            scos3.add(sc);
        }
        student3.setName("zhs3");
        student3.setScorts(scos3);

        list.add(student);
        list.add(student2);
        list.add(student3);

        //观察者
        /**
         * 创建的方式 可以有 new 或者直接定义Action0 1
         * observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
         * 0是无参一般用作 complete
         */
     /*   Subscriber<Scort> sub=new Subscriber<Scort>() {

            @Override
            public void onCompleted() {
                System.out.println("complete                    ==================================");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("ereeeeeeeeeeeeeeeeeeeeee");
            }

            @Override
            public void onNext(Scort scort) {
                System.out.println(scort.getName()+"          ==============================");
            }
        };*/
        //被观察者

        /**
         * 创建的方式有很多种，create from just
         */
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//            }
//        });
       // .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
        // .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
  /*      Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
        Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
        Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，
        区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
        不要把计算工作放在 io() 中，可以避免创建不必要的线程。
        computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，
        例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，
        否则 I/O 操作的等待时间会浪费 CPU。
        另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
        */
       /* Observable.from(list).flatMap(new Func1<Student, Observable<Scort>>() {
            @Override
            public Observable<Scort> call(Student student) {
                return Observable.from(student.getScorts());
            }
        }).subscribe(sub);*/
        // RX2.0 出现了Flowable （支持背压）与Observable一样，出现了Consumer 与Action1 类似
     /*   Subscriber<Scort> subscriber=new Subscriber<Scort>() {

            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Scort scort) {
                System.out.println(scort.getName()+"================================");
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };*/
        Consumer<Scort> consumer=new Consumer<Scort>() {
            @Override
            public void accept(Scort scort) throws Exception {
                System.out.println("========================       "+scort.getName());
            }
        };

        Flowable.fromIterable(list).flatMap(new Function<Student, Publisher<Scort>>() {
            @Override
            public Publisher<Scort> apply(Student student) throws Exception {
                return Flowable.fromIterable(student.getScorts());
            }
        }).subscribe(consumer);

        HttpManager.init().getMovie(1, 50, new BaseObserver<List<Subject>>(this) {
            @Override
            protected void onHandleSuccess(List<Subject> subjects) {
                System.out.println(subjects.get(2).toString());
            }
        });
    }
}
