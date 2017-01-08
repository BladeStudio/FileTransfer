package playground.jkzhou.filetransfer.files.scanner;

import java.util.List;

/**
 * Created by JK.Zhou on 2016/12/17.
 */

public interface ResourceScanner {

	List<String> scan(String path);

	List<String> scanAll();

	List<String> scanShareDir();
}
