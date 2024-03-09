package com.example.dialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "hihi", Toast.LENGTH_SHORT);
        initView();
    }

    private void initView() {
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            dialog(view);
        } else if (id == R.id.button2) {
            dialog2(view);
        } else if (id == R.id.button3) {
            dialog3(view);
        }
    }

    public void dialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("简单消息对话框")
                .setMessage("大家好")
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

    private String[] str = {"语文", "属性", "memory"};
    public void dialog2(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("title")
//                .setMessage("content")
                .setItems(str, new DialogInterface.OnClickListener() { // 不要设置message就会显示下面的列表对话框
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您选择的是" + str[i], Toast.LENGTH_SHORT);
                    }
                });
        builder.show();
    }

    /**
     * 单选对话框
     * @param view
     */
    public void dialog3(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("title")
                .setSingleChoiceItems(str, 2, new DialogInterface.OnClickListener() { // 默认第3项被选择
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您选择的是" + str[i], Toast.LENGTH_SHORT);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "您选择的是 取消" , Toast.LENGTH_SHORT);

                    }
                });
        builder.show();
    }
}