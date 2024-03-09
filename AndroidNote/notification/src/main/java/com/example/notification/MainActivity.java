package com.example.notification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText title;
    EditText message;
    Button send;
    Button clear;
    NotificationManager manager;
    String channelId = "simple_notification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.editTextText);
        message = findViewById(R.id.editTextText2);
        send = findViewById(R.id.button);
        clear = findViewById(R.id.button2);
        // 检查通知权限是否已经授予
        boolean notificationPermissionGranted = NotificationManagerCompat.from(this).areNotificationsEnabled();

        /**
         * 记得动态申请权限，否则无法在真机上发送通知
         */
        if (!notificationPermissionGranted) {
            dialog();
            // 请求通知权限
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 要求最低版本为26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "simple", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("sss", "onClick: jklasjfa");
                    Notification.Builder builder = new Notification.Builder(MainActivity.this, channelId)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                            .setTicker("通知来了")
                            .setContentTitle(title.getText().toString())
                            .setContentText(message.getText().toString())
                            .setDefaults(Notification.DEFAULT_ALL);

                    manager.notify(0, builder.build());

                }
            });
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manager.cancelAll();
                }
            });
        }
    }

    public void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("申请notification")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您单机了确定", Toast.LENGTH_SHORT);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您单机了取消", Toast.LENGTH_SHORT);
                    }
                });
        Toast.makeText(MainActivity.this, "hihi", Toast.LENGTH_SHORT);
        System.out.println("jklsajf");
        builder.show();
    }
}