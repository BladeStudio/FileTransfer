package playground.jkzhou.filetransfer.server;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

import playground.jkzhou.filetransfer.server.handler.WebHandler;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by JK.Zhou on 2016/11/29.
 */

public class ServerStarter {

    private static final String TAG = "ServerStarter";

    private final Context appContext;
    private final View mainUI;
    private final Server server;
    private Runnable postStart;

    public ServerStarter(Context context, View view) {
        this.appContext = context;
        this.mainUI = view;
        this.server = new Server(8080);

//        final ServerConnector connector = new ServerConnector(server);
//        connector.setPort(8080);
//        server.setConnectors(new Connector[]{connector});

        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath("/hello");
        contextHandler.setResourceBase(".");
        contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
        //contextHandler.setVirtualHosts(new String[] { "helloworld" });
        contextHandler.setHandler(new WebHandler());

        server.setHandler(contextHandler);
    }

    public void setPostStartAction(Runnable action) {
        this.postStart = action;
    }

    public String getHostAddress() {
        WifiManager wifiMgr = (WifiManager) appContext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        return Formatter.formatIpAddress(wifiInfo.getIpAddress());
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String ip = getHostAddress();

                try {
                    Log.d(TAG, "run: Server about to start");
                    server.start();
                    Log.d(TAG, "run: Server has started: " + ip);

                    //server.dumpStdErr();
                    if (postStart != null) mainUI.post(postStart);

                    server.join();
                    Log.d(TAG, "run: Server has joined");
                } catch (Exception e) {
                    Log.e(TAG, "run: Server has error", e);
                }
            }
        }).start();
    }
}
