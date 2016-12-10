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

define([
    'jquery',
	'jqm',
    'underscore',
    'backbone',
	'router',
], function($, jqm, _, Backbone, AppRouter) {

    var applyStyles = function () {
        $('a.app-btn').addClass("ui-btn ui-btn-inline ui-corner-all ui-shadow");
    };

    return {
        initialize: function() {
            // Pass in our Router module and call it's initialize function
    		AppRouter.initialize();
            console.log("Dependencies loaded");
            applyStyles();
        },
    };
});
