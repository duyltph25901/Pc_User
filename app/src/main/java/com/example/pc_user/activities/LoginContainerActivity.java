package com.example.pc_user.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pc_user.R;
import com.example.pc_user.fragments.LoginFragment;
import com.example.pc_user.fragments.SignUpFragment;

public class LoginContainerActivity extends AppCompatActivity {
    private static final int flagLogin = 0;
    private static final int flagSignUp = 1;
    private int currentFlag = flagLogin;

    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_container);

        _init();
        _setDefaultFragment();
        _setOnclick();
    }

    private void _init() {
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSigUp);
    }

    private void _setDefaultFragment() {
        currentFlag = flagLogin;
        _replaceFragment(new LoginFragment());
    }

    private void _replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out); // animation
        transaction.replace(R.id.containerLogin, fragment);
        transaction.commit();
    }

    private void _setOnclick() {
        btnLogin.setOnClickListener(view -> _handleLogin());
        btnSignUp.setOnClickListener(view -> _handleSignUp());
    }

    private void _handleLogin() {
        if(currentFlag != flagLogin){
            _replaceFragment(new LoginFragment());
            currentFlag = flagLogin;
        }
    }

    private void _handleSignUp() {
        if(currentFlag != flagSignUp){
            _replaceFragment(new SignUpFragment());
            currentFlag = flagSignUp;
        }
    }
}