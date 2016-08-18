angular.module('nxtims', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {
    $routeProvider.when('/', {
        templateUrl : 'partial/home.html',
        controller : 'home',
        controllerAs: 'controller'
    }).when('/login', {
        templateUrl : 'partial/login.html',
        controller : 'navigation',
        controllerAs: 'controller'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
}).controller('navigation',function($rootScope, $http, $location, $route) {
    var self = this;

    var authenticate = function(credentials, callback) {
        console.log("authenticate")
        
        var headers = credentials ? {
            authorization : "Basic "
                    + btoa(credentials.username + ":"
                            + credentials.password)
        } : {};
        
        console.log("http get /authentication")

        $http.get('authentication', {
            headers : headers
        }).then(function(response) {
            if (response.data.name) {
                console.log("authentication success")
                
                $rootScope.authenticated = true;
            } else {
                console.log("authentication fail")
                
                $rootScope.authenticated = false;
            }
            callback && callback($rootScope.authenticated);
        }, function() {
            console.log("authentication fail(401)")
            
            $rootScope.authenticated = false;
            callback && callback(false);
            
            $location.path("/login");
        });
    }

    console.log("call authenticate")
    authenticate();

    console.log("init credentials")
    self.credentials = {};
    
    self.login = function() {
        console.log("login")
        
        authenticate(self.credentials, function(authenticated) {
            if (authenticated) {
                console.log("Login succeeded")
                
                $location.path("/");
                self.error = false;
                $rootScope.authenticated = true;
            } else {
                console.log("Login failed")
                
                $location.path("/login");
                self.error = true;
                $rootScope.authenticated = false;
            }
        })
    };    
})
.controller('home', function($rootScope, $http, $location) {
    var self = this;

    $http.get('/resource').then(function(response) {
        console.log("http get /resource")
        
        //menu, 즐겨찾기, ....
        self.data = response.data;
    })
    
    self.logout = function() {
        console.log("logout")

        $http.post('logout', {}).finally(function() {
            $rootScope.authenticated = false;
            $location.path("/login");
        });
    }    
});