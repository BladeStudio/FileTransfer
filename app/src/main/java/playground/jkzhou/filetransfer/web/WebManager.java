package playground.jkzhou.filetransfer.web;

import android.util.Log;
import android.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import playground.jkzhou.filetransfer.message.MessageSender;
import playground.jkzhou.filetransfer.web.handler.RouteHandler;
import playground.jkzhou.filetransfer.web.request.RequestInfo;
import playground.jkzhou.filetransfer.web.request.RequestType;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by JK.Zhou on 2016/12/11.
 */

public class WebManager {
	public static final String WEB_BASE_DIR = "WebContent";
	private static final String TAG = "WebManager";
	private static final String URL_BASE_ROUTE = "/";
	private static final String URL_TEST_ROUTE = "/test";

	private static String staticFilesPath;

	private Map<Pair<RequestType, String>, Route> activeRoutesMap = new HashMap<>();
	private Set<RouteHandler> cachedRoutesSet = new HashSet<>();
	private boolean started = false;
	private MessageSender uiMessenger;

	/* Hide default constructor, use getInstance instead */
	private WebManager() {
		addRoute(createBaseRoute());
		addRoute(createTestRoute());
	}

	public static WebManager getInstance() {
		return WebManagerHolder.INSTANCE;
	}

	public void addRoute(RouteHandler handler) {
		if (!validate(handler))
			return;
		cachedRoutesSet.add(handler);
	}

	public void deleteRoute(RouteHandler handler) {
		if (handler != null && handler.getUrl() != null && handler.getType() != null) {
			cachedRoutesSet.remove(handler);
			unregisterRoute(handler);
		}
	}

	public void notifyRequest(Request req) {
		String info = new RequestInfo(req).toString();
		Log.d(TAG, "notifyRequest: " + info);
		uiMessenger.send(info);
	}

	public void registerRoute(final RouteHandler handler) {
		if (!validate(handler))
			return;

		register(handler);
	}

	public void setStaticFilesPath(String path) {
		if (staticFilesPath == null) {
			if (path == null || path.isEmpty())
				throw new IllegalArgumentException("Static files location cannot empty");
			staticFilesPath = path;
			Spark.externalStaticFileLocation(path);
		} else
			Log.e(TAG, "setStaticFilesPath: Static file location has already been set");
	}

	public void setUiMessenger(MessageSender uiMessenger) {
		this.uiMessenger = uiMessenger;
	}

	public void startServer() {
		if (!started) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					registerRoutes();
				}
			}, getClass().getSimpleName() + "-Thread").start();
			started = true;
		} else
			Log.e(TAG, "startServer: server already started");
	}

	public void stopServer() {
		Spark.stop();
		activeRoutesMap.clear();
		started = false;
	}

	private RouteHandler createBaseRoute() {
		return new RouteHandler(RequestType.GET, URL_BASE_ROUTE) {
			@Override
			public Object handle(Request request, Response response) {
				response.redirect("app.html");
				return null;
			}
		};
	}

	private RouteHandler createTestRoute() {
		return new RouteHandler(RequestType.GET, URL_TEST_ROUTE) {
			@Override
			public Object handle(Request request, Response response) {
				return "Hello World!";
			}
		};
	}

	private void register(final RouteHandler handler) {
		Route route = new Route(handler.getUrl()) {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return handler.handle(request, response);
			}
		};

		if (RequestType.GET == handler.getType())
			Spark.get(route);
		else if (RequestType.POST == handler.getType())
			Spark.post(route);
		else {
			Log.e(TAG, "register: unsupported request type " + handler.getType());
			return;
		}
		activeRoutesMap.put(new Pair<>(handler.getType(), handler.getUrl()), route);
	}

	private void registerRoutes() {
		for (RouteHandler handler : cachedRoutesSet) register(handler);
	}

	private void unregisterRoute(RouteHandler handler) {
		Pair<RequestType, String> routeInfo = new Pair<>(handler.getType(), handler.getUrl());
		Route route = activeRoutesMap.get(routeInfo);
		if (route != null) {
			Spark.delete(route);
			activeRoutesMap.remove(routeInfo);
		}
	}

	private boolean validate(RouteHandler handler) {
		if (handler == null || handler.getUrl() == null || handler.getUrl().isEmpty() || handler.getType() == null)
			throw new IllegalArgumentException("Invalid route handler");

		if (cachedRoutesSet.contains(handler))
			throw new IllegalArgumentException("Route handler for " + handler.getType() + ":" + handler.getUrl() + " already exists");

		return true;
	}

	private static class WebManagerHolder {
		private static final WebManager INSTANCE = new WebManager();
	}
}
