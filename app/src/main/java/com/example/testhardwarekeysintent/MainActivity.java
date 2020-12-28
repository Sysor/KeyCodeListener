package com.example.testhardwarekeysintent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String SYSTEM_KEY_EVENT = "com.cipherlab.keymappingmanager.SEND_KEY_EVENT";
    private Activity activity;
    private ListView lv;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1);
        lv = findViewById(R.id.lst);
        lv.setAdapter(adapter);

        registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if(action == null){
                            return;
                        }
                        if(action.equals(SYSTEM_KEY_EVENT)) {
                            Bundle args = intent.getExtras();
                            if (args != null) {
                                KeyEvent keyEvent = args.getParcelable("KeyCode_Data");
                                if (keyEvent != null) {
                                    String msg = String.format(Locale.getDefault(),"deviceID: %d key(%d): %c action: %d",
                                            keyEvent.getDeviceId(),
                                            keyEvent.getKeyCode(),
                                            keyEvent.getNumber(),
                                            keyEvent.getAction());
                                    adapter.add(msg);
                                    lv.smoothScrollToPosition(lv.getCount()-1);
                                    //Toast.makeText(activity, "keyCode: " + keyEvent.getKeyCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }, new IntentFilter(SYSTEM_KEY_EVENT));
    }
}