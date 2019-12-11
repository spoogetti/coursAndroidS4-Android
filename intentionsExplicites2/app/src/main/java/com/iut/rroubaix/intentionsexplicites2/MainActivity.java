package com.iut.rroubaix.intentionsexplicites2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * L’activité principale présente à l'utilisateur deux parties contenant respectivement :
 * o Pour la partie supérieure, des champs correspondant à un nom, un prénom et un
 * numéro de téléphone, ainsi qu'un bouton modifier ;
 * o Pour la partie inférieure, une adresse sous la forme des champs numéro, nom de
 * rue, code postal et ville, ainsi qu'un bouton Modifier.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String typeBundle = extras.getString("TYPE_ENVOI");

            switch (typeBundle) {
                case "USER_INFO" :
                    String nom = extras.getString("EXTRA_LASTNAME");
                    String prenom = extras.getString("EXTRA_FIRSTNAME");
                    String phone = extras.getString("EXTRA_PHONE");

                    TextView t_nom = findViewById(R.id.c_nom);
                    TextView t_prenom = findViewById(R.id.c_prenom);
                    TextView t_phone = findViewById(R.id.c_phone);

                    t_nom.setText(nom);
                    t_prenom.setText(prenom);
                    t_phone.setText(phone);
                    break;
                case "USER_ADDRESS" :
                    String numAdresse = extras.getString("EXTRA_ADDRESSNUM");
                    String streetName = extras.getString("EXTRA_STREETNAME");
                    String postalCode = extras.getString("EXTRA_POSTALCODE");
                    String city = extras.getString("EXTRA_CITY");

                    TextView t_addressNum = findViewById(R.id.c_addressNum);
                    TextView t_streetName = findViewById(R.id.c_streetName);
                    TextView t_postalCode = findViewById(R.id.c_postalCode);
                    TextView t_city = findViewById(R.id.c_city);

                    t_addressNum.setText(numAdresse);
                    t_streetName.setText(streetName);
                    t_postalCode.setText(postalCode);
                    t_city.setText(city);
            }
        }
    }


    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.b_modifierUser :
                loadEditUser();
                break;
            case R.id.b_modifierAddress :
                loadEditAddress();
                break;
        }
    }

    private void loadEditAddress() {
        // startActivityForResult
        TextView t_nom = findViewById(R.id.c_nom);
        TextView t_prenom = findViewById(R.id.c_prenom);
        TextView t_phone = findViewById(R.id.c_phone);

        String nom = t_nom.getText().toString();
        String prenom = t_prenom.getText().toString();
        String phone = t_phone.getText().toString();

        Intent intent = new Intent(this, editUser.class);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_LASTNAME", nom);
        extras.putString("EXTRA_FIRSTNAME", prenom);
        extras.putString("EXTRA_PHONE", phone);

        intent.putExtras(extras);

        startActivity(intent);
    }

    private void loadEditUser() {
        // startActivityForResult
        TextView t_addressNum = findViewById(R.id.c_addressNum);
        TextView t_streetName = findViewById(R.id.c_streetName);
        TextView t_postalCode = findViewById(R.id.c_postalCode);
        TextView t_city = findViewById(R.id.c_city);

        String addressNum = t_addressNum.getText().toString();
        String streetName = t_streetName.getText().toString();
        String postalCode = t_postalCode.getText().toString();
        String city = t_city.getText().toString();

        Intent intent = new Intent(this, editUser.class);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_ADDRESSNUM", addressNum);
        extras.putString("EXTRA_STREETNAME", streetName);
        extras.putString("EXTRA_POSTALCODE", postalCode);
        extras.putString("EXTRA_CITY", city);

        intent.putExtras(extras);

        startActivity(intent);
    }

}
