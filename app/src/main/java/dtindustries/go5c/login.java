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

//hello
public class login extends Activity implements View.OnClickListener {
    private EditText user, pass;
    private Button bLogin, bSignUp;
    // Progress Dialog
    private ProgressDialog pDialog;
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://10.0.2.2/5CGO/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user = (EditText) findViewById(R.id.edtuserid);
        pass = (EditText) findViewById(R.id.edtrpass);
        bLogin = (Button) findViewById(R.id.btLogin);
        bSignUp = (Button) findViewById(R.id.btSignUp);
        bLogin.setOnClickListener(this);
        bSignUp.setOnClickListener(this);

    }

    @Override public void onClick(View v) {
        // TODO Auto-generated method
        switch (v.getId()) {
            case R.id.btLogin:
                String username = user.getText().toString();
                String password = pass.getText().toString();
                new AttemptLogin().execute(username, password); // here we have used, switch case, because on login activity you may //also want to show registration button, so if the user is new ! we can go the //registration activity , other than this we could also do this without switch //case.
            break;
            case R.id.btSignUp:
                startActivity(new Intent(login.this, signup.class));
            break;
        }
    }

    class AttemptLogin extends AsyncTask< String, String, String > { /** * Before starting background thread Show Progress Dialog * */

    boolean failure = false;@Override protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(login.this);
        pDialog.setMessage("Attempting to login...");
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
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params); // checking log for json response
                Log.d("Login attempt", json.toString());
                // success tag for
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Successfully Login!", json.toString());
                    startActivity(new Intent(login.this, main_activity.class));
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
                Toast.makeText(login.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
