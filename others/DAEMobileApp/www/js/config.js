angular.module(_APP_).config([
    '$ionicConfigProvider',
    '$mdThemingProvider',
    '$mdIconProvider',
    '$mdColorPalette',
    'localStorageServiceProvider',
    '$translateProvider',
    '$httpProvider',
    'tmhDynamicLocaleProvider',
    'formlyConfigProvider',
function ($ionicConfigProvider, $mdThemingProvider, $mdIconProvider, $mdColorPalette, localStorageServiceProvider, $translateProvider, $httpProvider, tmhDynamicLocaleProvider, formlyConfigProvider) {

    // Inizializzo il localStorageService rimuovendo il prefisso
    localStorageServiceProvider.setPrefix('');

    // Inizializzo il modulo di traduzione
    $translateProvider.useLoader('translateService');
    $translateProvider.useSanitizeValueStrategy('escaped');

    // Disabilito lo swipe da usare come back su iOS
    $ionicConfigProvider.views.swipeBackEnabled(false);
    // Disabilito lo scolling Javscript facendo usare all'app quello nativo
    $ionicConfigProvider.scrolling.jsScrolling(false);

    $ionicConfigProvider.views.maxCache(5);
    // Use for change ionic spinner to android pattern.
    $ionicConfigProvider.spinner.icon("android");

    $ionicConfigProvider.navBar.alignTitle('center');

    // Inizializzo l'interceptor HTTP
    $httpProvider.interceptors.push('apiInterceptor');

    formlyConfigProvider.setType([
        {
            name: 'dae-datepicker',
            template:'<md-icon md-font-icon="dae-event" ng-click="to.onclick(model,options)" class="name"></md-icon><input ng-click="to.onclick(model,options)"  value="{{ model[options.key] | date : to.dateFormat }}" ng-model="model[options.key]" type="button">',
            defaultOptions: {}
        }
    ]);

    formlyConfigProvider.setType([
        {
            name: 'dae-timepicker',
            template:'<md-icon md-font-icon="dae-time" class="name"></md-icon><input ng-click="to.onclick(model,options)" value="{{ model[options.key] | date : to.dateFormat }}" ng-model="model[options.key]" type="button">',
            defaultOptions: {}
        }
    ]);

    formlyConfigProvider.setType([
        {
            name: 'dae-localize',
            template: '<div class="row row-center no-padding"><div class="col col-15 no-padding"><a class="md-button md-fab" ng-click="to.onclick(model,options)" aria-label="Add"><i class="icon dae-localize"></i></a></div><div class="col col-center no-padding"><span>Rileva posizione</span></div></div>',
            defaultOptions: {}
        }
    ]);

    formlyConfigProvider.setType([
        {
            name: 'dae-upload',
            templateUrl: 'templates/formly/upload.html',
            defaultOptions: {}
        }
    ]);

    formlyConfigProvider.setType([
        {
            name: 'dae-municipalityList',
            templateUrl: 'templates/formly/municipalityList.html',
            defaultOptions: {}
        }
    ]);

    formlyConfigProvider.setType([
        {
            name: 'dae-availabilityList',
            templateUrl: 'templates/formly/availabilityList.html',
            defaultOptions: {}
        }
    ]);

    formlyConfigProvider.setType({
        name: 'switch',
        template: '<md-switch class="md-primary" ng-model="model[options.key]" md-theme="{{to.theme}}">{{to.label}}</md-switch>'
    });

    formlyConfigProvider.setType({
        name: 'select',
        template: '<md-select ng-model="model[options.key]"><md-option ng-repeat="option in to.options track by option.id" ng-value="option[to.valueProp]" ng-selected="{{ option.id === model[options.key] ? \'true\' : \'false\' }}">{{ option[to.labelProp]}}</md-option></md-select>',
        defaultOptions: {
            templateOptions: {
                disabled: false
            },
            ngModelAttrs: {
                disabled: {
                    bound: 'ng-disabled'
                },
                onOpen: {
                    statement: 'md-on-open'
                },
                onClose: {
                    statement: 'md-on-close'
                }
            }
        },
        overwriteOk: true
    });

    formlyConfigProvider.setType({
        name: 'select-multiple',
        template: '<md-select ng-model="model[options.key]" multiple><md-option ng-repeat="option in to.options track by option.id" ng-value="option[to.valueProp]" ng-selected="{{ option.id === model[options.key] ? \'true\' : \'false\' }}">{{ option[to.labelProp]}}</md-option></md-select>',
        defaultOptions: {
            templateOptions: {
                disabled: false
            },
            ngModelAttrs: {
                disabled: {
                    bound: 'ng-disabled'
                },
                onOpen: {
                    statement: 'md-on-open'
                },
                onClose: {
                    statement: 'md-on-close'
                }
            }
        },
        overwriteOk: true
    });

    formlyConfigProvider.setType({
        name: 'autocomplete',
        templateUrl: 'templates/formly/autocomplete.html',
        overwriteOk: true
    });

    formlyConfigProvider.setType({
        name: 'ion-autocomplete',
        templateUrl: 'templates/formly/ion-autocomplete.html',
        overwriteOk: true
    });

    formlyConfigProvider.setType({
        name: 'input',
        template: '<input autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false" ng-model="model[options.key]">',
        overwriteOk: true
    });

    formlyConfigProvider.setType({
        name: 'checkbox',
        template: '<md-checkbox ng-model="model[options.key]">{{to.label}}</md-checkbox>',
        overwriteOk: true
    });

    formlyConfigProvider.setType({
        name: 'textarea',
        template: '<textarea ng-model="model[options.key]" md-maxlength="{{to.maxlength}}" rows="{{to.rows}}" md-select-on-focus>',
        overwriteOk: true
    });

    formlyConfigProvider.setWrapper({
        name: 'mdLabel',
        types: ['input', 'textarea', 'dae-datepicker', 'dae-timepicker', 'select', 'select-multiple', 'dae-upload', 'ion-autocomplete'],
        template: '<label>{{to.label}}</label><formly-transclude></formly-transclude>'
    });

    formlyConfigProvider.setWrapper({
        name: 'validation',
        types: ['input', 'select', 'select-multiple', 'ion-autocomplete'],
        templateUrl: 'templates/formly/error-messages.html',
        overwriteOk: true
    });

    formlyConfigProvider.setWrapper({
        name: 'mdInputContainer',
        types: ['input', 'switch', 'textarea', 'dae-datepicker', 'dae-timepicker', 'select', 'select-multiple', 'dae-upload', 'ion-autocomplete'],
        template: '<md-input-container class="md-block"><formly-transclude></formly-transclude></md-input-container>'
    });

    formlyConfigProvider.templateManipulators.preWrapper.push(function (template, options) {
        if (!options.data.icon) {
            return template;
        }
        return '<md-icon class="step" md-font-icon="icon-' + options.data.icon + '"></md-icon>' + template;
    });

    var availabilityUnique = 1;
    formlyConfigProvider.setType({
        name: 'repeatAvailabilitySection',
        templateUrl: 'templates/formly/repeatAvailabilitySection.html',
        controller: function($scope, deviceService) {
            $scope.formOptions = {formState: $scope.formState};
            $scope.addNew = addNew;
            $scope.copyFields = copyFields;

            function addNew() {
                $scope.model[$scope.options.key] = $scope.model[$scope.options.key] || [];
                var repeatsection = $scope.model[$scope.options.key];
                var lastSection = repeatsection[repeatsection.length - 1];
                var newsection = {};
                if (lastSection) {
                    repeatsection.push(newsection);
                    newsection.orarioDa = "00:00";
                    newsection.orarioA = "23:59";
                }
                else {
                    newsection.orarioDa = "00:00";
                    newsection.orarioA = "23:59";
                    repeatsection.push(newsection);
                }

            }

            function copyFields(fields) {
                fields = angular.copy(fields);
                addRandomIds(fields);
                return fields;
            }

            function addRandomIds(fields) {
                availabilityUnique++;
                angular.forEach(fields, function(field, index) {
                    if (field.fieldGroup) {
                        addRandomIds(field.fieldGroup);
                        return; // fieldGroups don't need an ID
                    }

                    if (field.templateOptions && field.templateOptions.fields) {
                        addRandomIds(field.templateOptions.fields);
                    }

                    field.id = field.id || (field.key + '_' + index + '_' + availabilityUnique + getRandomInt(0, 9999));
                });
            }

            function getRandomInt(min, max) {
                return Math.floor(Math.random() * (max - min)) + min;
            }
        }
    });

    var unique = 1;
    formlyConfigProvider.setType({
        name: 'repeatSection',
        templateUrl: 'templates/formly/repeatSection.html',
        controller: function($scope) {
            $scope.formOptions = {formState: $scope.formState};
            $scope.addNew = addNew;

            $scope.copyFields = copyFields;


            function addNew() {
                $scope.model[$scope.options.key] = $scope.model[$scope.options.key] || [];
                var repeatsection = $scope.model[$scope.options.key];
                var lastSection = repeatsection[repeatsection.length - 1];
                var newsection = {};
                if (lastSection) {
                    newsection = angular.copy(lastSection);
                    newsection.id = lastSection.id + 1;
                    newsection.orarioDa = "";
                    newsection.orarioA = "";
                    newsection.giornoSettimana = "";
                }
                repeatsection.push(newsection);
            }

            function copyFields(fields) {
                fields = angular.copy(fields);
                addRandomIds(fields);
                return fields;
            }

            function addRandomIds(fields) {
                unique++;
                angular.forEach(fields, function(field, index) {
                    if (field.fieldGroup) {
                        addRandomIds(field.fieldGroup);
                        return; // fieldGroups don't need an ID
                    }

                    if (field.templateOptions && field.templateOptions.fields) {
                        addRandomIds(field.templateOptions.fields);
                    }

                    field.id = field.id || (field.key + '_' + index + '_' + unique + getRandomInt(0, 9999));
                });
            }

            function getRandomInt(min, max) {
                return Math.floor(Math.random() * (max - min)) + min;
            }
        }
    });

    // mdIconProvider is function of Angular Material.
    // It use for reference .SVG file and improve performance loading.
    $mdIconProvider
        .icon('facebook', 'img/icons/facebook.svg')
        .icon('twitter', 'img/icons/twitter.svg')
        .icon('mail', 'img/icons/mail.svg')
        .icon('message', 'img/icons/message.svg')
        .icon('share-arrow', 'img/icons/share-arrow.svg')
        .icon('more', 'img/icons/more_vert.svg')
        .icon('clear', 'img/icons/clear_36px.svg')
        .icon('add', 'img/icons/add_36px.svg')
        .icon('volume_mute', 'img/icons/volume_mute.svg')
        .icon('volume_up', 'img/icons/volume_up.svg')
        .iconSet('social', 'img/icons/sets/social-icons.svg', 24)
        .iconSet('device', 'img/icons/sets/device-icons.svg', 24)
        .iconSet('communication', 'img/icons/sets/communication-icons.svg', 24)
        .defaultIconSet('img/icons/sets/core-icons.svg', 24);

    //mdThemingProvider use for change theme color of Ionic Material Design Application.
    /* You can select color from Material Color List configuration :
     * red
     * pink
     * purple
     * purple
     * deep-purple
     * indigo
     * blue
     * light-blue
     * cyan
     * teal
     * green
     * light-green
     * lime
     * yellow
     * amber
     * orange
     * deep-orange
     * brown
     * grey
     * blue-grey
     */
    //Learn more about material color patten: https://www.materialpalette.com/
    //Learn more about material theme: https://material.angularjs.org/latest/#/Theming/01_introduction
    var customPrimary = {
        '50': '#07e22e',
        '100': '#06c929',
        '200': '#05b024',
        '300': '#04971f',
        '400': '#047f1a',
        '500': '#036615',
        '600': '#024d10',
        '700': '#02340b',
        '800': '#011c06',
        '900': '#000301',
        'A100': '#0af835',
        'A200': '#23f94a',
        'A400': '#3cf95e',
        'A700': '#000000'
    };
    $mdThemingProvider
        .definePalette('customPrimary',
            customPrimary);
    var customAccent = {
        '50': '#490008',
        '100': '#62000b',
        '200': '#7c000e',
        '300': '#950011',
        '400': '#af0014',
        '500': '#c80017',
        '600': '#fb001d',
        '700': '#ff1631',
        '800': '#ff2f47',
        '900': '#ff495e',
        'A100': '#fb001d',
        'A200': '#e2001a',
        'A400': '#c80017',
        'A700': '#ff6274'
    };
    $mdThemingProvider
        .definePalette('customAccent',
            customAccent);
    var customBackground = {
        '50': '#ffffff',
        '100': '#ffffff',
        '200': '#ffffff',
        '300': '#ffffff',
        '400': '#ffffff',
        '500': '#fff',
        '600': '#f2f2f2',
        '700': '#e6e6e6',
        '800': '#d9d9d9',
        '900': '#cccccc',
        'A100': '#ffffff',
        'A200': '#ffffff',
        'A400': '#ffffff',
        'A700': '#bfbfbf'
    };
    $mdThemingProvider
        .definePalette('customBackground',
            customBackground);
    $mdThemingProvider
        .theme('default')
        .primaryPalette('customPrimary')
        .accentPalette('customAccent')
        //.backgroundPalette('customBackground');

    //appPrimaryColor = $mdColorPalette[$mdThemingProvider._THEMES.default.colors.primary.name]["500"]; //Use for get base color of theme.
    appPrimaryColor = $mdThemingProvider._PALETTES[$mdThemingProvider._THEMES.default.colors.primary.name]["500"];

    // Configurazione su dove recuperare i file locale
    tmhDynamicLocaleProvider.localeLocationPattern("lib/angular-i18n/angular-locale_{{locale}}.js");


}]);
