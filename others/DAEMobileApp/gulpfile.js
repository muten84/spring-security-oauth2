var gulp = require('gulp');
var gutil = require('gulp-util');
var angularTemplateCache = require('gulp-angular-templatecache');
var bower = require('bower');
var concat = require('gulp-concat');
var sass = require('gulp-sass');
var minifyCss = require('gulp-minify-css');
var rename = require('gulp-rename');
var sh = require('shelljs');
var inject = require('gulp-inject');
var bowerFiles = require('main-bower-files');
var del = require('del');

var paths = {
  sass: ['./scss/**/*.scss'],
  templates: "./www/templates/**/*.html"
};

gulp.task('default', ['sass', 'templates', 'inject:vendor']);

gulp.task('sass', function(done) {
  gulp.src('./scss/*.scss')
    .pipe(sass())
    .on('error', sass.logError)
    .pipe(gulp.dest('./www/css/'))
    .pipe(minifyCss({
      keepSpecialComments: 0
    }))
    .pipe(rename({ extname: '.min.css' }))
    .pipe(gulp.dest('./www/css/'))
    .on('end', done);
});

gulp.task('inject:vendor', function () {
  return gulp.src(bowerFiles(), {
    base: 'bower_components'
  }).pipe(gulp.dest('./www/lib/'));
});

gulp.task('inject:sass', function () {
  return gulp.src(bowerFiles({filter:['**/*.{scss,sass}']}), {
    base: 'bower_components'
  }).pipe(gulp.dest('./www/lib/'));
});

gulp.task('serve:before', [ "default", "watch" ], function() {
});

gulp.task('watch', function() {
  gulp.watch(paths.sass, ['sass']);
  gulp.watch(paths.templates, ['templates']);
});

gulp.task('templates', function(){
  return gulp.src(paths.templates)
    .pipe(angularTemplateCache())
    .pipe(concat('templates.js'))
    .pipe(gulp.dest('./www/lib/'));
});

gulp.task('install', ['git-check'], function() {
  return bower.commands.install()
    .on('log', function(data) {
      gutil.log('bower', gutil.colors.cyan(data.id), data.message);
    });
});

gulp.task('git-check', function(done) {
  if (!sh.which('git')) {
    console.log(
      '  ' + gutil.colors.red('Git is not installed.'),
      '\n  Git, the version control system, is required to download Ionic.',
      '\n  Download git here:', gutil.colors.cyan('http://git-scm.com/downloads') + '.',
      '\n  Once git is installed, run \'' + gutil.colors.cyan('gulp install') + '\' again.'
    );
    process.exit(1);
  }
  done();
});
