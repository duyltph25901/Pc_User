package com.example.pc_user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_user.R;
import com.example.pc_user.db_local.UserCurrentDatabase;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.notification_dialog.ShowDialog;
import com.example.pc_user.functions.validate.ValidateInput;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditPasswordActivity extends AppCompatActivity {
    private ImageView imageX;
    private TextView textTitle, textShowHidePass;
    private EditText inputCurrentPass, inputNewPass, inputConfirmPass;
    private Button buttonUpdate;
    private ProgressDialog progressDialog;

    private int currentHideShow = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        _init();
        _setOnclick();
    }

    private void _init() {
        imageX = findViewById(R.id.imageX);
        textTitle = findViewById(R.id.textTitle);
        textShowHidePass = findViewById(R.id.textHideShow);
        inputCurrentPass = findViewById(R.id.inputPassCurrent);
        inputNewPass = findViewById(R.id.inputNewPass);
        inputConfirmPass = findViewById(R.id.inputPassConfirm);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        progressDialog = new ProgressDialog(this);
    }

    private void _setOnclick() {
        buttonUpdate.setOnClickListener(view -> _update());
        imageX.setOnClickListener(view -> finish());
        textShowHidePass.setOnClickListener(view -> _handleShowHidePass());
    }

    private void _update() {
        String currentPass = inputCurrentPass.getText().toString().trim();
        String newPass = inputNewPass.getText().toString().trim();
        String confirmPass = inputConfirmPass.getText().toString().trim();

        boolean isNull = ValidateInput.isNull(currentPass, newPass, confirmPass);
        boolean isPass = ValidateInput.isPass(newPass);
        boolean isMatchPass = ValidateInput.isConfirm(newPass, confirmPass);
        boolean isTruePassword = currentPass.matches(_getPasswordCurrent());
        boolean isBreaking = ValidateInput.isBreakingUpdatePass(EditPasswordActivity.this, isNull, isPass, isMatchPass, isTruePassword);

        if (isBreaking) return;

        _handleUpdate(newPass);
    }

    private void _handleUpdate(String newPass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = "SOME-SECURE-PASSWORD";

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ShowDialog.handleShowDialog(EditPasswordActivity.this, Const.flagSuccessDialog, "Cập nhật mật khẩu thành công!");
                            Toast.makeText(EditPasswordActivity.this, "Hệ thống sẽ tự đăng xuất trong vòng 5s!", Toast.LENGTH_LONG).show();
                            _enableButton();
                            new Handler()
                                    .postDelayed(() -> _handleLogOut(), 5000);
                        } else {
                            ShowDialog.handleShowDialog(EditPasswordActivity.this, Const.flagErrorDialog, task.getException()+"");
                        }
                    }
                });
    }

    private void _handleShowHidePass() {
        ++currentHideShow;
        /*
         *   currentHideShow == 1 => hide
         *   currentHideShow == 2 => show
         * */
        if (currentHideShow % 2 == 0) {
            inputCurrentPass.setInputType(InputType.TYPE_CLASS_TEXT);
            textShowHidePass.setText("Ẩn");
        } else {
            inputCurrentPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            textShowHidePass.setText("Hiện");
        }
    }

    private void _enableButton() {
        imageX.setEnabled(false);
        buttonUpdate.setEnabled(false);
    }

    private void _handleLogOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(EditPasswordActivity.this, LoginContainerActivity.class));
                        finishAffinity();
                    } else {
                        ShowDialog.handleShowDialog(EditPasswordActivity.this, Const.flagErrorDialog, "Đăng xuất thất bại!");
                    }
                });
    }

    private String _getPasswordCurrent() {
        return UserCurrentDatabase.getInstance(EditPasswordActivity.this).userDAO().getUser().getPass();
    }
}