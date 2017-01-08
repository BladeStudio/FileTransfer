package playground.jkzhou.filetransfer.files;

import java.io.InputStream;

/**
 * Created by JK.Zhou on 2016/12/17.
 */

public interface ResourceReader {

	InputStream read(String url);

}
