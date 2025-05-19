package edu.polytech.balancetalan;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class NotificationService extends FirebaseMessagingService {
    private final static String TAG = "balance ";
    public static final String CHANNEL_1_ID = "channel LOW";
    public static final String CHANNEL_2_ID = "channel DEFAULT";
    public static final String CHANNEL_3_ID = "channel HIGH";
    private Uri uriSound;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d( TAG, "Service created" );
        // create NotificationChannel, only pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            //remove existing channels
            notificationManager.deleteNotificationChannel(CHANNEL_1_ID);
            notificationManager.deleteNotificationChannel(CHANNEL_2_ID);
            notificationManager.deleteNotificationChannel(CHANNEL_3_ID);
            //create channels
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1",  NotificationManager.IMPORTANCE_LOW);
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Channel 2",  NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3_ID, "Channel 3",  NotificationManager.IMPORTANCE_HIGH);
            //add descripotions
            channel1.setDescription("This Channel has low priority");
            channel2.setDescription("This Channel has default priority");
            channel3.setDescription("This Channel has high priority");
            //add sound effect
            uriSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.mj);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel3.setSound(uriSound, audioAttributes); // apply sound here
            // register the channels
            notificationManager.createNotificationChannels(List.of(channel1,channel2,channel3));
            Log.d( TAG, "channels created " );
            Log.d(TAG, "uriSound = "+uriSound);
        }
        else{
            Log.d( TAG, "channels NOT created: only pour API 26+" );
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "message received");
        if ( remoteMessage.getNotification() != null ) {
            //callback on clic on the notification  --> MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(getString(R.string.title), remoteMessage.getNotification().getTitle());
            intent.putExtra(getString(R.string.message), remoteMessage.getNotification().getBody());
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            // send notification
            send( pendingIntent, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            Log.d(TAG, "title: " + remoteMessage.getNotification().getTitle());
        }
        else {
            Log.d(TAG, "message NULL");
        }
    }

    private void send(PendingIntent pendingIntent, String title, String message){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent) // <- très important !
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(uriSound);
            notificationManager.notify(1, builder.build());
        } else {  // not have the permission
            Log.d(TAG, "I don't have the permission :/");
        }
    }
}