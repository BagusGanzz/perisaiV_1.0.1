package com.studioninja.locker.lock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.studioninja.locker.util.Log;

import java.util.Calendar;


public class LolipopReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            Log.e("LolipopReciever" ," MD ************ onReceive");
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Calendar calendar = Calendar.getInstance();
                Intent myIntent = new Intent(context, LolipopReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
               // alarmManager.set(AlarmManager.ELAPSED_REALTIME, -10, pendingIntent);
                startLockService(context);

            }
         }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void startLockService(Context context) {
        Log.i("LolipopReciever" ," MD ************ startService");
        /*if (!AppLockService.isRunning(context)) {
            Intent in = new Intent(context, AppLockService.class);
            context.startService(in);
        }*/
        if (AppLockService.isRunning(context)) {
            Intent in = new Intent(context, AppLockService.class);
            in.setAction(AppLockService.ACTION_START);
            context.startService(in);
        }

    }
}
