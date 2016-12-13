package playground.jkzhou.filetransfer.message.handler;

import android.os.Handler;
import android.os.Message;

import playground.jkzhou.filetransfer.message.MessageReceiver;
import playground.jkzhou.filetransfer.message.MessageSender;
import playground.jkzhou.filetransfer.message.MessageType;

/**
 * Created by JK.Zhou on 2016/12/12.
 */

public class UIMessageHandler extends Handler implements MessageSender {

	private MessageReceiver receiver;

	public UIMessageHandler() {
	}

	@Override
	public void handleMessage(Message msg) {
		receiver.receive("TypeCode:" + msg.what + ", Content:" + msg.obj);
	}

	@Override
	public void send(Object message) {
		sendMessage(obtainMessage(MessageType.RequestNotify, message));
	}

	public void setReceiver(MessageReceiver receiver) {
		this.receiver = receiver;
	}
}
