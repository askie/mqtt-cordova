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
var sero = {
	connect : function (data) {
		var dataUrl, cleanSes;
		if (data.secure) {
			dataUrl = "ssl://"+data.url+data.port;
		} else{
			dataUrl = "tcp://"+data.url+data.port;
		};
		if (data.cleanSession) {
			cleanSes = true;
		} else{
			cleanSes = false;
		};
		cordova.exec(
			function (message) {
				data.success(message);
			},
			function (message) {
				data.error(message);
			},
			"MqTTPlugin",
			"connect",
			[dataUrl, data.clientId]
		);
	},
	publish : function (data) {
		var quietM;
		if (data.debug) {
			quietM = true;
		} else{
			quietM = false;
		};
		cordova.exec(
			function (response) {
				data.success(response)
			},
			function (error) {
				data.error(error);
			},
			"MqTTPlugin",
			"publish",
			[quietM, data.username, data.password, data.topic, data.qos, data.message]
		);
	},
	subscribe : function (data,callback) {
		var quietM;
		if (data.debug) {
			quietM = true;
		} else{
			quietM = false;
		};
		cordova.exec(
			function (response) {
				data.success(response)
			},
			function (error) {
				data.error(error);
			},
			"MqTTPlugin",
			"subscribe",
			[quietM, data.username, data.password, data.topic, data.qos]
		);
	}
}
module.exports = sero;
