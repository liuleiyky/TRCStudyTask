package com.liul.trc_study_task.datepick;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.liul.trc_study_task.Global;
import com.liul.trc_study_task.R;

public class DatePickDialogActivity extends AppCompatActivity {

    private Button btnDatePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick_dialog);

        btnDatePick = (Button) findViewById(R.id.btnDatePick);
        btnDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDatePickDialog();
                Intent intent=new Intent(DatePickDialogActivity.this,Test1Activity.class);
                startActivityForResult(intent, Global.REQUEST_CODE_TEST1);
            }
        });

//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        mBluetoothAdapter.disable();
//
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(false);

        TextView textView=(TextView)findViewById(R.id.textView);
        SpannableString spannableString = new SpannableString("当前开具文书为违法行为处理通知书【是】确认操作，【否】退出当前界面");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0,spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        textView.setText(Html.fromHtml("当前开具文书为<h4><font color='red'>违法行为处理通知书</font></h4>【是】确认操作，【否】退出当前界面"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Global.REQUEST_CODE_TEST1){
            Log.d("resultCode",""+resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showDatePickDialog() {
        Dialog dialog=new Dialog(this);
        View dateTimePickerView = LayoutInflater.from(this).inflate(R.layout.date_and_time_pick_dialog, null);
        DatePicker datePicker=(DatePicker)dateTimePickerView.findViewById(R.id.datePicker);
        TimePicker timePicker=(TimePicker)dateTimePickerView.findViewById(R.id.timePicker);

        datePicker.setSpinnersShown(true);
        datePicker.setCalendarViewShown(false);
        datePicker.init(2018, 10, 29, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });
        dialog.setContentView(dateTimePickerView);
        dialog.show();

//        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//            }
//        },2018,10,29);
//        datePickerDialog.show();

        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        },9,12,true);
        timePickerDialog.show();
    }
}
