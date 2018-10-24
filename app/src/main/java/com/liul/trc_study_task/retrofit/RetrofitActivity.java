package com.liul.trc_study_task.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liul.trc_study_task.R;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("www.baidu.com")
                .build();
        APi aPi = retrofit.create(APi.class);
//        Call<News> news = aPi.getNews("1", "10");
        aPi.getNews("1", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<News>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(News news) {

                    }
                });

    }
}
