package com.example.ifootball;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnRegister;
    private EditText email, pass, rePass;
    private String mail, pw,rePw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findId();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });
    }

    private void findId(){
        btnBack =  findViewById(R.id.back);
        btnRegister = findViewById(R.id.btnRegister);
        email = findViewById(R.id.register_email);
        pass = findViewById(R.id.register_pass);
        rePass = findViewById(R.id.register_re_pass);
    }

    private void validateInput(){

        mail = email.getText().toString();
        pw = pass.getText().toString();
        rePw= rePass.getText().toString();
        if (mail.isEmpty() || pw.isEmpty() || rePw.isEmpty()){
            //validate here
            Toast.makeText(Register.this,"Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else{
            //success
            Toast.makeText(Register.this, "Đăng ký thành công với tài khoản: " + mail, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
