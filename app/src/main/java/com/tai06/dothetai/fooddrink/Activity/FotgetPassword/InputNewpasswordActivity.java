package com.tai06.dothetai.fooddrink.Activity.FotgetPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tai06.dothetai.fooddrink.Activity.LoginSignup.LoginActivity;
import com.tai06.dothetai.fooddrink.Activity.MainActivity;
import com.tai06.dothetai.fooddrink.Contacts.Address;
import com.tai06.dothetai.fooddrink.Contacts.HomeLink;
import com.tai06.dothetai.fooddrink.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class InputNewpasswordActivity extends AppCompatActivity {

    private TextInputLayout layout1_newpsw,layout2_newpsw;
    private TextInputEditText email_newpsw,password_newpsw,confirm_newpsw;
    private Button btn_newpsw;
    private String email;
    private Toolbar toolbar_inputnewpsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_newpassword);
        init();
        event();
    }

    private void init(){
        layout1_newpsw = findViewById(R.id.layout1_newpsw);
        layout2_newpsw = findViewById(R.id.layout2_newpsw);
        email_newpsw = findViewById(R.id.email_newpsw);
        password_newpsw = findViewById(R.id.password_newpsw);
        confirm_newpsw = findViewById(R.id.confirm_newpsw);
        btn_newpsw = findViewById(R.id.btn_newpsw);
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        email_newpsw.setText(email);
        toolbar_inputnewpsw = findViewById(R.id.toolbar_inputnewpsw);
        setSupportActionBar(toolbar_inputnewpsw);
        getSupportActionBar().setTitle("Tạo mật khẩu mới");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void event(){
        btn_newpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                check_edittext();
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

    private void check_edittext(){
        String psw = password_newpsw.getText().toString().trim();
        String confirm = confirm_newpsw.getText().toString().trim();
        if (psw.equals("") || confirm.equals("")){
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
        }else{
            if (check_passord()){
                if (psw.trim().equals(confirm)){
                    UpdatePassword();
                }else{
                    Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean check_passord(){
        String pass = password_newpsw.getText().toString().trim();
        Matcher matcher = Address.PATTERN_PASSWORD.matcher(pass);
        if (!matcher.matches()){
            layout2_newpsw.setErrorEnabled(true);
            layout2_newpsw.setError("Mật khẩu dài hơn 8 kí tự, a-z0-9");
            return false;
        }else{
            layout2_newpsw.setErrorEnabled(false);
            return true;
        }
    }

    private void UpdatePassword(){
        String pass = password_newpsw.getText().toString().trim();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(InputNewpasswordActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeLink.URL_UPDATE_PSW, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            Toast.makeText(InputNewpasswordActivity.this, "Tạo mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(InputNewpasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(InputNewpasswordActivity.this, "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("Email",email);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InputNewpasswordActivity.this,CheckcodeAcceptActivity.class);
        startActivity(intent);
        finish();
    }
}