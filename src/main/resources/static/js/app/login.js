/*
 * 로그인 모듈 js
 *
 * @author 정철균
 */

'use strict';

angular.module('nxtIms.login',[])
.constant('Login.CookieKey', 'nxtIms_save_id')
.controller("NavigationController",["$state", "Authentication", "constants", "$kWindow", "$cookies", "Login.CookieKey", "UrlMessageStorage",
                            function($state, Authentication, constants, $kWindow, $cookies, CookieKey, UrlMessageStorage) {
    console.log("navigation controller");
    var self = this;
    self.credentials = {};
    self.buttonDisabled = false;
    self.login = function(event) {
        self.buttonDisabled = true;
        Authentication.login(self.credentials)
        .then(
                function(message){
                    self.error = false;
                    console.log(message);
                    self.saveId ? $cookies.put(CookieKey, self.credentials.username, 7) : null;
                    //save id 추가해야함
                    $state.go(constants.main);
                }, 
                function(data){
                    console.log(data);
                    self.error = true;
                    self.errorData = data;
                    console.log(self);
                    self.loginNotify.show(data.message/*kValue: data.message*/, "error"/*"ngTemplate"*/);
                }
        ).catch(function(error){
            console.log("[#catch] " + error);
        })
        .finally(function(){
            self.buttonDisabled = false;
        });
    };
    self.log = function(){
        console.log(self.loginForm);
    }
    self.find = function(){
        var windowInstance = $kWindow.open({
            options:{
                modal: true,
                title: "find Password",
                resizable: true,
                height: 150,
                width: 400,
                visible: false
            },
            windowTemplateUrl: constants.popTemplUrl + "window.html",
            templateUrl: constants.popTemplUrl + "commonPopup.html",
            controller: ["$scope", "$windowInstance", "message", function($scope, $windowInstance, message){
                $scope.message = message;
                $scope.$windowInstance = $windowInstance;
            }],
            resolve: {
                message: function () {
                    return "password find"
                }
            }
        });


        windowInstance.result.then(function (result) {
            if (result) {
                self.result = "confirmed!";
            }
            else{
                self.result = "canceled!";
            }
        });
    };
    
    self.tracking = function(){
        var windowInstance = $kWindow.open({
            options:{
                modal: true,
                title: "find Password",
                resizable: true,
                height: 150,
                width: 400,
                visible: false
            },
            windowTemplateUrl: constants.popTemplUrl + "window.html",
            templateUrl: constants.popTemplUrl + "commonPopup.html",
            controller: ["$scope", "$windowInstance", "message", function($scope, $windowInstance, message){
                $scope.message = message;
                $scope.$windowInstance = $windowInstance;
            }],
            resolve: {
                message: function () {
                    return self.tracker;
                }
            }
        });


        windowInstance.result.then(function (result) {
            if (result) {
                self.result = "confirmed!";
            }
            else{
                self.result = "canceled!";
            }
        });
    };
    self.notifyOptions = {
                           templates: [{
                               type: "ngTemplate",
                               template: '<p style="width: 16em; padding:1em;white-space:nowrap"> {{ngValue}}, #= kValue # </p>'
                           }]
                       };
}]);