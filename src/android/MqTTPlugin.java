package com.arcoirislabs.plugin.mqtt;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

// MQTT
import org.eclipse.paho.android.service;
/**
 * This class echoes a string called from JavaScript.
 */

public class MqTTPlugin extends CordovaPlugin {
	public String id;
	public CordovaWebView webView;					// WebView object
	public CordovaInterface cordova;
	public MqttAndroidClient client;

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		assert this.cordova == null;
		this.cordova = cordova;
		this.webView = webView;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	if (action == "publish" || action == "subscribe") {
		String url = args.getString(0);
		String clientId = args.getString(1);
		Boolean quietMode = args.getBoolean(2);
		String username = args.getString(3);
		String password = args.getString(4);
		Boolean cleanSession = args.getBoolean(5);
		String topic = args.getString(6);
		String qos = args.getString(7);
		String message = args.getString(8);
	}
	boolean result = false;
	switch (action) {
		case "connect":
			this.connect(args.getString(0), args.getString(1));
			result = true;
			break;
		case "publish":
			this.publish(url, clientId, quietMode, username, password, cleanSession, topic , qos, message, callbackContext);
			result = true;
			break;
		case "subscribe":
			this.subscribe(url, clientId, quietMode, username, password, cleanSession, topic , qos, callbackContext);
			result = true;
			break;
		default:
			result = false;
			break;
		}
		return result;
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

					Log.i(LOGTAG, "Message published");

					client.disconnect();
					Log.i(LOGTAG, "client disconnected");
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
