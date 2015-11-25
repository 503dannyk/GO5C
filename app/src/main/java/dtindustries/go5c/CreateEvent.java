package dtindustries.go5c;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class CreateEvent extends Activity implements View.OnClickListener{

    private EditText title, description, location, date, category, starttime, endtime;
    private Button create;
    private CheckBox checkPrivate;
    // Progress Dialog
    private ProgressDialog pDialog;
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String SIGNUP_URL = "http://10.0.2.2/5CGO/createEvent.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        title = (EditText) findViewById(R.id.eventTitle);
        description = (EditText) findViewById(R.id.description);
        location = (EditText) findViewById(R.id.location);
        date = (EditText) findViewById(R.id.date);
        starttime = (EditText) findViewById(R.id.starttime);
        endtime = (EditText) findViewById(R.id.endtime);
        category = (EditText) findViewById(R.id.category);
        create = (Button) findViewById(R.id.createEventbt);
        create.setOnClickListener(this);
        checkPrivate = (CheckBox) findViewById(R.id.checkPrivate);
    }

    @Override public void onClick(View v) {
        // TODO Auto-generated method
        boolean checked = checkPrivate.isChecked();
         String intChecked;
        switch (v.getId()) {
            case R.id.createEventbt:
                String titl = title.getText().toString();
                String descrp = description.getText().toString();
                String loc = location.getText().toString();
                String dt = date.getText().toString();
                String cat = category.getText().toString();
                String starttim = starttime.getText().toString();
                String endtim = endtime.getText().toString();
                loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                String user = loginPreferences.getString("username", "");

                if(checked){
                    intChecked = "one";
                }
                else{
                    intChecked = "zero";
                }

                new AttemptCreateAccount().execute(titl, descrp, loc, dt, cat, starttim, endtim, intChecked, user); // here we have used, switch case, because on login activity you may //also want to show registration button, so if the user is new ! we can go the //registration activity , other than this we could also do this without switch //case.
            default: break;

        }
    }

    class AttemptCreateAccount extends AsyncTask< String, String, String > { /** * Before starting background thread Show Progress Dialog * */

    boolean failure = false;@Override protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(CreateEvent.this);
        pDialog.setMessage("Creating Event...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
        @Override protected String doInBackground(String...args) { // TODO Auto-generated method stub // here Check for success tag
            int success;
            try {
                List< NameValuePair > params = new ArrayList< NameValuePair >();
                params.add(new BasicNameValuePair("title", args[0]));
                params.add(new BasicNameValuePair("description", args[1]));
                params.add(new BasicNameValuePair("location", args[2]));
                params.add(new BasicNameValuePair("date", args[3]));
                params.add(new BasicNameValuePair("category", args[4]));
                params.add(new BasicNameValuePair("stime", args[5]));
                params.add(new BasicNameValuePair("endtime", args[6]));
                params.add(new BasicNameValuePair("private", args[7]));
                params.add(new BasicNameValuePair("user", args[8]));

                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest(SIGNUP_URL, "POST", params); // checking log for json response
                Log.d("Create event attempt", json.toString());
                // success tag for
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Created Event!", json.toString());
                    startActivity(new Intent(CreateEvent.this, main_activity.class));
                    finish(); // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        } /** * Once the background process is done we need to Dismiss the progress dialog asap * **/
        protected void onPostExecute(String message) {
            pDialog.dismiss();
            if (message != null) {
                Toast.makeText(CreateEvent.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}

