package com.liul.trc_study_task.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.liul.trc_study_task.R;

public class OkhttpActivity extends AppCompatActivity {

    private static final String TAG = "OkhttpActivity";
    private Button btnJsonToObject;
    private Button btnObjectToJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        btnJsonToObject = (Button)findViewById(R.id.btnJsonToObject);
        btnObjectToJson = (Button)findViewById(R.id.btnObjectToJson);
            
        btnJsonToObject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Person person=new Gson().fromJson("{\"age\":25,\"name\":\"张三\",\"sex\":\"男\",\"interest\":\"篮球\"}",Person.class);
                Person person=new Gson().fromJson("{\"age\":25,\"name\":\"张三\",\"sex\":\"男\",\"interest\":\"篮球\"}",Person.class);
                Log.d(TAG, new Gson().toJson(person));
            }
        });

        btnObjectToJson.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Person person=new Person("张三",25,"男");
                person.setInterest(new String[]{"篮球","足球","乒乓球"});
                person.setStudent(new Student("武汉大学",98));
                Student student1=new Student("华科",97);
                Student student2=new Student("地大",96);
                Student[] students=new Student[2];
                students[0]=student1;
                students[1]=student2;

                person.setStudents(students);
                Log.d(TAG, new Gson().toJson(person));
            }
        });
    }
}
