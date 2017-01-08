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
  'underscore',
  'backbone',
  'models/BaseModel',
  'views/HeaderView',
  'views/FooterView',
  'views/MainView'
], function($, _, Backbone, BaseModel, HeaderView, FooterView, MainView) {

  var AppRouter = Backbone.Router.extend({
    routes: {
      // Define some URL routes
      'home': 'defaultAction',
      // Default
      '*actions': 'defaultAction'
    }
  });

  var initialize = function(){

    var app_router = new AppRouter;

    app_router.on('route:defaultAction', function (actions) {
        /* We have no matching route, lets display the home page */

        /* Display page header */
        var headerView = new HeaderView(new BaseModel());
        headerView.render();

        /* Display main content */
        var mainView = new MainView(new BaseModel());
        mainView.render();

        var footerView = new FooterView(new BaseModel());
        footerView.render();
    });

    Backbone.history.start();
  };

  return {
    initialize: initialize
  };
});
