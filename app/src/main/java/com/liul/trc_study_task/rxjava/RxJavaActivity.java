package com.liul.trc_study_task.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liul.trc_study_task.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {

    private static final String TAG ="RxJavaActivity" ;
    private Button btnRxJava;
    private Thread mainThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        mainThread = Thread.currentThread();

        btnRxJava = (Button)findViewById(R.id.btnRxJava);
        btnRxJava.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Observable.create(new Observable.OnSubscribe<String>() {
//                    @Override
//                    public void call(Subscriber<? super String> subscriber) {
//                        subscriber.onNext("测试1");
//                        subscriber.onCompleted();
//                    }
//                }).subscribeOn(Schedulers.io())
//                  .observeOn(AndroidSchedulers.mainThread())
//                  .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Toast.makeText(RxJavaActivity.this,"完成",Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Toast.makeText(RxJavaActivity.this,s,Toast.LENGTH_LONG).show();
//                    }
//                });

                //打印一组学生的名字
                List<Course> list1=new ArrayList<>();
                Student student1=new Student();
                student1.setName("张三");
                list1.add(new Course("英语"));
                student1.setCourseList(list1);

                List<Course> list2=new ArrayList<>();
                Student student2=new Student();
                student2.setName("李四");
                list2.add(new Course("英语"));
                list2.add(new Course("数学"));
                student2.setCourseList(list2);

                List<Course> list3=new ArrayList<>();
                Student student3=new Student();
                student3.setName("王五");
                list3.add(new Course("英语"));
                list3.add(new Course("数学"));
                list3.add(new Course("语文"));
                student3.setCourseList(list3);

                Student[] students=new Student[]{student1,student2,student3};

//                Observable.from(students)
//                    .subscribe(new Action1<Student>() {
//                        @Override
//                        public void call(Student student) {
//                            Log.d(TAG, student.getName());
//                        }
//                    });

                //打印每个学生所需要修的选修课的名称（每个学生有多个选修课）
                Observable.from(students)
                    .flatMap(new Func1<Student, Observable<Course>>() {
                        @Override
                        public Observable<Course> call(Student student) {
//                            Log.d(TAG, "flatMap");
                            Log.d(TAG, new Gson().toJson(student.getCourseList()));
                            return Observable.from(student.getCourseList());
                        }
                    })
                    .subscribe(new Subscriber<Course>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Course course) {
//                            Log.d(TAG, "onNext");
                        }
                    });

                Observable.create(new Observable.OnSubscribe<Student>() {
                    @Override
                    public void call(Subscriber<? super Student> subscriber) {

                    }
                }).subscribeOn(Schedulers.io())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {

                            }
                        });

            }
        });
    }

}
