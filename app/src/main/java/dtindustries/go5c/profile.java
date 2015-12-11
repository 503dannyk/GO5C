package dtindustries.go5c;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.TextView;

public class profile extends Activity implements View.OnClickListener {

    EditText txtPass, txtFirst, txtLast, txtSchool, txtMajor;
    TextView txtUsername;
    Button btnSave, btnAttending, btnHosting;
    private SharedPreferences loginPreferences;

    // Progress Dialog
    private ProgressDialog pDialog;

    String username;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_profile = "http://10.0.2.2/5CGO/profile.php";

    // url to update product
    private static final String url_update_profile = "http://10.0.2.2/5CGO/updateProfile.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROFILE = "userinfo";
    private static final String TAG_USER = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_FIRSTNAME = "firstname";
    private static final String TAG_LASTNAME = "lastname";
    private static final String TAG_SCHOOL = "school";
    private static final String TAG_MAJOR = "major";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAttending = (Button) findViewById(R.id.btnAttending);
        btnHosting = (Button) findViewById(R.id.btnHosting);
        btnAttending.setOnClickListener(this);
        btnHosting.setOnClickListener(this);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = loginPreferences.getString("username", "");

        // Getting complete product details in background thread
        new GetProductDetails().execute(username);

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String password = txtPass.getText().toString();
                String firstname = txtFirst.getText().toString();
                String lastname = txtLast.getText().toString();
                String school = txtSchool.getText().toString();
                String major = txtMajor.getText().toString();
                // starting background task to update product
                new SaveProductDetails().execute(username, password, firstname, lastname, school, major);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAttending:
                startActivity(new Intent(profile.this, eventsAttending.class));
                break;
            case R.id.btnHosting:
                startActivity(new Intent(profile.this, eventsHosting.class));
                break;

        }

    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, JSONObject> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(profile.this);
            pDialog.setMessage("Loading profile info. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected JSONObject doInBackground(String... args) {

            JSONObject product = null;
            // updating UI from Background Thread
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username", args[0]));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(url_profile, "GET", params);

                        // check your log for json response
                        Log.d("Profile Details", json.toString());


                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json.getJSONArray(TAG_PROFILE); // JSON Array

                            // get first product object from JSON Array
                            product = productObj.getJSONObject(0);

                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



            return product;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(JSONObject product) {
            if (product != null) {
                txtUsername = (TextView) findViewById(R.id.username);
                txtPass = (EditText) findViewById(R.id.inputPassword);
                txtFirst = (EditText) findViewById(R.id.inputFirstName);
                txtLast = (EditText) findViewById(R.id.inputLastname);
                txtSchool = (EditText) findViewById(R.id.inputSchool);
                txtMajor = (EditText) findViewById(R.id.inputMajor);

                try {
                    // display product data in EditText
                    txtUsername.setText(product.getString(TAG_USER));
                    txtPass.setText(product.getString(TAG_PASSWORD));
                    txtFirst.setText(product.getString(TAG_FIRSTNAME));
                    txtLast.setText(product.getString(TAG_LASTNAME));
                    txtSchool.setText(product.getString(TAG_SCHOOL));
                    txtMajor.setText(product.getString(TAG_MAJOR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(profile.this);
            pDialog.setMessage("Saving profile info ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // getting updated data from EditTexts


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_USER, args[0]));
            params.add(new BasicNameValuePair(TAG_PASSWORD, args[1]));
            params.add(new BasicNameValuePair(TAG_FIRSTNAME, args[2]));
            params.add(new BasicNameValuePair(TAG_LASTNAME, args[3]));
            params.add(new BasicNameValuePair(TAG_SCHOOL, args[4]));
            params.add(new BasicNameValuePair(TAG_MAJOR, args[5]));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_profile, "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

}