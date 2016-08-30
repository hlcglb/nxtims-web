/*
 * 로그인 모듈 js
 *
 * @author 정철균
 */

'use strict';

angular.module('nxtIms.login',[])
.controller("NavigationController",["$state", "Authentication", "constants",
                            function($state, Authentication, constants) {
    console.log("navigation controller");
    var self = this;
    self.credentials = {};
    self.login = function() {
        Authentication.login(self.credentials)
        .then(
                function(message){
                    console.log(message);
                    $state.go(constants.main);
                    self.error = false;
                }, 
                function(message){
                    console.log(message);
                    $state.go(constants.login);
                    self.error = true;
                }
        ).catch(function(error){
            console.log("[#catch] " + error);
        });
    };

}])