/*
 * Authenticate Module define js
 *
 * @author 정철균
 */

'use strict';

/*
 * 인증 및 로그인, 로그아웃
 * 
 */
angular.module('comn.service.auth',['ngCookies'])
.config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push("AuthenticationInterceptor");
}])
.constant('Auth.CookieKey', 'nxtImsAuthorization')
.provider('Authentication',['Auth.CookieKey', function(CookieKey){
    this.authenticated = false;
    this.loginServiceName = "authentication";
    this.logoutServiceName = "logout";
    
    
    this.setAuthenticate = function(bool){
        this.authenticated = bool;
    }
    this.init= function(loginServiceName, logoutServiceName){
        this.loginServiceName = loginServiceName;
        this.logoutServiceName = logoutServiceName;
    }
    this.$get = ['$q', '$rootScope', '$http', '$cookies',
                 function($q, $rootScope, $http, $cookies){
        
        var that = this;
        
        //login
        var authenticate = function(credentials){
            // set header
            var headers = credentials ? {
                authorization : "Basic "
                        + btoa(credentials.username + ":"
                                + credentials.password)
            } : {};
            
            var deferred = $q.defer();
            $http.get(that.loginServiceName, {headers: headers})
            .then(
                    function(response) {
                        if (response.data.name) {
                            deferred.resolve("success");
                            $cookies.put(CookieKey, response.config.headers.authorization);
                            that.setAuthenticate(true);
                        } else {
                            deferred.reject("error");
                            that.setAuthenticate(false);
                        }
                    }, function(data) {
                        deferred.reject("error : " + data.status);
                        that.setAuthenticate(false);
                    }
            ).catch(function(error){console.log("[#catch] login catch : " + error); throw error;});
            return deferred.promise;  
        };
        
        //logout
        var unAuthenticate = function(data){
            //clear auth info
            that.setAuthenticate(false);
            $cookies.remove(CookieKey);
            
            //start logout
            var deferred = $q.defer();
            $http.post(that.logoutServiceName)
            .then(
                    function(response) {
                        console.log("success! cookie delete");
                        deferred.resolve("success: " + response.status);
        
                    }, function(data) {
                        console.log("falie! cookie delete");
                        deferred.reject("error: " + data.status);
                    }
            ).catch(function(error){console.log("[#catch] logout catch : " + error); throw error;});
            return deferred.promise;
        };
        
        //인증 확인 promise
        function isAuthPromise(option){
            var deferred = $q.defer();
            if(that.authenticated){
                deferred.resolve({
                    isAuth: that.authenticated
                });
            }
            else{
                if($cookies.get(CookieKey)){
                    that.setAuthenticate(true);
                    deferred.resolve({
                        isAuth: that.authenticated
                    });
                }
                else{
                    that.setAuthenticate(false);
                    deferred.reject({isAuth: that.authenticated});
                }
            }
            return deferred.promise;
        };
        
        //인증 확인
        var isAuth = function(){
            return that.authenticated;
        };
        var init= function(){
            if(!that.authenticated && $cookies.get(CookieKey)) that.setAuthenticate(true);
        }             
        return{
            init: init,
            login: authenticate,
            logout: unAuthenticate,
            isAuth: isAuth,
            isAuthP: isAuthPromise,
        };
    }];
}]).factory('AuthenticationInterceptor', ['$q', '$injector', '$cookies', 'Auth.CookieKey',
                                         function ($q, $injector, $cookies, CookieKey) {
    return {
        request : function(config){
            //console.log(config);
            if ($cookies.get(CookieKey)) {
                //console.log("token["+$cookies.get('nxtImsAuthorization')+"], config.headers: ", config.headers);
                //if(!config.headers.authorization) config.headers.authorization = $cookies.get('nxtImsAuthorization');
            }
            return config || $q.when(config);
        },
        responseError: function(rejection) {
            if (rejection.status == 401) {
                // 쿠키값이 존재하면 삭제 하고 로그인페이지로 이동
                if ($cookies.get(CookieKey)) $cookies.remove(CookieKey);
                console.log("[#AuthenticationInterceptor] Access denied (error 401), please login again");
                /*var $state = $injector.get('$state');
                $state.go("login");*/
            }
            return $q.reject(rejection);
        }
    }
}]);