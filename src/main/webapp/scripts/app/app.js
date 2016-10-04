'use strict';

angular.module('babylone14166App', ['LocalStorageModule', 'ngResource', 'ngCookies', 'ngAria', 'ngCacheBuster',
    'ngFileUpload', 'ui.bootstrap', 'ui.router', 'infinite-scroll', 'angular-loading-bar', 'siyfion.sfTypeahead',
    'mgcrea.ngStrap.datepicker', 'ngSanitize', 'jsonFormatter', 'ui.select'])

    .run(function ($rootScope, $location, $window, $http, $state, Auth, Principal, ENV, VERSION) {

        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

        });

        $rootScope.$watch('searchToken', function (value) {
            if (value && value.length > 2) {
                $state.go('home');
                $http({
                    method: 'GET',
                    url: 'search/' + value
                }).then(function successCallback(result) {
                    $rootScope.dtos = result.data;
                }, function errorCallback(response) {
                });
            } else {
                $rootScope.dtos = []
            }
        });

        $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
            var titleKey = 'babylone14166';

            // Remember previous state unless we've been redirected to login or we've just
            // reset the state memory after logout. If we're redirected to login, our
            // previousState is already set in the authExpiredInterceptor. If we're going
            // to login directly, we don't want to be sent to some previous state anyway
            if (toState.name != 'login' && $rootScope.previousStateName) {
                $rootScope.previousStateName = fromState.name;
                $rootScope.previousStateParams = fromParams;
            }

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            $window.document.title = titleKey;
        });

        $rootScope.back = function () {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };

        $rootScope.typeaheadOptionsZeroChar = {
            minLength: 0,
            hint: false,
            highlight: true
        };
        $rootScope.typeaheadOptionsThreeChars = {
            minLength: 3,
            hint: false,
            highlight: true
        };

        var autocomplete = function (data) {
            var b = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.whitespace,
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: data
            });
            return {
                limit: 10,
                source: function (q, sync) {
                    if (q === '') {
                        sync(b.get(data));
                    }
                    else {
                        b.search(q, sync);
                    }
                }
            }
        };

        $rootScope.finsDeMandatAutocomplete = autocomplete(finsDeMandat);
        $rootScope.finsGroupePolitiqueAutocomplete = autocomplete(finsGroupePolitique);
        $rootScope.typesOrganismeAutocomplete = autocomplete(typesOrganisme);
        $rootScope.secteursActiviteAutocomplete = autocomplete(secteursActivite);
        $rootScope.statutsAppartenanceOrganismeAutocomplete = autocomplete(statutsAppartenanceOrganisme);
        $rootScope.typesNominationAutocomplete = autocomplete(typesNomination);
        $rootScope.professionsAutocomplete = autocomplete(professions);
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, httpRequestInterceptorCacheBusterProvider, AlertServiceProvider) {
        // uncomment below to make alerts look like toast
        //AlertServiceProvider.showAsToast(true);

        //enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ]
            }
        });

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
        $httpProvider.interceptors.push('notificationInterceptor');

    })
    .config(['$urlMatcherFactoryProvider', function ($urlMatcherFactory) {
        $urlMatcherFactory.type('boolean', {
            name: 'boolean',
            decode: function (val) {
                return val == true ? true : val == "true" ? true : false
            },
            encode: function (val) {
                return val ? 1 : 0;
            },
            equals: function (a, b) {
                return this.is(a) && a === b;
            },
            is: function (val) {
                return [true, false, 0, 1].indexOf(val) >= 0
            },
            pattern: /bool|true|0|1/
        });
    }])
    // TODO good pr pb de cache ?
    .config(['$httpProvider', function ($httpProvider) {
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }
        $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
        $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
    }])
    // TODO mlo reprendre
    .filter('ouinon', [function () {
        return function (value) {
            if (value) {
                return 'oui';
            }
            return 'non';
        };
    }])
    .filter('civilite', [function () {
        return function (value) {
            switch (value) {
                case 'MADAME':
                    return 'Mme.';
                case 'MONSIEUR':
                    return 'M.';
            }
            return '';
        };
    }])
    .filter('properso', [function () {
        return function (value) {
            switch (value) {
                case 'PRO':
                    return 'Professionnelle';
                case 'PERSO':
                    return 'Personnelle';
            }
            return 'Inconnu';
        };
    }])
    .filter('confidentialite', [function () {
        return function (value) {
            switch (value) {
                case'PUBLIABLE':
                    return 'Publiable';
                case 'CONFIDENTIEL':
                    return 'Confidentiel';
                case 'INTERNE':
                    return 'Interne';
            }
            return 'Inconnu';
        };
    }])
    .filter('typeIdentiteInternet', [function () {
        return function (value) {
            switch (value) {
                case 'SiteInternet':
                    return '<span class="grey">(site Internet)</span>';
                case 'Blog':
                    return '<span class="grey">(bloc)</span>';
                case 'Twitter':
                    return '<span class="grey icon fa fa-twitter"></span>';
                case 'LinkedIn':
                    return '<span class="grey icon fa fa-linkedin"></span>';
                case 'Viadeo':
                    return '<span class="grey">(Viadeo)</span>';
                case 'Facebook':
                    return '<span class="grey icon fa fa-facebook"></span>';
                case 'GooglePlus':
                    return '<span class="grey icon fa fa-google-plus"></span>';
                case 'Autre':
                    return '<span class="grey">(autre)</span>';
            }
            return '<span class="grey">(inconnu)</span>';
        };
    }])
    .filter('natureFixeMobile', [function () {
        return function (value) {
            switch (value) {
                case'FIXE':
                    return 'Fixe';
                case 'MOBILE':
                    return 'Mobile';
            }
            return 'Inconnu';
        };
    }])
    .filter('auditTrailAction', [function () {
        return function (value) {
            switch (value) {
                case'CREATE':
                    return 'Création';
                case 'UPDATE':
                    return 'Édition';
                case 'CLOSE':
                    return 'Fermeture';
                case 'DELETE':
                    return 'Suppression';
            }
            return 'Inconnu';
        };
    }])
    .filter('typeSeance', [function () {
        return function (value) {
            switch (value) {
                case'COMMISSION_PERMANENTE':
                    return 'Commission permanente';
                case 'PLENIERE':
                    return 'Plénière';
            }
            return 'Inconnu';
        };
    }]);
