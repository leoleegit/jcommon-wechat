
function jiaoka_wechat_log_search(openid,next,number){
	if($("#wechat-log-table tbody").length>0)
		$("#wechat-log-table tbody").remove();
	var option = {
			'openid':openid && ""!=openid?openid:undefined,
			'next':next,
			'number':number
	}
	$.get(jiaoka.conf.serice+'/wechat-jiaoka/service/auditlog/wechat/search',option,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				if(msg.startWith('{'))
					json = eval('('+msg+')');
				else if(msg.startWith('['))
					json = eval(msg);
				if(json.code==0){
					var templet = jiaoka.ui.templet().get('wechat-log-table-data');
					var parent  = $('#wechat-log-table')[0];
					if(json.datas){
						for(i=0;i<json.datas.length;i++){
							json.datas[i].create_time = json.datas[i].create_time?new Date(parseInt(json.datas[i].create_time)).Format("yyyy-MM-dd hh:mm"):'';
							json.datas[i].number = i+1;
						}
					}
					cc.tool.add_child_with_json(parent,json,templet);
					if(json.count)
						jiaoka.wechat_log.data_num = json.count;
					jiaoka.wechat_log.show_page_nav();
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

function jiaoka_user_search(phone_number,nickname,next,number){
	if($("#user-data-table tbody").length>0)
		$("#user-data-table tbody").remove();
	var option = {
			'nickname':nickname && ""!=nickname?nickname:undefined,
			'phone_number':phone_number && ""!=phone_number?phone_number:undefined,
			'next':next,
			'number':number
	}
	$.get(jiaoka.conf.serice+'/wechat-jiaoka/service/user/search',option,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				if(msg.startWith('{'))
					json = eval('('+msg+')');
				else if(msg.startWith('['))
					json = eval(msg);
				if(json.code==0){
					var templet = jiaoka.ui.templet().get('users-table');
					var parent  = $('#user-data-table')[0];
					if(json.datas){
						for(i=0;i<json.datas.length;i++){
							json.datas[i].create_time = json.datas[i].create_time?new Date(parseInt(json.datas[i].create_time)).Format("yyyy-MM-dd hh:mm"):'';
							json.datas[i].subscribe_time = json.datas[i].subscribe_time?new Date(parseInt(json.datas[i].subscribe_time)).Format("yyyy-MM-dd hh:mm"):'';
							json.datas[i].sex = json.datas[i].sex=='1'?'男':'女';
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
	$.get(jiaoka.conf.serice+'/wechat-jiaoka/service/case/search',option,function(msg,statusTxt,xhr){
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
							json.datas[i].create_time = json.datas[i].create_time?new Date(parseInt(json.datas[i].create_time)).Format("yyyy-MM-dd hh:mm"):'';
							json.datas[i].update_time = json.datas[i].update_time?new Date(parseInt(json.datas[i].update_time)).Format("yyyy-MM-dd hh:mm"):'';
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