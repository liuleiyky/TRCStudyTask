package com.liul.trc_study_task.datepick;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liul.trc_study_task.Global;
import com.liul.trc_study_task.R;

public class Test2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Button btnFinish=(Button)findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra(Global.EXTRA_KEY, "我是Test2的数据");
                setResult(Activity.RESULT_OK,intent);

                finish();
            }
        });
    }
}
