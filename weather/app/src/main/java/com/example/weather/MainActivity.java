package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * La réponse fournie par le service web est bien au format JSON.
 * Pour  vous  familiariser  avec  les  taches asynchrones  et  le  JSON,
 * créez une  activité  (avec  un  bouton  et  un Textview) et votre classe asynchrone Getdata.
 * L’appui sur le bouton appellera la classe asynchrone qui  retournera  la  «météo»  de  la  ville  passée  en paramètre.
 * Analysez(parsez) le résultat à l'aide de la classe JSONObject
 * Affichez quelques éléments dans LogCat pour vérifier que l’analyse fonctionne.
 *
 * Modifier votre classe Getdata pour récupérer  ces données météo
 * (N’oubliez pas votre clé d’API et le format, donclisez rapidement la docde l’API ☺).
 * Maintenant, dans l’activité  principale,  créer  un  EditTtext  pour  saisir  la  ville  et  un  spinner  pour  pouvoir réutiliser les villes déjà saisies.
 * Pour cela, on utilisera les shared preferences pour sauvegarder les villes et peupler le spinner de sélection.
 * Ainsi, lors de l’initialisation de l’application le spinner est déjà peuplé des villes précédentes et on ajoute systématiquement au spinner les nouvelles recherches.
 * Maintenant  lors  du  parsing,  vous  devez au  moins récupérer
 *
 * Pour récupérer, l’image associée à cette URL, vous devrez sans doute utiliser une autre tâche asynchrone.
 * Il vous reste à mettre en forme votre application
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String cities = sharedPref.getString("cities", "");
        String[] citiez = new String[0];

        if(!cities.isEmpty()) {
            citiez = cities.split(",");
        }

        Spinner spin = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, citiez);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_request :
                TextView city = findViewById(R.id.s_city);
                String textViewText = city.getText().toString();

                Spinner spin = findViewById(R.id.spinner);
                String spinText = "";
                if(spin.getSelectedItem() != null)
                    spinText = spin.getSelectedItem().toString();

                if(!spinText.isEmpty()) {
                    launchAPI(spinText);
                } else {
                    launchAPI(textViewText);
                }

                if(!city.getText().toString().isEmpty()) {
                    SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String cities = sharedPref.getString("cities", "");
                    editor.putString("cities", cities.concat("," + city.getText().toString()));
                    editor.commit();
                }
                break;
        }
    }

    private void launchAPI(String result) {
        ApiRequest asyncTask = new ApiRequest();
        asyncTask.execute(result);
    }

    public class ApiRequest extends AsyncTask<String, JSONObject, JSONObject> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected JSONObject doInBackground(String... city) {
            // Stop if cancelled
            if(isCancelled()){
                return null;
            }

            String apiUrlString = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=9d3bcf9f45d14b9392e124624191402&q=Talence&num_of_days=1&tp=3&format=json";
            if(!city[0].isEmpty()) {
                apiUrlString = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=9d3bcf9f45d14b9392e124624191402&q=" + city[0] + "&num_of_days=1&tp=3&format=json";
            }

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
                    Log.w(getClass().getName(), "Request failed. Response Code: " + responseCode);
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
                Log.d(getClass().getName(), "IOException when connecting to API.");
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.d(getClass().getName(), "JSONException when connecting to  API.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject responseJson) {
            if(isCancelled()){
                // Request was cancelled due to no network connection.
                System.out.println("Cancelled");
            } else if(responseJson == null){
                System.out.println("Null response");
            }
            else{
                try {
                    JSONObject jsonObj = new JSONObject(responseJson.toString());

                    // la  température  ambiante  "temp_C",
                    // la description "weatherDesc" et l’adresse de l’icône "weatherIconUrl"

                    JSONObject data = (JSONObject) jsonObj.get("data");
                    JSONObject current_condition = (JSONObject) data.getJSONArray("current_condition").get(0);
                    String temp_C = getInfo( current_condition, "temp_C");

                    JSONObject weatherDesc = (JSONObject) current_condition.getJSONArray("weatherDesc").get(0);
                    String desc = (String) weatherDesc.get("value");

                    JSONObject weatherIconUrl = (JSONObject) current_condition.getJSONArray("weatherIconUrl").get(0);
                    String iconUrl = (String) weatherIconUrl.get("value");

                    TextView textViewTemp = findViewById(R.id.c_temp);
                    TextView textViewMeteo = findViewById(R.id.c_desc);

                    textViewTemp.setText(temp_C);
                    textViewMeteo.setText(desc);

                    GetData getIcon = new GetData();
                    getIcon.execute(iconUrl);

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
            }
        }



        private String getInfo(JSONObject jsonInfo, final String info) throws JSONException {
            String result = "";
            try {
                result = (String) jsonInfo.get(info);
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage() + info,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return result;
        }
    }

    class GetData extends AsyncTask<String, String, Drawable> {

        @Override
        protected Drawable doInBackground(String... uri) {
            return LoadImageFromWebOperations(uri[0]);
        }

        public Drawable LoadImageFromWebOperations(String url) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable d = Drawable.createFromStream(is, "weatherIcon");
                return d;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            ImageView imageView = findViewById(R.id.imageView);
            Bitmap anImage      = ((BitmapDrawable) result).getBitmap();
            imageView.setImageBitmap(anImage);
        }
    }
}
