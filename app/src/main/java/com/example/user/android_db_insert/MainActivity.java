package com.example.user.android_db_insert;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private UIHandler uiHandler;
    private Editable text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.main_textView);
        editText = (EditText) findViewById(R.id.main_editText);

        uiHandler = new UIHandler();
    }

    public void button(View v) {
        text = editText.getText();
        new Thread() {
            @Override
            public void run() {
                try {
                    MultipartUtility mu = new MultipartUtility("https://android-test-db-ppking2897.c9users.io/DataBase/AccountQuery02.php", "UTF-8");
                    mu.addFormField("");

                    List<String> ret = mu.finish();

                    parseJSON(ret.toString());

//                    Message mesg = new Message();
//                    Bundle data = new Bundle();
//                    data.putCharSequence("data",ret.toString());
//                    mesg.setData(data);
//                    mesg.what=0;
//                    uiHandler.sendMessage(mesg);

                } catch (Exception e) {
                    Log.v("ppking", "DB Error:" + e.toString());
                }
            }
        }.start();

    }

    private void parseJSON(String json){
        LinkedList accountInfo = new LinkedList<>();
        try{

            JSONObject jsonObject = new JSONArray(json).getJSONObject(0);

            String stringNo1 = jsonObject.getString("Account");
            accountInfo.add(stringNo1);
            String stringNo2 = jsonObject.getString("SeatIdNumber");
            accountInfo.add(stringNo2);
            String stringNo3 = jsonObject.getString("Checkout");
            accountInfo.add(stringNo3);

            Log.v("ppking" , " ::"+accountInfo.get(1));

            Message mesg = new Message();
            Bundle data = new Bundle();
            data.putCharSequence("data0",accountInfo.get(0).toString());
            data.putCharSequence("data1",accountInfo.get(1).toString());
            data.putCharSequence("data2",accountInfo.get(2).toString());
            mesg.setData(data);
            mesg.what=0;
            uiHandler.sendMessage(mesg);


        }catch (Exception e){
            Log.v("ppking", "Error : " + e.toString());
        }
    }



    private class UIHandler extends android.os.Handler{

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
                    textView.setText("Accound : "+msg.getData().getCharSequence("data0")+"\n");
                    textView.append("SeatIdNumber : "+msg.getData().getCharSequence("data1")+"\n");
                    textView.append("Checkout : $"+msg.getData().getCharSequence("data2")+"\n");
                    break;
            }
        }
    }

}
