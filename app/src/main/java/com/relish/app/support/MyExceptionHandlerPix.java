package com.relish.app.support;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.app.NotificationCompat;

//import com.google.android.gms.measurement.AppMeasurement;
import com.relish.app.Activity.PolishHomeActivity;
import com.relish.app.MyApp;


public class MyExceptionHandlerPix implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandlerPix(Activity a) {
        this.activity = a;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = null;
        if (this.activity != null) {
            intent = new Intent(this.activity, PolishHomeActivity.class);
        } else if (MyApp.getContext() != null) {
            intent = new Intent(MyApp.getContext(), PolishHomeActivity.class);
        }
//        intent.putExtra(AppMeasurement.CRASH_ORIGIN, true);
        intent.addFlags(335577088);
//        ((AlarmManager) MyApp.getContext().getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC, System.currentTimeMillis() + 10, PendingIntent.getActivity(MyApp.getContext(), 0, intent,0));
        System.exit(2);
    }
}
