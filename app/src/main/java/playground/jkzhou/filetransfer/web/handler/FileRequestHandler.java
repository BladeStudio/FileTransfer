package playground.jkzhou.filetransfer.web.handler;

import com.google.gson.Gson;

import java.util.Arrays;

import playground.jkzhou.filetransfer.web.WebManager;
import playground.jkzhou.filetransfer.web.request.RequestType;
import spark.Request;
import spark.Response;

/**
 * Created by JK.Zhou on 2016/12/11.
 */

public class FileRequestHandler extends RouteHandler {

	public FileRequestHandler(WebManager webMgr) {
		super(RequestType.GET, "/list", webMgr);
	}

	@Override
	public Object handle(Request request, Response response) {
		notifyManager(request);
		Gson gson = new Gson();
		String msg = gson.toJson(Arrays.toString(new String[]{"abc", "def", "ghi", "jkl"}));
		return msg;
	}

}
