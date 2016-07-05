package com.r2m.clima;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rdasilvar on 05/07/2016.
 */
public class ObterClima extends AsyncTask {

    private String urlString = null;
    private ObterClimaListener listener = null;
    private JSONObject jsonObj;
    public ObterClima(ObterClimaListener listener, String url){
        this.urlString = url;
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL(urlString); //Adoto que a urlString é do INPE e já vem com latitude e longitude
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            //INPE fornece os dados em ISO-8859-1
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"));
            StringBuilder builder = new StringBuilder();
            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            try {
                jsonObj = XML.toJSONObject(builder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
            System.out.println("JSON > "+jsonObj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Chegou no final da thread");
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        System.out.println("Aqui no post execute...");
        try {
            listener.jSONObtido((JSONObject)jsonObj.get("cidade"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
