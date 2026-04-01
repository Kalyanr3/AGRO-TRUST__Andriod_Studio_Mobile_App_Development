package com.example.farmer_blockchain_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import android.support.v7.app.AppCompatActivity;

public class LoginMainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    EditText usr;
    EditText pwd;
    TextView studname,studemail;
    Button lbtn;
    Intent intent = null,i1;
    String email,pass;
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        usr=findViewById(R.id.usn);
        pwd=findViewById(R.id.pw);
        lbtn=findViewById(R.id.loginbtn);
     //   i1 =new Intent(LoginMainActivity.this, UserHome.class);

        /*to store username and password*/
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        //Toast.makeText(this, username+password, Toast.LENGTH_SHORT).show();
        if (username == null || password == null) {
            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_USERNAME, usr.getText().toString())
                    .putString(PREF_PASSWORD, pwd.getText().toString())
                    .apply();
        }
        else
        {
            usr.setText(username);
            pwd.setText(password);
        }

        //add the function to connect to database
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(PREF_USERNAME, usr.getText().toString())
                        .putString(PREF_PASSWORD, pwd.getText().toString())
                        .apply();
                email=usr.getText().toString().trim();
                pass=pwd.getText().toString().trim();
                try {
                    //send_data();
                    loginUser(email,pass);
                } catch (Exception e) {
                    Toast.makeText(LoginMainActivity.this, "Network Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    public void send_data() throws IOException, JSONException {
        // Replace with your Flask server URL
        URL registerUrl = new URL(Global.url+"loginm");
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("pass", pass)
                .build();

//        Request request = new Request.Builder()
//                .url(registerUrl)
//                .post(formBody)
//                .build();
        loginUser(email,pass);

//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                runOnUiThread(() ->
//                        Toast.makeText(LoginMainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String res = response.body().string();
//                runOnUiThread(() -> {
//                    Toast.makeText(LoginMainActivity.this, res, Toast.LENGTH_LONG).show();
//                    String arr[]=res.split("#");
//                    if(arr[6].equals("ok"))
//                    {
//
//                        UserHome.fname = arr[0];
//                        UserHome.lname = arr[1];
//                        UserHome.email = arr[2];
//                        UserHome.mobile = arr[3];
//                        UserHome.dob = arr[4];
//                        UserHome.gender = arr[5];
//
//
//                        startActivity(intent);
//                    }
//
//                });
//            }
//        });
    }
    private void loginUser(String email,String pass) {




        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", pass)
                .build();

        Request request = new Request.Builder()
                .url(Global.url+"loginm")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginMainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject obj = new JSONObject(res);
                        if (obj.getString("status").equals("success")) {
                          //  Toast.makeText(LoginMainActivity.this, "Welcome " + obj.getString("name"), Toast.LENGTH_SHORT).show();

                            // Navigate to home screen
                            Intent intent = new Intent(LoginMainActivity.this, User_home.class);
                            intent.putExtra("email", obj.getString("email"));
                            intent.putExtra("name", obj.getString("name"));
                            intent.putExtra("mobile", obj.getString("mobile"));

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginMainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginMainActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
