package com.example.optiononeweighttrackingappjacobsegarra.sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

/**
 * Utility class responsible for sending SMS notifications.
 * It includes checks for the necessary SEND_SMS permission.
 */
public class SmsNotifier {

    private static final String TAG = "SmsNotifier";

    /**
     * Attempts to send an SMS message if the SEND_SMS permission is granted.
     * @param context The application context.
     * @param phoneNumber The recipient's phone number.
     * @param message The content of the SMS message.
     */
    public static void sendSms(Context context, String phoneNumber, String message) { // ERROR 5 FIX
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                // Using the older, widely compatible SmsManager.
                // Note: PendingIntent is generally required for delivery reports, but for simplicity
                // in this example, we skip it. A real app would track delivery via broadcast receiver.
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(context, "SMS Alert Sent to " + phoneNumber, Toast.LENGTH_LONG).show();
                Log.i(TAG, "SMS successfully sent.");
            } catch (Exception e) {
                Log.e(TAG, "SMS failed to send: " + e.getMessage());
                Toast.makeText(context, "SMS failed to send (Check number format).", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.w(TAG, "SMS permission denied. Cannot send alert.");
            Toast.makeText(context, "SMS alert prevented: Permission denied.", Toast.LENGTH_LONG).show();
        }
    }
}
