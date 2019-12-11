package com.iut.rroubaix.intentionsexplicites;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        String intentedString = intent.getStringExtra(Intent.EXTRA_TEXT) ;
        TextView secondTextView = findViewById(R.id.editText2);
        System.out.println(intentedString);
        secondTextView.setText(intentedString);
    }
}
