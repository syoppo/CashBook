package com.example.cashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputDaliy extends AppCompatActivity {

    ImageView mainBtn;
    Button expenseBtn, iSaveBtn, dateBtn;
    TextView eDate;

    EditText iMemo, iCash;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy"+"년 "+"M"+"월 "+"dd"+"일");
    String formatDate = sdfNow.format(date);

    int y=0, m=0, d=0;

    String id;

    int bdNum = 1;  //수입, 지출 구분하는 번호 수입:1

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_daliy);

        Intent intent = new Intent(this.getIntent());
        id = intent.getStringExtra("member");   //Login.java의 인텐트에서 가져온다.

        iMemo = (EditText)findViewById(R.id.memo);
        iCash = (EditText)findViewById(R.id.cash);

        eDate = (TextView)findViewById(R.id.date);
        eDate.setText(formatDate);

        dateBtn = (Button)findViewById(R.id.dateBtn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateShow();
            }
        });

        mainBtn = (ImageView)findViewById(R.id.mainButton);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        Intent expenseIntent = new Intent(InputDaliy.this, MainActivity.class);
                        expenseIntent.putExtra("member", id);
                        startActivityForResult(expenseIntent, 201);
                        setResult(RESULT_OK, expenseIntent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });

        expenseBtn = (Button)findViewById(R.id.expenseButton);
        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        Intent expenseIntent = new Intent(InputDaliy.this, ExpenseDaily.class);
                        expenseIntent.putExtra("member", id);
                        startActivityForResult(expenseIntent, 201);
                        setResult(RESULT_OK, expenseIntent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }
        });;

        iSaveBtn = (Button)findViewById(R.id.iSaveBtn);
        iSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iSave();
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
                eDate.setText(y+"년 "+m+"월 "+d+"일");
            }
        },2020, 07, 17);
        datePickerDialog.setMessage("날짜 선택");
        datePickerDialog.show();
    }

    public void iSave(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("http://syoppo.dothome.co.kr/input.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id.toString()).append("/").append(iCash.getText()).append("/").append(eDate.getText()).append("/").append(iMemo.getText()).append("/").append(bdNum).append("/");
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
        Toast.makeText(InputDaliy.this, "입력이 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
