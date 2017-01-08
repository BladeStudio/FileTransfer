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
    'backbone'
], function(Backbone) {

    var HeaderView = Backbone.View.extend({

        el: $("#appHeader"),

        render: function() {
            this.$el.html("<h1>File Transfer App</h1>");
        }

    });

    return HeaderView;

});
