package dtindustries.go5c;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class signup extends Activity implements View.OnClickListener{

    private EditText user, pass, rpass, fname, lname, sch, mj, eml;
    private Button create;
    // Progress Dialog
    private ProgressDialog pDialog;
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String SIGNUP_URL = "http://10.0.2.2/5CGO/signup.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        user = (EditText) findViewById(R.id.edtuser);
        pass = (EditText) findViewById(R.id.edtpass);
        rpass = (EditText) findViewById(R.id.edtrpass);
        fname = (EditText) findViewById(R.id.edtfname);
        lname = (EditText) findViewById(R.id.edtlname);
        sch = (EditText) findViewById(R.id.edtschool);
        mj = (EditText) findViewById(R.id.edtmajor);
        eml = (EditText) findViewById(R.id.edtemail);
        create = (Button) findViewById(R.id.btcreate);
        create.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        // TODO Auto-generated method
        switch (v.getId()) {
            case R.id.btcreate:
                String username = user.getText().toString();
                String password = pass.getText().toString();
                String rpassword = rpass.getText().toString();
                String firstname = fname.getText().toString();
                String lastname = lname.getText().toString();
                String school = sch.getText().toString();
                String major = mj.getText().toString();
                String email = eml.getText().toString();

                new AttemptCreateAccount().execute(username, password, firstname, lastname, school, major, email, rpassword); // here we have used, switch case, because on login activity you may //also want to show registration button, so if the user is new ! we can go the //registration activity , other than this we could also do this without switch //case.
            default: break;
        }
    }

    class AttemptCreateAccount extends AsyncTask< String, String, String > { /** * Before starting background thread Show Progress Dialog * */

    boolean failure = false;@Override protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(signup.this);
        pDialog.setMessage("Creating account");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
        @Override protected String doInBackground(String...args) { // TODO Auto-generated method stub // here Check for success tag
            int success;
            try {
                List< NameValuePair > params = new ArrayList< NameValuePair >();
                params.add(new BasicNameValuePair("username", args[0]));
                params.add(new BasicNameValuePair("password", args[1]));
                params.add(new BasicNameValuePair("firstname", args[2]));
                params.add(new BasicNameValuePair("lastname", args[3]));
                params.add(new BasicNameValuePair("school", args[4]));
                params.add(new BasicNameValuePair("major", args[5]));
                params.add(new BasicNameValuePair("email", args[6]));
                params.add(new BasicNameValuePair("rpassword", args[7]));

                    Log.d("request!", "starting");
                    JSONObject json = jsonParser.makeHttpRequest(SIGNUP_URL, "POST", params); // checking log for json response
                    Log.d("Signup attempt", json.toString());
                    // success tag for
                    success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Created Account!", json.toString());
                    startActivity(new Intent(signup.this, login.class));
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
                Toast.makeText(signup.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}

