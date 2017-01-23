package playground.jkzhou.filetransfer.files;

import java.util.List;

/**
 * Created by @author J.K. Zhou <zhoujk@mcmaster.com> on Date 2017/1/22.
 */

public interface IPublisher <T> extends IProvider<List<T>>, ISubscribable<ISubscriber> {

	void publish(T content);
}
