/*
MQTT-Cordova 
Doc:-
Publish (with credentials)
mqtt.publish({
	url:"",
	port:"",
	topic:"",
	secure:false/true,
	qos:"",
	message:"",
	cleanSession:true/false,
	username:"",
	password:"",
	debug:true/false,
	success:function(data){},
	error:function(data){}
});
*/

function processPluginParams(pluginParams, data) {
	for(var key in pluginParams) {
		if (pluginParams.hasOwnProperty(key)) {
			if (typeof(data[key]) === "undefined") {
				if (pluginParams[key]) {
					throw new Error(key + " is undefined but is required");
				} else {
					pluginParams[key] = null;
					console.log(key + " set to null");
				}
			} else {
				pluginParams[key] = data[key];
			}
		}
	}
	pluginParams = Object.keys(pluginParams).map(function (key) {return pluginParams[key]});
	console.log(pluginParams);
	return pluginParams;
}

function addToCallbackMap(key, callback) {
	if (typeof(callbackMap[key]) === "undefined") {
		callbackMap[key] = [];
	}
	callbackMap[key].push(callback);
	console.log("Callback added for " + key);
}

var callbackMap = {};

var sero = {
	connect: function (data) {
		var pluginParams = {//true if required
			url: true,
			clientId: true,
			username: false,
			password: false,
			cleanSession: false
		};
		console.log('Connecting');
		pluginParams = processPluginParams(pluginParams, data);
		cordova.exec(
			function (message) {
				data.success(message);
			},
			function (message) {
				data.error(message);
			},
			"MqttPlugin",
			"connect",
			pluginParams
		);
	},
	publish: function (data) {
		var pluginParams = {//true if required
			debug: false,
			topic: true,
			qos: true,
			message: true
		};
		console.log('Publishing');
		pluginParams = processPluginParams(pluginParams, data);
		cordova.exec(
			function (response) {
				data.success(response)
			},
			function (error) {
				data.error(error);
			},
			"MqttPlugin",
			"publish",
			pluginParams
		);
	},
	subscribe:  function (data, callback) {
		var pluginParams = {//true if required
			debug: false,
			topic: true,
			qos: true
		};
		console.log('Subscribing');
		pluginParams = processPluginParams(pluginParams, data);
		cordova.exec(
			function (topics) {
				console.log("JS: subscribed to " + topics);
				data.success(topics);
				for (var i = 0; i < topics.length; i++) {
					addToCallbackMap(topics[i], callback);
				}
			},
			function (error) {
				data.error(error);
			},
			"MqttPlugin",
			"subscribe",
			pluginParams
		);
	},
	_onMessageReceived: function (topic, payload) {
		console.log('Message received in JavaScript:');
		console.log(topic);
		console.log(payload);
		var callbacks = callbackMap[topic];
		if (callbacks instanceof Array) {
			console.log("Found " + callbacks.length + " callbacks");
			for (var i = 0; i < callbacks.length; i++) {
				callbacks[i].apply(this, [topic, payload]);
			}
		}
	}
}

module.exports = sero;
