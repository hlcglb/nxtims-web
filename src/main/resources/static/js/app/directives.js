/*
 * AngularJS Directive 설정 js
 *
 * @author 정철균
 */

angular.module('nxtims.directives',[])
.directive('programMenuTree', ['$window', '$state', function ($window, $state) {
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
                menu.data = KendoDataHelper.toKendoData(angular.copy(UserService.getMenuList()), "tree");
            },50);
            
        }],
        controllerAs: 'menu',
        link: function($scope, element, attrs, ctrl) {
            element.css({
                "display": "inline-block",
                "overflow": "hidden"
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
                        var id = url.substring(url.lastIndexOf("/"), url.lastIndexOf("."));
                        $window.open("program" + id,"_blank");
                    }
                    
                }
            };
        }
    };

}]);