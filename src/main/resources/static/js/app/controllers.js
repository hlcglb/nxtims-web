/*
 * AngularJS Controller 설정 js
 *
 * @author 정철균
 */


/**
 * login 모듈 컨트롤러
 */
loginApp.controller('LoginController', ['$scope', '$window', 'LoginDao', 'constants',
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
 * main 모듈 컨트롤러
 */
mainApp.controller("MainController", ['$scope', '$compile', '$timeout', '$interpolate', 'nxLocale', '$locale',
                                function($scope, $compile, $timeout, $interpolate, nxLocale, $locale) {
    //nxLocale.set("ko-kr");
    console.log($locale.id);
    this.locale = "ko-kr"
    this.text = "This is Main";
    this.openNav = function(){
        angular.element("#programMenu").css("width","250px");
    }
    this.closeNav = function(){
        angular.element("#programMenu").css("width", "0");
    }
    this.num = 1000;
    this.script = $interpolate("js/angularjs/i18n/angular-locale_{{main.locale}}.js")($scope);
    console.log(this.script);
    
}]);