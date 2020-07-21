package com.tai06.dothetai.fooddrink.Activity.FotgetPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tai06.dothetai.fooddrink.R;

public class CheckcodeAcceptActivity extends AppCompatActivity {

    private EditText input_codeAccept;
    private Button btn_checkcode;
    private int codeAccept;
    private String email;
    private Toolbar toolbar_checkcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkcode_accept);
        init();
        event();

    }

    private void init(){
        input_codeAccept = findViewById(R.id.input_codeAccept);
        btn_checkcode = findViewById(R.id.btn_checkcode);
        Intent intent = getIntent();
        codeAccept = intent.getIntExtra("codeAccept",0);
        email = intent.getStringExtra("Email");
        toolbar_checkcode = findViewById(R.id.toolbar_checkcode);
        setSupportActionBar(toolbar_checkcode);
        getSupportActionBar().setTitle("Kiểm tra mã xác nhận");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void event(){
        btn_checkcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = input_codeAccept.getText().toString().trim();
                if (input.equals("")){
                    Toast.makeText(CheckcodeAcceptActivity.this, "Vui lòng nhập mã xác nhận", Toast.LENGTH_SHORT).show();
                }else{
                    int checkCode = Integer.parseInt(input_codeAccept.getText().toString().trim());
                    if (codeAccept == checkCode){
                        Intent intent = new Intent(getApplicationContext(), InputNewpasswordActivity.class);
                        intent.putExtra("Email", email);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(CheckcodeAcceptActivity.this, "Kiểm tra lại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckcodeAcceptActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}