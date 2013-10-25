package com.signove.health.servicetest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.signove.health.database.HealthDAO;
import com.signove.health.service.HealthAgentAPI;
import com.signove.health.service.HealthServiceAPI;
import com.signove.health.structures.HealthData;

public class HealthServiceTestActivity extends Activity {
	private static final String APP_PREF_FILE = "appPreferences";
	private static final String NOTIFICATION_ACTIVE = "notificationActive";
	private static final String NOTIFICATION_HOUR = "notificationHour";
	private static final String NOTIFICATION_MINUTE = "notificationMinute";
	
	int [] specs = {0x1004};
	HealthServiceAPI api;
	private HealthServiceTestActivity frame = this;

	TextView status;
	TextView menssage;
	TextView device;
	TextView data;
	TextView history;
	CheckBox cbNotification;
	TimePicker timePicker; 
	Button btnOk;
	Button btnClearHistory;
	
	private HealthAgentAPI.Stub agent;

	Map <String, String> map;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.w("HST", "Service connection established");

			// that's how we get the client side of the IPC connection
			api = HealthServiceAPI.Stub.asInterface(service);
			agent = new AgentHealth(device, data, menssage, map, api, frame).getAgent();
			try {
				Log.w("HST", "Configuring...");
				api.ConfigurePassive(agent, specs);
			} catch (RemoteException e) {
				Log.e("HST", "Failed to add listener", e);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.w("HST", "Service connection closed");
		}
	};


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		SharedPreferences sharedPreferences = 
                 getSharedPreferences(APP_PREF_FILE, MODE_PRIVATE);
	    boolean notification = sharedPreferences.getBoolean(NOTIFICATION_ACTIVE, true);
	    if (notification) {        
	       //TODO theo
	    } 
	    
		OnLongClickListener l = new OnLongClickListener() {
		    @Override
            public boolean onLongClick(View v) {
		    	finish();
		    	return true;
		    }
		};
		
		OnClickListener o = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.checkBoxNotifications:
					if(cbNotification.isChecked()){
						timePicker.setEnabled(true);
						btnOk.setEnabled(true);
					}else{
						timePicker.setEnabled(false);
						btnOk.setEnabled(false);
					}
					break;
				case R.id.btnOk:
					if(cbNotification.isChecked()){
						setNotificationActive(cbNotification.isChecked(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
						Toast.makeText(getApplicationContext(), "Notifications preferences saved with success", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.btnClearHistory:
					//TODO put clear history
					Toast.makeText(getApplicationContext(), "Cleaned history", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				
			}
		};
		
		TabHost tabs=(TabHost)findViewById(R.id.tabhost); 
		tabs.setup(); 
		TabHost.TabSpec spec=tabs.newTabSpec("tag1"); 
		spec.setContent(R.id.data); 
		spec.setIndicator("Data"); 
		tabs.addTab(spec); 
		spec=tabs.newTabSpec("tag2"); 
		spec.setContent(R.id.history); 
		spec.setIndicator("History"); 
		tabs.addTab(spec); 
		spec=tabs.newTabSpec("tag3"); 
		spec.setContent(R.id.settings); 
		spec.setIndicator("Settings"); 
		tabs.addTab(spec); 
		tabs.setCurrentTab(0); 
		
	    ListView list = (ListView) findViewById(R.id.listViewDataHistory);
        status = (TextView) findViewById(R.id.tvSugestion);
		menssage = (TextView) findViewById(R.id.tvMsg);
		device = (TextView) findViewById(R.id.tVDevice);
		data = (TextView) findViewById(R.id.tVMeasurement);
		history = (TextView) findViewById(R.id.tvHistoryEmpty);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		cbNotification = (CheckBox) findViewById(R.id.checkBoxNotifications);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnClearHistory = (Button) findViewById(R.id.btnClearHistory);
		
		status.setOnLongClickListener(l);
		menssage.setOnLongClickListener(l);
		device.setOnLongClickListener(l);
		data.setOnLongClickListener(l);
		cbNotification.setOnClickListener(o);
		cbNotification.setChecked(notification);
	    btnOk.setOnClickListener(o);
	    btnClearHistory.setOnClickListener(o);
	    
		map = new HashMap<String, String>();

	    List<HealthData> datasHistory = HealthDAO.getInstance(this).ListAll();
        if(datasHistory.size() == 0){
        	history.setText("History empty");
        }else{
        	HealthDataAdapter adapter = new HealthDataAdapter(getApplicationContext(), datasHistory);
            list.setAdapter(adapter);
        }
	        
		Intent intent = new Intent("com.signove.health.service.HealthService");
		startService(intent);
		bindService(intent, serviceConnection, 0);
		Log.w("HST", "Activity created");

		status.setText("Ready");
		menssage.setText("--");
		device.setText("--");
		data.setText("--");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try {
			Log.w("HST", "Unconfiguring...");
			api.Unconfigure(agent);
		} catch (Throwable t) {
			Log.w("HST", "Erro tentando desconectar");
		}
		Log.w("HST", "Activity destroyed");
		unbindService(serviceConnection);

	}
	
	private void setNotificationActive(boolean value, Integer hour, Integer minute) {
		  SharedPreferences sharedPreferences =     
		      getSharedPreferences(APP_PREF_FILE, MODE_PRIVATE);
		  SharedPreferences.Editor editor = sharedPreferences.edit();      
		  editor.putBoolean(NOTIFICATION_ACTIVE, value); 
		  editor.putInt(NOTIFICATION_HOUR, hour);   
		  editor.putInt(NOTIFICATION_MINUTE, minute);   
		  editor.commit();   
	}
	
}
