/*
 * 로그인 모듈 js
 *
 * @author 정철균
 */

'use strict';

angular.module('nxtIms.navigation',[])
.constant('Login.CookieKey', 'NXTIMS_SAVE_ID') //아이디 저장 cookie key
.constant('PAGE_SESSION', 'NXTIMS_PAG_IN')     //이동할 페이지 정보 저장 session key
/**
 * 페이지 이동 
 * session storage에 ID/PATH저장 후 새탭에서 사용
 */
.provider('ProgramInfo',['$windowProvider', 'PAGE_SESSION', function($windowProvider, PAGE_SESSION){
    var $window = $windowProvider.$get();
    var init = null;
    this.programInfo;
    this.init = function(){
        var cookiValue = $window.sessionStorage.getItem(PAGE_SESSION);
        this.programInfo = cookiValue && angular.fromJson($window.atob(cookiValue));
        if(this.programInfo == null || angular.isUndefined(this.programInfo)){
            this.programInfo = {
               pageNo: "",
               pagePath: ""
                   /*,pageList: [] //페이지리스트*/
            }
        }
    }
    this.getId = function(){
        return this.programInfo.pageNo;
    }
    this.getPath = function(){
        return this.programInfo.pagePath;
    }
    this.$get = [function(){
        var that = this;
        var setProgramNo = function(no, path){
            if(!no || !path) return;
            var isPage = false;
            
            /* 페이지 존재여부
            for(var i in that.programInfo.pageList){
                if(that.programInfo.pageList[i] == no){
                    isPage = true;
                }
            }*/
            if(!isPage) {
                that.programInfo.pageNo = no;
                that.programInfo.pagePath = path;
                //that.programInfo.pageList.push(no);
                $window.sessionStorage.setItem(PAGE_SESSION, $window.btoa(angular.toJson(that.programInfo)));
            }
            return true; //!isPage; //true: page is not
        };
        var setProgramUrl = function(url){
            if(!url) return;
            that.programUrl = url;
        };
        var getProgramId = function(){
            return that.programInfo.pageNo
        };
        var getProgramPath = function(){
            var pagePath = "";
            for(var i in UserService){
                if(UserService[i].RESOURCE_ID == that.programInfo.pageNo){
                    pagePath = UserService[i].RESOURCE_URL
                }
            }
            return pagePath.replace("/partial","");
        }
        return{
            setId: setProgramNo,
            setUrl: setProgramUrl,
            getId: getProgramId,
            getPath: getProgramPath
        };
    }];
}])
/**
 * @description
 * 프로그램 메뉴버튼 동작 directive
 * 
 * @requires $window
 * @requires $state
 * @requires constants (nxtIms 모듈에서 정의한)
 */
.directive('programMenu', ['$window', '$state', 'constants', function ($window, $state, constants) {
    return {
        scope: false,
        restrict: "A",
        replace: true,
        templateUrl: constants.comnTemplUrl + 'menu.html',
        controller: ['$scope', function($scope){
            var ctrl = this;
        }],
        controllerAs: 'menu',
        link: function($scope, element, attrs, ctrl) {
            var c_height = angular.element(window).height();
            ctrl.all = function(event){
                angular.element("#allmenu_box").css({'display': 'block' }).animate({'left': '0px'}, 500);
                angular.element("#allmenu_box .all_cont").height(c_height);
                //var iscroll = new iScroll("wrapper", { hScroll:false });
                angular.element("html, body").addClass('body_hidden');
            };
            ctrl.allbg = function(event){
                angular.element("#allmenu_box").stop().animate({'left': '-' + 313 + 'px' }, 300, function() {
                    angular.element('#allmenu_box').css({'display': 'none' });
                });
                angular.element('#allmenu_box').off('scroll touchmove mousewheel');
                //var iscroll = new iScroll("wrapper", { hScroll:false });
                angular.element("html, body").removeClass('body_hidden');
            };
            ctrl.allmenu_close = function(event){
                angular.element("#allmenu_box").stop().animate({'left': '-' + 313 + 'px' }, 300, function() {
                    angular.element('#allmenu_box').css({'display': 'none' });
                });
                angular.element('html, body').removeClass('body_hidden');
            };
        }
    };

}])
/**
 * 프로그램 메뉴 트리 뷰 directive
 */
.directive('programMenuTree', ['$window', '$state', 'ProgramInfo', 'Authentication',
                               function ($window, $state, ProgramInfo, Authentication) {
    return {
        scope: true,
        template: ['<div kendo-tree-view="menu.tree" k-options="menu.treeSetting" k-data-source="menu.data" k-ng-delay ="menu.data" ng-click="menu.toggle($event)">',
                   '</div>'].join(''),
        controller: ['$scope', '$timeout', 'KendoDataHelper', 'UserService', 
                     function($scope, $timeout, KendoDataHelper, UserService){
            var menu = this;
            menu.treeSetting = {
                                
            };
            $timeout(function(){
                menu.data = KendoDataHelper.toKendoData(UserService.getMenuList(), "tree");
            },100);
            
        }],
        controllerAs: 'menu',
        link: function($scope, element, attrs, ctrl) {
            element.css({
                /*"display": "inline-block",
                "overflow": "hidden"*/
            });
            ctrl.toggle = function(e){
                //e.preventDefault();
                var target = angular.element(e.target);
                console.log(target);
                var toggleIcon = target.closest(".k-icon");
                if (!toggleIcon.length) {
                    var currData = ctrl.tree.dataItem(ctrl.tree.select());
                    console.log(currData);
                    if(angular.equals(currData.RESOURCE_URL, null)){
                        this.tree.toggle(target.closest(".k-item"));
                    }
                    else{
                        var url = currData.RESOURCE_URL;
                        url = url.replace(".html","").replace("/partial","");
                        var id = url.substring(url.lastIndexOf("/"), url.lastIndexOf("."));
                        if(ProgramInfo.setId(currData.RESOURCE_ID, url)){
                            if(!Authentication.isAuth()){ 
                                // 메뉴클릭시 권한 없을때 ui router event 전송
                                $scope.$emit("$stateChangeStart", {}, {name: url}, null, $state.current, $state.params);
                            }
                            else $window.open("/program" + url,currData.RESOURCE_NM);
                        }
                            
                    }
                    
                }
            };
        }
    };

}])
/**
 * navigation controller
 */
.controller("NavigationController",["$state", "Authentication", "constants", "$kWindow", "$cookies", "Login.CookieKey", "$timeout",
                            function($state, Authentication, constants, $kWindow, $cookies, CookieKey, $timeout) {

    var self = this;
    self.credentials = {};
    self.buttonDisabled = false; //button diabled
    
    /*
     * 아이디 저장 쿠키 존재여부 확인 후 셋팅
     */
    if($cookies.get(CookieKey)){
        self.credentials.username = atob($cookies.get(CookieKey));
        self.saveId = true;
    }
    //login
    self.login = function(event) {
        self.buttonDisabled = true;
        Authentication.login(self.credentials)
        .then(
                function(message){
                    self.error = false;
                    console.log(message);
                    if(!$cookies.get(CookieKey) && self.saveId){
                        // 쿠키가 없고 아이디저장 체크된 상태이면 쿠키 7일간 저장
                        var now = new Date();
                        now.setDate(now.getDate() + 7);
                        $cookies.put(CookieKey, btoa(self.credentials.username), {expires: now});
                    }
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
            // 로그인 버튼 2번 클릭 방지
            $timeout(function(){
                self.buttonDisabled = false;
            },500);
            
        });
    };
    
    //logout
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
    
    //password 찾기
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
    
    // container tracking
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
    
    //
    self.notifyOptions = {
                           templates: [{
                               type: "ngTemplate",
                               template: '<p style="width: 16em; padding:1em;white-space:nowrap"> {{ngValue}}, #= kValue # </p>'
                           }]
                       };
}]);