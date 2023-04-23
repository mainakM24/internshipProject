package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    //variables
    TextInputLayout tilName;
    EditText etUserName, etPassword;
    Button btGo;
    CheckBox rememberMe;

    private void requestAllPermission() {
        ActivityCompat.requestPermissions(LoginScreen.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 100)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        requestAllPermission();
        //Assignment
        tilName = findViewById(R.id.username);
        etUserName = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btGo = findViewById(R.id.goButton);
        rememberMe = findViewById(R.id.rememberMe);


        //login button action
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //collecting user input
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();


                //volley
                RequestQueue queue = Volley.newRequestQueue(LoginScreen.this);

////////////////////////////////////////////////////////////////////////////////////////////////////

                String url = "https://dummyjson.com/auth/login";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String token = jsonObject.getString("token");
                                   // Toast.makeText(LoginScreen.this, token, Toast.LENGTH_SHORT).show();
                                    if (!token.isEmpty()){
                                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        if(rememberMe.isChecked()){
                                            editor.putBoolean("flag", true);
                                            editor.apply();
                                        }
                                        editor.putString("username", username);
                                        editor.apply();
                                        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(LoginScreen.this, "Invalid User details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginScreen.this, "Invalid user details", Toast.LENGTH_SHORT).show();

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("password", password);

                        return params;
                    }
                };
                queue.add(postRequest);

////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });
    }
}