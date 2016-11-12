package com.shiun.bmi;

//import android.icu.text.DecimalFormat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Button button_calc;
    private EditText num_height;
    private EditText num_weight;
    private TextView show_result;
    private TextView show_suggestion;
    private TextView show_previous;
    //指定一個呼叫這個Acitvity的識別碼(數字)
    private static final int ACTIVITY_REPORT = 1000;
    //Log標籤->目前的Activity
    private static final String TAG = MainActivity.class.getSimpleName();
    //public static final String PREF = "BMI_PREF";
    //public static final String PREF_HEIGHT = "BMI_HEIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加入一個Log的記錄點
        //參數一:標籤,也可用目前的Activity名稱來作爲記錄標籤(this.toString()), 二:欲記錄的資訊
        //.v:詳細資訊, .d除錯資訊, .i:通知資訊, .w警告資訊, .e錯誤資訊
        Log.v(TAG,"onCreate");
        setContentView(R.layout.activity_main);

        initViews();

        //呼叫setListeners
        setListeners();

        //Button button = (Button)findViewById(R.id.button_submit);
        //button.setOnClickListener(calcBMI);
    }

    private void initViews(){
        //Log.d(TAG, "init Views");
        //從資源檔取得對應的介面元件實體
        button_calc = (Button)findViewById(R.id.button_submit);
        num_height = (EditText)findViewById(R.id.editText_height);
        num_weight = (EditText)findViewById(R.id.editText_weight);
        show_result = (TextView)findViewById(R.id.text_result);
        show_suggestion = (TextView)findViewById(R.id.text_suggestion);
        show_previous = (TextView)findViewById(R.id.text_previoustext);
    }

    private void setListeners(){
        button_calc.setOnClickListener(calcBMI);
    }

    private View.OnClickListener calcBMI = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //多執行緒;將比較花時間的計算放到不同的執行緒,來避免ANR(應用程式不回應)問題
            new BmiCalcTask().execute();

            //必須去掉import android.ictu.ext.DecimalFormat 再加上 import java.text.DecimalFormat
            //DecimalFormat df = new DecimalFormat("0.00");

            //EditText fieldheight = (EditText)findViewById(R.id.editText_height);
            //EditText fieldweight = (EditText)findViewById(R.id.editText_weight);

            //錯誤處理(try...catch)
            /*
            try {
                double height =
                        Double.parseDouble(num_height.getText().toString()) / 100;
                double weight =
                        Double.parseDouble(num_weight.getText().toString());
                double BMI = weight / (height + height);

                //TextView result = (TextView)findViewById(R.id.text_result);
                // Present result
                show_result.setText(df.format(BMI));

                //Give health advice
                //TextView suggestion = (TextView)findViewById(R.id.text_suggestion);
                if (BMI > 25) {
                    show_suggestion.setText(R.string.advice_heavy);
                } else if (BMI < 20) {
                    show_suggestion.setText(R.string.advice_light);
                } else {
                    show_suggestion.setText(R.string.advice_average);
                }
            } catch (Exception obj) {
                Toast.makeText(MainActivity.this, R.string.input_error, Toast.LENGTH_SHORT).show();
            }
            */


            //Switch to report page (顯式呼叫, 適合用在同一個應用程式中的呼叫)
            /*
            Intent intent = new Intent();
            //從目前的MainActivity頁面, 前往ReportActivity頁面
            intent.setClass(MainActivity.this, ReportActivity.class);
            startActivity(intent);
            */

            //隱式呼叫(需在Manifest中加入intent-filter判斷, 可用來呼叫其他應用程式的Activity)
            /*
            Intent intent = new Intent();
            intent.setAction("bmi.action.report");
            startActivity(intent);
            */


            //Switch to report page
            Intent intent = new Intent();
            //指定 從目前的MainActivity頁面, 到ReportActivity頁面
            intent.setClass(MainActivity.this, ReportActivity.class);
            //附加在Intent上的訊息都儲存在bundle物件實體中
            Bundle bundle = new Bundle();
            //第一位是識別符號, 第二位是不是只能攜帶string, 也可以是int形態
            bundle.putString("KEY_HEIGHT", num_height.getText().toString());
            bundle.putString("KEY_WEIGHT", num_weight.getText().toString());
            //將bundle物件實體附加在intent物件實體上, 隨着Intent送出而送出
            intent.putExtras(bundle);
            startActivity(intent);

            /*startActivity(intent), 該函數與onActivityResult函數是共生關係,
              startActivityForResult負責呼叫其他Activity;
              傳入一個intent,並指定一個呼叫這個Activity的識別碼,並傳送intent給這個Activity來處理*/
            startActivityForResult(intent, ACTIVITY_REPORT);
        }
    };

    private class BmiCalcTask extends AsyncTask<Void, Void, Void>{
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        Double BMI, height, weight;

        //當execute方法被呼叫,AsyncTask會先在onPreExecute方法中將從UI執行緒中讀到的身高體重儲存到區域變量中
        @Override
        protected void onPreExecute(){
            //TODO Auto-generated method stub
            super.onPreExecute();

            //建立一個提示框,提示使用者目前正在計算
            Dialog.setMessage("calc.........");
            Dialog.show();

            height = Double.parseDouble(num_height.getText().toString())/100;
            weight = Double.parseDouble(num_weight.getText().toString());
        }

        //使用這些區域變量來做計算,計算結果也儲存在區域變量中
        //之所以會用區域變量,是因爲包在doInBackground中的程式碼都會在另外一個執行緒中處理,
        //所以在doInBackground中不能直接使用所有和UI有關的呼叫
        @Override
        protected Void doInBackground(Void... arg0){
            //TODO Auto-generated method stub
            BMI = weight/(height*height);
            return null;
        }

        //從區域變量中讀取結果,並顯示到畫面上
        @Override
        protected void onPostExecute(Void unused){
            //TODO Auto-generated method stub
            //計算完畢,移除提示框
            Dialog.dismiss();

            DecimalFormat nf = new DecimalFormat("0.00");
            show_result.setText(getText(R.string.bmi_result)+nf.format(BMI));

            //Give health advice
            if (BMI > 25) {
                show_suggestion.setText(R.string.advice_heavy);
            } else if (BMI < 20) {
                show_suggestion.setText(R.string.advice_light);
            } else {
                show_suggestion.setText(R.string.advice_average);
            }
        }
    }

    //對話框
    private void openOptionsDialog(){
        /*
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("About Android BMI");
        dialog.setMessage("Android BMI Calculate");
        dialog.show();
        */
        //用以下匿名的方法改寫, 以節省記憶體
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                //在對話框中加入按鈕
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNeutralButton(R.string.homepage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //parse不接受資源識別符號形態, 所以要轉成string
                        Uri uri = Uri.parse(getString(R.string.homepage_add));
                        //Intent(動作,內容);ACTION_VIEW是開啓對應的程式以檢視內容的資料
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .show();

        //用Toast的方式
        /*Toast popup = Toast.makeText(MainActivity.this, R.string.app_name, Toast.LENGTH_SHORT);
        popup.show();*/
        //將以上方法改寫成匿名方式, 以節省記憶體
        Toast.makeText(MainActivity.this, R.string.app_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    //onCreateOptionsMenu是選單列的主體
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //可按ctrl+o 選onOptionsItemSelected,此method是處理所有選項的主體
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_about:
                //openOptionsDialog();

                //將上述的開啓對話框改爲前往偏好設定頁面
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(MainActivity.this, Pref.class);
                startActivity(intent);

                //openOptionsMenu();
                break;
            case R.id.action_close:
                //finish();
                Intent intent_history = new Intent(Intent.ACTION_VIEW);
                intent_history.setClass(MainActivity.this, HistoryActivity.class);
                startActivity(intent_history);

                break;
            case R.id.action_map:
                Uri uri = Uri.parse("geo:25.047192, 121.516981");
                final Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
                break;
            case R.id.action_call:
                Uri uri2 = Uri.parse("tel:0800080080");
                Intent intent3 = new Intent(Intent.ACTION_DIAL, uri2);
                startActivity(intent3);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //該函數與startActivityForResult是共生關係, 該函數負責處理從其他Activity返回的訊息
    //requestCode:呼叫該Activity時使用的識別碼; resultCode:該Activity傳回的回傳碼
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        //先確認回傳碼(resultCode),再判斷識別碼(requestCode)
        if (resultCode == RESULT_OK){
            if (requestCode == ACTIVITY_REPORT){
                Bundle bundle = intent.getExtras();
                String bmi = bundle.getString("BMI");
                show_previous.setText(getString(R.string.advice_history) + bmi);
                //清空輸入欄位
                num_weight.setText(R.string.input_empty);
                //將預設遊標指向weight輸入欄
                num_weight.requestFocus();
            }
        }
    }

    //restore preferences
    private void restorePrefs(){
        //Save user preferences. use Editor object to make changes.
        //宣告一個偏好設定(SharedPreferences),並使用getShardPreferences函式
        //來尋找系統中有無符合以"BMI_PREF"字串(PREF參數)作爲檔名的偏好設定檔,
        //如果有,就將這個偏好設定指定使用"settings"作爲代號來操作.
        //如果沒有,getSharedPreferences函式會回傳null給settings.
        /*SharedPreferences settings = getSharedPreferences(PREF,0);*/

        //可以透過getXXX函式來從偏好設定SharedPreferences讀取不同型別的內容
        //本例中,當PREF_HEIGHT偏好設定參數存在時,字串pref_height就會得到參數的內容,
        //沒有的話就會會得到一個我們指定的""空字串
        /*String pref_height = settings.getString(PREF_HEIGHT,"");*/

        //另外寫了一個Pref的Class,並從這裡呼叫它的函數
        String pref_height = Pref.getHeight(this);

        //如果不是空字串的話
        if(!"".equals(pref_height)){
            num_height.setText(pref_height);
            num_weight.requestFocus();
        }
    }

    public void onRestart(){
        super.onRestart();
        Log.v(TAG,"onRestart");
    }

    public void onStart(){
        super.onStart();
        Log.v(TAG,"onStart");
    }

    //當從第一個Activity跳轉到其他Activity後,在透過BACK鍵返回時,第一個Activity只會執行onResume方法中的動作
    public void onResume(){
        super.onResume();
        Log.v(TAG,"onResume");

        restorePrefs();
    }

    //儲存偏好設定
    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TAG,"onPause");

        //宣告一個偏好設定(SharedPreferences),並使用getShardPreferences函式
        //來尋找系統中有無符合以"BMI_PREF"字串(PREF參數)作爲檔名的偏好設定檔,
        //如果有,就將這個偏好設定指定使用"settings"作爲代號來操作.
        //如果沒有,getSharedPreferences函式會回傳null給settings.
        /*SharedPreferences setting = getSharedPreferences(PREF,0);*/

        //要改變SharePreferences型別的內容,需要透過edit函式來編輯
        /*SharedPreferences.Editor editor = setting.edit();*/

        //可以透過putXXX函式來爲SharePreferences填入不同型別的內容
        //本例是透過num_height介面元件識別符號來取得身高的字串後,再儲存到PREF_HEIGHT的偏好設定參數中
        /*editor.putString(PREF_HEIGHT,num_height.getText().toString());*/

        //最後要透過commit函式來將改變寫到系統中
        /*editor.commit();*/

        //爲減少記憶體的使用,使用串連的寫法來設定偏好設定參數,這種寫法就不必再多宣告一個Editor類別
        /*setting.edit()
                .putString(PREF_HEIGHT,num_height.getText().toString())
                .commit();*/

        //另外寫了一個Pref的Class,並從這裡呼叫它的函數
        Pref.setHeight(this, num_height.getText().toString());
    }

    public void onStop(){
        super.onStop();
        Log.v(TAG,"onStop");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }
}
