// Author: Anthony Ricco 12/29/2015
// Used this reference: https://www.safaribooksonline.com/library/view/efficient-android-threading/9781449364120/ch04.html#CO1-6

package com.attendancetracker;

import java.net.URLEncoder;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class DatabaseManager extends Thread {

	private final String logTag = "AVRInfoLog";

	public Handler mHandler;

    public void run() {
            Looper.prepare();
            mHandler = new Handler() { 
                    public void handleMessage(Message msg) { 
                            if(msg.what == 0) {
                            	Person p = (Person) msg.obj;
                            	postData(p);
                            }
                    }
            };
            Looper.loop();
    }
    
	private void postData(Person person) {

		Thread t = Thread.currentThread();
		long l = t.getId();
		String name = t.getName();
		// URL for the old form 
//		String fullUrl = "https://docs.google.com/forms/d/1SI-qO4ocEMPtC4mdDiwDeHS_6yPtpUaz3RsVc8STI6A/formResponse";
		// URL for the new form called Attendance Tracker
		String fullUrl = "https://docs.google.com/forms/d/1U0ri7PsC0RNADRfy9txxjpPmkTbTurX8d-ufkS91Az0/formResponse";
		HttpRequest mReq = new HttpRequest();

		@SuppressWarnings("deprecation")
		// The following are the formats for the OLD Google Forms version.
//		String data = "entry_1397604506=" + URLEncoder.encode(person.getName()) + "&"  +
//				"entry_472008123=" + URLEncoder.encode(person.getPhoneNum());
		// The following are the formats for the new Google Forms version.
		String data = "entry.121767134=" + URLEncoder.encode(person.getName()) + "&"  +
				"entry.1669406896=" + URLEncoder.encode(person.getPhoneNum());
		String response = mReq.sendPost(fullUrl, data);
		Log.i(logTag, response);
	}

}
