package com.example.pc_user.functions;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.widget.TextView;

public class CustomTextColor {
    public static void customTextColor(TextView txtView, int... color) {
        TextPaint textPaint = txtView.getPaint();
        float width = textPaint.measureText(txtView.getText().toString());

        Shader shader = new LinearGradient(0, 0, width, txtView.getTextSize(), color, null, Shader.TileMode.CLAMP);
        txtView.getPaint().setShader(shader);
        txtView.setTextColor(color[0]);
    }
}
