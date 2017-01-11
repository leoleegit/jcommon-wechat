var fb = {};
fb.conf={};
fb.conf.service = "http://spotlightx.protel.com.hk:8080/facebook-manage";
var jiaoka={};
jiaoka.conf={};
jiaoka.conf.serice="http://192.168.2.104:8080";



fb.alert = function(msg,tit,calbak){
	BootstrapDialog.alert({
        title: tit,
        message: msg,
        type: BootstrapDialog.TYPE_DEFAULT,
        callback:calbak
    });
}

fb.confirm = function(msg,tit,calbak){
	BootstrapDialog.confirm({
        title: tit,
        message: msg,
        type: BootstrapDialog.TYPE_DEFAULT, 
        closable: false, 
        draggable: true, 
        btnCancelLabel: 'No',
        btnOKLabel: 'Yes', 
        callback: calbak
    });
}
