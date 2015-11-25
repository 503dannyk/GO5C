package dtindustries.go5c;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class main_activity extends Activity implements View.OnClickListener {

    private Button createEvent;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_activity);
            createEvent = (Button) findViewById(R.id.createEventbt);
            createEvent.setOnClickListener(this);
        }
    @Override public void onClick(View v) {
        // TODO Auto-generated method
        switch (v.getId()) {
            case R.id.createEventbt:
                startActivity(new Intent(main_activity.this, CreateEvent.class));
                break;

        }
    }
}
