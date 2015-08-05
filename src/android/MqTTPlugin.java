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

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
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
			String qos = args.getString(4);
			String message = args.getString(5);

			if (action.equals("publish")) {
				this.publish(quietMode, username, password, topic , qos, message, callbackContext);
				return true;
			} else if (action.equals("connect")) {
				this.subscribe(quietMode, username, password, topic , qos, callbackContext);
				return true;
			}
		}
		return false;
	}

	private void connect(String url, String clientId, CallbackContext callbackContext) {
		client = new MqttAndroidClient(getApplicationContext(), url, clientId);
		client.connect(null, new IMqttActionListener() {
			@Override
			public void onSuccess(IMqttToken mqttToken) {
				callbackContext.success();
				Log.i(LOGTAG, "Client connected");
				Log.i(LOGTAG, "Topics="+mqttToken.getTopics());

				MqttMessage message = new MqttMessage("Hello, I am Android Mqtt Client.".getBytes());
				message.setQos(2);
				message.setRetained(false);

				try {
					client.publish("messages", message);
					Log.i(LOGTAG, "Message published to 'messages' topic");
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
				callbackContext.error();
				// TODO Auto-generated method stub
				Log.i(LOGTAG, "Client connection failed: "+arg1.getMessage());
			}
		});
	}

	private void publish(Boolean quietMode, String username, String password, String topic , String qos, String message, CallbackContext callbackContext); {
		client.publish(quietMode, username, password, topic , qos, message);
		if (message != null && message.length() > 0) {
			String html= "Publish";
			callbackContext.success(html);

		} else {
			callbackContext.error("Check your parameters");
		}
	}
	private void subscribe(Boolean quietMode, String username, String password, String topic , String qos, CallbackContext callbackContext); {
		client.subscribe(quietMode, username, password, topic , qos, message);
		if (url != null && url.length() > 0) {
			String html= "Subscribe";
			callbackContext.success(html);

		} else {
			callbackContext.error("Check your parameters");
		}
	}
}
