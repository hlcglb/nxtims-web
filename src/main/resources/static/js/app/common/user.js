/*
 * User Module define js
 *
 * @author 정철균
 */

'use strict';

angular.module('comn.service.user',[])
.service('UserService', [function () {
        var user = [];
        var menuList = [];
        this.User = function(userInfo, menu){
            user = userInfo;
            menuList = menu;
        }
        this.setUser = function(userInfo){
            user = userInfo;
        }
        this.setMenuList = function(menu){
            menuList = menu;
        }
        
        this.getUser = function(){
            return user;
        }
        this.getMenuList = function(){
            return menuList;
        }
}]);