package com.liul.trc_study_task.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liul.trc_study_task.R;

import java.lang.ref.WeakReference;

public class BookShowActivity extends AppCompatActivity/*BaseActivity<IBookView,BookPresenter> implements IBookView*/{

    private TextView tvBook;
    private Button btnQuery;
    private Button btnCancel;
    private BookPresenter bookPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_show);

        tvBook = (TextView)findViewById(R.id.tvBook);
        btnQuery = (Button)findViewById(R.id.btnQuery);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        bookPresenter = new BookPresenter();
        btnQuery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                bookPresenter.queryBookInfo(new IBookView() {
                    @Override
                    public void show(String bookInfo) {
                        tvBook.setText(bookInfo);
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Log.d("BookShowActivity",""+presenter.getAttachView());
                bookPresenter.cancelQueryBookInfo();
            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
