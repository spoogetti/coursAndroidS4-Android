package com.iut.rroubaix.intentionsexplicites2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Une troisième activité, fonctionnant de manière similaire, sur les informations
 * concernant l'adresse de l'utilisateur.
 */
public class editAddress extends Activity {
    private String addressNum;
    private String streetName;
    private String postalCode;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        Bundle extras = getIntent().getExtras();

        this.setAddressNum(extras.getString("EXTRA_LASTNAME"));
        this.setStreetName(extras.getString("EXTRA_FIRSTNAME"));
        this.setPostalCode(extras.getString("EXTRA_PHONE"));
        this.setCity(extras.getString("EXTRA_PHONE"));

        TextView t_addressNum = findViewById(R.id.c_addressNum);
        TextView t_streetName = findViewById(R.id.c_streetName);
        TextView t_postalCode = findViewById(R.id.c_postalCode);
        TextView t_city = findViewById(R.id.c_city);

        t_addressNum.setText(this.getAddressNum());
        t_streetName.setText(this.getStreetName());
        t_postalCode.setText(this.getPostalCode());
        t_city.setText(this.getCity());
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
        TextView t_addressNum = findViewById(R.id.c_addressNum);
        TextView t_streetName = findViewById(R.id.c_streetName);
        TextView t_postalCode = findViewById(R.id.c_postalCode);
        TextView t_city = findViewById(R.id.c_city);

        Intent intent = new Intent(this, MainActivity.class);

        Bundle extras = new Bundle();

        extras.putString("EXTRA_ADDRESSNUM", t_addressNum.getText().toString());
        extras.putString("EXTRA_STREETNAME", t_streetName.getText().toString());
        extras.putString("EXTRA_POSTALCODE", t_postalCode.getText().toString());
        extras.putString("EXTRA_CITY", t_city.getText().toString());

        extras.putString("TYPE_ENVOI", "ADDRESS_INFO");

        intent.putExtras(extras);

        startActivity(intent);
    }

    private void cancelLoadMainAct() {
        Intent intent = new Intent(this, MainActivity.class);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_ADDRESSNUM", this.getAddressNum());
        extras.putString("EXTRA_STREETNAME", this.getStreetName());
        extras.putString("EXTRA_POSTALCODE", this.getPostalCode());
        extras.putString("EXTRA_CITY", this.getCity());

        extras.putString("TYPE_ENVOI", "USER_INFO");

        intent.putExtras(extras);

        startActivity(intent);
    }

    public String getAddressNum() {
        return addressNum;
    }

    public void setAddressNum(String addressNum) {
        this.addressNum = addressNum;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
