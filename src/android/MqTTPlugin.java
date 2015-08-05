package com.arcoirislabs.plugin.mqtt;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

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

public class MqTTPlugin extends CordovaPlugin {
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
		if (action.equals("connect")) {
			String url = args.getString(0);
			String clientId = args.getString(1);
			this.connect(url, clientId, callbackContext);
			return true;
		} else {
			Boolean quietMode = args.getBoolean(0);
			String username = args.getString(1);
			String password = args.getString(2);
			String topic = args.getString(3);
			int qos = args.getInt(4);
			String message = args.getString(5);
			Boolean retained = false;
			if (action.equals("publish")) {
				this.publish(topic, qos, message, retained, callbackContext);
				return true;
			} else if (action.equals("connect")) {
				this.subscribe(topic, qos, callbackContext);
				return true;
			}
		}
		return false;
	}

	private void connect(String url, String clientId, CallbackContext callbackContext) {
		client = new MqttAndroidClient(webView.getContext(), url, clientId);
		try {
			client.connect(null, new ConnectListener());
		} catch (MqttException e) {
			callbackContext.error(e.getMessage());
		}
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
				callbackContext.success(token.getTopics().toString());
			} else {
				callbackContext.error("token is null");
			}
		} catch (MqttException e) {
			callbackContext.error(e.getMessage());
		}
	}

	private class ConnectListener implements IMqttActionListener {

		private CallbackContext callbackContext;

		public void initialize(CallbackContext callbackContext) {
			this.callbackContext = callbackContext;
		}

		@Override
		public void onSuccess(IMqttToken mqttToken) {
			String[] topics = mqttToken.getTopics();
			callbackContext.success(topics.toString());

			MqttMessage message = new MqttMessage("Hello, I am Android Mqtt Client.".getBytes());
			message.setQos(2);
			message.setRetained(false);

			try {
				client.publish("messages", message);
			} catch (MqttPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(IMqttToken arg0, Throwable arg1) {
			String message = arg1.getMessage();
			callbackContext.error(message);
		}
	}
}
