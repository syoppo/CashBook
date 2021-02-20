package com.example.cashbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button loginButton, logoutButton, tripBtn;
    ImageButton searchBtn;
    TextView nowDate;
    FloatingActionButton fab;

    ListView lv;
    ListViewAdapter adapter;

    Handler handler = new Handler();

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy"+"년 "+"M"+"월 "+"dd"+"일");
    String formatDate = sdfNow.format(date);

    String eMail, listNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        member = (TextView)findViewById(R.id.member);

        lv = (ListView)findViewById(R.id.lv);
        adapter = new ListViewAdapter();

        dataList();

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(loginIntent);
                overridePendingTransition(0, 0);
            }
        });

        Intent intent = new Intent(this.getIntent());
        eMail = intent.getStringExtra("member");   //Login 클래스에서 가져온다.
//        member.setText(id);

        if(eMail==null){   //로그인이 되어 있지 않다면
            loginButton.setVisibility(View.VISIBLE);    //화면에 버튼이 보이게 된다.
        }else{
            loginButton.setVisibility(View.INVISIBLE);  //화면에 버튼이 보이지 않게 된다.
        }

        logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this/*해당 액티비티를 가르킴*/)
                        .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(MainActivity.this/*현재 액티비티 위치*/, Login.class/*이동 액티비티 위치*/);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
            }
        });

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        Intent expenseIntent = new Intent(MainActivity.this, ExpenseDaily.class);
                        expenseIntent.putExtra("member", eMail);
                            startActivityForResult(expenseIntent, 201);
                            setResult(RESULT_OK, expenseIntent);
                            finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });

        searchBtn = (ImageButton)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        Intent expenseIntent = new Intent(MainActivity.this, SearchDaily.class);
                        expenseIntent.putExtra("member", eMail);
                        startActivityForResult(expenseIntent, 201);
                        setResult(RESULT_OK, expenseIntent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });

        tripBtn = (Button)findViewById(R.id.tripBtn);
        tripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tripIntent = new Intent(MainActivity.this, TripMain.class);
                MainActivity.this.startActivity(tripIntent);
                overridePendingTransition(0, 0);
            }
        });

        nowDate = (TextView)findViewById(R.id.nowDate);
        nowDate.setText(formatDate);
    }

    public void dataList(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("Http://syoppo.dothome.co.kr/list.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(eMail.toString()).append("/");

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
                                    Intent detailIntent = new Intent(MainActivity.this, DetailDaily.class);
                                    detailIntent.putExtra("kind", item.getKind());
                                    detailIntent.putExtra("date", item.getDate());
                                    detailIntent.putExtra("memo", item.getContent());
                                    detailIntent.putExtra("cash", item.getCash());
                                    detailIntent.putExtra("member", eMail);
                                    detailIntent.putExtra("listNum", item.getInputNum());
                                    MainActivity.this.startActivity(detailIntent);
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
