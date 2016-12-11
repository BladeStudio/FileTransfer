package playground.jkzhou.filetransfer.server;

import android.util.Log;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by JK.Zhou on 2016/11/30.
 */

public class SparkStarter {

    public static final String WEB_BASE_DIR = "WebContent";

    private static final String TAG = "SparkStarter";
    private boolean mStarted;

    private SparkStarter() {
    }

    public static SparkStarter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setStaticFileLocation(String absPath) {
        Spark.externalStaticFileLocation(absPath);
    }

    private Runnable getHandler() {
        return new Runnable() {
            @Override
            public void run() {

                Spark.get(new Route("/") {
                    @Override
                    public Object handle(Request request, Response response) throws Exception {
                        response.redirect("app.html");
                        return null;
                    }
                });

                Spark.get(new Route("/hello") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "Hello World!";
                    }
                });

                Spark.post(new Route("/hello") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "Hello World: " + request.body();
                    }
                });

                Spark.get(new Route("/private") {
                    @Override
                    public Object handle(Request request, Response response) {
                        response.status(401);
                        return "Go Away!!!";
                    }
                });

                Spark.get(new Route("/users/:name") {
                    @Override
                    public Object handle(Request request, Response response) {
                        return "Selected user: " + request.params(":name");
                    }
                });

                Spark.get(new Route("/news/:section") {
                    @Override
                    public Object handle(Request request, Response response) {
                        response.type("text/xml");
                        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
                    }
                });

                Spark.get(new Route("/protected") {
                    @Override
                    public Object handle(Request request, Response response) {
                        halt(403, "I don't think so!!!");
                        return null;
                    }
                });

                Spark.get(new Route("/redirect") {
                    @Override
                    public Object handle(Request request, Response response) {
                        response.redirect("/news/world");
                        return null;
                    }
                });

            }
        };
    }

    public void stop() {
        Spark.stop();
        mStarted = false;
    }

    public void start() {
        if (!mStarted) {
            new Thread(getHandler()).start();
            mStarted = true;
        } else
            Log.e(TAG, "start: server already started");
    }

    private static class SingletonHolder {
        private static final SparkStarter INSTANCE = new SparkStarter();
    }

}
