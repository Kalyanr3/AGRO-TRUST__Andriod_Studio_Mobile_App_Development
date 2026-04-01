package com.example.farmer_blockchain_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Size;
import android.widget.TextView;



import java.util.concurrent.ExecutionException;

public class scan_qr extends AppCompatActivity {


    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_qr);


        txtResult = findViewById(R.id.txtResult);


    }


}
