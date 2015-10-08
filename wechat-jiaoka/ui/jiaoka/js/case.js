
function jiaoka_case_search(status,phone_number,nickname,next,number){
	if($("#case-data-table tbody").length>0)
		$("#case-data-table tbody").remove();
	var option = {
			'status':status,
			'nickname':nickname && ""!=nickname?nickname:undefined,
			'phone_number':phone_number && ""!=phone_number?phone_number:undefined,
			'next':next,
			'number':number
	}
	$.get('http://127.0.0.1:8080/wechat-jiaoka/service/case/search',option,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				if(msg.startWith('{'))
					json = eval('('+msg+')');
				else if(msg.startWith('['))
					json = eval(msg);
				if(json.code==0){
					var templet = jiaoka.ui.templet().get('case-table');
					var parent  = $('#case-data-table')[0];
					if(json.datas){
						for(i=0;i<json.datas.length;i++){
							json.datas[i].create_time = json.datas[i].create_time?new Date(parseInt(json.datas[i].create_time)).Format("hh:mm"):'';
							json.datas[i].update_time = json.datas[i].update_time?new Date(parseInt(json.datas[i].update_time)).Format("hh:mm"):'';
							json.datas[i].number = i+1;
						}
					}
					cc.tool.add_child_with_json(parent,json,templet);
				}else{
					jiaoka.ui.alert_.warn(json.error);
				}
			}
		}
		else if(statusTxt=="error"){
			jiaoka.ui.alert_.warn('网络异常，请稍后再试');
		}
	});
}