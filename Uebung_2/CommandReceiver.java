package com.example.uebung02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

public class CommandReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    private String TAGe;
    private String TAGi;

    public final String Intro = "intro";

    private String phraseSub;

    public CommandReceiver(MainActivity activity) {
        this.mainActivity = activity;

        mainActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));

        TAGe = "Error";
        TAGi = "Info";

        try {
            VuzixSpeechClient speechClient = new VuzixSpeechClient(activity);   //install vocabulary
            speechClient.deletePhrase("*");		//delete all default phrases

            try {
                speechClient.insertWakeWordPhrase("hey vuzix");

            } catch (NoSuchMethodError e) {
                Log.e(TAGe,"Setting wake words not supported"); }

            try {
                speechClient.insertVoiceOffPhrase("privacy please");

            } catch (NoSuchMethodError e) {
                Log.e(TAGe,"Setting off words not supported"); }

            try {
                speechClient.insertPhrase("introduce yourself",Intro);
                //your phrases

            } catch (NoSuchMethodError e) {
                Log.e(TAGe,"Setting phrase not supported"); }

            Log.i(TAGi, speechClient.dump()); //see what phrases were added
            VuzixSpeechClient.EnableRecognizer(mainActivity,true);

        } catch(NoClassDefFoundError e) {
            Log.e(TAGe,"Device not supporting Vuzix Speech");
        } catch (Exception e) {
            Log.e(TAGe,"Error setting custom vocabulary: " + e.getMessage());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(VuzixSpeechClient.ACTION_VOICE_COMMAND)) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                if (extras.containsKey(VuzixSpeechClient.PHRASE_STRING_EXTRA)) {
                    phraseSub = intent.getStringExtra(VuzixSpeechClient.PHRASE_STRING_EXTRA);

                    if (phraseSub != null) {
                    	if (phraseSub.equals(Intro)) {
                    		mainActivity.OnIntroduction();
                    	} 
                    }
                }
                else if (extras.containsKey(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA)) {
                    //recogniser activated or stopped
                }
                else {
                    Log.e(TAGe, "Intent not handled"); }
            }
        }
        else {
            Log.e(TAGe,"Other Intent not handled " + intent.getAction());
        }
    }

    public void unregister() {
        try {
            mainActivity.unregisterReceiver(this);
            Log.i(TAGi,"Custom vocab removed");
            mainActivity = null;
        } catch (Exception e) { Log.e(TAGe,"Custom vocab died " + e.getMessage()); }
    }

}