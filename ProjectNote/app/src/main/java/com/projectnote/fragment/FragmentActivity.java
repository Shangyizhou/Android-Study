package com.projectnote.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.projectnote.R;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }
}