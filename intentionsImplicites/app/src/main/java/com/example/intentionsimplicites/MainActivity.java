package com.example.intentionsimplicites;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Créez un nouveau projet et créer l’IHM ci-contre.
 * Complétez votre activité principale de telle sorte que l’appui sur chacun d’un de ces boutons provoque
 * l’affichage du texte du bouton dans la console ( une seule fonction )
 * Modifiez le code de gestion de l’appui sur le bouton SMS de telle sorte que cet événement
 * déclenche la une intention visant l’envoi d’un sms. L’action à utiliser est ACTION SENDTO et l’URI
 * à créer doit utiliser le schéma sms et une information correspondant à un numéro de téléphone quelconque.
 * Testez cette fonctionnalité ;Faites de même pour le bouton MMS et pour le boton appel;Complétez le code de gestion du bouton web,
 * de manière à ouvrir une page web.Faite de même pour le bouton map avec geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+CA+94043
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        TextView toParseView;
        String parsedString;
        switch(view.getId()) {
            case R.id.b_sms :
                System.out.println(getString(R.string.b_sms));
                toParseView = findViewById(R.id.c_sms);
                parsedString = toParseView.getText().toString();
                createIntent(Uri.parse("sms:" + parsedString));
                break;
            case R.id.b_mms :
                System.out.println(getString(R.string.b_MMS));
                toParseView = findViewById(R.id.c_mms);
                parsedString = toParseView.getText().toString();
                createIntent(Uri.parse("sms:" + parsedString));
                break;
            case R.id.b_appel :
                System.out.println(getString(R.string.b_appel));
                toParseView = findViewById(R.id.c_appel);
                parsedString = toParseView.getText().toString();
                createIntent(Uri.parse("tel:" + parsedString));
                break;
            case R.id.b_web :
                System.out.println(getString(R.string.b_web));
                toParseView = findViewById(R.id.c_web);
                parsedString = toParseView.getText().toString();
                createIntent(Uri.parse("https://" + parsedString));
                break;
            case R.id.b_map :
                System.out.println(getString(R.string.b_map));
                toParseView = findViewById(R.id.c_map_latitude);
                String latitude = toParseView.getText().toString();
                toParseView = findViewById(R.id.c_map_longitude);
                String longitude = toParseView.getText().toString();
                createIntent(Uri.parse("geo:" + latitude + "," + longitude + "?z=1600"));
                break;
        }
    }

    private void createIntent(Uri uri) {
        Intent sendIntent;
        sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        startActivity(sendIntent);
    }

}
