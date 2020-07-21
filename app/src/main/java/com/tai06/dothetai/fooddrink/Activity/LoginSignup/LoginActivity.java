package com.tai06.dothetai.fooddrink.Activity.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.tai06.dothetai.fooddrink.Activity.FotgetPassword.ForgetPasswordActivity;
import com.tai06.dothetai.fooddrink.Activity.MainActivity;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email,password;
    private TextView forget_pass,add_user;
    private Button btn_login;
    private String emai,pass;
    public  String emai_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        process_event();
    }

    private void init() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forget_pass = findViewById(R.id.forget_pass);
        add_user = findViewById(R.id.add_user);
        btn_login = findViewById(R.id.btn_login);
        emai_txt = email.getText().toString().trim();
    }

    private void process_event(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                checkLogin();
            }
        });
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void checkLogin(){
        emai = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        if (emai.trim().equals("") || pass.trim().equals("")){
            Toast.makeText(this, "Vui lòng nhập thông tin!", Toast.LENGTH_SHORT).show();
        }else{
            postLogin();
        }
    }

    private void postLogin(){
        HomeLink.Email = email.getText().toString().trim();
        emai = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_CHECK_LOGIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email",emai);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA","Error" + error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("email",emai);
                        param.put("password",pass);
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startActivity(startMain);
        finish();
    }
}
