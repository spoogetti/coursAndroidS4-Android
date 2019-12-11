package com.example.menuetimage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * L'objectif de cet exercice est d’écrire une application permettant de charger une image stockée sur le périphérique et de l'afficher.
 * L’interface sera simplement constituée d’un bouton (pour permettre le chargement) et de l’image.
 * Dans un premier temps, créez l’interface graphique.
 * Le widget à utiliser pour l’image est ImageView.
 * Compléter le code de l’application pour gérer l’appui sur le bouton de chargement, en faisant afficher un message dans la console.
 * Complétez l’application pour qu’elle émette une intention dont l’action sera ACTION GET CONTENT et le type celui correspondant à une image,
 * qu’elle récupère l’image retournée et l’affiche dans le widget correspondant.
 * •La classe à utiliser pour l’image est ImageView ;
 * •Pour pouvoir charger une image, votre application doit sans doute disposer d’une permission Lorsque le chargement de vos images fonctionne,
 * ajoutez des boutons à votre application, permettant d’effectuer
 * •Un miroir horizontal de l’image
 * •Un miroir vertical de l’image
 */

public class MainActivity extends AppCompatActivity {
    Boolean picOk = false;
    Boolean grayScale = false;
    Boolean inverted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.registerForContextMenu(findViewById(R.id.b_displayMenu));
    }


    public void onClick(View view) { /*
        ImageView imageView = findViewById(R.id.i_mageView);
        switch(view.getId()) {
            case R.id.c_display :
                System.out.println("Bouton image clické");
                createIntent(1);
                break;
            case R.id.b_vMirror :
                if(picOk) {
                    if(imageView.getRotationX() == 180)
                        imageView.setRotationX(0);
                    else
                        imageView.setRotationX(180);
                }
                break;
            case R.id.b_hMirror :
                if(picOk) {
                    if(imageView.getRotationY() == 180)
                        imageView.setRotationY(0);
                    else
                        imageView.setRotationY(180);
                }
                break;
        }
    */}

    private void createIntent(int requestCode) {
        Intent sendIntent;
        sendIntent = new Intent(Intent.ACTION_GET_CONTENT);
        sendIntent.setType("image/png");
        startActivityForResult(sendIntent, requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        ImageView imageView;
        switch (requestCode) {
            case 1 :
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageView = findViewById(R.id.i_mageView);
                    imageView.setImageBitmap(bitmap);
                    picOk = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // Menu dans le coin en haut à droite
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return processMenuOptions(item);
    }

    // Menu qui s'affiche au clic long sur un bouton
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        return processMenuOptions(item);
    }

    private boolean processMenuOptions(MenuItem item) {
        ImageView imageView = findViewById(R.id.i_mageView);
        switch (item.getItemId()) {
            case R.id.m_choosePic:
                createIntent(1);
                return true;
            case R.id.m_vMirror:
                if (picOk) {
                    if (imageView.getRotationX() == 180)
                        imageView.setRotationX(0);
                    else
                        imageView.setRotationX(180);
                }
                return true;
            case R.id.m_hMirror:
                if (picOk) {
                    if (imageView.getRotationY() == 180)
                        imageView.setRotationY(0);
                    else
                        imageView.setRotationY(180);
                }
                return true;
            case R.id.m_invert:
                if(!inverted) {
                    imageView.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
                    inverted = true;
                }
                else {
                    imageView.setColorFilter(null);
                    inverted = false;
                }

                return true;
            case R.id.m_8bit: // https://stackoverflow.com/questions/28308325/androidset-gray-scale-filter-to-imageview
                if(!grayScale) {
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);  //0 means grayscale
                    ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                    imageView.setColorFilter(cf);
                    imageView.setImageAlpha(128);   // 128 = 0.5
                    grayScale = true;
                } else {
                    imageView.setColorFilter(null);
                    imageView.setImageAlpha(255);
                    grayScale = false;
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Color matrix that flips the components (<code>-1.0f * c + 255 = 255 - c</code>)
     * and keeps the alpha intact.
     * https://stackoverflow.com/questions/17841787/invert-colors-of-drawable-android
     */
    private static final float[] NEGATIVE = {
            -1.0f,     0,     0,    0, 255, // red
            0,     -1.0f,     0,    0, 255, // green
            0,         0, -1.0f,    0, 255, // blue
            0,         0,     0, 1.0f,   0  // alpha
    };
}
