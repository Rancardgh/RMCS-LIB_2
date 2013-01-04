var _globalScope=this;function Jmaki(){var _1=this;this.version=".9.7";this.debugGlue=false;this.verboseDebug=false;this.debug=false;var _2=[];this.loaded=false;this.initialized=false;this.webRoot="";this.resourcesRoot="resources";this.extensions=[];this.inspectDepth=2;var _3=0;this.genId=function(){return "jmk_"+_3++;};this.Map=function(){var _4={};this.keys=function(){var o={};var _6=[];for(var _i in _4){if(typeof o[_i]=="undefined"){_6.push(_i);}}return _6;};this.put=function(_8,_9){_4[_8]=_9;};this.get=function(_a){return _4[_a];};this.remove=function(_b){delete _4[_b];};this.clear=function(){delete _4;_4={};};};this.attributes=new this.Map();var _c=new this.Map();this.unsubscribe=function(_d){for(var _l=0;_1.subs&&_l<_1.subs.length;_l++){if(_1.subs[_l].id==_d.id){_1.subs.splice(_l,1);break;}}};this.publish=function(_f,_10,_11,_12){if(typeof _f=="undefined"||typeof _10=="undefined"){return;}if(_1.debugGlue){_1.log("<span style='color:red'>Publish </span> : Topic:"+_f+" message: "+_1.inspect(_10));}if(_1.subs){for(var _l=0;_l<_1.subs.length;_l++){var _14=_1.subs[_l];if((_14.topic instanceof RegExp&&_14.topic.test(_f))||(_14.topic==_f)||(typeof _14.topic.charAt=="function"&&_14.topic.charAt(_14.topic.length-1)=="*"&&(_f.indexOf(_14.topic.substring(0,_14.topic.length-1))==0)&&_f.substring(0,_14.topic.length-1)==_14.topic.substring(0,_14.topic.length-1))||(typeof _14.topic.charAt=="function"&&_14.topic.charAt(0)=="*"&&(_f.indexOf(_14.topic.substring(1,_14.topic.length))!=-1)&&_f.substring(_f.indexOf(_14.topic.substring(1,_14.topic.length)),_f.length)==_14.topic.substring(1,_14.topic.length))||(typeof _14.topic.charAt=="function"&&_14.topic.charAt(0)=="*"&&_14.topic.charAt(_14.topic.length-1)=="*"&&_f.indexOf(_14.topic.substring(1,_14.topic.length-1))!=-1)){if(_1.debugGlue){_1.log("<span style='color:green'>Publish Match</span> : Topic:"+_f+" listener: "+_14);}if(_14.action=="call"&&_14.target){var _15;if(_14.target.functionName){_15=_1.findObject(_14.target.object);if(typeof _15=="function"){myo=new _15;}else{if(_15){myo=_15;}else{if(_1.debugGlue){_1.log("Publish Error :  Object found: "+_14.target.object);}}}if(typeof myo!="undefined"&&typeof myo[_14.target.functionName]=="function"){myo[_14.target.functionName].call(_globalScope,_10);}else{if(_1.debugGlue){_1.log("Publish Error : function "+_14.target.functionName+" not found on object "+_14.target.object);}}}else{if(_14.target.functionHandler){_14.target.functionHandler.call(_globalScope,_10);}else{if(_1.debugGlue){_1.log("Publish Error : no fuctionName or functionHandler specified on listener mapped to "+_f);}}}}}else{if(_1.subs[_l].action=="forward"){var _16=_1.subs[_l].topics;for(var ti=0;ti<_16.length;ti++){if(_16[ti]!=_f){_1.publish(_16[ti],_10);}}}}}}var bd=true;if(typeof _11!="undefined"){bd=_11;}if(bd&&window.frames.length>0){for(var i=0;i<window.frames.length;i++){if(window.frames[i].jmaki){window.frames[i].jmaki.publish("/global"+_f,_10,true,false);}}}if(window!=window.top){var bu=true;if(typeof _12!="undefined"){bu=_12;}if(bu&&window.parent.jmaki){window.parent.jmaki.publish("/global"+_f,_10,false,true);}}};this.addLibraries=function(_1b,_cb,_1d){if(_1b.length<=0){if(typeof _cb=="function"){_cb();return;}}if(typeof _1d=="undefined"){_1d=new _1.Map();}var _1e=new Date().getMilliseconds();var _1f=_1b[_1b.length-1];var _20="c_script_"+_1b.length+"_"+_1e;var _21=document.getElementsByTagName("head")[0];var e=document.createElement("script");e.start=_1e;e.id=_20;e.type="text/javascript";_21.appendChild(e);var se=document.getElementById(_20);_1d.put(_20,_1f);var _24=function(_id){var t=document.getElementById(_id);if(t&&t.timeoutHandler){clearInterval(t.timeoutHandler.interval);delete t.timeoutHandler;}var _s=document.getElementById(_id);if(_s){_s.parentNode.removeChild(_s);}_1d.remove(_id);var _28=_cb;if(_1b.length-1>0){_1b.pop();_1.addLibraries(_1b,_cb,_1d);}if(_1d.keys().length==0){if(typeof _cb!="undefined"){var _29=0;delete _1d;setTimeout(function(){_28();},0);}}};if(/MSIE/i.test(navigator.userAgent)){se.onreadystatechange=function(){if(this.readyState=="loaded"){var _id=_20;_24(_id);}};document.getElementById(_20).src=_1f;}else{if(se.addEventListener){var _2b=function(_id){var _c=0;var _2e=this;this.interval=setInterval(function(){if(_c>2){clearInterval(_2e.interval);_24(_id);}else{_c++;}},250);};se.timeoutHandler=new _2b(_20);se.addEventListener("load",function(){var _id=_20;_24(_id);},true);}setTimeout(function(){document.getElementById(_20).src=_1f;},0);}se=null;_21=null;};this.getXHR=function(){if(window.XMLHttpRequest){return new XMLHttpRequest();}else{if(window.ActiveXObject){return new ActiveXObject("Microsoft.XMLHTTP");}}};function handleAjaxError(_m,_r,_32){if(_32.onerror){_32.onerror(_m,_r);}else{_1.log("jMaki.doAjax Error: "+_m);}}this.doAjax=function(_33){if(typeof _33=="undefined"||!_33.url){_1.log("jmaki.doAjax: url required");return;}var _34=this.getXHR();var _35="GET";var _36=true;var _37;var _c=false;if(_33.timeout){setTimeout(function(){if(_c==false){_c=true;if(_34.abort){_34.abort();}handleAjaxError("Request timed out",_34,_33);return;}},_33.timeout);}if(typeof _33.asynchronous!="undefined"){_36=_33.asynchronous;}if(_33.method){_35=_33.method;}if(typeof _33.callback=="function"){_37=_33.callback;}var _39=null;if(_33.body){_39=_33.body;}else{if(_33.content){_39="";for(var l in _33.content){_39=_39+l+"="+encodeURIComponent(_33.content[l])+"&";}}}if(_36==true&&_c==false){_34.onreadystatechange=function(){if(_34.readyState==4&&_c==false){_c=true;if((_34.status==200||_34.status==0)&&_37){_37(_34);}else{if(_34.status!=200){handleAjaxError("jmaki.doAjax error communicating with "+_33.url,_34,_33);}}return;}};}try{if(!_c){_34.open(_35,_33.url,_36);}}catch(e){handleAjaxError("jmaki.doAjax error creating xmlhttprequest",_34,_33);return;}if(_33.headers&&_33.headers.length>0){for(var _h=0;_h<_33.headers.length;_h++){_34.setRequestHeader(_33.headers[_h].name,_33.headers[_h].value);}}if(_33.method){_35=_33.method;if(_35.toLowerCase()=="post"){if(!_33.contentType){_34.setRequestHeader("Content-Type","application/x-www-form-urlencoded");}}}if(_33.contentType){_34.setRequestHeader("Content-Type",_33.contentType);}try{if(_c==false){_34.send(_39);}}catch(e){handleAjaxError("Error sendong body of xmlhttprequest",_34,_33);return;}if(_c==false&&_36==false){_c=true;if(_34.status==200){if(_37){_37(_34);}}else{handleAjaxError("Error communicating with external service",_34,_33);}return;}};this.addLibrary=function(lib,cb){var _3e=[];_3e.push(lib);return _1.addLibraries(_3e,cb);};this.addWidget=function(_3f){_2.push(_3f);if(this.loaded){this.loadWidget(_3f);}};this.addExtension=function(ext){_1.extensions.push(ext);};this.bootstrapWidgets=function(){_1.loaded=true;for(var l=0;l<_2.length;l++){this.loadWidget(_2[l]);}};this.loadExtensions=function(){for(var l=0;l<_1.extensions.length;l++){this.loadExtension(_1.extensions[l]);}};this.writeScript=function(_s,_id){if(_1.loaded==true){if(document.getElementById(_id)){document.getElementById(_id).innerHTML="Attempt to write a script that can not be dynamically load widget with  id "+_id+". Consider using the widget in an iframe.";}}else{document.write("<script src='"+_s+"'></script>");}};this.loadStyle=function(_45){var _46=document.createElement("link");_46.type="text/css";_46.rel="stylesheet";if(_45[0]=="/"){_45=_1.webRoot+_45;}_46.href=_45;if(document.getElementsByTagName("head").length==0){var _47=document.createElement("head");document.documentElement.insertBefore(_47,document.documentElement.firstChild);}document.getElementsByTagName("head")[0].appendChild(_46);};this.replaceStyleClass=function(_48,_49,_4a){var _4b=this.getElementsByStyle(_49,_48);for(var i=0;i<_4b.length;i++){if(_4b[i].className.indexOf(" ")!=-1){var _4d=_4b[i].className.split(" ");for(var ci in _4d){if(_4d[ci]==_49){_4d[ci]=_4a;}}_4b[i].className=_4d.join(" ");}else{if(_4b[i].className==_49){_4b[i].className=_4a;}}}};this.getElementsByStyle=function(_4f,_50){var _51=[];if(typeof _50!="undefined"){var _52=_50;if(typeof _50=="string"){_52=document.getElementById(_50);}_51=this.getAllChildren(_52,[]);}else{_51=(document.all)?document.all:document.getElementsByTagName("*");}var _53=[];for(var i=0;i<_51.length;i++){if(_51[i].className.indexOf(" ")!=-1){var cn=_51[i].className.split(" ");for(var ci=0;ci<cn.length;ci++){if(cn[ci]==_4f){_53.push(_51[i]);}}}else{if(_51[i].className==_4f){_53.push(_51[i]);}}}return _53;};this.getAllChildren=function(_57,_58){var _nc=_57.childNodes;for(var l=0;_nc&&l<_nc.length;l++){if(_nc[l].nodeType==1){_58.push(_nc[l]);if(_nc[l].childNodes.length>0){this.getAllChildren(_nc[l],_58);}}}return _58;};this.loadExtension=function(_5b){var _5c="jmaki.extensions."+_5b.name+".Extension";var con=this.findObject(_5c);if(typeof con!="function"){jmaki.log("Could not find widget constructor for: "+_5c+". Please make sure the extension constructor is properly defined.");}else{var ex=new con(_5b);if(ex.postLoad){ex.postLoad.call(_globalScope);}}};this.loadWidget=function(_5f){if(_1.attributes.get(_5f.uuid)!=null){return;}var _60="jmaki.widgets."+_5f.name+".Widget";var con=this.findObject(_60);if(typeof con!="function"){logError("Could not find widget constructor for: "+_60+". Please make sure the widget constructor is properly defined.",document.getElementById(_5f.uuid));}var _62;if((typeof _5f.value=="string")&&_5f.value.indexOf("@{")==0){var _vw=/[^@{].*[^}]/.exec(_5f.value);_5f.value=_1.findObject(new String(_vw));}var _64=_5f.uuid;if(/MSIE/i.test(navigator.userAgent)){var _65=_5f.uuid;var _66=null;if(window.onerror){_66=window.onerror;}var eh=function(_68,url,_6a){var _6b=_65;logWidgetError(_60,_6b,url,_6a,_68,document.getElementById(_6b));};window.onerror=eh;_62=new con(_5f);window.onerror=null;if(_66){window.onerror=_66;}}else{if(typeof con=="function"){try{_62=new con(_5f);}catch(e){var _6c="unknown";var _6d=null;if(e.lineNumber){_6c=e.lineNumber;}if(e.message){_6d=e.message;}if(_1.debug){logWidgetError(_60,_5f.uuid,_5f.script,_6c,_6d,document.getElementById(_5f.uuid));return;}}}else{if(typeof con=="undefined"){logError("Unable to find widget constructor "+_60+" check log and make sure constructor is defined.",document.getElementById(_5f.uuid));return;}}}if(typeof _62=="object"){_1.attributes.put(_5f.uuid,_62);if(_62.postLoad){_62.postLoad.call(_globalScope);}if(_5f.subscribe&&_5f.subscribe.push){for(var _wi=0;_wi<_5f.subscribe.length;_wi++){var _t=_5f.subscribe[_wi].topic;var _m=_5f.subscribe[_wi].handler;var _h=null;if(typeof _m=="string"&&_m.indexOf("@{")==0){var _hw=/[^@{].*[^}]/.exec(_m);_h=_1.findObject(new String(_hw));}else{if(_62[_m]){_h=_62[_m];}}if(_h!=null){_1.subscribe(_5f.subscribe[_wi].topic,_h);}}}_1.publish("/jmaki/runtime/widget/loaded",{id:_5f.uuid});}else{logError("Unable to create an instance of "+_60+". See the error log for more details.",document.getElementById(_5f.uuid));}};function logWidgetError(_73,_74,url,_76,_m,div){var _79="<span>Error loading "+_73+" : id="+_74+"<br>"+"Script: "+url+" (line:"+_76+")."+"<br>Message: "+_m+"</span>";logError(_79,div);}function logError(_7a,div){if(div==null||typeof div.className=="undefined"){div=document.createElement("div");document.body.appendChild(div);}div.className="";div.style.color="red";div.innerHTML=_7a;}this.getWidget=function(id){return _1.attributes.get(id);};this.clearWidgets=function(_7d){if(typeof _7d=="undefined"){var _k=_1.attributes.keys();for(var l=0;l<_k.length;l++){_1.removeWidget(_k[l]);}_1.loaded=false;_2=[];}else{var _ws=_1.getAllChildren(_7d,[]);for(var l=0;l<_ws.length;l++){var _w;if(_ws[l].id){_1.removeWidget(_ws[l].id);}}}};this.removeWidget=function(_83){var _w=_1.getWidget(_83);if(_w&&typeof _w.destroy=="function"){_w.destroy();var _p=document.getElementById(_83);_p.parentNode.removeChild(_p);}_1.attributes.remove(_83);};this.inspect=function(_o,_87){if(typeof _87=="undefined"){_87=0;}else{if(_87>=_1.inspectDepth){return _o;}else{_87++;}}var _rs=[];if(typeof _o=="undefined"){_o=this;}if(_o instanceof Array){for(var i=0;i<_o.length;i++){_rs.push(i+" : "+_1.inspect(_o[i],_87));}return "["+_rs.join(" , ")+"]";}else{if(typeof _o=="string"){return "'"+_o+"'";}else{if(typeof _o=="number"||typeof _o=="boolean"){return _o;}else{if(typeof _o=="object"){for(var _oi in _o){try{if(_oi!="toString"){_rs.push(_oi+" : "+_1.inspect(_o[_oi],_87));}}catch(e){}}if(_rs.length>0){return "{"+_rs.join(" , ")+"}";}else{return "{}";}}else{return _o;}}}}};this.subscribe=function(l,t){if(typeof l=="undefined"){return;}if(typeof l=="object"&&!(l instanceof RegExp)){if(l.topic){l.topic=_1.trim(l.topic);}if(l.topicRegExp){l.topic=new RegExp(l.topicRegExp);}lis=l;}else{if(typeof t=="string"){lis={};if(l.topicRegExp){lis.topic=new RegExp(l.topicRegExp);}else{lis.topic=l;}lis.target={};var _is=t.split(".");lis.action="call";lis.target.functionName=_is.pop();lis.target.object=_is.join(".");}else{if(typeof t=="function"){lis={};if(l.topicRegExp){lis.topic=new RegExp(l.topicRegExp);}else{lis.topic=l;}lis.target={};lis.action="call";lis.target.functionHandler=t;}else{_1.log("jmaki:subscribe error:  Handler undefined for "+l);}}}if(typeof lis!="undefined"){if(typeof _1.subs=="undefined"){_1.subs=[];}if(!lis.id){lis.id=_1.genId();}if(typeof lis.topic=="string"){lis.topic=_1.trim(lis.topic);var _wc=lis.topic.indexOf("*");if(_wc!=-1&&_wc!=0&&_wc!=lis.topic.length-1){lis.topic=new RegExp(lis.topic.substring(0,_wc)+"(.*)"+lis.topic.substring(_wc+1,lis.topic.length));}}if(lis.topic){lis.toString=_1.inspect;_1.subs.push(lis);}else{_1.log("jmaki:subscribe error:  topic or topicRegExp required for "+l);return null;}return lis;}return null;};this.trim=function(t){return t.replace(/^\s+|\s+$/g,"");};this.addGlueListener=this.subscribe;this.extend=function(_90,_91){_90.prototype=new _91();_90.prototype.constructor=_90;_90.superclass=_91.prototype;for(i in _91){_90.prototype[i]=_91[i];}};this.hideLogger=function(){if(jmaki.ld){_1.ld.style.visibility="hidden";}};this.clearLogger=function(){var b=document.getElementById("jmakiLoggerContent");if(b){b.innerHTML="";}};this.log=function(_93,_94){if(!_1.debug){return;}if(!_1.ld){ld=document.createElement("div");ld.id="jmakiLogger";ld.style.border="1px solid #000000";ld.style.fontSize="12px";ld.style.position="absolute";ld.style.zIndex="999";ld.style.bottom="0px";ld.style.background="#FFFF00";ld.style.right="0px";ld.style.width="500px";ld.style.height="200px";var tb="<div  style='height: 14px; background : black; color : white; font-size : 10px'><div style='float:left;width:450px;text-align : center; '>jMaki Logger</div><div style='right: 0px,text-align:left'><a href='javascript:jmaki.clearLogger()' title='Hide' style='color:white; text-decoration: none'>[Clear]</a> <a href='javascript:jmaki.hideLogger()' title='Hide' style='color:white; text-decoration: none'>[X]</a></div></div>";var tbE=document.createElement("div");tbE.innerHTML=tb;ld.appendChild(tbE);var tbC=document.createElement("div");tbC.id="jmakiLoggerContent";tbC.style.height="186px";tbC.style.overflowY="auto";ld.appendChild(tbC);if(document.body){document.body.appendChild(ld);_1.ld=ld;}}if(_1.ld){_1.ld.style.visibility="visible";}var b=document.getElementById("jmakiLoggerContent");var lm=document.createElement("div");lm.style.clear="both";if(_93.length>125&&_1.verboseDebug==false){var lid=_1.genId();var tn=document.createElement("div");tn.innerHTML="<div style='float:left;width:435px;height:12px;overflow:hidden'>"+_93.substring(0,135)+"</div><div style='float:left'>...&nbsp;</div><a id='"+lid+"_href' href=\"javascript:jmaki.showLogMessage('"+lid+"')\" style='text-decoration: none'><span id='"+lid+"_link'>[more]</span></a>";var mn=document.createElement("div");mn.id=lid;mn.innerHTML=_93;mn.style.margin="5px";mn.style.background="#FF9900";mn.style.display="none";lm.appendChild(tn);lm.appendChild(mn);}else{lm.innerHTML=_93;}if(b){b.appendChild(lm);}};this.showLogMessage=function(id){var n=document.getElementById(id);if(n&&n.style){n.style.display="block";var h=document.getElementById(id+"_href");h.href="javascript:jmaki.hideLogMessage('"+id+"')";var l=document.getElementById(id+"_link");l.innerHTML="&nbsp;[X]";}};this.hideLogMessage=function(id){var n=document.getElementById(id);if(n&&n.style){n.style.display="none";var h=document.getElementById(id+"_href");h.href="javascript:jmaki.showLogMessage('"+id+"')";var l=document.getElementById(id+"_link");l.innerHTML="[more]";}};this.initialize=function(){if(!_1.config){_1.config={};}if(!_1.config.glue){_1.config.glue={};}jmaki.doAjax({url:this.webRoot+this.resourcesRoot+"/config.json",asynchronous:false,timeout:3000,callback:function(req){if(req.responseText!=""){var obj=eval("("+req.responseText+")");if(obj.config){_1.config.theme=obj.config.theme;if(obj.config.glue.timers){_1.addTimers(obj.config.glue.timers);}if(obj.config.gluelisteners){for(var gl=0;gl<obj.config.glue.listeners.length;gl++){_1.subscribe(obj.config.glue.listeners[gl]);}}}}}});postInitialize();};var _a8=[];this.namespace=function(_a9,_aa){var _ab=_a9.split(".");var _ac=_globalScope[_ab[0]];if(typeof _ac=="undefined"){_globalScope[_ab[0]]=_ac={};}for(var ii=1;ii<_ab.length;ii++){if(typeof _ac[_ab[ii]]!="undefined"){_ac=_ac[_ab[ii]];}else{_ac[_ab[ii]]={};_ac=_ac[_ab[ii]];}}if(typeof _aa=="object"){_ac=_aa;}return _ac;};this.findObject=function(_ae){var _af=_ae.split(".");var _b0=_globalScope[_af[0]];var _b1=true;if(typeof _b0!="undefined"){for(var ii=1;ii<_af.length;ii++){var _lp=_af[ii];if(_lp.indexOf("()")!=-1){var _ns=_lp.split("()");if(typeof _b0[_ns[0]]=="function"){var _fn=_b0[_ns[0]];return _fn.call(_globalScope);}}if(typeof _b0[_lp]!="undefined"){_b0=_b0[_lp];_b1=true;}else{_b1=false;break;}}if(_b1){return _b0;}}return null;};this.Timer=function(_b6,_b7){var _b8=this;this.args=_b6;var _b9;this.processTopic=function(){for(var ti=0;ti<_b6.topics.length;ti++){_1.publish(_b6.topics[ti],{topic:_b6.topics[ti],type:"timer",src:_b8,timeout:_b6.to});}};this.processCall=function(){if(!_b9){var _bb=_1.findObject(_b6.on);if(typeof _bb=="function"){_b9=new _bb();}else{if(typeof _bb=="object"){_b9=_bb;}}}if((_b9&&typeof _b9=="object")){if(typeof _b9[_b6.fn]=="function"){_b9[_b6.fn]({type:"timer",src:_b8,timeout:_b6.to});}}};this.run=function(){if(_b7){_b8.processCall();}else{_b8.processTopic();}_globalScope.setTimeout(_b8.run,_b6.to);};};this.addTimer=function(_bc){var _bd=[];_bd.push(_bc);this.addTimers(_bd);};this.addTimers=function(_be){if(typeof _be!="undefined"){for(var _l=0;_l<_be.length;_l++){var _c0=_be[_l];if(_c0.action=="call"&&_c0.target!="undefined"&&_c0.target.object!="undefined"&&_c0.target.functionName!="undefined"&&typeof _c0.timeout!="undefined"){var _c1={on:_c0.target.object,fn:_c0.target.functionName,to:_c0.timeout};var _c2=new _1.Timer(_c1,true);_a8.push(_c2);_c2.run();}else{if(_be[_l].action=="publish"){var _c3={topics:_be[_l].topics,to:_c0.timeout};var _c4=new _1.Timer(_c3,false);_a8.push(_c4);_c4.run();}}}}};function postInitialize(){if(_1.initialized){return;}else{_1.initialized=true;}_1.publish("/jmaki/runtime/intialized",{});_1.loadExtensions();_1.publish("/jmaki/runtime/extensionsLoaded",{});_1.bootstrapWidgets();_1.publish("/jmaki/runtime/widgetsLoaded",{});if(_1.config&&_1.config.theme){var _c5=_1.config.theme;if(!/(^http)/i.test(_c5)){_c5=_1.webRoot+_c5;}_1.loadStyle(_c5);}_1.publish("/jmaki/runtime/loadComplete",{});}this.filter=function(_c6,_c7){if(typeof _c7=="string"){var h=_1.findObject(_c7);return h.call(_globalScope,_c6);}else{if(typeof _c7=="function"){return _c7.call(_globalScope,_c6);}}};this.getPosition=function(_e){var pX=0;var pY=0;if(typeof _e.offsetParent=="number"){pX=_e.offsetLeft;pY=_e.offsetTop;while(_e.offsetParent){_e=_e.offsetParent;pX+=_e.offsetLeft;pY+=_e.offsetTop;}}return {x:pX,y:pY};};this.DContainer=function(_cc){var _cd=this;var _ce;var _cf;var ie=/MSIE/i.test(navigator.userAgent);if(typeof _cc.target=="string"){_ce=_cc.target;_cf=document.getElementById(target);}else{_ce=_cc.target.id;_cf=_cc.target;}if(typeof overflow!="undefined"&&_cf.style.overflow){_cf.style.overflow="true";}var _d1;this.url=null;var _d2=false;var _d3=false;if(_cc.autosize){_d2=true;_d3=true;}var _d4;var _d5;this.loadURL=function(_d6){if(_d6.message){_d6=_d6.message;}if(typeof _d6=="string"){_cd.url=_d6;}else{if(_d6.url){_cd.url=_d6.url;}else{if(_d6.value){_cd.url=_d6.value;}}}if(_cc.useIframe){if(!_cd.iframe){var _t=setInterval(function(){if(document.getElementById(_ce+"_iframe")){clearInterval(_t);_cd.iframe=document.getElementById(_ce+"_iframe");_cd.iframe.src=_cd.url;init();}},5);}else{_cd.iframe.src=_cd.url;}}else{_1.injector.inject({url:_cd.url,injectionPoint:_cf});}};this.resize=function(){var pos=_1.getPosition(_cf);if(_d2||_d3){if(!_cf.parentNode){return;}var pos=_1.getPosition(_cf);if(_cf.parentNode.nodeName=="BODY"){if(window.innerHeight){_d5=window.innerHeight-pos.y-40;_d4=window.innerWidth-20;}else{var _da=_cf.parentNode;while(_da!=null&&(_da.clientHeight==0||typeof _da.clientWidth=="undefined")){_da=_da.parentNode;}if(_da==null){_d4=400;}else{_d4=_da.clientWidth-20;_d5=_da.clientHeight-pos.y-15;}}}else{var _db=_cf.parentNode;while(_db!=null&&(_db.clientHeight==0||typeof _db.clientWidth=="undefined")){_db=_db.parentNode;}if(_db==null){_d4=400;}else{_d4=_db.clientWidth;_d5=_db.clientHeight;}}if(_d2){if(_d5<0){_d5=320;}_cf.style.height=_d5+"px";}if(_d3){_cf.style.width=_d4+"px";}}else{_cf.style.width=_d4+"px";_cf.style.height=_d5+"px";}if(_d5<0){_d5=320;}if(_d4<0){_d4=500;}if(_cc.useIframe){if(_cd.iframe){_cd.iframe.style.height=_d5+"px";_cd.iframe.style.width=_d4+"px";}}_d1=document.body.clientWidth;};this.setContent=function(_c){var _dd;if(_c.message){_c=_c.message;}if(_c.value){_dd=_c.value;}else{_dd=_c;}if(!_cd.iframe){_cf.innerHTML=_dd;}};function init(){if(window.attachEvent){window.attachEvent("onresize",layout);}else{if(window.addEventListener){window.addEventListener("resize",layout,true);}}if(_cc.startWidth){_d4=Number(_cc.startWidth);_cf.style.width=_d4+"px";}else{_d4=_cf.clientWidth;_d3=true;}if(_cc.startHeight){_d5=Number(_cc.startHeight);}else{_d5=_cf.clientHeight;_d2=true;}if(_d5<=0){_d5=320;}_cf.style.height=_d5+"px";if(_cc.useIFrame&&_cd.iframe){_cd.iframe.style.height=_d5+"px";}if(_cc.topic){_1.subscribe(_cc.topic,_cd.loadURL);_1.subscribe(_cc.topic+"/setInclude",_cd.loadURL);_1.subscribe(_cc.topic+"/setContent",_cd.setContent);}_cd.resize();if(_cc.url&&!_cc.useIframe){_cd.loadURL(_cc.url);}else{if(_cc.content&&!_cd.iframe){_cf.innerHTML=_cc.content;}}}var _de=false;var _df=0;function layout(){if(!ie){_cd.resize();return;}if(_d1!=document.body.clientWidth&&!_de){if(!_de){_de=true;setTimeout(layout,500);}}else{if(_de&&document.body.clientWidth==_df){_de=false;_cd.resize();}else{if(_de){_df=document.body.clientWidth;setTimeout(layout,500);}}}}if(_cc.useIframe&&_cc.useIframe==true){var _e0="";if(_cc.url){_e0="src='"+_cc.url+"'";}var _e1="Loading...";if(_cc.content){_e1=_cc.content;}var _e2="<IFRAME ID='"+_ce+"_iframe' "+_e0+" FRAMEBORDER=0 SCROLLING="+((!_cc.overflow)?"NO":"YES")+"></IFRAME>";_cf.innerHTML=_e2;var _t=setInterval(function(){if(document.getElementById(_ce+"_iframe")){clearInterval(_t);_cd.iframe=document.getElementById(_ce+"_iframe");setTimeout(function(){init();},0);}},5);}else{init();}};this.destroy=function(){if(window.attachEvent){window.dettachEvent("onresize",layout);}else{if(window.addEventListener){window.removeEventListener("resize",layout,true);}}};this.Injector=function(){var _e4=new Date().getMilliseconds();var _e5=this;var _e6=false;var _e7=[];var _e8=[];this.inject=function(_e9){if(_e8.length==0&&!_e6){inject(_e9);}else{_e8.push(_e9);}};function inject(_ea){_e6=true;_1.doAjax({method:"GET",url:_ea.url,asynchronous:false,callback:function(req){getContent(req.responseText,_ea);var _ec;if(typeof _ea.injectionPoint=="string"){_ec=document.getElementById(_ea.injectionPoint);if(!document.getElementById(_ea.injectionPoint)){var _t=setInterval(function(){if(document.getElementById(_ea.injectionPoint)){clearInterval(_t);_ec=document.getElementById(_ea.injectionPoint);setTimeout(function(){processTask(_ec,_ea);},0);}},25);}else{processTask(_ec,_ea);}}else{processTask(_ea.injectionPoint,_ea);}},onerror:function(){var ip=_ea.injectionPoint;if(typeof _ea.injectionPoint=="string"){ip=document.getElementById(_ea.injectionPoint);}ip.innerHTML="Unable to load URL "+_ea.url+".";processNextTask();}});}function processTask(_ef,_f0){_1.clearWidgets(_ef);var _id="injector_"+_e4;var _f2=_f0.content+"<div id='"+_id+"'></div>";_ef.innerHTML=_f2;var _t=setInterval(function(){if(document.getElementById(_id)){clearInterval(_t);try{_e5.loadScripts(_f0,processNextTask);}catch(e){_ef.innerHTML="<span style='color:red'>"+e.message+"</span>";}}},25);}function processNextTask(){if(_e8.length>0){var _t=_e8.shift();inject(_t);}_e6=false;}this.get=function(p){var _f6;_1.doAjax({method:"GET",url:p.url,asynchronous:false,callback:function(req){_f6=getContent(req.responseText);}});return _f6;};function getContent(_f8,_f9){_f9.embeddedScripts=[];_f9.embeddedStyles=[];_f9.scriptReferences=[];_f9.styleReferences=[];var _t=_f8;var _fb="";var _fc=document.getElementsByTagName("script");var _fd=document.getElementsByTagName("link");while(_t.indexOf("<script")!=-1){var _fe=_t.indexOf("<script");var _ff=_t.indexOf("src=",(_fe));var _100=_t.indexOf(">",_fe);var end=_t.indexOf("</script>",(_fe))+"</script>".length;if(_fe!=-1&&_ff!=-1){var _102;var _103=_ff+5;var _104=_t.substring(_ff+4,(_ff+5));var _105=_t.indexOf("\"",(_103+1));_105=_t.indexOf(_104,(_103+1));if(_ff<_100){_102=_t.substring(_103,_105);var _106=false;for(var i=0;i<_fc.length;i++){if(typeof _fc[i].src){if(_fc[i].src==_102){_106=true;break;}}}if(!_106){_f9.scriptReferences.push(_102);}}}var _108=_100+1;var _109=_t.substring(_108,end-"</script>".length);if(_109.length>0){_f9.embeddedScripts.push(_109);}_t=_t.substring(0,_fe)+_t.substring(end,_t.length);_105=-1;}while(_t.indexOf("<style")!=-1){var _10a=_t.indexOf("<style");var _10b=_t.indexOf(">",_10a);var end=_t.indexOf("</style>",(_10a));var _10d=_10b+1;var _10e=_t.substring(_10d,end);if(_10e.length>0){_f9.embeddedStyles.push(_10e);}_t=_t.substring(0,_10a)+_t.substring(end+"</style>".length,_t.length);}while(_t.indexOf("<link")!=-1){var _10f=_t.indexOf("<link");var _110=_t.indexOf("href=",(_10f));var _111=_t.indexOf(">",_10f)+1;if(_10f!=-1&&_110!=-1){var _112;var _113=_110+6;var _114=_t.substring(_110+5,(_110+6));var _115=_t.indexOf(_114,(_113+1));if(_110<_111){styleSourceName=_t.substring(_113,_115);var _116=false;for(var i=0;i<_fd.length;i++){if(typeof _fd[i].src!="undefined"){if(_fd[i].src==styleSourceName){_116=true;}}}if(!_116){_f9.styleReferences.push(styleSourceName);}}_t=_t.substring(0,_10f)+_t.substring(_111,_t.length);}}var head=document.getElementsByTagName("head")[0];for(var loop=0;_f9.styleReferences&&loop<_f9.styleReferences.length;loop++){var link=document.createElement("link");link.href=_f9.styleReferences[loop];link.type="text/css";link.rel="stylesheet";head.appendChild(link);}var _11b;if(_f9.embeddedStyles.length>0){_11b=document.createElement("style");_11b.type="text/css";var _11c;for(var loop=0;loop<_f9.embeddedStyles.length;loop++){_11c=_11c+_f9.embeddedStyles[loop];}if(document.styleSheets[0].cssText){document.styleSheets[0].cssText=document.styleSheets[0].cssText+_11c;}else{_11b.appendChild(document.createTextNode(_11c));head.appendChild(_11b);}}_f9.content=_t;}this.loadScripts=function(task,_11f){var _120=function(){for(var loop=0;task.embeddedScripts&&loop<task.embeddedScripts.length;loop++){var _122=task.embeddedScripts[loop];eval(_122);if(loop==(task.embeddedScripts.length-1)){if(typeof _11f!="undefined"){_11f();}return;}}if(task.embeddedScripts&&task.embeddedScripts.length==0&&typeof _11f!="undefined"){_11f();}};if(task.scriptReferences&&task.scriptReferences.length>0){return _1.addLibraries(task.scriptReferences.reverse(),_120);}else{_120();}return true;};};this.injector=new this.Injector();}if(typeof jmaki=="undefined"){var jmaki=new Jmaki();jmaki.widgets={};var oldLoad=window.onload;window.onload=function(){if(!jmaki.initialized){jmaki.initialize();}else{jmaki.bootstrapWidgets();return;}if(typeof oldLoad=="function"){oldLoad();}};}