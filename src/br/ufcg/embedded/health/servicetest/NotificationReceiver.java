package br.ufcg.embedded.health.servicetest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import br.ufcg.embedded.health.service.health.servicetest.R;

public class NotificationReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, SplashScreen.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1,
                0);

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.heart_80px);

        Notification n = new NotificationCompat.Builder(context)
                .setContentTitle("CheckMyPressure Reminder")
                .setContentText("Click here to check your pressure today")
                .setSmallIcon(R.drawable.heart).setLargeIcon(bm)
                .setContentIntent(pIntent).build();

        n.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, n);
    }

}
