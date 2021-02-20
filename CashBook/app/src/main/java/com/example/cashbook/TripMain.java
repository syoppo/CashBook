package com.example.cashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TripMain extends AppCompatActivity {

    Button dailyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_main);

        dailyBtn = (Button)findViewById(R.id.dailyBtn);
        dailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyIntent = new Intent(TripMain.this, MainActivity.class);
                TripMain.this.startActivity(dailyIntent);
                overridePendingTransition(0, 0);
            }
        });
    }
}
