/*
MqTT-Cordova 
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
function processPluginParams(pluginParams) {
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
		pluginParams = processPluginParams(pluginParams);
		cordova.exec(
			function (message) {
				data.success(message);
			},
			function (message) {
				data.error(message);
			},
			"MqTTPlugin",
			"connect",
			pluginParams
		);
	},
	publish : function (data) {
		var pluginParams = {//true if required
			debug: false,
			topic: true,
			qos: true,
			message: true
		};
		console.log('Publishing');
		pluginParams = processPluginParams(pluginParams);
		cordova.exec(
			function (response) {
				data.success(response)
			},
			function (error) {
				data.error(error);
			},
			"MqTTPlugin",
			"publish",
			pluginParams
		);
	},
	subscribe : function (data, callback) {
		var pluginParams = {//true if required
			debug: false,
			topic: true,
			qos: true
		};
		console.log('Subscribing');
		pluginParams = processPluginParams(pluginParams);
		cordova.exec(
			function (response) {
				data.success(response)
			},
			function (error) {
				data.error(error);
			},
			"MqTTPlugin",
			"subscribe",
			pluginParams
		);
	}
}
module.exports = sero;
