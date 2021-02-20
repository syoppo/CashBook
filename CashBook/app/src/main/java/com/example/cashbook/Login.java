package com.example.cashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class Login extends AppCompatActivity {

    TextView registerButton;
    EditText emailText, pwdText;
    ImageView backButton;
    Button loginButton;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText)findViewById(R.id.emailText);
        pwdText = (EditText)findViewById(R.id.pwdText);

        registerButton = (TextView)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registerIntent);
                overridePendingTransition(0, 0);        //화면전환 끄기
            }
        });

        backButton = (ImageView)findViewById(R.id.mainButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(backIntent);
                overridePendingTransition(0, 0);

            }
        });

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataLogin();
            }
        });

    }

    public void dataLogin(){
        new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("Http://syoppo.dothome.co.kr/login.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection)url.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(emailText.getText()).append("/").append(pwdText.getText()).append("/");

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

                    final String[] sResult = resultData.split("/");
                    final int count = sResult.length;
                    handler.post(new Runnable() {
                        @Override
                        public void run(){
                            if(count ==1){
                                Toast.makeText(getApplicationContext(), "이메일 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("member", sResult[0]);
                                startActivityForResult(intent, 201);
                                setResult(RESULT_OK, intent);
                                finish();
                                Toast.makeText(getApplicationContext(), (sResult[0])+"로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("", "Error", e);
                }
            }
        }.start();
    }
}
