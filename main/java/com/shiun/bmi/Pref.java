package com.shiun.bmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Shiun on 9/1/2016.
 */
public class Pref extends PreferenceActivity {
    public static final String PREF = "BMI_PREF";
    public static final String PREF_HEIGHT = "BMI_HEIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //讀取偏好設定的XML描述檔,將描述檔綁定Pref這個Activity.
        //在執行時,描述檔中的宣告將被轉換成使用者所看到的偏好設定介面
        addPreferencesFromResource(R.xml.pref);
    }

    public static void setHeight(Context context, String height){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);

        //要改變SharePreferences型別的內容,需要透過edit函式來編輯
        /*SharedPreferences.Editor editor = pref.edit();*/

        //可以透過putXXX函式來爲SharePreferences填入不同型別的內容
        //本例是透過num_height介面元件識別符號來取得身高的字串後,再儲存到PREF_HEIGHT的偏好設定參數中
        /*editor.putString(PREF_HEIGHT, height);*/

        //最後要透過commit函式來將改變寫到系統中
        /*editor.commit();*/

        //爲減少記憶體的使用,使用串連的寫法來設定偏好設定參數,這種寫法就不必再多宣告一個Editor類別
        pref.edit()
                .putString(PREF_HEIGHT, height)
                .commit();

        //return;
    }

    public static String getHeight(Context context){
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);

        //可以透過getXXX函式來從偏好設定SharedPreferences讀取不同型別的內容
        //本例中,當PREF_HEIGHT偏好設定參數存在時,字串pref_height就會得到參數的內容,
        //沒有的話就會會得到一個我們指定的""空字串
        return pref.getString(PREF_HEIGHT,"");
    }

}
