package com.example.smutable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HoWeAre extends AppCompatActivity {
    ConstraintLayout constraintLayoutWho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_we_are);
        init();
        onClick();
    }

    private void init() {
        constraintLayoutWho = findViewById(R.id.constraintLayoutWho);
    }

    private void onClick() {
        constraintLayoutWho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}