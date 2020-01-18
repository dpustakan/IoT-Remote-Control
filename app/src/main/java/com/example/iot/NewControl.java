package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.DatabaseHelper;
import helpers.MQTTHelper;

public class NewControl extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView signalStatus, commandName;
    String rawData;
    DatabaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_control);

        myDatabase = new DatabaseHelper(this);

        signalStatus = (TextView) findViewById(R.id.signal_status_textview);
        commandName = (TextView) findViewById(R.id.command_name_textview);
        getMqttMessage();

        Button newControl = (Button) findViewById(R.id.save_data_btn);
        newControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!TextUtils.isEmpty(rawData)) {
                    if (!commandName.getText().toString().equals("")) {
                        myDatabase.insertData(commandName.getText().toString(), rawData);
                        signalStatus.setText("Waiting for signal...");
                        commandName.setText("");
                        rawData ="";
                        Toast.makeText(NewControl.this, "Command saved!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(NewControl.this, "Please fill this command name!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(NewControl.this, "Remote signal is not received yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMqttMessage(){
        mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.connectToSubscribe();
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {


            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("Debug", message.toString());
                if(topic.contains("raw")){
                    rawData = message.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            signalStatus.setText("Signal Received");
                        }
                    });
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
