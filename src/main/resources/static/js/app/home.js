/*
 * 홈 모듈 js
 *
 * @author 정철균
 */

'use strict';

angular.module('nxtIms.home',[])
.controller("MainController", 
        ["$rootScope", "$state", "Authentication", "constants",
         function($rootScope, $state, Authentication, constants) {
    var self = this;
    
    self.authenticated = Authentication.isAuth();
    console.log("home controller");
    self.logout = function() {
        Authentication.logout()
        .then(
                function(message){
                    console.log(message);
                }, function(message){
                    console.log(message);
                })
        .finally(function() {
            console.log("!-- go login");
            $state.go(constants.login);
        });
    }
    //menu navi
    self.openNav = function(){
        angular.element("#programMenu").css("width","250px");
    }
    self.closeNav = function(){
        angular.element("#programMenu").css("width", "0");
    }
    self.program = [];
    self.test = "test Text";
}]);