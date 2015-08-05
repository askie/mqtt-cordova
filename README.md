mqtt-cordova
============

# MqTT Plugin for Cordova

This is a plugin for MqTT protocol of messaging. Currently Android is supported. Forthcoming support for Windows Phone. More features are stil being developed.

## Features

This plugin can publish and subscribe for any MqTT server. Currently it works only for compulsory authentication.
to add the plugin 

## Usage

```
mqtt.connect({
    url: "m2m.eclipse.org",
    clientId: "SampleJavaV3_",
    success: function (){
        alert('success');
    },
    error: function () {
         alert('error');
    }    
	cleanSession: true/false, // optional
    secure: false/true // optional
});
```

To publish a message you can use this function

```
mqtt.publish({
    topic:"topic",
    secure:false,
    qos:"2",
    portNo:"1883",
    message:"Howzaaa",
    username:null,
    password:null,
    debug:false,
    success:function(data){
        alert(data);
    },
    error:function(data){
         alert(data);
    }
});
```

To subscribe you can use this function

```
mqtt.subscribe({
	topic:"",
	qos:"",
	username:"",
	password:"",
	debug:true/false,
	success:function(data){},
	error:function(data){}
});
```

## Building

This plugin makes use of the Android Service lib org.eclipse.paho.android.service. An App which uses this service must include the appropriate Service tag in its manifest - e.g.

	<!-- Mqtt Service -->
	<service android:name="org.eclipse.paho.android.service.MqttService" />


Created and maintained by Arcoiris Labs

Have fun!

Released under Apache 2.0 Licence
