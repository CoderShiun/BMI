package com.shiun.bmi;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class HistoryActivity extends ListActivity {

    private DB mDbHelper;
    private Cursor mCursor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /*
    static final String[] records = new String[]{
            "20",
            "21",
            "22",
            "24",
            "23",
            "22",
            "20"
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //這行不需要,沒註解掉會錯
        //setContentView(R.layout.activity_history);
        setContentView(R.layout.activity_history);

        //Tell the list view which view to display when the list is empty
        getListView().setEmptyView(findViewById(R.id.tx_empty));

        //透過setAdapter的方法,將array寫入list
        setAdapter();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //與資料庫的接口
    private void setAdapter() {
        /*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.records,android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        */

        //比上述更爲省記憶體的寫法
        //第一個參數:Activity實體本身,二:接口樣式的識別符號,三:傳入的陣列,陣列定義在strings中
        /*
        setListAdapter(ArrayAdapter.createFromResource(this,
                R.array.records, android.R.layout.simple_list_item_1));
        */

        //建立起DB類別的mDbbHelper物件,並呼叫open方法來打開db
        mDbHelper = new DB(this);
        mDbHelper.open();

        //返回能查詢資料表所有資料的指針,儲存到mCursor類別中
        mCursor = mDbHelper.getAll();
        //讓Activity可以基於本身的生命週期(LifeCycle),來一併管理Cursor的生命週期
        startManagingCursor(mCursor);

        //from_column參數中的各欄位,映射到android.R.layout.simple_list_item_2列表格式中對應的to_layout參數的各欄位裡
        String[] from_column = new String[]{DB.KEY_ITME, DB.KEY_CREATED};
        int[] to_layout = new int[]{android.R.id.text1, android.R.id.text2};

        //Now create a simple cursor adapter
        //將db中的item和created欄位,對應到simple_list_item_2列表格式中的text1和text2欄位
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                        mCursor, from_column, to_layout);

        //將接口聯繫到ListView介面元件上
        setListAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "History Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shiun.bmi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "History Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shiun.bmi/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}

/*
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }
}
*/
