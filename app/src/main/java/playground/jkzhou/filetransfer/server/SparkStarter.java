package playground.jkzhou.filetransfer.server;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import spark.Request;
import spark.Response;
import spark.Route;
import playground.jkzhou.filetransfer.utils.AppUtils;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by JK.Zhou on 2016/11/30.
 */

public class SparkStarter {

    public static final String WEB_BASE_DIR = "WebContent";

    private static final String TAG = "SparkStarter";
    private Context appContext;

    public SparkStarter(Context appContext) {
        this.appContext = appContext;
    }

    public void init() {
        Log.d(TAG, "init: Host IP Address:" + AppUtils.getDeviceWifiIP(appContext));

        try {
            AppUtils.listAssets(appContext, WEB_BASE_DIR);
            deployWebContent();
        } catch (IOException e) {
            Log.e(TAG, "init: deployWebContent FAILED: ", e);
        }

    }

    private boolean deployWebContent() throws IOException {

        AppUtils.copyAssets(appContext, WEB_BASE_DIR, WEB_BASE_DIR);

        externalStaticFileLocation(AppUtils.getCacheDir(appContext) + File.separator + WEB_BASE_DIR);

        return true;
    }

    private Runnable getHandler() {
        return new Runnable() {
            @Override
            public void run() {

                AppUtils.acquireHostInfo();

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

            }
        };
    }

    public void start() {

        new Thread(getHandler()).start();

    }

}
