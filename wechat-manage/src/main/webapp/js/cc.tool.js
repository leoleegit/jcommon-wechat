

this.cc = {
		version: '2015-07-13'
};
if('$' in this) $.cc = this.cc;

(function(){
	cc.tool = {
			str_to_element:function(str){
				var dom = document.createDocumentFragment();
				if(DOMParser){
					var doc = new DOMParser().parseFromString(str, "text/xml");
					return doc.documentElement;
				}else if(ActivexObject){
					var doc = new ActivexObject ("MSXML2.DOMDocument");
					doc.loadXML(str);
					return doc;
				}
				return dom;
			},
			get_json_mould : function(element,json){
		        if(typeof element == 'string'){
		            var el = this.str_to_element(element);
		        }else {
		            var el = element;
		        }

		        if(!json)
		            json = {};

		        if(!el)
					return null;
				if(!el.localName && el.nodeValue){
					var names = this.get_names(el.nodeValue);
					if(names){
						for(var i=0;i<names.length;i++){
							json[names[i]] = names[i];
						}
					}
					return json;
				}

				if(!el.localName)
					return json;

				var attributes = el.attributes;
				if(attributes){
					var dep   = el.attributes["dependency"];
					var group = el.attributes["group"];
					if(dep){
						el.attributes["dependency"]=0;
						json[dep.nodeValue] = this.get_json_mould(el,json[dep.nodeValue]);
						return json;
					}else if(group){
						el.attributes["group"]=0;
						json[group.nodeValue] = new Array();
						json[group.nodeValue].push(this.get_json_mould(el));
						return json;
					}
				}
				
				
				for(var attr=0;attr<attributes.length;attr++){
					if(attributes[attr] && attributes[attr].nodeName && attributes[attr].nodeValue){
						var names = this.get_names(attributes[attr].nodeValue);
						if(names){
							for(var i=0;i<names.length;i++){
								json[names[i]] = names[i];
							}
						}
					}
				}
				if(el.childNodes){
					var nodes = el.childNodes;
					for(var node=0; node < nodes.length;node++){
						json = this.get_json_mould(el.childNodes[node],json);
					}
				}
				return json;
		    },
			createElement:function (localName,attr,text,innerHTML){
				var dom = document.createElement(localName);
				for(var i in attr){
					 $(dom).attr(i,attr[i]);
				}
				if(text){
					var node = document.createTextNode(text);
					dom.appendChild(done);
				}
				if(innerHTML)
					dom.innerHTML = innerHTML;
				return dom;
			},
			element_to_str:function (element){
				//
				if(!element)
					return '';
				if(!element.localName && element.nodeValue)
					return element.nodeValue;
				if(!element.localName)
					return '';

				var str = '<'+element.localName;
				var attributes = element.attributes;
				for(var attr in attributes){
					if(attributes[attr] && attributes[attr].nodeName && attributes[attr].nodeValue)
						str = str + ' '+attributes[attr].nodeName + '="'+attributes[attr].nodeValue+'"'
				}
				str = str + ' >';
				if(element.childNodes){
					var nodes = element.childNodes;
					for(var node=0; node < nodes.length;node++){
						str = str + this.element_to_str(nodes[node]);
					}
				}

				str = str + '</'+element.localName+'>'
				return str;
			},
			map:function(){
				this.store = new Array();
				this.put   = function(key,value){
					var obj = this.get(key);
					if(obj)
						this.del(key);
					else
						obj = new Object();
					obj.key = key;
					obj.value = value;
					this.store.push(obj);
				}
				this.get  = function(key){
					for(var i=0;i<this.store.length;i++){
						var obj = this.store[i];
						if(obj.key && obj.key==key)
							return obj.value;
					}
				}
				this.del  = function(key){
					var index;
					for(var i=0;i<this.store.length;i++){
						var obj = this.store[i];
						if(obj.key && obj.key==key){
							index = i;
							break;
						}
					}
					if(index)
						this.store.remove(index);
				}
				this.size = function(){
					return this.store.length;
				}
			},
			json_format : function(json){
				return this.json_to_str(json,'</br>','&nbsp;&nbsp;&nbsp;&nbsp;');
			},
			json : function(obj){
				if(typeof obj == 'string'){
					if(obj.startWith('{'))
						obj = eval('('+obj+')');
					else if(obj.startWith('['))
						obj = eval(obj);
		        }
				this.obj = obj;
				this.decode = function(){
					var obj = this.obj;
					if(obj && cc.tool.is_option_object(obj)){
						for(var key in obj){
							var value = obj[key];
							var is_option = cc.tool.is_option_object(value);
							if(is_option || (value instanceof Array)){
								obj[key] = new cc.tool.json(value).decode();
							}else{
								obj[key] = decodeURIComponent(obj[key]);
							}
						}
					}else if(obj && (typeof obj === "object") && (obj instanceof Array)){
						if(cc.tool.has_prop(obj)){
							for(var key=0;key<obj.length; key++){
								var value = obj[key];
								if(cc.tool.is_option_object(value)){
									obj[key] = new cc.tool.json(value).decode();
								}
							}
						}
					}
					return obj;
				};
				this.toString = function(){
					var msg = '';
					var obj = this.obj;
					if(obj && cc.tool.is_option_object(obj)){
						msg = msg + '{';
						for(var key in obj){
							var value = obj[key];
							var is_option = cc.tool.is_option_object(value);
							if(is_option || (value instanceof Array)){
								value = new cc.tool.json(value).toString();
							}else{
								value = '"'+ encodeURIComponent(value) + '"';
							}
							key = '"'+ key + '"';
							msg = msg + key + ':' + value + ',';
						}
						if(msg.lastWith((",")))
							msg = msg.substring(0,msg.length-1);
						msg = msg + '}';
					}else if(obj && (typeof obj === "object") && (obj instanceof Array)){
						if(cc.tool.has_prop(obj)){
							msg = msg +'[';
							for(var key=0;key<obj.length; key++){
								var value = obj[key];
								if(cc.tool.is_option_object(value)){
									value = new cc.tool.json(value).toString();
									msg = msg + value + ',';
								}
							}
							if(msg.lastWith((",")))
								msg = msg.substring(0,msg.length-1);
							msg = msg + ']';
						}
					}
					return msg;
				};
			},
			json_to_str : function(obj,spirt,tab,base_tab){
				if(typeof obj == 'string'){
					if(obj.startWith('{'))
						obj = eval('('+obj+')');
					else if(obj.startWith('['))
						obj = eval(obj);
		        }
				
				if(!spirt)spirt='';
				if(!tab)tab='';
				if(!base_tab)base_tab=tab;
				var temp = tab;
				tab = tab + base_tab;
				var msg = '';
				if(obj && this.is_option_object(obj)){
					msg = temp + msg + '{'+spirt;
					for(var key in obj){
						var value = obj[key];
						var is_option = this.is_option_object(value);
						if(is_option || (value instanceof Array)){
							line = this.json_to_str(value,spirt,tab,base_tab);
							if(line.lastWith((spirt)))
								line = line.substring(0,line.length-(spirt.length));
							value = spirt + line;
						}
						else if(typeof value === 'number' || typeof value === 'boolean')
							value = value;
						else
							value = '"'+ value + '"';
						key = '"'+ key + '"';
						line =  tab + key + ':' + value + ','+spirt;
						msg = msg + line;
					}
					if(msg.lastWith((","+spirt)))
						msg = msg.substring(0,msg.length-((","+spirt).length))+spirt;
					line = temp + '}' + spirt;
					msg = msg + line;
				}else if(obj && (typeof obj === "object") && (obj instanceof Array)){
					if(this.has_prop(obj)){
						msg = msg + temp +'['+spirt;
						for(var key=0;key<obj.length; key++){
							if(this.is_option_object(obj[key])){
								line = this.json_to_str(obj[key],spirt,tab,base_tab);
								if(line.lastWith((spirt)))
									line = line.substring(0,line.length-(spirt.length))+','+spirt;
								msg = msg +line;
							}
						}
						if(msg.lastWith((","+spirt)))
							msg = msg.substring(0,msg.length-((","+spirt).length))+spirt;
						msg = msg + temp + ']';
					}
				}else{
					msg = obj;
				}
				return msg;
			},
		    has_prop : function(obj){
				var hasProp = false;
			    for (var prop in obj){
			    	if(this.is_option_object(obj[prop])){
				    	hasProp = true;
				        break;
			    	}
			    }
			    return hasProp;
			},
		    is_option_object : function(obj){
				if(obj && (typeof obj === "object") && !(obj instanceof Array)){
					var hasProp = false;
				    for (var prop in obj){
				        hasProp = true;
				        break;
				    }
				    return hasProp;
				}
				return false;
			},
			get_names:function (str){
				var arr = new Array();
				if(!str)
					return arr;
				var index = str.indexOf('{{');
				if(index!=-1){
					var ss = str.split('{{');
					for(var s=0;s<ss.length;s++){
						if(ss[s].indexOf('}}')!=-1){
							if(ss[s].startWith('{')){
								var start = 1;
								value = value + '{';
							}else{
								start = 0;
							}
							var name = ss[s].substring(start,ss[s].indexOf('}}'));
							arr.push(name);
						}
					}
				}
				return arr;
			},
			get_json_value:function(json,name){
				if(!json)
					return null;
				var value = json;
				if(name && name.indexOf(':')!=-1){
					name = name.split(':')[1];
				}
				return value[name]?value[name]:'';
			},
			get_group_name:function(str){
				var arr = this.get_names(str);
				for(var i=0;i<arr.length;i++){
					var val = arr[i];
					if(val && val.indexOf(':')!=-1){
						val = val.split(':')[0];
						return val;
					}
				}
				return null;
			},
			/**
			 *  str:#collapse{{index}}and{{id}}
			 *  json:
				{
					id:1,
					index:2
				}
			 *  return #collapse2and1
			 */
			map_data:function (str,json){
				if(!str)
					return null;
				var value = '';
				var index = str.indexOf('{{');
				if(index!=-1){
					var ss = str.split('{{');
					for(var s=0;s<ss.length;s++){
						if(ss[s].indexOf('}}')!=-1){
							if(ss[s].startWith('{')){
								var start = 1;
								value = value + '{';
							}else{
								start = 0;
							}
							var name = ss[s].substring(start,ss[s].indexOf('}}'));
							var json_value = this.get_json_value(json,name);
							if(typeof json_value != 'undefined'){
								value = value + json_value;
								value = value + ss[s].substring(ss[s].indexOf('}}')+2);
							}else{
								if(s==0)
									value = value + ss[s];
								else
									value = value+ "{{"+ ss[s];
							}
						}else{
							if(s==0)
								value = value + ss[s];
							else
								value = value+ "{{"+ ss[s];
						}
					}
				}else
					return str;
				return value;
			},
			element_init_data:function (element,json){
				if(!element)
					return null;
				if(!element.localName && element.nodeValue){
					element.nodeValue = this.map_data(element.nodeValue,json);
					return element;
				}
				if(!element.localName)
					return element;

				var attributes = element.attributes;
				var dependency = false;
				for(var attr in attributes){
					if(attributes[attr] && attributes[attr].nodeName && attributes[attr].nodeValue){
						if(attributes[attr].nodeName=="dependency")
							dependency = true;
						attributes[attr].nodeValue = this.map_data(attributes[attr].nodeValue,json);
					}
				}

				if(dependency && !json)
					return null;

				if(element.childNodes){
					var nodes_clone = new Array();
					for(var i=0;i<element.childNodes.length;i++){
						var cl = element.childNodes[i].cloneNode(true);
						nodes_clone.push(cl);
					}
					element.innerHTML = "";
					for(var c=0; c<nodes_clone.length;c++){
						var cl = nodes_clone[c];

						var dep   = (!cl.attributes)?null:cl.attributes["dependency"];
						var group = (!cl.attributes)?null:cl.attributes["group"];
						if(dep){
							if(json[dep.nodeValue])
								var cl_ = this.element_init_data(cl,json[dep.nodeValue]);
						}
						else if(group){
							if(json[group.nodeValue])
							{
								for(var g=0;g<json[group.nodeValue].length;g++)
								{
									var cl_ = this.element_init_data(cl.cloneNode(true),json[group.nodeValue][g]);
									if(cl_){
										element.appendChild(cl_);
										element.appendChild(document.createTextNode('\n'));
										cl_ = null;
									}
								}
							}

						}
						else{
							var cl_ = this.element_init_data(cl.cloneNode(true),json);
						}
						if(cl_)
							element.appendChild(cl_.cloneNode(true));
						cl_ = null;
						dep = null;
						group = null;
					}
				}

			    return element;
			},
			add_child_with_json:function(parent,json,templet){
				if(!parent || !json || !templet)
					return;
				if(json instanceof Array){
					for(var i=0;i<json.length;i++){
						var temp = cc.tool.element_init_data(templet.cloneNode(true),json[i]);
						parent.appendChild(temp);
					}
				}else{
					var temp = cc.tool.element_init_data(templet.cloneNode(true),json);
					parent.appendChild(temp);
					return temp;
				}
			},
			get_parameter:function(name,url){
				url = typeof url != 'undefined'?url:location.search;
				var theRequest = new Object();
				if(url.indexOf("?") != -1)
				{
				  var str = url.substr(1);
				  strs = str.split("&");
				  for(var i = 0; i < strs.length; i ++)
				  {
				     theRequest[strs[i].split("=")[0]]=strs[i].split("=")[1];
				  }
				}
				if(name)return theRequest[name];
				return theRequest;
			},
			downloadURL : function(url) {
			    var hiddenIFrameID = 'hiddenDownloader',
			        iframe = document.getElementById(hiddenIFrameID);
			    if (iframe == null) {
			        iframe = document.createElement('iframe');
			        iframe.id = hiddenIFrameID;
			        iframe.style.display = 'none';
			        document.body.appendChild(iframe);
			    }
			    iframe.src = url;
			}
	}
	
	cc.cookie = {
			get_cookie:function(c_name){
			  if(!navigator.cookieEnabled)return "";
			  if (document.cookie.length>0)
			  {
			    c_start=document.cookie.indexOf(c_name + "=")
			    if (c_start!=-1)
			    { 
			      c_start=c_start + c_name.length+1 
			      c_end=document.cookie.indexOf(";",c_start)
			      if (c_end==-1) c_end=document.cookie.length
			      return unescape(document.cookie.substring(c_start,c_end))
			    } 
			  }
			  return ""
			},	
			del_cookie:function(name){ 
				   var date = new Date();
				   date.setTime(date.getTime() - 10000);
				   document.cookie = name + "=a; expires=" + date.toGMTString();
			},
			set_cookie:function(c_name,value,expiremins){
				if(!navigator.cookieEnabled)return;
			    var exdate=new Date();
				exdate.setTime(exdate.getTime()+(expiremins*60 * 1000));
				chat_cookie=c_name+ "=" +escape(value)+
				((expiremins==null) ? "" : ";expires="+exdate.toGMTString());
				document.cookie=chat_cookie;
				return chat_cookie;
			}
	}
})();

//string
String.prototype.startWith=function(str){
	var temp = this.toString();
	if(str==null||str==""||temp.length==0||str.length>temp.length)
	  return false;
	if(temp.substr(0,str.length)==str)
	  return true;
	else
	  return false;
};
String.prototype.lastWith=function(str){
	var temp = this.toString();
	if(str==null||str==""||temp.length==0||str.length>temp.length)
	  return false;
	if(temp.substr(temp.length - str.length)==str)
	  return true;
	else
	  return false;
};
String.prototype.replaceAll=function(str1,str2){
	 var temp = this;
	 while(temp.indexOf(str1)!=-1)
		  temp = temp.replace(str1,str2);
	 return temp;
};
String.prototype.trim = function(str){
	if(!str){
		var temp = this;
		while(temp.startWith(str))
			 temp = temp.substring(1);
		while(temp.lastWith(str))
			 temp = temp.substring(0,temp.length-1);
		return temp;
	}
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

//Array Remove
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};


//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt)
{
	var o = {
		"M+" : this.getMonth()+1,                 
		"d+" : this.getDate(),                     
		"h+" : this.getHours(),                   
		"m+" : this.getMinutes(),                  
		"s+" : this.getSeconds(),                  
		"q+" : Math.floor((this.getMonth()+3)/3),  
		"S"  : this.getMilliseconds()              
	};
    if(/(y+)/.test(fmt))
	   fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
       if(new RegExp("("+ k +")").test(fmt))
    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
};
