package playground.jkzhou.filetransfer.files;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by @author J.K. Zhou <zhoujk@mcmaster.com> on Date 2017/1/22.
 */

public class ContentManager implements IPublisher<Uri> {

	private static final String TAG = "ContentManager";
	private Context context;
	private List<ISubscriber> subscribers = new ArrayList<>();
	private Map<String, Uri> uriMap = new HashMap<>();

	public ContentManager(Context context) {
		this.context = context;
	}

	@Override
	public void addSubscriber(ISubscriber subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public List<Uri> get() {
		return new ArrayList<>(uriMap.values());
	}

	@Override
	public void publish(Uri newContent) {
		printInfo(newContent);
		uriMap.put(newContent.getPath(), newContent);
		for (ISubscriber s : subscribers) {
			s.notifyUpdate();
		}
	}

	private void printInfo(Uri uri) {
		DocumentFile doc = DocumentFile.fromSingleUri(context, uri);
		String info = "\n==================== URI ====================";
		info += "\nUri = " + uri;
		info += "\nAuthority: " + uri.getAuthority();
		info += "\nPath: " + uri.getPath();
		info += "\nQuery: " + uri.getQuery();
		info += "\n==================== Doc ====================";
		info += "\nName: " + doc.getName();
		info += "\nType: " + doc.getType();
		info += "\nexists: " + doc.exists();
		info += "\nisFile: " + doc.isFile();
		info += "\ncanRead: " + doc.canRead();
		info += "\ncanWrite: " + doc.canWrite();
		info += "\nlength: " + doc.length();

		Log.i(TAG, "Publishing:\n" + info);
	}
}
