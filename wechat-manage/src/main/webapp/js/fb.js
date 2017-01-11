$(document).ready(function(){
	fb.ui = new cc.ui();
	fb.ui.templet_.load(new Array('sidebar_menu.html','tabs_menu.html','common_templet.html'),
			function(responseTxt,statusTxt,xhr){
		fb.ui.alert_.resp(statusTxt,cc.succ[2001],cc.error[4001],'',1);
		fb.ui.tab_.set_home_tab();
		fb.ui.menu_.load();
	    
//	    setTimeout(function(){
//	    	jiaoka.ui.alert_.succ(cc.succ[2001],cc.error[4001]);
//	    },5000);
	
	});
});

/**
 Facebook Token Functions Start
 **/
fb.addToken = function(app_id,app_name){
	var templet = fb.ui.templet().get('add-accesstoken-form');
	var $tr = $("#accesstoken-search-table tbody tr");
	var datas = new Array();
	if($tr && $tr.length>0){
		for(i=0;i<$tr.length;i++){
			var data  = null;
			var tr    = $tr[i];
			var $check = $(tr).first().find('input');
			
			var $td= $(tr).find('td');
			if("user"==$($td[2]).html()){
				data = {};
				data.token_id   = $($td[0]).html();
				data.owner_name = $($td[1]).html();
				datas.push(data);
			}
		}
	}
	cc.tool.element_init_data(templet,{'app_name':app_name, 'datas':datas});
	$(templet).find('#token-type').on('change',function(){
		var type = $(templet).find('#token-type').val();
		if("page"==type){
			$(templet).find('#token-owner-div').removeClass('hidden');
		}else{
			$(templet).find('#token-owner-div').addClass('hidden');
		}
	});
	$(templet).find('#token-type').change();
	BootstrapDialog.confirm({
	        title: 'Generate Facebook AccessToken Dialog',
	        message: templet,
	        type: BootstrapDialog.TYPE_DEFAULT, 
	        closable: false, 
	        draggable: true, 
	        btnCancelLabel: 'Cancel',
	        btnOKLabel: 'Next', 
	        callback: function(result) {
	            if(result) {
	            	var type = $(templet).find('#token-type').val();
	            	if("page"==type){
	            		var token_id = $(templet).find('#token-owner').val();
	            		if(!token_id){
	            			var title = 'Generate Facebook Page AccessToken Dialog';
	            			fb.alert('Have to generate page owner accesstoken first.',title);
	            			return;
	            		}
	            		var option = {
		            		'token_id':token_id
		            	}
		            	
		            	$.get(fb.conf.service+'/service/token/page',option,function(msg,statusTxt,xhr){
		            		var title = 'Generate Facebook Page AccessToken Dialog';
		            		if(statusTxt=="success"){
		            			if(msg){
		            				json = new cc.tool.json(msg).decode();
		            				if(json.code!=0){
		            					fb.alert(json.msg,title);
		            				}else{
		            					var tokens = [];
		            					var templet = fb.ui.templet().get('confirm-token-form');
		            					for(i=0;i<json.datas.length;i++){
		            						token = {};
		            						token.name = json.datas[i].name;
		            						token.json = new cc.tool.json(json.datas[i]).toString();
		            						tokens.push(token);
		            					}
		            					cc.tool.element_init_data(templet,{'datas':tokens});
		            					fb.confirm(templet,title,function(result){
		            						 if(result) {
		            							 var selected_token = [];
		            							 $(templet).find('#pagetoken-select').find("option:selected").each(function(i) {
		            								 selected_token[i] = new cc.tool.json($(this).val()).decode();
		            						     }); 
		            							 var option = {'data': selected_token};
		            							 $.post(fb.conf.service+'/service/token/add',new cc.tool.json(option).toString(),function(msg,statusTxt,xhr){
		            								 if(statusTxt=="success"){
		            									 json = new cc.tool.json(msg).decode();
		            									 fb.alert(json.msg,title,function(result){fb.tokenlist();});
		            								 }else if(statusTxt=="error"){
	            				            			 fb.alert('network error, try again latter.',title);
	            				            		 }
		            							 });
		            						 }
		            					});
		            				}
		            				
		            			}
		            		}
		            		else if(statusTxt=="error"){
		            			fb.alert('network error, try again latter.',title);
		            		}
		            	});
	        		}else{
	        			var option = {
		            			'app_id':app_id,
		            			'redirect_uri':location.href.substring(0,location.href.lastIndexOf('/'))+'/resp.html?cmd=token&app_id='+app_id
		            	}
		            	
		            	$.get(fb.conf.service+'/service/token/codeUrl',option,function(msg,statusTxt,xhr){
		            		if(statusTxt=="success"){
		            			if(msg){
		            				json = new cc.tool.json(msg).decode();
		            				if(json.code!=0){
		            					fb.alert(json.msg);
		            				}else{
		            					var url = json.msg;
		            					var width = 800;
		            					var left  = ($(document).width() - width)/2;
		            					left     = left>0?left:0;
		            					var features = 'width='+width+',height=600,top=50,left='+left;
		            					window.open(url,'Generate Facebook AccessToken',features);
		            				}
		            			}
		            		}
		            		else if(statusTxt=="error"){
		            			fb.alert('network error, try again latter.');
		            		}
		            	});
	        		}
	            }
	        }
	    });
}

fb.delToken = function(callback){
	var $tr = $("#accesstoken-search-table tbody tr");
	var datas = new Array();
	var title = 'Del Facebook Token Dialog';
	if($tr && $tr.length>0){
		for(i=0;i<$tr.length;i++){
			var data  = null;
			var tr    = $tr[i];
			var $check = $(tr).first().find('input');
			if($check.prop("checked")){
				data = {};
				var $td= $(tr).find('td');
				data.id = $td.first().html();
				datas.push(data);
			}
		}
	}else{
		fb.alert('Please seclet delete raw.',title);
	}
	if(datas.length==0){
		fb.alert('Please seclet delete raw.',title);
	}else{
		var msg = new cc.tool.json(datas).toString();
		fb.confirm(msg,title,function(result){
			 if(result) {
				 var option = {'data': datas};
				 $.post(fb.conf.service+'/service/token/del',new cc.tool.json(option).toString(),callback);
			 }
		});
	}
}

fb.tokenlist = function(callback){
	var option = {
	}
	$.get(fb.conf.service+'/service/token/list',option,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				json = new cc.tool.json(msg).decode();
				if(json.code!=0){
					fb.alert(json.msg);
				}else{
					if(json.datas){
						var tbody  = $('#accesstoken-search-table tbody');
						if(tbody.length>0)
							tbody.remove();
						var tokens = [];
						var app = $("#apps-select").val();
						for(i=0;i<json.datas.length;i++){
							if(app == json.datas[i].app.app_id){
								var token    = {};
								token.id     = json.datas[i].id?json.datas[i].id:json.datas[i].user.id;
								token.name   = json.datas[i].name?json.datas[i].name:json.datas[i].user.name;
								token.token  = json.datas[i].access_token;
								token.type   = json.datas[i].token_type;
								token.expires_in   = json.datas[i].expires_in;
								tokens.push(token);
							}
						}
						var parent  = $('#accesstoken-search-table')[0];
						var templet = fb.ui.templet().get('token-table');
						cc.tool.add_child_with_json(parent,{'datas':tokens},templet);
					}
				}
			}
		}
		else if(statusTxt=="error"){
			fb.alert('network error, try again latter.');
		}
	});
}

fb.scopeslist = function($permissions){
	//clear datas
//	var tbody  = $('#app-search-table tbody');
//	if(tbody.length>0)
//		tbody.remove();
	
	var option = {
	}
	$.get(fb.conf.service+'/service/app/scopes',option,function(msg,statusTxt,xhr){
		if(statusTxt=="success"){
			if(msg){
				json = new cc.tool.json(msg).decode();
				if(json.code!=0){
					fb.alert(json.msg);
				}else{
					var datas={};
					datas.user = [];
					datas.extended = [];
					
					var data = json.data;
					var userPermission     = data.userPermission.toString().split(',');
					var extendedPermission = data.extendedPermission.toString().split(',');
					
					var j=0;
					for(var i=0;i<userPermission.length;i++){
						if(j % 3 == 0){
							var Per = {};
							Per.permissions = [];
							datas.user.push(Per);
						}
						Per.permissions.push({'id':'userPermission-'+i,
							name:userPermission[i]});
						j++;
					}
					var j=0;
					for(var i=0;i<extendedPermission.length;i++){
						if(j % 3 == 0){
							var Per = {};
							Per.permissions = [];
							datas.extended.push(Per);
						}
						Per.permissions.push({'id':'userPermission-'+i,
							name:extendedPermission[i]});
						j++;
					}
					fb.selectPermissions(datas,$permissions);
				}
			}
		}
		else if(statusTxt=="error"){
			fb.alert('network error, try again latter.');
		}
	});
}

fb.selectPermissions = function(datas,$permissions){
	var templet = fb.ui.templet().get('select-permissions-table');
	cc.tool.element_init_data(templet,datas);
	var selected           = $permissions.html().split(',');
	BootstrapDialog.confirm({
	        title: 'Select Permissions Dialog',
	        message: templet,
	        type: BootstrapDialog.TYPE_DEFAULT, 
	        closable: false, 
	        draggable: true, 
	        btnCancelLabel: 'Cancel',
	        btnOKLabel: 'Confirm', 
	        callback: function(result) {
	            if(result) {
	            	var sb = '';
	            	var $checkbox = $(templet).find("[type='checkbox']");
	            	for(var i=0;i<$checkbox.length;i++){
	            		if($($checkbox[i]).is(':checked')){
	            			sb = sb + $($checkbox[i]).attr("name")+ ',';
	            		}
	            	}
	            	if(sb.lastWith(','))
	            		sb = sb.substring(0,sb.length-1);
	            	$permissions.html(sb);
	            }
	        }
	    });
	
	var $checkbox = $(templet).find("[type='checkbox']");
	for(var i=0;i<$checkbox.length;i++){
		if($.inArray($($checkbox[i]).attr('name'), selected)!=-1){
			$($checkbox[i]).attr('checked','true');
		}
		if($($checkbox[i]).attr('name')=='user_about_me' || $($checkbox[i]).attr('name')=='manage_pages'){
			$($checkbox[i]).attr('checked','true');
			$($checkbox[i]).attr('disabled',1);
		}
	}
	$(templet).find('.uiPillButton').on( "click",function(){
		$(templet).find('.uiPillButton').removeClass('uiPillButtonSelected');
		$(this).addClass('uiPillButtonSelected');
		$('.add-permissions-form table').addClass('hidden');
		if($(this).hasClass('user')){
			$(templet).find('.add-permissions-form').find('.user').removeClass('hidden');	
		}else{
			$(templet).find('.add-permissions-form').find('.extended').removeClass('hidden');	
		}
	});
	$(templet).find('.add-permissions-form').find('.user').click();
}
/**
Facebook Token Functions End
**/

/**
 Facebook App Functions Start
 **/
fb.applist = function(callback){
	var option = {
	}
	$.get(fb.conf.service+'/service/app/list',option,callback);
}

fb.delApp = function(callback){
	var $tr = $("#app-search-table tbody tr");
	var datas = new Array();
	var title = 'Del Facebook App Dialog';
	if($tr && $tr.length>0){
		for(i=0;i<$tr.length;i++){
			var data  = null;
			var tr    = $tr[i];
			var $check = $(tr).first().find('input');
			if($check.prop("checked")){
				data = {};
				var $td= $(tr).find('td');
				data.app_id = $td.first().html();
				datas.push(data);
			}
		}
	}else{
		fb.alert('Please seclet delete raw.',title);
	}
	if(datas.length==0){
		fb.alert('Please seclet delete raw.',title);
	}else{
		var msg = new cc.tool.json(datas).toString();
		fb.confirm(msg,title,function(result){
			 if(result) {
				 var option = {'data': datas};
					$.post(fb.conf.service+'/service/app/del',new cc.tool.json(option).toString(),callback);
			 }
		});
	}
}

fb.updateApp = function(callback,$td,permission_onclick){
	var $tr = $("#app-search-table tbody tr");
	var title = 'Edit Facebook App Dialog';
	var seclet = false;
	if($td){
		var option = {
    		'app_id':$($td[0]).html(),
    		'app_name':$($td[1]).html(),
    		'app_secret':$($td[2]).html(),
    		'verify_token':$($td[3]).html(),
    		'permissions':$($td[4]).find('p').html(),
    		'permission_onclick':permission_onclick
    	}
		fb.addApp(callback,option);
		return;
	}
	if($tr && $tr.length>0){
		for(i=0;i<$tr.length;i++){
			var tr    = $tr[i];
			var $check = $(tr).first().find('input');
			if($check.prop("checked")){
				var $td= $(tr).find('td');
				seclet = true;
				var option = {
	        		'app_id':$($td[0]).html(),
	        		'app_name':$($td[1]).html(),
	        		'app_secret':$($td[2]).html(),
	        		'verify_token':$($td[3]).html(),
	        		'permissions':$($td[4]).find('p').html(),
	        		'permission_onclick':permission_onclick
	        	}
				fb.addApp(callback,option);
				return;
			}
		}
	}else{
		fb.alert('Please seclet delete raw.',title);
	}
	if(!seclet){
		fb.alert('Please seclet delete raw.',title);
	}
}

fb.addApp = function(callback,option){
	var templet = fb.ui.templet().get('add-app-form');
	if(option){
		if(typeof(option) === "function"){
			$(templet).find('#permissions_input').html('user_about_me');
			$(templet).find('#permissions_input').on('click',option);
		}else{
			$(templet).find('#app_id_input').val(option.app_id);
	    	$(templet).find('#app_name_input').val(option.app_name);
	    	$(templet).find('#app_secret_input').val(option.app_secret);
	    	$(templet).find('#verify_token_input').val(option.verify_token);
	    	$(templet).find('#permissions_input').html(option.permissions);
	    	if(option.permission_onclick){
	    		$(templet).find('#permissions_input').on('click',option.permission_onclick);
	    	}
		}
		
	}
    BootstrapDialog.confirm({
        title: option?'Edit Facebook App Dialog':'Add Facebook App Dialog',
        message: templet,
        type: BootstrapDialog.TYPE_DEFAULT, 
        closable: false, 
        draggable: true, 
        btnCancelLabel: 'Cancel',
        btnOKLabel: option?'Save':'Confirm', 
        callback: function(result) {
            if(result) {
            	var app_id       = $(this.dialog.$modalContent).find('#app_id_input').val();
            	var app_name     = $(this.dialog.$modalContent).find('#app_name_input').val();
            	var app_secret   = $(this.dialog.$modalContent).find('#app_secret_input').val();
            	var verify_token = $(this.dialog.$modalContent).find('#verify_token_input').val();
            	var permissions = $(this.dialog.$modalContent).find('#permissions_input').html();
            	
            	var url = option?fb.conf.service+'/service/app/update':fb.conf.service+'/service/app/add';
            	var option = {
            		'app_id':app_id,
            		'app_name':app_name,
            		'app_secret':app_secret,
            		'verify_token':verify_token,
            		'permission_str':permissions
            	}
            	
            	$.post(url,new cc.tool.json(option).toString(),callback);
            }
        }
    });
}

/**
Facebook App Functions End
**/

