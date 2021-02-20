package com.example.cashbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailDaily extends AppCompatActivity {

    String dkind, ddate, dmemo, dcash, id, listNum;
    Button dateBtn, updateBtn, deleteBtn;

    TextView kind, date;
    EditText memo, cash;

    ImageView mainBtn;

    Handler handler = new Handler();

    int y=0, m=0, d=0;  //달력을 띄울때 쓰는 함수에서 필요

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daliy);

        Intent intent = new Intent(this.getIntent());
        dkind = intent.getStringExtra("kind");
        ddate = intent.getStringExtra("date");
        dmemo = intent.getStringExtra("memo");
        dcash = intent.getStringExtra("cash");
        id = intent.getStringExtra("member");
        listNum = intent.getStringExtra("listNum");

        kind = (TextView) findViewById(R.id.kind);
        kind.setText(ddate);    //왜인지는 모르겠는데 종류랑 날짜 부분이 바껴서 나옴

        date = (TextView) findViewById(R.id.date);
        date.setText(dkind);    //왜인지는 모르겠는데 종류랑 날짜 부분이 바껴서 나옴

        memo = (EditText)findViewById(R.id.memo);
        memo.setText(dmemo);

        cash = (EditText)findViewById(R.id.cash);
        cash.setText(dcash);

        mainBtn = (ImageView) findViewById(R.id.mainButton);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(DetailDaily.this, MainActivity.class);
                        DetailDaily.this.startActivity(mainIntent);
                        mainIntent.putExtra("member", id);
                        startActivityForResult(mainIntent, 201);
                        setResult(RESULT_OK, mainIntent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });

        dateBtn = (Button)findViewById(R.id.dateBtn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateShow();
            }
        });

        updateBtn = (Button)findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailDaily.this/*해당 액티비티를 가르킴*/)
                        .setTitle("수정").setMessage("수정 하시겠습니까?")
                        .setPositiveButton("수정", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                update();
                                Intent mainIntent = new Intent(DetailDaily.this, MainActivity.class);
                                DetailDaily.this.startActivity(mainIntent);
                                mainIntent.putExtra("member", id);
                                startActivityForResult(mainIntent, 201);
                                setResult(RESULT_OK, mainIntent);
                                finish();
                                overridePendingTransition(0, 0);
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

        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailDaily.this/*해당 액티비티를 가르킴*/)
                        .setTitle("삭제").setMessage("삭제 하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                delete();
                                Intent mainIntent = new Intent(DetailDaily.this, MainActivity.class);
                                DetailDaily.this.startActivity(mainIntent);
                                mainIntent.putExtra("member", id);
                                startActivityForResult(mainIntent, 201);
                                setResult(RESULT_OK, mainIntent);
                                finish();
                                overridePendingTransition(0, 0);
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
    }

    void dateShow(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y=year;
                m=month;
                d=dayOfMonth;
                date.setText(y+"년 "+m+"월 "+d+"일");
            }
        },2020, 07, 17);
        datePickerDialog.setMessage("날짜 선택");
        datePickerDialog.show();
    }

    void update(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("http://syoppo.dothome.co.kr/update.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id.toString()).append("/").append(date.getText()).append("/").append(memo.getText())
                            .append("/").append(cash.getText()).append("/").append(listNum.toString()).append("/");
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    while(reader.readLine() != null){
                        System.out.println(reader.readLine());
                    }
                }catch (Exception e){
                    Log.e("", "", e);
                }
            }
        }.start();
        Toast.makeText(DetailDaily.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }

    void delete(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("http://syoppo.dothome.co.kr/delete.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id.toString()).append("/").append(listNum.toString()).append("/");
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "utf-8");
                    final BufferedReader reader = new BufferedReader(tmp);
                    while(reader.readLine() != null){
                        System.out.println(reader.readLine());
                    }
                }catch (Exception e){
                    Log.e("", "", e);
                }
            }
        }.start();
        Toast.makeText(DetailDaily.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
