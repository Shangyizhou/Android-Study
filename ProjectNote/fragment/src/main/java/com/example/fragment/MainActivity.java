package com.example.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.checkout_btn);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkout_btn:
                replaceFragment(new AnotherRightFragment());
                break;
            default:
                break;
        }
    }

   private void replaceFragment(Fragment fragment) {
       FragmentManager fragmentManager = getSupportFragmentManager();
       FragmentTransaction transaction = fragmentManager.beginTransaction();
       transaction.replace(R.id.right_fragment, fragment);
       transaction.addToBackStack(null);
       transaction.commit();
   }
}