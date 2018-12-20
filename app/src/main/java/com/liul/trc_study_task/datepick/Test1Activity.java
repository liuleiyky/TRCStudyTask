package com.liul.trc_study_task.datepick;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.liul.trc_study_task.Global;
import com.liul.trc_study_task.R;

public class Test1Activity extends AppCompatActivity {
    public static final int REQUEST_CODE=00001;
    private Button btnSkip;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        btnSkip = (Button)findViewById(R.id.btnSkip);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSkip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Test1Activity.this,Test2Activity.class);
                startActivityForResult(intent, Global.REQUEST_CODE_TEST2);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                setResult(Activity.RESULT_OK);

                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Global.REQUEST_CODE_TEST2){
            Log.d("resultCode",""+resultCode);
        }
    }
}
