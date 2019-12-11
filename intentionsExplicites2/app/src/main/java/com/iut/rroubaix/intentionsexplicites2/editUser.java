package com.iut.rroubaix.intentionsexplicites2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Une seconde activité, déclenchée par appui du bouton Modifier de la partie supérieure,
 * permettant à l'utilisateur de modifier la valeur des différents champs concernant son
 * identité. Deux boutons seront présents, l'un permettant de valider les modifications
 * saisies (renvoyées à l'activité principale) et l'autre permettant d'annuler ces
 * modifications et de revenir à l'activité principale ;
 */

public class editUser extends Activity {
    private String lastName;
    private String firstName;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Bundle extras = getIntent().getExtras();

        this.setLastName(extras.getString("EXTRA_LASTNAME"));
        this.setFirstName(extras.getString("EXTRA_FIRSTNAME"));
        this.setPhone(extras.getString("EXTRA_PHONE"));

        TextView t_nom = findViewById(R.id.c_nom);
        TextView t_prenom = findViewById(R.id.c_prenom);
        TextView t_phone = findViewById(R.id.c_phone);

        t_nom.setText(this.getLastName());
        t_prenom.setText(this.getFirstName());
        t_phone.setText(this.getPhone());
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.b_valid:
                validLoadMainAct();
                break;
            case R.id.b_cancel:
                cancelLoadMainAct();
                break;
        }
    }

    private void validLoadMainAct() {
        TextView t_nom = findViewById(R.id.c_nom);
        TextView t_prenom = findViewById(R.id.c_prenom);
        TextView t_phone = findViewById(R.id.c_phone);

        Intent intent = new Intent(this, MainActivity.class);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_LASTNAME", t_nom.getText().toString());
        extras.putString("EXTRA_FIRSTNAME", t_prenom.getText().toString());
        extras.putString("EXTRA_PHONE", t_phone.getText().toString());

        extras.putString("TYPE_ENVOI", "USER_INFO");

        intent.putExtras(extras);

        startActivity(intent);
    }

    private void cancelLoadMainAct() {
        Intent intent = new Intent(this, MainActivity.class);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_LASTNAME", this.getLastName());
        extras.putString("EXTRA_FIRSTNAME", this.getLastName());
        extras.putString("EXTRA_PHONE", this.getPhone());

        extras.putString("TYPE_ENVOI", "USER_INFO");

        intent.putExtras(extras);

        startActivity(intent);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
