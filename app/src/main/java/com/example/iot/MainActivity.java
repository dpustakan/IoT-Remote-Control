package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.MQTTHelper;

public class MainActivity extends AppCompatActivity {

    //SQLiteDatabase sqLiteDatabaseObj;
    MQTTHelper mqttHelper;
    TextView tempHumid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQLiteDataBaseBuild();
        //SQLiteTableBuild();

        tempHumid = (TextView) findViewById(R.id.temperature_textview);
        subscribeMqtt();

        Button newControl = (Button) findViewById(R.id.new_control_btn);
        newControl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newControlAct = new Intent(MainActivity.this , NewControl.class);
                startActivity(newControlAct);
            }
        });

        Button sendCommand = (Button) findViewById(R.id.send_activity_btn);
        sendCommand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendCommandAct = new Intent(MainActivity.this , SendCommand.class);
                startActivity(sendCommandAct);
            }
        });
    }

    private void subscribeMqtt(){
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
                final String msg = message.toString()+"\u00B0";
                if(topic.contains("temp")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tempHumid.setText(msg);
                        }
                    });
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    //public void SQLiteDataBaseBuild(){

    //    sqLiteDatabaseObj = openOrCreateDatabase("IRDatabase", Context.MODE_PRIVATE, null);

    //}

//    public void SQLiteTableBuild(){
//
//        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS IRTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR, data VARCHAR);");
//
//    }
}
