mqtt-cordova
============

# MQTT Plugin for Cordova

This is a plugin for MQTT protocol of messaging. Currently Android is supported. Forthcoming support for Windows Phone.
More features are stil being developed.

## Features

This plugin can publish and subscribe for any MQTT server. Currently it works only for compulsory authentication.
to add the plugin 

## Usage

You can use the following functions after onDeviceReady was triggered. Use connect() before any other functions.

```
mqtt.connect({
    url: "tcp://m2m.eclipse.org:1883",
    clientId: "SampleJavaV3_",
    success: function (subscribedTopicsString) {
        alert('success: ' + subscribedTopicsString);
    },
    error: function (message) {
         alert('error:' + message);
    }    
	cleanSession: true/false, // optional
});
```

To publish a message you can use this function. It should be used after connect() was succesful, maybe use the
corresponding callback of the latter.

```
mqtt.publish({
    topic: "topic",
    qos: "2",
    message: "Howzaaa",
	username:"",
	password:"",
	debug:true/false,
    success: function (data){
        alert(data);
    },
    error: function (data){
         alert(data);
    }
});
```

To subscribe you can use this function. This is not tested yet and you will likely have to work on MQTTPlugin.java yourself

```
mqtt.subscribe({
	topic:"",
	qos:"",
	username:"",
	password:"",
	debug:true/false,
	success:function (data) {},
	error:function (data) {}
});
```

## Building

This plugin makes use of the Android Service lib org.eclipse.paho.android.service. An App which uses this service must
include the appropriate service tag in its manifest - e.g.

	<!-- Mqtt Service -->
	<service android:name="org.eclipse.paho.android.service.MqttService" />
	
This might be added by Cordova automatically through this repository's plugin.xml specification.

Add "Android Support Repository" through the Standalone Android SDK Manager in Android Studio.
            
Then add the following line to your dependencies in the build.gradle file of the Android Module, not in CordovaLib.
It's likely that the specified version is not the only one that's working, but that's not tested.

    compile "com.android.support:support-v4:22.2.+"


Created and maintained by Arcoiris Labs

Have fun!

Released under Apache 2.0 Licence
