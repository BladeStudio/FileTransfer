package playground.jkzhou.filetransfer.files;

/**
 * Created by @author J.K. Zhou <zhoujk@mcmaster.com> on Date 2017/1/22.
 */

public interface ISubscribable<T> {

	void addSubscriber(T subscriber);
}
