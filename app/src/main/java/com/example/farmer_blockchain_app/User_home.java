package com.example.farmer_blockchain_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class User_home extends AppCompatActivity {
    public static String name, mobile, email;
    ImageView iv1,iv2,ivLogout;
    OkHttpClient client = new OkHttpClient();
    Intent i1,i2,i3;
    TextView tvMessage,tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home2);
        tvMessage=findViewById(R.id.tvMessage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
         tvWelcome = findViewById(R.id.tvWelcome);
        tvMessage = findViewById(R.id.tvMessage);


        tvWelcome.setText("Welcome, " + name + "\n\"AGRO-TRUST!!\"");

// Update message dynamically
        tvMessage.setText("AGRO-TRUST!!");
        iv1=findViewById(R.id.iv1);
        iv2=findViewById(R.id.iv2);
        ivLogout=findViewById(R.id.ivLogout);
        i2=new Intent(this,MainActivity.class);
        i3=new Intent(this,Splashscreen.class);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(User_home.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();

                //startActivity(i1);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(i2);
            }
        });
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i3);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                get_qr_details(result.getContents());
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void get_qr_details(String qr) {




        RequestBody formBody = new FormBody.Builder()
                .add("qr", qr)

                .build();

        Request request = new Request.Builder()
                .url(Global.url+"get_qr_details")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(User_home.this, "Connection Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject obj = new JSONObject(res);
                        if (obj.getString("status").equals("success")) {
                            //  Toast.makeText(LoginMainActivity.this, "Welcome " + obj.getString("name"), Toast.LENGTH_SHORT).show();

                            tvMessage.setText(obj.getString("message")
                            +"\nProduct ID: "+obj.getString("pid")
                            +"\nProduct Name: "+obj.getString("pname")
                            +"\nCategory: "+obj.getString("category")
                            +"\nPrice: "+obj.getString("price")
                            +"\nBatch: "+obj.getString("batch")
                            +"\nDate: "+obj.getString("date")
                            +"\nDescription: "+obj.getString("description")
                            +"\nFarmer: "+obj.getString("farmer")



                            );



                            //finish();
                        } else {
                            tvMessage.setText("Invalid QR");
                            Toast.makeText(User_home.this, "Invalid QR", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(User_home.this, "Invalid QR", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }
        });
    }


}