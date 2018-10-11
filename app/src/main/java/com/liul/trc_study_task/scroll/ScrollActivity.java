package com.liul.trc_study_task.scroll;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.liul.trc_study_task.R;

import java.util.ArrayList;

public class ScrollActivity extends AppCompatActivity {
    private Button btnScrollTo;
    private Button btnScrollBy;
    private ScrollerView scrollerView;
    private LinearLayout linearLayout;
    private HorizontalScrollViewEx horizontalScrollViewEx;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        btnScrollTo = (Button)findViewById(R.id.btnScrollTo);

//        btnScrollBy = (Button)findViewById(R.id.btnScrollBy);
//        scrollerView = (ScrollerView)findViewById(R.id.scrollerView);
//
//        btnScrollTo.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int left=btnScrollTo.getLeft();
//                int top=btnScrollTo.getTop();
//                int right=btnScrollTo.getRight();
//                int bottom=btnScrollTo.getBottom();
//                float x=btnScrollTo.getX();
//                float y=btnScrollTo.getY();
//                float translationX = btnScrollTo.getTranslationX();
//                float translationY = btnScrollTo.getTranslationY();
//                Log.d("btnLocation",""+left+","+top+","+right+","+bottom+":"+x+","+y+","+translationX+","+translationY);
//
//                ObjectAnimator.ofFloat(btnScrollTo,"translationX",left).setDuration(1000).start();
//                left=btnScrollTo.getLeft();
//                top=btnScrollTo.getTop();
//                right=btnScrollTo.getRight();
//                bottom=btnScrollTo.getBottom();
//                x=btnScrollTo.getX();
//                y=btnScrollTo.getY();
//                translationX = btnScrollTo.getTranslationX();
//                translationY = btnScrollTo.getTranslationY();
//                Log.d("btnLocation",""+left+","+top+","+right+","+bottom+":"+x+","+y+","+translationX+","+translationY);
//            }
//        });
//
//        btnScrollBy.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        scrollerView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                scrollerView.smoothScrollTo(-500,0);
//            }
//        });

        initView();

    }

    private void initView() {
        inflater = getLayoutInflater();
        horizontalScrollViewEx = (HorizontalScrollViewEx)findViewById(R.id.horizontalScrollViewEx);
        int screenWidth=getWindowManager().getDefaultDisplay().getWidth();

        for(int i=0;i<3;i++){
            View layout= inflater.inflate(R.layout.content_layout,horizontalScrollViewEx,false);
//            LinearLayout linearLayout=layout.findViewById(R.id.layout);
            layout.getLayoutParams().width=screenWidth;
            TextView textView=(TextView)layout.findViewById(R.id.title);
            textView.setText("page "+(i+1));
            layout.setBackgroundColor(Color.rgb(255/(i+1),255/(i+1),0));
            createList(layout);
            horizontalScrollViewEx.addView(layout);
        }

    }

    private void createList(View layout){
        ListView list=(ListView)layout.findViewById(R.id.list);
//        ListViewEx list=(ListViewEx)layout.findViewById(R.id.listViewEx);
//        list.setParentView(horizontalScrollViewEx);
        ArrayList<String> datas=new ArrayList<String>();
        for(int i=0;i<50;i++){
            datas.add("name "+i);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datas);
        list.setAdapter(adapter);
    }


}
