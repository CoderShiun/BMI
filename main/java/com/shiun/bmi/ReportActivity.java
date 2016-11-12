package com.shiun.bmi;

//import android.icu.text.DecimalFormat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ReportActivity extends AppCompatActivity {

    //宣告物件
    private Button button_back;
    private TextView show_result, show_suggest;
    private double BMI;
    //Log標籤
    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initViews();
        showResults();
        setListensers();
    }

    //將宣告的物件和實體化介面(View)做結合
    private void initViews(){
        //參數一:標籤,也可用目前的Activity名稱來作爲記錄標籤(this.toString()), 二:欲記錄的資訊
        //.v:詳細資訊, .d除錯資訊, .i:通知資訊, .w警告資訊, .e錯誤資訊
        Log.d(TAG, "init Views");

        button_back = (Button)findViewById(R.id.report_back);
        show_result = (TextView)findViewById(R.id.result);
        show_suggest = (TextView)findViewById(R.id.suggest);
    }

    private void showResults(){
        //必須去掉import android.icu.ext.DecimalFormat 再加上 import java.text.DecimalFormat
        DecimalFormat nf = new DecimalFormat("0.00");

        //用getExtras函數取得附加在Intent上的bundle物件
        Bundle bundle = this.getIntent().getExtras();
        double height = Double.parseDouble(bundle.getString("KEY_HEIGHT"))/100;
        double weight = Double.parseDouble(bundle.getString("KEY_WEIGHT"));
        BMI = weight / (height * height);
        show_result.setText(getString(R.string.bmi_result) + nf.format(BMI));

        //Give health advice
        if (BMI > 25) {
            showNotification(BMI);
            show_suggest.setText(R.string.advice_heavy);
        } else if (BMI < 20) {
            show_suggest.setText(R.string.advice_light);
        } else {
            show_suggest.setText(R.string.advice_average);
        }
    }

    //Listen for button clicks
    private void setListensers(){
        Log.d(TAG, "set Listeners");
        button_back.setOnClickListener(backtoMAin);
    }

    private Button.OnClickListener backtoMAin = new Button.OnClickListener(){
        public void onClick(View view){
            DecimalFormat nf = new DecimalFormat("0.00");

            Bundle bundle = new Bundle();
            //第一位是識別符號
            bundle.putString("BMI", nf.format(BMI));
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);

            //close this Activity
            ReportActivity.this.finish();
        }
    };

    protected void showNotification(double BMI){
        //先宣告一個NotificationManager的物件, 來管理整個與訊息提醒相關的事物
        //若改成ALARM_SERVICE, 就可以取得負責管理鬧鐘的AlarmManager物件
        NotificationManager barManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //PendingIntent的作用是將Intent包裝起來, 讓開發者可以指定這則Intent的執行時機
        //getActivity方法讓使用者點選狀態欄中的訊息時, 跳回預設的MainActivity
        //第三個參數是定義要呼叫的Intent爲何, 如果把參數改成null, 則不會做任何動作
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Ticker:跳出的訊息. ContentTitle:大標題. ContentText:顯示的訊息內容.
        //這裡的icon是使用系統內建的狀態圖示
        //ContentIntent:要做的動作(contentIntent函數寫在上面)
        Notification.Builder barMsg = new Notification.Builder(this)
                .setTicker("Oh no, you are too heavy!")
                .setContentTitle("Your BMI is too high!")
                .setContentText("Notification for you")
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentIntent(contentIntent);

        /*
        //再宣告一個Notification的物件, 用來放顯示的訊息內容
        //第三個參數是Notification傳送的時間, currentTimeMillis是立刻顯示這則訊息
        Notification bar Msg = new Notification(
                R.mipmap.ic_launcher,
                "Oh no, you are too heavy!",
                System.currentTimeMillis());
        */

        /*
        //使用setLatestEventInfo, 來加入狀態欄中的詳細訊息
        //第一個參數傳入當前的Activity物件本身以供參照, 第二是狀態欄中顯示的主題, 第三是狀態欄中顯示的說明
        //第四個是接受PendingIntent類別的傳入參數, 這邊指定當按下狀態欄中的該訊息時, 去執行某些功能
        barMsg.setLatestEventInfo(
                ReportActivity.this,
                "Your BMI is too high!",
                "Notification for someone",
                contentIntent);
        */

        //最後使用NotificationManager類型的notify方法, 將barMsg物件傳送給Android框架, 將訊息顯示在狀態欄上
        barManager.notify(0, barMsg.build());

    }

}
