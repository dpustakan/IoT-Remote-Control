package helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;

public class MQTTHelper implements Serializable {
    public MqttAndroidClient mqttAndroidClient;

    private static String serverUrl = "tcp://tailor.cloudmqtt.com:15085";
    private static String username = "dyhpbhkz";
    private static String password = "APnM4sxeKJWC";

    private String clientId = "AndroidClient";
    private String subscriptionTopic[] = {"temp","raw"};
    private int subscriptionQoS[] = {0,0};

    public MQTTHelper(final Context context){
        mqttAndroidClient = new MqttAndroidClient(context, serverUrl, clientId);

        try {
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context, "MQTT Connected", Toast.LENGTH_LONG).show();
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context, "MQTT Connection Failed", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    private MqttConnectOptions getMqttConnectOption(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        return mqttConnectOptions;
    }


    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, subscriptionQoS, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public void PublishMessage(final String topic, final String msg){
        try {
            mqttAndroidClient.publish(topic,new MqttMessage(msg.getBytes()),0,null);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
