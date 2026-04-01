package com.example.farmer_blockchain_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.farmer_blockchain_app.Global;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.*;
public class RegisterActivity extends AppCompatActivity {
    EditText etfname, etlname, etemail, etpass, etcpass, etphone;
    Button b;
    OkHttpClient client = new OkHttpClient();
    String fname, lname, email, pass, cpass, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etfname = findViewById(R.id.fnametxt);
        etlname = findViewById(R.id.lnametxt);
        etemail = findViewById(R.id.emailtxt);
        etpass = findViewById(R.id.stdpwd);
        etcpass = findViewById(R.id.stdcpwd);
        etphone = findViewById(R.id.phonetxt);
        b = findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                fname = etfname.getText().toString().trim();
                lname = etlname.getText().toString().trim();
                email = etemail.getText().toString().trim();
                pass = etpass.getText().toString().trim();
                cpass = etcpass.getText().toString().trim();
                phone = etphone.getText().toString().trim();

                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);

                if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Enter a valid Email", Toast.LENGTH_LONG).show();
                    return;
                }

                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || pass.isEmpty() || cpass.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass.equals(cpass)) {
                    etpass.setError("Password Mismatch");
                    etcpass.setError("Password Mismatch");
                    return;
                }

                try {
                    send_data();
                } catch (IOException | JSONException e) {
                    Toast.makeText(RegisterActivity.this, "Network Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void send_data() throws IOException, JSONException {
        // Replace with your Flask server URL
        URL registerUrl = new URL(Global.url+"register");
        RequestBody formBody = new FormBody.Builder()
                .add("fname", fname)
                .add("lname", lname)
                .add("email", email)
                .add("phone", phone)
                .add("pass", pass)
                .build();

        Request request = new Request.Builder()
                .url(registerUrl)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(RegisterActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, res, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginMainActivity.class);

                    startActivity(intent);

                });
            }
        });
    }
}
