package com.signove.health.servicetest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	private NotificationManager notificationManager;


	@Override
	public void onReceive(Context context, Intent intent) {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent intent1 = new Intent(context, HealthServiceTestActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);
		
		Log.w("NOTIFICATION","NOTIFICOU");
		
		Notification n = new NotificationCompat.Builder(context)
				.setContentTitle("Lembrete do MyPressure")
				.setContentText("Clique aqui para medir sua pressao")
				.setSmallIcon(R.drawable.heart).setContentIntent(pIntent)
				.build();

		n.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, n);
	}

}
