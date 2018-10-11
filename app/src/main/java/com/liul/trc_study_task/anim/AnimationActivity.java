package com.liul.trc_study_task.anim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liul.trc_study_task.R;
import com.liul.trc_study_task.customview.CircleView;

public class AnimationActivity extends AppCompatActivity {

    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        circleView = (CircleView)findViewById(R.id.circleView);
        
    }
}
