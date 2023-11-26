package com.example.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventBusSendActivity extends AppCompatActivity  implements View.OnClickListener {
    private TextView text;
    private Button bt_eventbus_send;
    private Button bt_eventbus_sticky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_send);
    }

    void initView() {
        text = findViewById(R.id.text);
        bt_eventbus_send = findViewById(R.id.bt_eventbus_send);
        bt_eventbus_sticky = findViewById(R.id.bt_eventbus_sticky);

        bt_eventbus_send.setOnClickListener(this);
        bt_eventbus_sticky.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}