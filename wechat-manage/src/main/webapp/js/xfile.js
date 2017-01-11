var xfile = {};
xfile.conf={};
xfile.conf.service = ".";
//xfile.conf.service = "http://192.168.3.113:8080/xfile";

xfile.conf.owner;
xfile.conf.xgroup = "1";
xfile.conf.login  = "login";
xfile.conf.xgroupList = {
	'sidebar-Home':0,
	'sidebar-UAT-CR':1,
	'sidebar-UAT2-FB-LiveChat':2,
	'sidebar-PRO':3
}
xfile.conf.xgroupTab = {
	0:'contect-sidebar-Home',
	1:'contect-sidebar-UAT-CR',
	2:'contect-sidebar-UAT2-FB-LiveChat',
	3:'contect-sidebar-PRO'
}
	
$(document).ready(function(){
	jQuery.fn.center = function () { 
		this.css("position","absolute"); 
		this.css("top", ( $(window).height() - this.height() ) / 2+$(window).scrollTop() + "px"); 
		this.css("left", ( $(window).width() - this.width() ) / 2+$(window).scrollLeft() + "px"); 
		return this;
	}
	fb.ui = new cc.ui();
	fb.ui.templet_.load(new Array('sidebar_menu.html','tabs_menu.html','common_templet.html'),
			function(responseTxt,statusTxt,xhr){
		fb.ui.alert_.resp(statusTxt,cc.succ[2001],cc.error[4001],'',1);
		fb.ui.tab_.set_home_tab();
		fb.ui.menu_.load();
	});
	
	$('#site-nav-logout').on('click',xfile.logout);
	if(!xfile.conf.owner)
		xfile.loginCheck();
});

xfile.rcReay = function(){
	$.get(xfile.conf.service+'/services/user/list',{},function(msg,statusTxt,xhr){
		 if(statusTxt=="success"){
			 json = new cc.tool.json(msg).decode();
			 if(json.code==0){
				 var user_tab = xfile.conf.xgroupTab[xfile.conf.xgroup];
				 if(json.datas){
					
					 var users   = [];
					 for(i=0;i<json.datas.length;i++){
						 if(json.datas[i].username!=xfile.conf.owner){
							 var user = {};
							 user.title = json.datas[i].username;
							 user.user  = json.datas[i].username;
							 users.push(user);
						 }
					 }
					 users.unshift({title:xfile.conf.owner,user:xfile.conf.owner});
					 
					 var templet = fb.ui.templet().get('user-xfile-dive');
					 var parent  = $('#'+user_tab).find('#xfile-contect')[0];
					 cc.tool.add_child_with_json(parent,{'datas':users},templet);
					 xfile.start();
				 }
			 }else
				 fb.alert(json.msg);
		 }else if(statusTxt=="error"){
			 fb.alert('network error, try again latter.');
		 }
	});
	
}

xfile.start = function(){
	var $a = $('.add-file-'+xfile.conf.owner);
	$a.removeClass('hidden');
	
	var user_tab = xfile.conf.xgroupTab[xfile.conf.xgroup];
	$('#'+user_tab).find('.add-file').on( "click", function(){
		xfile.addFile(function(msg,statusTxt,xhr){
			var title = 'Add Document Dialog';
			if(statusTxt=="success"){
				if(msg){
					json = new cc.tool.json(msg).decode();
					if(json.code!=0){
						fb.alert(json.msg,title);
					}else{
						fb.alert(json.msg,title);
					}
					$('#'+user_tab).find('.refresh-file').click();
				}
			}
			else if(statusTxt=="error"){
				fb.alert('network error, try again latter.',title);
			}
		});
	});

	$('#'+user_tab).find('.refresh-file').on( "click", function(){
		var owner = $(this).attr('name');
		xfile.listFile(owner);
	});
	
	$('#'+user_tab).find('.refresh-file').click();
}

xfile.loginEdit = function(){
	var username = $('#loginbox').find('#login-username').val();
	var passwd   = $('#loginbox').find('#login-password').val();
	var passwd2  = $('#loginbox').find('#login-password2').val();
	
	if(passwd!=passwd2){
		 $('#login-warn').html("password is not same");
		 return;
	}
	if(!username || !passwd){
		 $('#login-warn').html("please input username & password");
		 return;
	}
	
	var option = {
			'username':username,
			'passwd':passwd
	}
	
	$.post( xfile.conf.service+'/services/user/update',new cc.tool.json(option).toString(),function(msg,statusTxt,xhr){
		 if(statusTxt=="success"){
			 json = new cc.tool.json(msg).decode();
			 if(json.code==0){
				 xfile.login();
			 }	
			 else{
				 $('#login-warn').html(json.msg);
			 }
		 }else if(statusTxt=="error"){
			 $('#login-warn').html('network error, try again latter.');
		 }
	});
}

xfile.logout = function(){
	 var option = {};
	 $.get(xfile.conf.service+'/services/user/logout',option,function(){
		 window.location.reload(true);
	 });
}

xfile.login = function(){
	var username = $('#loginbox').find('#login-username').val();
	var passwd   = $('#loginbox').find('#login-password').val();
	
	if(!username || !passwd){
		 $('#login-warn').html("please input username & password");
		 return;
	}
	
	var option = {
			'username':username,
			'passwd':passwd
	}
	$.post( xfile.conf.service+'/services/user/login',new cc.tool.json(option).toString(),function(msg,statusTxt,xhr){
		 if(statusTxt=="success"){
			 json = new cc.tool.json(msg).decode();
			 if(json.code==0){
				 xfile.conf.owner = option.username;
				 $('#site-nav-login').html(xfile.conf.owner);   
				 $('#loginbox').hide();
				 $('#login-warn').html("");
			 }else if(json.code==408){
				 $('#login-warn').html(json.msg);
				 $('#password-div').removeClass('hidden');
				 $('#btn-login').unbind('click', xfile.login);
				 $('#btn-login').on('click', xfile.loginEdit);
			 }		
			 else{
				 $('#login-warn').html(json.msg);
			 }
		 }else if(statusTxt=="error"){
			 $('#login-warn').html('network error, try again latter.');
		 }
	});
};

xfile.loginCheck = function(){
	 $.get(xfile.conf.service+'/services/user/islogin',{},function(msg,statusTxt,xhr){
		 var dologin = false;
		 if(statusTxt=="success"){
			 json = new cc.tool.json(msg).decode();
			 if(json.code==0){
				 dologin = true;
				 xfile.conf.owner = json.msg;
				 $('#site-nav-login').html(xfile.conf.owner);   
			 }
		 }
		 if(!dologin){
			 if($('#loginbox').length==0){
					$('.login-div').load('./login/index.html',function(msg,statusTxt,xhr){
						if(statusTxt=="success"){
							$('#btn-login').on('click', xfile.login);
							$('#loginbox').keydown(function(e){
							    if(e.keyCode == 13){
							    	$('#btn-login').click();
							    }
							});
						}
						else if(statusTxt=="error"){
							ui.alert_.warn("login:system error");
						}
					});
				}else{
					$('#loginbox').show();
					$('#btn-login').on('click', xfile.login);
					$('#loginbox').keydown(function(e){
					    if(e.keyCode == 13){
					    	$('#btn-login').click();
					    }
					});
				}
		 }
	 });
}

xfile.uploadOptions = function(obj,add){
	return {
    	//acceptFileTypes:  '/(\.|\/)(gif|jpe?g|png)$/i',
	    // maxNumberOfFiles : 1,
	    //maxFileSize: 4*1024*1024, 
	        dataType: 'text',
	        done: function (e, data) {
	           var file_msg = data._response.result;
	           var json = new cc.tool.json(file_msg).decode();
	           console.dir(json);
	           if(json.url){
	        	   var file_info = document.createElement('div');
	        	   $(file_info).attr("id","file_info_id"); 
	        	   $(file_info).html(file_msg);
	        	   $(file_info).addClass('hidden');
	        	   $(obj).append(file_info);
	           }else if(json.code && json.code==403){
					 xfile.loginCheck();
			   }else{
	        	   fb.alert(json.message,"upload document");
	           }
	        },
	        add: function (e, data) {
	            if (e.isDefaultPrevented()) {
	                return false;
	            }
	            var file_size = data.originalFiles[0].size;
	        	var file_name = data.originalFiles[0].name;
	        	console.dir(obj);
	        	if(add)
	        		$(obj).find('#file_name_input').val(file_name);
	        	$(obj).find('#file-upload-div').addClass('hidden');
	        	$(obj).find('#file-upload').removeClass('hidden');
	        	
	        	var jqXHR = null;
	        	$(obj).find('#start_button_id').on('click',function(){
	        		data.process().done(function () {
	            		jqXHR = data.submit();
	            		$(obj).find('#start_button_id').attr("disabled","disabled"); 
	            	});
	        	});
	        	$(obj).find('#cancel_button_id').on('click',function(){
	        		$(obj).find('#start_button_id').removeAttr("disabled"); 
	        		$(obj).find('#file_name_input').val('');
	        		$(obj).find('#file-upload').addClass('hidden');
		        	$(obj).find('#file-upload-div').removeClass('hidden');
		        	$(obj).find('#file_info_id').remove(); 
		        	$('#progress .progress-bar').css('width','0%');
		        	if(jqXHR){
		        		jqXHR.abort();
		        	}
	        	});
	        },
	        submit: function (e, data) {
	        	data.url       = xfile.conf.service + "/file";
	        	$(obj).find('#start_button_id').attr("disabled","disabled"); 
	        },
	        fail: function (e, data) {
	        	 fb.alert(data.errorThrown,"upload document");
	        },
			autoUpload: false,
			progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            if(progress<20)
	            	progress = 0;
	            else if(progress<30)
	            	progress = 20;
	            else if(progress<50)
	            	progress = 40;
	            else if(progress<70)
	            	progress = 60;
	            else if(progress<90)
	            	progress = 80;
	            else if(progress<100)
	            	progress = 90;
	            else
	            	progress = 100;
	            
	            $('#progress .progress-bar').css(
	                'width',
	                progress + '%'
	            );
	        }
	    };
}

xfile.download = function(this_){
	$td = $(this_).parent().parent().find('td');
	if($td.length>0){
		url = $($td[2]).html();
	}
	if(url){
		url = xfile.conf.service+'/file/'+url;
		$.fileDownload(url)
		.done(function (msg) { 
			//alert('File download success!');
			console.dir(msg);
		})
		.fail(function (msg) { 
			//<pre style="word-wrap: break-word; white-space: pre-wrap;">{"code":403,"msg":"user%20is%20not%20authorized","count":0}</pre>
			var dom = cc.tool.str_to_element(msg);
			var json = $(dom).html();
			json = new cc.tool.json(msg).decode();
			if(json.code==403){
				 xfile.loginCheck();
			}
		});
	}
}

xfile.del = function(this_){
	$td = $(this_).parent().parent().find('td');
	if($td.length>0){
		var xfileid = $($td[0]).html();
		var name    = $($td[3]).html();
		var owner   = $($td[1]).html();
		var msg     = 'Delete Document : ' + name;
	}
	if(xfileid){
		fb.confirm(msg,'Delete Document Dialog',function(result){
			 if(result) {
				 var option = {'xfileid': xfileid,'owner':owner};
				 $.get(xfile.conf.service+'/services/xfile/del',option,function(msg,statusTxt,xhr){
					 if(statusTxt=="success"){
						 json = new cc.tool.json(msg).decode();
						 if(json.code==0){
							 xfile.listFile(option.owner);
						 }else if(json.code==403){
							 xfile.loginCheck();
						 }else
							 fb.alert(json.msg);
					 }else if(statusTxt=="error"){
						 fb.alert('network error, try again latter.');
					 }
				 });
			 }
		});
	}
}

xfile.listFile = function(user){
	$.get(xfile.conf.service+'/services/xfile/search',{'fileOwner':user,'groupID':xfile.conf.xgroup},function(msg,statusTxt,xhr){
		 if(statusTxt=="success"){
			 json = new cc.tool.json(msg).decode();
			 if(json.code==0){
				 var user_low = user.toLowerCase();
				 var user_tab = xfile.conf.xgroupTab[xfile.conf.xgroup];
				 var tbody  = $('#'+user_tab).find('#file-search-table-'+user_low+' tbody');
				 if(tbody.length>0)
					tbody.remove();
				 
				 if(json.datas && json.datas.length>0){				
					var files = [];
					for(i=0;i<json.datas.length;i++){
						var file    = {};
						file.idxfile     = json.datas[i].idxfile;
						file.xfileName   = json.datas[i].xfileName;
						file.fileID      = json.datas[i].fileID;
						file.fileOwner   = json.datas[i].fileOwner;
						file.update_time = json.datas[i].update_time?new Date(parseInt(json.datas[i].update_time)).Format("yyyy-MM-dd hh:mm"):'';
						file.commonts    = json.datas[i].commonts;
						files.push(file);
					}					
					
					var parent  = $('#'+user_tab).find('#file-search-table-'+user_low)[0];
					var templet = fb.ui.templet().get('cr-file-table');
					
					var obj = cc.tool.add_child_with_json(parent,{'datas':files},templet);
					if(user_low!=xfile.conf.owner.toLowerCase()){
						$(obj).find('.hidden-later').addClass('hidden');
					}
					
				 }
			 } else if(json.code==403){
				 xfile.loginCheck();
			 }else
				 fb.alert(json.msg);
		 }else if(statusTxt=="error"){
			 fb.alert('network error, try again latter.');
		 }
	});
}

xfile.editFile = function(this_){
	console.log(this_);
	var templet = fb.ui.templet().get('add-file-form');
	$(templet).find('#file-upload-button').fileupload(xfile.uploadOptions(templet));
	$td = $(this_).parent().parent().find('td');
	if($td.length>0){
		$(templet).find('#file_name_input').val($($td[3]).html());
		$(templet).find('#comments_id').val($($td[4]).html());
		$(templet).find('#file-id').html($($td[2]).html());
		$(templet).find('#idxfile').html($($td[0]).html());
		$(templet).find('#file-owner').html($($td[1]).html());
	}
	BootstrapDialog.confirm({
        title: 'Add Document Dialog',
        message: templet,
        type: BootstrapDialog.TYPE_DEFAULT, 
        closable: false, 
        draggable: true, 
        btnCancelLabel: 'Cancel',
        btnOKLabel: 'Confirm', 
        callback: function(result) {
        	var title = 'Add Document Dialog';
            if(result) {
            	$('#progress .progress-bar').css('width','0%');
        		
        		var name     = $(templet).find('#file_name_input').val();
        		var commnets = $(templet).find('#comments_id').val();
        		var fileid  = $(templet).find('#file-id').html();
        		var idxfile  = $(templet).find('#idxfile').html();
        		var owner    = xfile.conf.owner;
        		var option = {'xfileName':name,'commonts':commnets,'fileOwner':owner,'idxfile':idxfile,'fileID':fileid};
        		
        		var file_info = $(templet).find('#file_info_id');
        		if(file_info.length !=0 ){
        			option.file = new cc.tool.json(file_info.html()).decode();
            	}
        		
				$.post( xfile.conf.service+'/services/xfile/update',new cc.tool.json(option).toString(),function(msg,statusTxt,xhr){
					 if(statusTxt=="success"){
						 json = new cc.tool.json(msg).decode();
						 if(json.code==0)
							 fb.alert(json.msg,title,function(result){xfile.listFile(owner);});
						 else if(json.code==403){
							 xfile.loginCheck();
						 }
						 else
							 fb.alert(json.msg,title);
					 }else if(statusTxt=="error"){
            			 fb.alert('network error, try again latter.',title);
            		 }
				});
				
            	var file_info = $(templet).find('#file_info_id');
            	if(file_info.length==0){
            		
            	}else{
            		
            	}
            }else{
            	$('#progress .progress-bar').css('width','0%');
            }
        }
    });
}

xfile.addFile = function(){
	var templet = fb.ui.templet().get('add-file-form');
	$(templet).find('#file-upload-button').fileupload(xfile.uploadOptions(templet,1));
	BootstrapDialog.confirm({
        title: 'Add Document Dialog',
        message: templet,
        type: BootstrapDialog.TYPE_DEFAULT, 
        closable: false, 
        draggable: true, 
        btnCancelLabel: 'Cancel',
        btnOKLabel: 'Confirm', 
        callback: function(result) {
        	var title = 'Add Document Dialog';
            if(result) {
            	var file_info = $(templet).find('#file_info_id');
            	if(file_info.length==0){
            		
            	}else{
            		$('#progress .progress-bar').css('width','0%');
            		var file     = new cc.tool.json(file_info.html()).decode();
            		var name     = $(templet).find('#file_name_input').val();
            		var commnets = $(templet).find('#comments_id').val();
            		var owner    = xfile.conf.owner;
            		var groupID  = xfile.conf.xgroup;
            		var option = {'file': file,'xfileName':name,'commonts':commnets,'fileOwner':owner,'groupID':groupID};
					$.post( xfile.conf.service+'/services/xfile/add',new cc.tool.json(option).toString(),function(msg,statusTxt,xhr){
						 if(statusTxt=="success"){
							 json = new cc.tool.json(msg).decode();
							 if(json.code==0)
								 fb.alert(json.msg,title,function(result){xfile.listFile(owner);});
							 else if(json.code==403){
								 xfile.loginCheck();
							 }
							 else
								 fb.alert(json.msg,title);
						 }else if(statusTxt=="error"){
	            			 fb.alert('network error, try again latter.',title);
	            		 }
					});
            	}
            }else{
            	$('#progress .progress-bar').css('width','0%');
            }
        }
    });
}
