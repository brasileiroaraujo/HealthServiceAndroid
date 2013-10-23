package com.signove.health.servicetest;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

import com.signove.health.service.HealthAgentAPI;
import com.signove.health.service.HealthServiceAPI;

public class HealthServiceTestActivity extends Activity {
	int [] specs = {0x1004};
	HealthServiceAPI api;

	TextView status;
	TextView menssage;
	TextView device;
	TextView data;
	
	private HealthAgentAPI.Stub agent;

	Map <String, String> map;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.w("HST", "Service connection established");

			// that's how we get the client side of the IPC connection
			api = HealthServiceAPI.Stub.asInterface(service);
			agent = new AgentHealth(device, data, menssage, map, api).getAgent();
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

		setContentView(R.layout.main);
		
		OnLongClickListener l = new OnLongClickListener() {
		    public boolean onLongClick(View v) {
		    	finish();
		    	return true;
		    }
		};
		
		status = (TextView) findViewById(R.id.status);
		menssage = (TextView) findViewById(R.id.msg);
		device = (TextView) findViewById(R.id.device);
		data = (TextView) findViewById(R.id.data);

		status.setOnLongClickListener(l);
		menssage.setOnLongClickListener(l);
		device.setOnLongClickListener(l);
		data.setOnLongClickListener(l);

		map = new HashMap<String, String>();

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
}
