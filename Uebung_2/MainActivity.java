package com.example.uebung02;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private CommandReceiver commandReceiver;

    private String TAGa;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.result);	//id Platzhalter

        commandReceiver = new CommandReceiver(this);

        TAGa = "Action";
    }

    @Override
    protected void onDestroy() {
        commandReceiver.unregister();
        super.onDestroy();
    }

    public void OnIntroduction() {
        textView.setText("Hello, my name is Vuzix Blade");
        Log.i(TAGa,commandReceiver.Intro);
    }
}