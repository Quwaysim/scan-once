package com.quwaysim.scanonce;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button scanButton;
    TextView scanTextView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = findViewById(R.id.btn_scan);
        scanTextView = findViewById(R.id.text_scan);

        //sharedPreferences in private mode
        sharedPreferences = this.getSharedPreferences("ScanPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //getting Date instance
        final Calendar time = Calendar.getInstance();

        /*checking if date has been saved in sharedPref before.
        empty "" is for first time (sharedPref default value) user opens the app, we don't expect
        a savedDate string on first app launch since the use has never scanned before*/
        String savedDate = sharedPreferences.getString("date", "");
        assert savedDate != null;
        //if there's no savedDate
        if (savedDate.equals("")) {
            //enable scan button
            scanButton.setEnabled(true);
            //tell user to please scan
            scanTextView.setText(R.string.scan_text);
        } else {
            //else if there's a date saved in the sharedPref
            //get today's date and...
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String date = dateFormat.format(time.getTime());
            //...compare to the date saved in sharedPref
            //if it's the same, user has scanned today
            //so disable the scan button and tell user to come back tomorrow
            if (date.equals(savedDate)) {
                scanButton.setEnabled(false);
                scanTextView.setText(R.string.scan_text_warning);
            } else {
                //else, the dates don't match so, enable scan button and allow user to scan
                scanButton.setEnabled(true);
                scanTextView.setText(R.string.scan_text);
            }
        }

        //executes only if user hasn't scanned today (button is enabled)
        scanButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on clicking the button, get today's date and save it in sharedPref
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String date = dateFormat.format(time.getTime());
                editor.putString("date", date);
                editor.apply();
                //tell user to come back another day to scan
                scanTextView.setText(R.string.scan_text_warning);
                //disables the scan button
                scanButton.setEnabled(false);
            }
        }));
    }
}