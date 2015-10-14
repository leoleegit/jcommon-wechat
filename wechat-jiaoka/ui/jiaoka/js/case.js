
jiaoka.export_excel = function(name,$tr_title,$tr){
	var title = {};
	if($tr_title && $tr_title.length>0){
		var tr  = $tr_title[0];
		var $td = $(tr).find('th');
		for(j=0;j<$td.length;j++){
			var td = $td[j];
			if($(td).hasClass('hidden')){
				continue;
			}
			if($(td).find('input').length==0)
				title[j-1] = $(td).html();
		}
	}else{
		jiaoka.ui.alert_.warn('请选择导出项');
		return;
	}
	var datas = new Array();
	if($tr && $tr.length>0){
		for(i=0;i<$tr.length;i++){
			var data = null;
			var check = $($tr).first().find('input');
			if($(check).attr("checked")=='checked'){
				data = {};
				var tr = $tr[i];
				var $td= $(tr).find('td');
				for(j=0;j<$td.length;j++){
					var td = $td[j];
					if($(td).hasClass('hidden')){
						continue;
					}
					data[j] = $(td).html();
				}
				datas.push(data);
			}
		}
	}else{
		jiaoka.ui.alert_.warn('请选择导出项');
	}
	if(datas.length==0){
		jiaoka.ui.alert_.warn('请选择导出项');
		return;
	}
	var excelObj = {};
	excelObj.name  = name;
	excelObj.title = title;
	excelObj.data  = datas;
	var json       = new cc.tool.json(excelObj).toString();
	$.post(jiaoka.conf.serice+'/wechat-jiaoka/service/export/excel',json,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				if(msg.startWith('{'))
					json = eval('('+msg+')');
				else if(msg.startWith('['))
					json = eval(msg);
				if(json.code==0){
					jiaoka.ui.alert_.succ("开始下载","导出日志");
					if(json.data){
						var url = jiaoka.conf.serice+'/wechat-jiaoka/media'+json.data.url;
						cc.tool.downloadURL(url);
					}
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

jiaoka.wechat_log_search = function(openid,next,number){
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

jiaoka.user_search = function(phone_number,nickname,next,number){
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

jiaoka.case_search = function(status,phone_number,nickname,next,number){
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