package com.example.cashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {

    TextView loginButton;
    EditText eMail, pwd;
    ImageView backButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eMail = (EditText)findViewById(R.id.emailText);
        pwd = (EditText)findViewById(R.id.pwdText);
//        pwd2 = (EditText)findViewById(R.id.pwdText2);

        loginButton = (TextView)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                Register.this.startActivity(loginIntent);
                overridePendingTransition(0, 0);
            }
        });

        backButton = (ImageView)findViewById(R.id.mainButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(Register.this, MainActivity.class);
                Register.this.startActivity(backIntent);
                overridePendingTransition(0, 0);
            }
        });

        registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataRegister();
            }
        });
    }

    private void dataRegister(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("Http://syoppo.dothome.co.kr/register.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(eMail.getText()).append("/").append(pwd.getText()).append("/");
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
        Toast.makeText(Register.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
