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

    var MainView = Backbone.View.extend({

        template: _.template(
            '<p id="msg"></p>' +
            '<a id="scan" href="#" class="app-btn ui-icon-grid ui-btn-icon-left" >Scan</a>' +
            '<a href="#" class="app-btn ui-icon-home ui-btn-icon-left">Button 3</a>'
        ),

        el: $("#appMain"),

        scanRes : function() {
            $.ajax({
                url: "/list",
                dataType: "json",
                success: function(data) {
                    console.log("scanRes: " + JSON.stringify(data));
                }
            });
        },

        render: function() {
            this.$el.html(this.template());
            this.$("#scan").on("click", this.scanRes);
        }

    });

    return MainView;

});
