/*
 * AngularJS Module 설정 js
 *
 * @author 정철균
 */

/**
 * common module
 */
var commonApp = angular.module('commonApp', ['kendo.directives', 'utilsApp']);

/**
 * utils module
 */
var utilsApp = angular.module('utilsApp', []);

/**
 * login module
 */
var loginApp = angular.module('loginApp', ['commonApp'])
.controller('LoginController', ['$scope', '$window', 'LoginDao', 'constants',
                                  function ($scope, $window, loginDao, constants) {
    $scope.userId= "";
    $scope.userPwd= "";
    $scope.loginBtnName= "로그인";
    $scope.findBtnName= "비밀번호 찾기";
    $scope.joinBtnName= "회원 가입";
    
    // login
    $scope.login = function(){
        $window.location.href = constants.main;
        /*loginDao.login("loginService").then(function(resutl){
            if(resutl.status == "success") console.log("성공");//$window.location.href = constants.main;
            else $scope.notf2.show("service error");
        },
        function(errorMsssage){
            $scope.notf2.show(errorMsssage);
        });*/
        
    };
    // find password
    $scope.find = function(){
    };
    
    $scope.kendoNotify = function () {
        $scope.notf2.show("error");
    };

}]);

/**
 * main module
 */
var mainApp = angular.module('mainApp', 
        ['commonApp',
         'ng',
         'nx.service.Locale']);
mainApp.config(['nxLocaleProvider', function(nxLocaleProvider){
    nxLocaleProvider.setLocaleUrlPattern('js/angularjs/i18n/angular-locale_{{locale}}.js');
}]);

