package com.example.cashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchDaily extends AppCompatActivity {

    ListView lv;
    ListViewAdapter adapter;
    Handler handler = new Handler();
    ImageButton searchBtn;
    EditText search;
    ImageView mainBtn;

    String eMail, listNum, date, memo, cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_daily);

        Intent intent = new Intent(this.getIntent());
        eMail = intent.getStringExtra("member");

        lv = (ListView)findViewById(R.id.lv);
        adapter = new ListViewAdapter();

        searchBtn = (ImageButton)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSearch();
            }
        });

        mainBtn = (ImageView)findViewById(R.id.mainButton);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        Intent expenseIntent = new Intent(SearchDaily.this, MainActivity.class);
                        expenseIntent.putExtra("member", eMail);
                        startActivityForResult(expenseIntent, 201);
                        setResult(RESULT_OK, expenseIntent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });

        search = (EditText)findViewById(R.id.search);
    }

    public void dataSearch(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("Http://syoppo.dothome.co.kr/search.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(eMail.toString()).append("/").append(search.getText()).append("/");

                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
                    outStream.write(buffer.toString());
                    outStream.flush();

                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    final StringBuilder builder = new StringBuilder();
                    String str = null;
                    while((str=reader.readLine()) != null){
                        builder.append(str+"\n");
                    }
                    final String resultData = builder.toString();

                    final String[] sResult = resultData.split("#");

                    final int count = sResult.length;
                    Log.e("Array"+sResult.length, String.valueOf(count));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.itemlist.clear();   //새로고침
                            adapter.notifyDataSetChanged(); //새로고침

                            for(int i=1; i<count; i++){
                                final String[] sResult2 = sResult[i].split("/");
                                Log.v("Array"+sResult2[0], String.valueOf(sResult2.length));
                                adapter.addItem(sResult2[0], sResult2[1], sResult2[2], sResult2[3], sResult2[4]);
                                listNum = sResult2[4];  //리스트 번호받아옴
                            }

                            lv.setAdapter(adapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                                    Intent detailIntent = new Intent(SearchDaily.this, DetailDaily.class);
                                    detailIntent.putExtra("kind", item.getKind());
                                    detailIntent.putExtra("date", item.getDate());
                                    detailIntent.putExtra("memo", item.getContent());
                                    detailIntent.putExtra("cash", item.getCash());
                                    detailIntent.putExtra("member", eMail);
                                    detailIntent.putExtra("listNum", listNum);
                                    SearchDaily.this.startActivity(detailIntent);
                                    startActivityForResult(detailIntent, 201);
                                    setResult(RESULT_OK, detailIntent);
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            });
                        }
                    });
                }catch (Exception e){
                    Log.e("", "Error", e);
                }
            }
        }.start();
    }
}
