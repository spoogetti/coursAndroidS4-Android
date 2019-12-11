package com.iut.rroubaix.intentionsexplicites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        TextView mainTextView = findViewById(R.id.editText);
        System.out.println(mainTextView.getText());

        Intent intent = new Intent(this, SecondActivity.class) ;
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText().toString());
        intent.setType("text/plain");
        startActivity(intent);
    }

}
