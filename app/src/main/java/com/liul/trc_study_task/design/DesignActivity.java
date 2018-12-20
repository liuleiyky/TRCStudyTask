package com.liul.trc_study_task.design;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liul.trc_study_task.R;

import java.util.ArrayList;
import java.util.List;

public class DesignActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> list;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        list = new ArrayList<String>();
        for(int i=0;i<50;i++){
            list.add("Item"+i);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager llLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = llLayoutManager.findFirstVisibleItemPosition();
                if(list.get(firstVisibleItemPosition).equals("Item20")){
                    llLayoutManager.scrollToPositionWithOffset(firstVisibleItemPosition,0);
                    llLayoutManager.setStackFromEnd(false);
                }

            }
        });
    }

    private RecyclerView.Adapter<TestViewHolder> adapter=new RecyclerView.Adapter<TestViewHolder>() {
//        private LayoutInflater layoutInflater=LayoutInflater.from(DesignActivity.this);
        @NonNull
        @Override
        public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView=new TextView(DesignActivity.this);
            textView.setTextSize(30);
            return new TestViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    };
    private class TestViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public TestViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView;
        }
    }

}
