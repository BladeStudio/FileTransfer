package playground.jkzhou.filetransfer.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

import playground.jkzhou.filetransfer.utils.AppUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by JK.Zhou on 2016/11/30.
 */

public class SparkStarter {

    private static final String TAG = "SparkStarter";
    private static final String PATH_SYS_BASE = Environment.getExternalStorageDirectory().toString() + "/Android/Data/";
    private static final String PATH_APP_BASE = PATH_SYS_BASE + "playground.jkzhou/webdata";
    private Context appContext;
    private String appBaseDir;

    public SparkStarter(Context appContext) {
        this.appContext = appContext;
        this.appBaseDir = appContext.getExternalCacheDir().getAbsolutePath().toString();
    }

    public void init() {

        try {
            deployWebContent();
            externalStaticFileLocation(PATH_APP_BASE);
        } catch (IOException e) {
            Log.e(TAG, "init: deployWebContent FAILED: ", e);
        }

    }

    private void displayFiles(AssetManager mgr, String path) {
        try {
            String list[] = mgr.list(path);
            if (list != null)
                for (int i = 0; i < list.length; ++i) {
                    Log.d("Assets:", path + "/" + list[i]);
                    displayFiles(mgr, path + "/" + list[i]);
                }
        } catch (IOException e) {
            Log.e("List error:", "can't list" + path);
        }
    }

    private boolean deployWebContent() throws IOException {

        AppUtils.copyAssets(appContext, "WebContent", "web");

        return true;
    }

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
