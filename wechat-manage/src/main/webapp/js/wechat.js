
var wechat  = {};
wechat.conf = {};

//wechat.conf.service = 'http://192.168.2.104:8080/wechat-manage';
wechat.conf.service = '.';
wechat.conf.id      = 'gh_f49bb9a333b3';
wechat.conf.count   = 1;


$(document).ready(function(){
	$.get(wechat.conf.service+'/service/token/signature',{'id':wechat.conf.id,'url':location.href},function(msg,statusTxt,xhr){
		 if(statusTxt=="success"){
			 json = new cc.tool.json(msg).decode();
			 if(json.code==0){
				 if(json.data){
					 wechat.conf.appId = json.data.appId;
					 wechat.conf.timestamp = json.data.timestamp;
					 wechat.conf.nonceStr = json.data.nonceStr;
					 wechat.conf.signature = json.data.signature;
					 
					 wx.config({
					    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
					    appId: wechat.conf.appId, // 必填，公众号的唯一标识
					    timestamp: wechat.conf.timestamp, // 必填，生成签名的时间戳
					    nonceStr: wechat.conf.nonceStr, // 必填，生成签名的随机串
					    signature: wechat.conf.signature,// 必填，签名，见附录1
					    jsApiList: ['getLocation','openLocation','getNetworkType'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
					 });
				 }
			 }else
				 fb.alert(json.msg);
		 }else if(statusTxt=="error"){
			 fb.alert('network error, try again latter.');
		 }
	});
});

wx.ready(function(){
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	
	$('#wx-view-map').on('click',function(){
		wx.openLocation({
		    latitude: wechat.location.latitude, // 纬度，浮点数，范围为90 ~ -90
		    longitude: wechat.location.longitude, // 经度，浮点数，范围为180 ~ -180。
		    name: wechat.location.accuracy, // 位置名
		    address: wechat.location.accuracy, // 地址详情说明
		    scale: 1, // 地图缩放级别,整形值,范围从1~28。默认为最大
		    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
		});
	});
	
	wx.getLocation({
	    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	    success: function (res) {
	        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	        var speed = res.speed; // 速度，以米/每秒计
	        var accuracy = res.accuracy; // 位置精度
	        wechat.location = res;
	        wechat.location.count = ++wechat.conf.count;
	        //var locat = new cc.tool.json(wechat.location);
	        $('#wx-location').html(cc.tool.json_format(wechat.location));
	    }
	});
	
	setInterval(function(){
		wx.getLocation({
		    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
		    success: function (res) {
		        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
		        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
		        var speed = res.speed; // 速度，以米/每秒计
		        var accuracy = res.accuracy; // 位置精度
		        wechat.location = res;
		        wechat.location.count = ++wechat.conf.count;
		        //var locat = new cc.tool.json(wechat.location);
		        $('#wx-location').html(cc.tool.json_format(wechat.location));
		    }
		});
	},10000);
	
});

wx.error(function(res){
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
	alert(ers);
});



