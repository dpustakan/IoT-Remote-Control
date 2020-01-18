package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import helpers.DatabaseHelper;
import helpers.MQTTHelper;

public class SendCommand extends AppCompatActivity {

    DatabaseHelper myDatabase;
    Spinner commandList;
    Button sendCommand;
    MQTTHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_command);

        myDatabase = new DatabaseHelper(this);
        commandList = (Spinner) findViewById(R.id.command_list);
        sendCommand = (Button)findViewById(R.id.send_command_btn);

        //Query to database
        final Cursor cr = myDatabase.getAllRawData();

        //Take column 'name' from database result and put it to arrayList
        final ArrayList commandNameList = myDatabase.cursorToArrayList(cr,"name");
        ArrayAdapter commandAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, commandNameList);

        //assign array adapter to spinner
        commandList.setAdapter(commandAdapter);

        //Take column 'data' from database query result and put it to arrayList
        final ArrayList<String> commandData = myDatabase.cursorToArrayList(cr,"data");
        //put data tp array of string
        final String[] data = commandData.toArray(new String[commandData.size()]);

        sendCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data of selected position
                String data = (String) commandData.get(commandList.getSelectedItemPosition());

                mqttHelper = new MQTTHelper(getApplicationContext());
                mqttHelper.connectToPublishMessage("inTopic", data);

                mqttHelper.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {

                    }

                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Toast.makeText(SendCommand.this, "Command sent!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
