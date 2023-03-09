package com.example.pc_user.functions.validate;

import android.app.Activity;

import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.notification_dialog.ShowDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateInput {

    public static boolean isNull(String... input) {
        for (int i=0; i<input.length; i++) {
            if (input[i].isEmpty()) return true;
        }

        return false;
    }

    public static boolean isEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isPass(String pass) {
        return pass.length() >= 6;
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(phoneNumber);
        return (m.matches());
    }

    public static boolean isConfirm(String pass, String confirm) {
        return pass.matches(confirm);
    }

    public static boolean isBreakingSignUp(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isEmail = var[1];
        boolean isPass = var[2];
        boolean isConfirm = var[3];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isEmail) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Email không đúng định dạng!");
            return true;
        } if (!isPass) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu phải chứa ít nhất 6 kí tự!");
            return true;
        } if (!isConfirm) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu không trùng khớp!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingLogin(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isEmail = var[1];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isEmail) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Email không hợp lệ!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingShopping(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isPhoneNumber = var[1];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isPhoneNumber) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Không tìm thấy số điện thoại!");
            return true;
        }

        return false;
    }

    public static boolean isBreakingUpdatePass(Activity activity, boolean... var) {
        boolean isNull = var[0];
        boolean isPass = var[1];
        boolean isConfirm = var[2];
        boolean isTruePass = var[3];

        if (isNull) {
            ShowDialog.handleShowDialog(activity, Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return true;
        } if (!isTruePass) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog,  "Mật khẩu cũ không đúng!");
            return true;
        } if (!isPass) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu phải chứa ít nhất 6 kí tự!");
            return true;
        } if (!isConfirm) {
            ShowDialog.handleShowDialog(activity,Const.flagErrorDialog, "Mật khẩu không trùng khớp!");
            return true;
        }

        return false;
    }
}
