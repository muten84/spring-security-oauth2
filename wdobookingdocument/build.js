({
    appDir: './app',
    baseUrl: './app',
    dir: './dist',    
    fileExclusionRegExp: /^(r|build)\.js$/,
    optimizeCss: 'standard',
    removeCombined: true,
    paths: {
				angular: './bower_components/angular/angular',
				'angular-animate': './bower_components/angular-animate/angular-animate',
				'angular-aria': './bower_components/angular-aria/angular-aria',
				'angular-cookies': './bower_components/angular-cookies/angular-cookies',
				'angular-messages': './bower_components/angular-messages/angular-messages',
				'angular-mocks': './bower_components/angular-mocks/angular-mocks',
				'angular-resource': './bower_components/angular-resource/angular-resource',
				'angular-route': './bower_components/angular-route/angular-route',
				'angular-sanitize': './bower_components/angular-sanitize/angular-sanitize',
				'angular-touch': './bower_components/angular-touch/angular-touch',
				bootstrap: './bower_components/bootstrap/dist/js/bootstrap',
				domReady: './bower_components/domReady/domReady',
				jquery: './bower_components/jquery/dist/jquery',
				'ng-table': './bower_components/ng-table/dist/ng-table.min',
				'angular-ui-router': './bower_components/angular-ui-router/release/angular-ui-router',
				'angular-bootstrap': './bower_components/angular-bootstrap/ui-bootstrap-tpls',
				'angular-pdf': './bower_components/angular-pdf/dist/angular-pdf',
				pdf: './bower_components/pdfjs-dist/build/pdf',
				'pdf.worker': './bower_components/pdfjs-dist/build/pdf.worker',
				moment: './bower_components/moment/moment',
				'angular-block-ui': './bower_components/angular-block-ui/dist/angular-block-ui'
			},
    shim: {
				angular: {
					exports: 'angular'
				},
				moment: {
					exports: 'moment'
				},
				'angular-route': [
					'angular'
				],
				'angular-cookies': [
					'angular'
				],
				'angular-sanitize': [
					'angular'
				],
				'angular-resource': [
					'angular'
				],
				'angular-animate': [
					'angular'
				],
				'angular-touch': [
					'angular'
				],
				'angular-aria': [
					'angular'
				],
				'angular-messages': [
					'angular'
				],
				'angular-bootstrap': [
					'angular',
					'angular-animate',
					'angular-touch'
				],
				'ng-table': [
					'angular'
				],
				'angular-pdf': [
					'angular',
					'pdf',
					'pdf.worker'
				],
				'angular-block-ui': [
					'angular'
				],
				'angular-mocks': {
					deps: [
						'angular'
					],
					exports: 'angular.mock'
				}
			},
    
})