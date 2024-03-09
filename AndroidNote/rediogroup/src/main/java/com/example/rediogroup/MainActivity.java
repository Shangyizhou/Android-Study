package com.example.rediogroup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 单选按钮和复选框
 * setOnCheckedChangeListener
 */
public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    Button button;
    CheckBox[] checkBoxes;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "";
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    // alt + enter
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.isChecked()) {
                        str = radioButton.getText().toString();
                        break;
                    }
                }
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            String str = "";
            @Override
            public void onClick(View view) {
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isChecked()) {
                        str += checkBoxes[i].getText().toString();
                    }
                }
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT);
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        radioGroup = findViewById(R.id.radioGroup);
        button = findViewById(R.id.button);

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBoxes = new CheckBox[] {checkBox1, checkBox2, checkBox3, checkBox4};
    }
}