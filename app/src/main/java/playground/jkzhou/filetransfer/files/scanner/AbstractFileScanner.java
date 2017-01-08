package playground.jkzhou.filetransfer.files.scanner;

import org.apache.commons.io.DirectoryWalker;

import java.util.List;

/**
 * Created by JK.Zhou on 2016/12/17.
 */

public class AbstractFileScanner extends DirectoryWalker implements ResourceScanner {
	@Override
	public List<String> scan(String path) {
		return null;
	}

	@Override
	public List<String> scanAll() {
		return null;
	}

	@Override
	public List<String> scanShareDir() {
		return null;
	}
}
