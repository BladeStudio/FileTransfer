package playground.jkzhou.filetransfer.web.handler;

import playground.jkzhou.filetransfer.web.WebManager;
import playground.jkzhou.filetransfer.web.request.RequestType;
import spark.Request;
import spark.Response;

/**
 * Created by JK.Zhou on 2016/12/11.
 */

public abstract class RouteHandler {

	private RequestType type;
	private String url;
	private WebManager webMgr;

	protected RouteHandler(RequestType type, String url) {
		this(type, url, null);
	}

	RouteHandler(RequestType type, String url, WebManager webMgr) {
		this.type = type;
		this.url = url;
		this.webMgr = webMgr;
	}

	public abstract Object handle(Request request, Response response);

	public RequestType getType() {
		return type;
	}

	public String getUrl() {

		return url;
	}

	@Override
	public int hashCode() {
		int result = url.hashCode();
		result = 31 * result + type.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RouteHandler)) return false;

		RouteHandler that = (RouteHandler) o;

		return url.equals(that.url) && type == that.type;
	}

	public void register() {
		webMgr.addRoute(this);
	}

	void notifyManager(Request newRequest) {
		webMgr.notifyRequest(newRequest);
	}

}
