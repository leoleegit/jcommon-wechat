$(document).ready(function(){
	 var cmd = cc.tool.get_parameter('cmd');
	 if(cmd == 'token'){
		 fb.token_resp();
	 }
});

fb.token_resp = function(){
	var code = cc.tool.get_parameter('code');
	var app_id = cc.tool.get_parameter('app_id'); 
	var redirect_uri = location.href.substring(0,location.href.lastIndexOf('&'));
	var option = {
			'app_id':app_id,
			'code':code,
			'redirect_uri':redirect_uri
	}
	
	$.get(fb.conf.service+'/service/token/generate',option,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				json = new cc.tool.json(msg).decode();
				var close_handler = function(){
					window.opener.fb.tokenlist();  
					window.close();  
				};
				var title = 'Generate Facebook AccessToken Dialog';
				fb.alert(json.msg,title,close_handler);
			}
		}
		else if(statusTxt=="error"){
			fb.alert('network error, try again latter.');
			window.close();  
		}
	});      
}