(function (win) {
    var hasOwnProperty = Object.prototype.hasOwnProperty;
    var JSBridge = win.JSBridge || (win.JSBridge = {});
    //JSBridge自定义协议的头部
    var JSBRIDGE_PROTOCOL = 'CGJSBridge';
    var Inner = {
        callbacks: {},
        NativeCall: function (obj, method, params, callback) {
            console.log("协议："+obj+"---- "+method+"---- "+params+"---- "+callback);
            if("CPNativeCall"==obj){
            	 var port = Util.getPort();
            	 this.callbacks[port] = callback;
            	 var uri=Util.getOtherActionUri(obj, method, params,port);
                 window.prompt(uri, "");
            }else {
            	 var port = Util.getPort();
                 console.log("port="+port);
                 this.callbacks[port] = callback;
                 var uri=Util.getUri(obj,method,params,port);
                 console.log("uri="+uri);
                 window.prompt(uri, "");
			}
        },
        onFinish: function (port, jsonObj){
            var callback = this.callbacks[port];
            callback && callback(jsonObj);
            delete this.callbacks[port];
        },
        post:function(url,params,callback){
            var port = Util.getPort();
            this.callbacks[port] = callback;
            var uri=Util.getPostUri(url,params,port);
            window.prompt(uri, "");
        },
        
        get:function(url,params,callback){
            var port = Util.getPort();
            this.callbacks[port] = callback;
            var uri=Util.getGetUri(url,params,port);
            window.prompt(uri, "");
        }
    };

    var Util = {
        getPort: function () {
            //创造一个非常大范围的随机数  1<<30  = 1 073 741 824   为保证port的唯一性，10亿的重复概率非常小
            return Math.floor(Math.random() * (1 << 30));
        },
        getUri:function(obj, method, params, port){
            params = this.getParam(params);
            var uri = JSBRIDGE_PROTOCOL + '://' + obj + ':' + port + '/' + method + '?' + params;
            return uri;
        },
        getPostUri:function(url,params,port){
           var params={
                'params':params,
                'url':url
           };
            params=this.getParam(params);
            var uri=JSBRIDGE_PROTOCOL+'://CPNativeHttp:'+port+'/post'+'?'+params;
            return uri;
        },
        getGetUri:function(url,params,port){
            var params={
                 'params':params,
                 'url':url
            };
             params=this.getParam(params);
             var uri=JSBRIDGE_PROTOCOL+'://CPNativeHttp:'+port+'/get'+'?'+params;
             return uri;
         },
        getParam:function(obj){
            if (obj && typeof obj === 'object') {
                return JSON.stringify(obj);
            } else {
                return obj || '';
            }
        },
        getOtherActionUri:function(obj, method, params,port){
        	var params={
        			'params':params,
        			'method':method
        	};
        	params=this.getParam(params);
        	var uri=JSBRIDGE_PROTOCOL+'://CPNativeCall:'+port+'/otherAction'+'?'+params;
        	return uri;
        }
    };


    //这一步实际上是将Inner对象的属性给到JSBridge对象，然后就能通过JSBridge对象调用Inner对象的相关属性
    //形如：JSBridge.call('bridge','testThread',{},function(res){alert(JSON.stringify(res))});
    for (var key in Inner) {
        //判断JSBridge对象是否有Inner对象的某个属性  Object.prototype.hasOwnProperty.call(对象名, 属性名);
        // 返回值：true，false
        if (!hasOwnProperty.call(JSBridge, key)) {
            //如果JSBridge对象不具备Inner对象的某个属性，则将该属性添加到JSBridge中
            JSBridge[key] = Inner[key];
        }
    }
})(window);