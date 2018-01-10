package com.finiq.mediaplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;

public class LoginActivity extends AppCompatActivity {

    EditText mUsername, mPassword;
    FrameLayout mFrame_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
