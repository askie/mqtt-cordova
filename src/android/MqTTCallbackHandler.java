package com.arcoirislabs.plugin.mqtt;

//import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import android.util.Log;

// MQTT
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Handles callbacks from the MQTT Client
 *
 */
public class MqttCallbackHandler implements MqttCallback {

  /** Cordova's web view to be able to communicate with it  */
  private CordovaWebView webView;

  /** Client handle to reference the client that this handler is attached to**/
  private String clientHandle;

  /**
   * Creates an <code>MqttCallbackHandler</code> object
   * @param clientHandle The handle to a client object
   */
  public MqttCallbackHandler(CordovaWebView webView, String clientHandle) {
	this.webView = webView;
	this.clientHandle = clientHandle;
  }

  /**
   * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
   */
  @Override
  public void connectionLost(Throwable cause) {
	Log.e(LOGTAG, "Connection Lost");
	if (cause != null) {
		Log.e(LOGTAG, "Cause:");
		cause.printStackTrace();
	}
  }

  /**
   * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
   */
  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
	String payload = new String(message.getPayload());
	message.getQos();
	message.isRetained();
	webView.sendJavascript("cordova.require(\"cordova/plugin/mqtt\")._onMessageReceived('"+topic+"','"+payload+"')");
  }

  /**
   * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
   */
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    // Do nothing
  }

}

