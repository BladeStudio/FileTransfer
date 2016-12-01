package playground.jkzhou.filetransfer.server;

import static spark.Spark.*;

import spark.*;

/**
 * Created by JK.Zhou on 2016/11/30.
 */

public class SparkStarter {

    private Runnable getHandler() {
        return new Runnable() {
            @Override
            public void run() {
                get(new Route("/hello") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "Hello World!";
                    }
                });

                post(new Route("/hello") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "Hello World: " + request.body();
                    }
                });

                get(new Route("/private") {
                    @Override
                    public Object handle(Request request, Response response) {
                        response.status(401);
                        return "Go Away!!!";
                    }
                });

                get(new Route("/users/:name") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "Selected user: " + request.params(":name");
                    }
                });

                get(new Route("/news/:section") {
                    @Override
                    public Object handle(Request request, Response response) {
                        response.type("text/xml");
                        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
                    }
                });

                get(new Route("/protected") {
                    @Override
                    public Object handle(Request request, Response response) {
                        halt(403, "I don't think so!!!");
                        return null;
                    }
                });

                get(new Route("/redirect") {
                    @Override
                    public Object handle(Request request, Response response) {
                        response.redirect("/news/world");
                        return null;
                    }
                });

                get(new Route("/") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "root";
                    }
                });
            }
        };
    }

    public void start() {

        new Thread(getHandler()).start();

    }

}
