// Author: Anthony Ricco 12/29/2015

package com.attendancetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.attendancetracker.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements OnClickListener {

	// Get the telephone number and device ID from the telephone itself 
	//	final TelephonyManager tMan = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	//	 deviceIDText.setText("DEVICE ID: " + tMan.getDeviceId());
	//	person.setPhoneNum(tMan.getLine1Number());

	final String logTag = "AVRInfoLog";

    DatabaseManager dbmLooperThread;
    
	// Variables used for obtaining scanning results.
	//	private TextView formatTxt, contentTxt, deviceIDText; 
	private Button scanBtn, manualPostBtn;
	private TextView textPostFeedback;
	private EditText textPersonName, textPhoneNum;
	private Person person = new Person();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Variables used for obtaining scanning results.
		//		formatTxt = (TextView)findViewById(R.id.scan_format);
		//		contentTxt = (TextView)findViewById(R.id.scan_content);
		//		deviceIDText = (TextView)findViewById(R.id.device_id);

		scanBtn          = (Button)findViewById(R.id.scan_button);
		textPersonName   = (EditText)findViewById(R.id.textPersonName);
		textPhoneNum     = (EditText)findViewById(R.id.textPhoneNum);
		textPostFeedback = (TextView)findViewById(R.id.textPostFeedback);
		manualPostBtn    = (Button)findViewById(R.id.manualPostBtn);

		scanBtn.setOnClickListener(this);
		manualPostBtn.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
        dbmLooperThread = new DatabaseManager(); 
        dbmLooperThread.start();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.scan_button) {
			// scan
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}

		if (v.getId() == R.id.manualPostBtn) {
			// read manually entered name and phone number
			person.setName(textPersonName.getText().toString());
			person.setPhoneNum(textPhoneNum.getText().toString());

			processUpdateRequest(person);
		} // end if
	}

	public void onActivityResult (int requestCode, int resultCode, Intent intent) {
		// retrieve the scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

		if (scanningResult != null) {
			// we have a result

			// Variables used for obtaining scanning results.
			//String scanFormat = scanningResult.getFormatName();			
			// formatTxt.setText("FORMAT: " + scanFormat);
			// contentTxt.setText("CONTENT: " + scanContent);

			String scanContent = scanningResult.getContents();

			// Parse the scanned result for the name and phone number 
			int startPos = scanContent.indexOf("N:") + "N:".length();
			int endPos = scanContent.indexOf(";");
			person.setName((scanContent.substring(startPos, endPos)));

			startPos = scanContent.indexOf("TEL:") + "TEL:".length();
			person.setPhoneNum(scanContent.substring(startPos, startPos+14));

			processUpdateRequest(person);

		} // end if scanningResult
		else {
			Toast toast = Toast.makeText(getApplicationContext(), "No scan data received", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void processUpdateRequest(Person person) {
		
        if (dbmLooperThread.mHandler != null) {
            Message msg = dbmLooperThread.mHandler.obtainMessage(0, person); 
                        dbmLooperThread.mHandler.sendMessage(msg); 
        }
		
//		textPostFeedback.setText("Entry posted for:  " + person.getName());	
	}

	 protected void onDestroy() {
		 	super.onDestroy();
	        dbmLooperThread.mHandler.getLooper().quit();
	    }
}
