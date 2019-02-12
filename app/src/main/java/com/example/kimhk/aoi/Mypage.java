package com.example.kimhk.aoi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kimhk on 2019-01-19.
 */

public class Mypage extends Activity {
    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_ID="id";
    private static final String TAG_NAME="name";
    private static final String TAG_AGE="age";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    ListView list;
    Button btn_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getData("http://jun6726.cafe24.com/php_connection.php"); //수정 필요
    }
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String image = c.getString(TAG_IMAGE);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String age = c.getString(TAG_AGE);


                HashMap<String, String> persons = new HashMap<String, String>();

                persons.put(TAG_IMAGE, image);
                persons.put(TAG_ID, id);
                persons.put(TAG_NAME, name);
                persons.put(TAG_AGE, age);


                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Mypage.this, personList, R.layout.list_item,
                    new String[]{TAG_IMAGE, TAG_ID, TAG_NAME, TAG_AGE},
                    new int[]{R.id.imageView, R.id.id, R.id.name, R.id.age}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }


}
