var jiaoka={};
jiaoka.conf={};
jiaoka.conf.serice="http://192.168.3.102:80";
$(document).ready(function(){
	jiaoka.ui = new cc.ui();
	jiaoka.ui.templet_.load(new Array('sidebar_menu.html','tabs_menu.html','common_templet.html'),
			function(responseTxt,statusTxt,xhr){
		jiaoka.ui.alert_.resp(statusTxt,cc.succ[2001],cc.error[4001],'',1);
		jiaoka.ui.tab_.set_home_tab();
		jiaoka.ui.menu_.load();
	    
	    setTimeout(function(){
	    	jiaoka.ui.alert_.succ(cc.succ[2001],cc.error[4001]);
	    },5000);
	
	});
	
});
