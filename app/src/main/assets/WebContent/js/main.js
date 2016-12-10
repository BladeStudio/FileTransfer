/*
 * BUG: Accessing file less than 1024 bytes will cause HTTP 500 error code. The reason
 *      is that Jetty 9.x uses JDK 1.7 and some of the classes in Java 7 are not avai-
 *      -liable in Android, in this case for example, java.nio.file.Path is missing,
 *      and method such as java.io.File.toPath() are not supported either. For now, we
 *      manually add place holder texts to files that are less than 1KB. In future may
 *      consider switch to Jetty 8.x + Spring MVC 4.x, which only requires JDK 1.6.
 *
 *      PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT
 *      PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT
 *      PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT
 *      PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT
 *      PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT
 *      PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT PLACE HOLDER TEXT
 */

require.config({
    paths: {
        jquery: 'libs/jquery-2.2.4.min',
        jqm: 'libs/jquery.mobile-1.4.5.min',
        underscore: 'libs/underscore-min',
        backbone: 'libs/backbone-min',
        app: 'app/App',
        router: 'app/AppRouter'
    }

});

require([
    'app',

], function(App) {
    // The "app" dependency is passed in as "App"
    // Again, the other dependencies passed in are not "AMD" therefore don't pass a parameter to this function
    App.initialize();
});
