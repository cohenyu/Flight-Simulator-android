package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class JoystickActivity extends AppCompatActivity implements MyJoystick.JoystickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyJoystick myJoystick= new MyJoystick(this);
        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");
        ClientCommunication.getInstance().connect(ip, port);
        setContentView(R.layout.activity_joystick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientCommunication.getInstance().close();
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        yPercent *= -1;
        ClientCommunication.getInstance().writeToServer("aileron",xPercent);
        ClientCommunication.getInstance().writeToServer("elevator",yPercent);
    }


}
