package com.example.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

  public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text;
    private Button bt_eventbus_send_main;
    private Button bt_eventbus_sticky_recv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    void initView() {
        text = findViewById(R.id.recv_text);
        bt_eventbus_send_main = findViewById(R.id.bt_eventbus_send_main);
        bt_eventbus_sticky_recv = findViewById(R.id.bt_eventbus_sticky_recv);

        bt_eventbus_send_main.setOnClickListener(this);
        bt_eventbus_sticky_recv.setOnClickListener(this);
    }

      @Override
      public void onClick(View view) {
          switch (view.getId()) {
              case R.id.bt_eventbus_send_main:
                  break;
              case R.id.bt_eventbus_sticky_recv:
                  break;
              default:
                  break;
          }
      }
  }