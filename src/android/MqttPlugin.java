package com.arcoirislabs.plugin.mqtt;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;

// MQTT
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * This class echoes a string called from JavaScript.
 */

public class MqttPlugin extends CordovaPlugin {
	public String id;
	public CordovaWebView webView;					// WebView object
	public CordovaInterface cordova;
	public MqttAndroidClient client;

	private static final String LOGTAG = "MQTT Android Cordova Plugin";

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		assert this.cordova == null;
		this.cordova = cordova;
		this.webView = webView;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		Log.d(LOGTAG, action + " action called");
		Log.d(LOGTAG, args.toString());
		if (action.equals("connect")) {
			String url = args.getString(0);
			String clientId = args.getString(1);
			String username = args.optString(2);
			String password = args.optString(3);
			Boolean cleanSession = args.optBoolean(4);
			this.connect(url, clientId, callbackContext);
			return true;
		} else {
			Boolean quietMode = args.getBoolean(0);
			String topic = args.getString(1);
			int qos = args.getInt(2);
			String message = args.optString(3);
			Boolean retained = false;
			if (action.equals("publish")) {
				this.publish(topic, qos, message, retained, callbackContext);
				return true;
			} else if (action.equals("subscribe")) {
				this.subscribe(topic, qos, callbackContext);
				return true;
			}
		}
		return false;
	}

	private void connect(final String url, final String clientId, final CallbackContext callbackContext) {
		Log.i(LOGTAG, "Connecting as " + clientId + " to " + url + " ...");
		cordova.getThreadPool().execute(new Runnable() {
			public void run() {
				client = new MqttAndroidClient(cordova.getActivity().getApplicationContext(), url, clientId);
				try {
					client.connect(null, new IMqttActionListener() {
						@Override
						public void onSuccess(IMqttToken mqttToken) {
							Log.i(LOGTAG, "Successfully connected");
							String[] topics = mqttToken.getTopics();
							if (topics != null) {
								callbackContext.success(topics.toString());
							} else {
								callbackContext.success("success");
							}
							MqttCallbackHandler callbackHandler = new MqttCallbackHandler(webView, url + clientId);
							client.setCallback(callbackHandler);

							MqttMessage message = new MqttMessage("Hello, I am Android Mqtt Client.".getBytes());
							message.setQos(2);
							message.setRetained(false);

							try {
								client.publish("messages", message);
							} catch (MqttPersistenceException e) {
								Log.e(LOGTAG, "MqttPersistenceException: " + e.getMessage());
								callbackContext.error(e.getMessage());
							} catch (MqttException e) {
								Log.e(LOGTAG, "MqttException: " + e.getMessage());
								callbackContext.error(e.getMessage());
							}
						}

						@Override
						public void onFailure(IMqttToken arg0, Throwable arg1) {
							String message = arg1.getMessage();
							Log.e(LOGTAG, "connect onFailure: " + message);
							callbackContext.error(message);
						}
					});
				} catch (MqttException e) {
					callbackContext.error("connect MqttException: " + e.getMessage());
				}
			}
		});
	}

	private void publish(String topic, int qos, String message, Boolean retained, CallbackContext callbackContext) {
		try {
			IMqttDeliveryToken token = client.publish(topic, message.getBytes(), qos, retained);
			if (token != null) {
				callbackContext.success(token.getMessage().toString());
			} else {
				callbackContext.error("token is null");
			}
		} catch (MqttException e) {
			callbackContext.error(e.getMessage());
		}

	}

	private void subscribe(String topic, int qos, CallbackContext callbackContext) {
		try {
			IMqttToken token = client.subscribe(topic, qos);
			if (token != null) {
				Log.i(LOGTAG, "subscribed to " + token.getTopics().toString());
				callbackContext.success(token.getTopics().toString());
			} else {
				Log.e(LOGTAG, "subscribe token is null");
				callbackContext.error("token is null");
			}
		} catch (MqttException e) {
			Log.e(LOGTAG, "exception when subscribing: " + e.getMessage());
			callbackContext.error(e.getMessage());
		}
	}

}
