


(function(){
	cc.ui = function(ui_option){
		this.default_option = {
			templet_url:'templet',
			home_url:'home.html',
			alert_id:'cc-alert',
			templet_id:'cc_templet',
			footer_html:'&copy; CClivechat 2015',
			header_html:'CCLivechat',
			tab_id:'content_tabs',
			contect_id:'cc_contect',
			menu_id:'cc_memu',
			menu_data:'json/sidebar.json'
		}

		this.option = (ui_option)?ui_option:this.default_option;

		this.footer = {
			html:this.option.footer_html
		}
		this.header = {
			name:this.option.header_html
		}

		this.menu = function(menu_id,menu_data){
			if(!menu_id)
				menu_id = this.option.menu_id;
            if(!menu_data)
				menu_data = this.option.menu_data;

			var load = function(){
					var menu_templet = this.cc.templet().get(menu_id);
					var menu_parent  = $('#'+menu_id)[0];
					var listener     = this.setListener;
					var ui           = this.cc;
					$.get(menu_data,function(msg,statusTxt,xhr){
						if(statusTxt=="success"){
							if(msg){
								if(msg.startWith('{'))
									sidebar_json = eval('('+msg+')');
								else if(msg.startWith('['))
									sidebar_json = eval(msg);
								cc.tool.add_child_with_json(menu_parent,sidebar_json,menu_templet);
								listener(ui);
								ui.alert_.info(cc.succ[2002]);
							}
						}
						else if(statusTxt=="error"){
							ui.alert_.warn(cc.error[4002]);
						}
					});
			}

			var setListener = function(cc){
				$( "#"+ menu_id+" .panel-heading a" ).prop('cc',cc);
				$( "#"+ menu_id+" .panel-collapse a" ).prop('cc',cc);

				$( "#"+ menu_id+" .panel-heading a" ).on( "click", function(event){
					if($(this).find('.glyphicon-chevron-down').length==0){
						var view     = $(this).attr('view');
						var name     = $(this).find('span').html();
						var id       = 'sidebar-'+name;
						this.cc.tab_.add_tab({'tab_name':name,'id':id,'view_url':view});
					}
				});
				$( "#"+ menu_id+" .panel-collapse a" ).on( "click", function(event){
					var view     = $(this).attr('view');
				    var name     = $(this).html();
				    var id       = 'sidebar-'+name;
				    this.cc.tab_.add_tab({'tab_name':name,'id':id,'view_url':view});
				});
			}

			var this_ = this.menu;
			this_.cc  = this;
			this_.load = load;
			this_.setListener = setListener;
			return this_;
		}

		this.tab = function(tab_id){
			if(!tab_id){
				tab_id = this.option.tab_id;
				home_url = this.option.home_url;
			}

			var set_home_tab = function(){
						var home = $( "#"+tab_id+" li.active")[0];
						$(home).find('a').attr('view',home_url);
						this.setListener(home);
			}

			var add_tab = function(tabs_json){
				if(tabs_json){
					var id = tabs_json.id;
					if($('#'+tab_id).find('#'+id).length!=0){
						var li = $('#'+tab_id).find('#'+id)[0];
						$(li).find('a').click();
						return;
					}
				}
				var tabs_menu = this.cc.templet().get(tab_id);
				var tabs_parent = $('#'+tab_id)[0];
				var li = cc.tool.add_child_with_json(tabs_parent,tabs_json,tabs_menu);
				this.setListener(li);
			}

			var setListener = function(li){
				if(!li)return;
				if($(li).find('a').length!=0)
					$(li).find('a')[0].cc = this.cc;
				if($(li).find('span').length!=0)
					$(li).find('span')[0].cc = this.cc;
				$( "#"+tab_id+" a" ).on( "click", this.onclick);
				$( "#"+tab_id+" span" ).on( "click", this.onclose);
				$( "#"+tab_id+" span" ).on( "mouseenter", function(event){$(this).addClass('glyphicon-remove-mouseenter')});
				$( "#"+tab_id+" span" ).on( "mouseleave", function(event){$(this).removeClass('glyphicon-remove-mouseenter')});
				$(li).find('a').click();
			}

			var onclick = function(event){
				var cc = this.cc;
				$( "#"+tab_id+" li.active").removeClass('active');
				$(this).parent().addClass('active');
				var id = $(this).parent().attr('id');
				if(!cc.tabcontents_.show('contect-'+id)){
					var view_url = $(this).attr('view');
					cc.tabcontents_.load(view_url,'contect-'+id,
						function(msg,statusTxt,xhr){

						});
				}
			}
			var onclose = function(event){
				var li = $(this).parent();
				if($(li).hasClass('active')){
					var next = li.next()[0];
					if(next)
						$(next).find('a').click();
					else{
						var prev = li.prevUntil()[0];
						$(prev).find('a').click();
					}
				}
				var id = $(this).parent().attr('id');
				var cc = this.cc;
				cc.tabcontents_.close('contect-'+id);
				$(this).parent().remove();
			}

			var this_   = this.tab;
			this_.cc    = this;
			this_.add_tab = add_tab;
			this_.onclick = onclick;
			this_.onclose = onclose;
			this_.setListener = setListener;
			this_.set_home_tab = set_home_tab;
			return this_;
		}

		this.tabcontents = function(contect_id){
			if(!contect_id){
				contect_id = this.option.contect_id;
			}

			var load = function(url,id,fun){
				    if(url.indexOf('?')!=-1){
				    	url = url + "&random="+Math.random();
				    }else{
				    	url = url + "?random="+Math.random();
				    }
					var div = cc.tool.createElement('div',{'id':id,'class':'cc-contect-body'});
					$("#"+contect_id)[0].appendChild(div);
					$(div).load(url,fun);
			}

			var show = function(id){
				  $("#"+contect_id+" .cc-contect-body").hide();
					if($('#'+id).length!=0){
						  $('#'+id).show();
							return true;
					}
				  return false;
			}
			
			var close = function(id){
				$("#"+contect_id).find('#'+id).remove();
			}

			var this_    = this.tabcontents;
			this_.cc     = this;
			this_.show   = show;
			this_.load   = load;
			this_.close  = close;
			return this_;
		}

		this.alert = function(alert_id){
			if(!alert_id){
				alert_id = this.option.alert_id;
			}
			var resp = function(statusTxt,msg,error,tit,dis){
				if(statusTxt=="success"){
					if(dis)
						this.info(msg,tit);
					else
						this.warn(msg,tit);
				}else if(statusTxt=="error"){
					if(dis)
						this.succ(msg,tit);
					else
						this.error(msg,tit);
				}
			}
			var succ = function(msg,tit){
				alert_json = {
						alert:'alert-success',
						close:{},
						title:(tit?tit:''),
						msg:msg
				}
				this.alert(alert_json);
			}
			var info = function(msg,tit){
				alert_json = {
						alert:'alert-info',
						title:(tit?tit:''),
						msg:msg
				}
				this.alert(alert_json);
			}
			var warn = function(msg,tit){
				alert_json = {
						alert:'alert-warning',
						title:(tit?tit:''),
						msg:msg
				}
				this.alert(alert_json);
			}
			var error = function(msg,tit){
				alert_json = {
						alert:'alert-danger',
						close:{},
						title:(tit?tit:''),
						msg:msg
				}
				this.alert(alert_json);
			}
			var alert = function(alert_json){
				var cc_alert = this.cc.templet().get(alert_id);
				var alert_parent = $('#'+alert_id)[0];
				$(alert_parent).html('');
				$(alert_parent).hide();
				cc.tool.add_child_with_json(alert_parent,alert_json,cc_alert);
				$(alert_parent).fadeIn("slow");
				if(alert_json && !alert_json.close)
					setTimeout(this.hide,3000);
			}
			var hide = function(){
				var alert_parent = $('#'+alert_id)[0];
				$(alert_parent).fadeOut("slow",function(){
					$(alert_parent).html('');
				});
			}
			
			var this_ = this.alert;
			this_.cc = this;
			this_.resp = resp;
			this_.succ = succ;
			this_.info = info;
			this_.warn = warn;
			this_.error= error;
			this_.alert= alert;
			this_.hide=hide;
			return this_;
		}

		this.templet = function(templet_id,templet_url){
			if(!templet_id)
				templet_id = this.option.templet_id;
			if(!templet_url)
				templet_url = this.option.templet_url;
			var this_ = this.templet;

			this_.get = function(name,fun){
				var t = $("#"+templet_id).find("[name='"+name+"']");
				if(t.length>0)
					return t[0];
			  return null;
			}
			this_.del = function(name){
				var t = $("#"+templet_id).find("[name='"+name+"']");
				if(t.length>0)
					return t.remove();
			},
			this_.load = function(names,fun){
				if(names){
					if(names instanceof Array){
						var fun_ = function(responseTxt,statusTxt,xhr){
							var this_ = this.this_;
							if(statusTxt=="success"){
						    }else if(statusTxt=="error"){
						    	this_.request_error=xhr;
						    }
							this_.request --;
							if(this_.request==0){
								if(this_.fun && !this_.request_error)
									this_.fun.call(this,'',"success");
								else
									this_.fun.call(this,'',"error",this_.request_error);
							}
						}

						var this_ = this;
						this_.fun = fun;
						this_.request = names.length;
						this_.request_error = null;
						for(var i=0;i<names.length;i++){
							var div = cc.tool.createElement('div');
							div.this_ = this_;

							$("#"+templet_id)[0].appendChild(div);
							$(div).load(templet_url+"/"+names[i],fun_);
						}
					}else{
						var div = cc.tool.createElement('div');
						$("#"+templet_id)[0].appendChild(div);
						$(div).load(templet_url+"/"+names,fun);
					}
				}
			}
			return this_;
		}

		this.templet_ = this.templet();
		this.alert_   = this.alert();
		this.tab_     = this.tab();
		this.tabcontents_ = this.tabcontents();
		this.menu_    = this.menu();
	}
})();
