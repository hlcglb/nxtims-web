/*
 * 로그인 모듈 js
 *
 * @author 정철균
 */

'use strict';

angular.module('nxtIms.login',[])
.constant('Login.CookieKey', 'nxtIms_save_id')
.controller("NavigationController",["$state", "Authentication", "constants", "$kWindow", "$cookies", "Login.CookieKey",
                            function($state, Authentication, constants, $kWindow, $cookies, CookieKey) {
    console.log("navigation controller");
    var self = this;
    self.credentials = {};
    self.login = function() {
        Authentication.login(self.credentials)
        .then(
                function(message){
                    self.error = false;
                    console.log(message);
                    self.saveId ? $cookies.put(CookieKey, response.config.headers.authorization) : null;
                    //save id 추가해야함
                    $state.go(constants.main);
                }, 
                function(data){
                    console.log(data);
                    self.error = true;
                    self.errorData = data;
                    self.notf2.show(data.message, "error");
                }
        ).catch(function(error){
            console.log("[#catch] " + error);
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
                console.log($scope);
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
    
    self.officeDataSource = {
       transport: {
           read: {
               dataType: "jsonp",
               url: "//demos.telerik.com/kendo-ui/service/Customers",
           }
       }
    };
     
    self.officeOptions = {
        dataSource: self.officeDataSource,
        dataTextField: "ContactName",
        dataValueField: "CustomerID"
    };
}]);