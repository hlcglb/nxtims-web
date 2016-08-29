/*
 * AngularJS Service Module 설정 js
 *
 * @author 정철균
 */

'use strict';

/**
 * auth Module
 */
angular.module('service.kendo.data',[])
.factory('KendoDataHelper',[function(){
    
    var transform = function(object, handler) {
        if(jQuery.isPlainObject(object) || angular.isArray(object)) {
            for( var key in object) {
                var value = object[key];
                if(angular.isFunction(handler)) {
                    handler(object, key, value);
                }
                transform(value, handler);
            }
        }
    };
    
    var transformHierarchical = function(arr) {
        var map = {}, node, roots = [];
        for(var i=0; i < arr.length; i++){
            node = arr[i];
            map[node.RESOURCE_LEVEL] = i; // use map to look-up the parents
            var nextkey = (i + 1) > (arr.length - 1) ? i : i + 1;
            var prevkey = (i - 1) < 0 ? i : i -1;
            node["text"] = node.RESOURCE_NM;
            if(node.RESOURCE_LEVEL == "1" || Number(node.RESOURCE_LEVEL) < Number(arr[nextkey].RESOURCE_LEVEL)){
                node["items"] = [];
            }
            node["PARENT_ID"] = angular.isUndefined(map[String(Number(node.RESOURCE_LEVEL)-1)]) ? 
                    "0" : arr[map[String(Number(node.RESOURCE_LEVEL)-1)]].RESOURCE_ID;

            if (node["PARENT_ID"] != "0" ) {
                arr[map[Number(node.RESOURCE_LEVEL-1)]].items.push(node);
            } else {
                roots.push(node);
            }
        }
        return roots;
    };
    
    var isEmpty = function(object) {
        if(object == null || object == "") {
            return true;
        }
        return false;
    }
    
    var toKendoData = function(data, type){
        
        var dataSource = null;
        switch(type) {
        case 'tree':
            var treelist = transformHierarchical(data);
            dataSource = new kendo.data.HierarchicalDataSource(
                    { data: treelist});
            if(dataSource.data().length == 0){
                dataSource.read();
            }
            
            break;
        case 'grid':
            break;
        default:
            break;
        }
        return dataSource;
    };
    
    return {
        toKendoData: toKendoData
    };

}]);


angular.module('service.constants',[])
.constant('constants', {
    baseUrl: '',
    main: '/main.html',
    findPassword: '/find'
})
.factory('LocaleService', ['$q','$http', '$log', 'constants', function($q, $http, $log, constants) {
    return {
        
    };
}])
.factory('MessageService', ['$q','$http', '$log', 'constants', function($q, $http, $log, constants) {
    return {
        
    };
}])
.factory('DaoService', ['$q','$http', '$log', 'constants', function($q, $http, $log, constants) {
    return function(){
        var serviceName = "";
        var deferred = $q.defer();
        
        this.setServiceName = function(name){
            if(name){
                this.serviceName = name;
            }
        };
        //promise setting
        this.resolve = function(resultData){
            deferred.resolve({
                result: resultData.data 
            });
        }
        this.reject = function(msg, code){
            deferred.reject(msg);
            $log.error(msg, code);
        }
        this.notify = function(value){
            this.deferred.notify(value);
        }
        //http method
        this.http = function(type, data){
            this.setServiceName(data.serviceName);
            var callback = "";
            if(type == "jsonp"){
                callback = "?callback=JSON_CALLBACK";
            }
            $http[type](constants.baseUrl + serviceName + callback , data)
            .then(this.resolve, this.reject, this.notify);
            return deferred.promise;
        };
        
        this.get = function(data){
            this.http("get", data);
        };
        
        this.post = function(data){
            this.http("post", data);
        };
        
        this.jsonp = function(data){
            this.http("jsonp", data);
        };
        
        //util
        this.setServiceName = function(data){
            if(data.serviceName){
                this.setServiceName(data.serviceName);
            }
        }
    };
}]);

angular.module('service.test',[]).service('LoginDao', ['CommonnDaoService', 'constants', function(CommonnDaoService, constants) {
    var daoService = new CommonnDaoService;
    daoService.login = function(data) {
        return this.post({
            serviceName: "loginService",
            data: data
        });
    };
    daoService.find = function(data) {
        return this.post({
            serviceName: constants.findPassword,
            data: data
        });
    };
    return daoService;
}]);