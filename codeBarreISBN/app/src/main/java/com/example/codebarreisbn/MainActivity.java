package com.example.codebarreisbn;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 1. Scanner le code barre : On utilisera la bibliothèque zxing
 * (https://github.com/journeyapps/zxing-android-embedded).
 * Je vous laisse lire la documentation sur cette page et tester le scan d’un code barre
 *
 * 2. Créer  une tache  asynchrone  pour  interroger  l’API  Google  avec l’ISBN  récupéré précédemment.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveViews(outState);
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String infosList[] = savedInstanceState.getStringArray("swapScreenBundle");
        if(infosList != null)
            fillTextViews(infosList[0], infosList[1], infosList[2], infosList[3], infosList[4]);
    }

    private void restoreViews() {
        Bundle textViewInfo = new Bundle();
        String infosList[] = textViewInfo.getStringArray("swapScreenBundle");
        if(infosList != null)
            fillTextViews(infosList[0], infosList[1], infosList[2], infosList[3], infosList[4]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("STOP");
        Bundle textViewInfo = new Bundle();

        saveViews(textViewInfo);
    }

    private void saveViews(Bundle textViewInfo) {
        TextView t_titre = findViewById(R.id.t_titre);
        TextView t_auteur = findViewById(R.id.t_auteur);
        TextView t_annee = findViewById(R.id.t_annee);
        TextView t_publisher = findViewById(R.id.t_editeur);
        TextView t_language = findViewById(R.id.t_langue);

        String title = (String) t_titre.getText();
        String auteur = (String) t_auteur.getText();
        String annee = (String) t_annee.getText();
        String editeur = (String) t_publisher.getText();
        String language = (String) t_language.getText();

        String[] infosList = new String[5];
        infosList[0] = title;
        infosList[1] = auteur;
        infosList[2] = annee;
        infosList[3] = editeur;
        infosList[4] = language;

        textViewInfo.putStringArray("swapScreenBundle", infosList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("PAUSE");

        Bundle textViewInfo = new Bundle();
        saveViews(textViewInfo);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("RESTART");

        restoreViews();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_scan :
                new IntentIntegrator(this).initiateScan();
                break;
            case R.id.b_historique:

                break;
        }
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                launchAPI(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void launchAPI(String result) {
        GoogleApiRequest asyncTask = new GoogleApiRequest();
        asyncTask.execute(result);
    }

    // Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
    public class GoogleApiRequest extends AsyncTask<String, Object, JSONObject> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected JSONObject doInBackground(String... isbns) {
            // Stop if cancelled
            if(isCancelled()){
                return null;
            }

            String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbns[0];
            try{
                HttpURLConnection connection = null;
                // Build Connection.
                try{
                    URL url = new URL(apiUrlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000); // 5 seconds
                    connection.setConnectTimeout(5000); // 5 seconds
                } catch (MalformedURLException e) {
                    // Impossible: The only two URLs used in the app are taken from string resources.
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    // Impossible: "GET" is a perfectly valid request method.
                    e.printStackTrace();
                }
                int responseCode = connection.getResponseCode();
                if(responseCode != 200){
                    Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                    connection.disconnect();
                    return null;
                }

                // Read data from response.
                StringBuilder builder = new StringBuilder();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = responseReader.readLine();
                while (line != null){
                    builder.append(line);
                    line = responseReader.readLine();
                }
                String responseString = builder.toString();
                Log.d(getClass().getName(), "Response String: " + responseString);
                JSONObject responseJson = new JSONObject(responseString);
                // Close connection and return response code.
                connection.disconnect();
                return responseJson;
            } catch (SocketTimeoutException e) {
                Log.w(getClass().getName(), "Connection timed out. Returning null");
                return null;
            } catch(IOException e){
                Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject responseJson) {
            if(isCancelled()){
                // Request was cancelled due to no network connection.
                System.out.println("Cancelled");
                //showNetworkDialog();
            } else if(responseJson == null){
                System.out.println("Null response");
            }
            else{
                String title = new String();
                String author = new String();
                String publisher = new String();
                String publishedDate = new String();
                String language = new String();

                try {
                    JSONObject jsonObj = new JSONObject(responseJson.toString());

                    // Getting JSON Array node
                    JSONArray items = jsonObj.getJSONArray("items");
                    JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo"); // volumeInfo
                    title = getVolumeInfo(volumeInfo, "title");
                    publisher = getVolumeInfo(volumeInfo, "publisher");
                    publishedDate = getVolumeInfo(volumeInfo, "publishedDate");
                    language = getVolumeInfo(volumeInfo, "language");
                    JSONArray authors = (JSONArray) volumeInfo.get("authors");
                    author = (String) authors.get(0);

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                fillTextViews(title, author, publisher, publishedDate, language);

            }
        }



        private String getVolumeInfo(JSONObject volumeInfo, final String info) throws JSONException {
            String result = "";
            try {
                result = (String) volumeInfo.get(info);
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return result;
        }
    }

    private void fillTextViews(String title, String author, String publisher, String publishedDate, String language) {
        TextView t_titre = findViewById(R.id.t_titre);
        t_titre.setText(title);

        TextView t_auteur = findViewById(R.id.t_auteur);
        t_auteur.setText(author);

        TextView t_annee = findViewById(R.id.t_annee);
        t_annee.setText(publishedDate);

        TextView t_publisher = findViewById(R.id.t_editeur);
        t_publisher.setText(publisher);

        TextView t_language = findViewById(R.id.t_langue);
        t_language.setText(language);
    }
}



