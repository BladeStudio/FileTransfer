package playground.jkzhou.filetransfer.controller;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import playground.jkzhou.filetransfer.R;

import android.text.format.Formatter;

/**
 * Created by @author J.K. Zhou <zhoujk@mcmaster.com> on Date 2017/1/22.
 */

public class FileListAdapter extends ArrayAdapter<Uri> {

	public FileListAdapter(Context context, int resource, List<Uri> data) {
		super(context, resource, data);
	}

	public void refresh(List<Uri> newData) {
		clear();
		addAll(newData);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get data for this position
		Uri uri = getItem(position);
		Context context = getContext();

		if (convertView == null) {
			// Check if an existing view is being reused, otherwise inflate the view
			convertView = LayoutInflater.from(context).inflate(R.layout.file_list_item, parent, false);
		}

		// Look up views
		TextView nameView = (TextView) convertView.findViewById(R.id.file_name);
		TextView descView = (TextView) convertView.findViewById(R.id.file_desc);

		// Populate data to views
		DocumentFile doc = DocumentFile.fromSingleUri(context, uri);
		String fileName = doc.getName();
		String fileDesc = "Type:" + doc.getType() + " Size:" + Formatter.formatFileSize(context, doc.length());

		nameView.setText(fileName);
		descView.setText(fileDesc);

		return convertView;
	}

}
