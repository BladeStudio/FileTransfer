package playground.jkzhou.filetransfer.web.request;

import spark.Request;

/**
 * Created by JK.Zhou on 2016/12/12.
 */

public class RequestInfo {
	private String clientIP;
	private int clientPort;
	private String contentType;
	private String path;
	private String requestType;
	private String url;

	public RequestInfo(Request request) {
		url = request.url();
		clientIP = request.ip();
		clientPort = request.port();
		path = request.pathInfo();
		contentType = request.contentType();
		requestType = request.requestMethod();
	}

	public String getClientIP() {
		return clientIP;
	}

	public int getClientPort() {
		return clientPort;
	}

	public String getContentType() {
		return contentType;
	}

	public String getPath() {
		return path;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "RequestInfo{" +
				"url='" + url + '\'' +
				", clientIP='" + clientIP + '\'' +
				", clientPort=" + clientPort +
				", contentType='" + contentType + '\'' +
				", requestType='" + requestType + '\'' +
				", path='" + path + '\'' +
				'}';
	}
}
