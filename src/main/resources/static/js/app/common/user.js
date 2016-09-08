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
        var menu = [];
        this.setResource = function(resource){
            this.User(resource.USER, resource.MENU_LIST);
        };
        this.User = function(userInfo, menuInfo){
            user = userInfo;
            this.setMenu(menuInfo);
        };
        this.setUser = function(userInfo){
            if(userInfo) user = userInfo;
        };
        this.setMenu = function(menuInfo){
            if(menuInfo){
                menu = menuInfo;
                this.setMenuList(menu);
            }
        };
        this.setMenuList = function(menu){
            menuList = transformHierarchical(angular.copy(this.getMenu()));
        };
        
        this.getUser = function(){
            return user;
        };
        this.getMenu = function(){
            return menu;
        };
        this.getMenuList = function(){
            return menuList;
        };
        
        var transformHierarchical = function(arr) {
            var map = {}, node, roots = [];
            for(var i=0; i < arr.length; i++){
                node = arr[i];
                map[node.RESOURCE_LEVEL] = i;
                var nextkey = (i + 1) > (arr.length - 1) ? i : i + 1;
                var prevkey = (i - 1) < 0 ? i : i -1;
                node["text"] = node.RESOURCE_NM;
                if(node.RESOURCE_LEVEL == "1" || Number(node.RESOURCE_LEVEL) < Number(arr[nextkey].RESOURCE_LEVEL)){
                    node["items"] = [];
                }
                // parent id == -1 은 최상위
                node["PARENT_ID"] = angular.isUndefined(map[String(Number(node.RESOURCE_LEVEL)-1)]) ? 
                        "-1" : arr[map[String(Number(node.RESOURCE_LEVEL)-1)]].RESOURCE_ID;

                if (node["PARENT_ID"] != "-1" ) {
                    arr[map[Number(node.RESOURCE_LEVEL-1)]].items.push(node);
                } else {
                    roots.push(node);
                }
            }
            return roots;
        };
}]);