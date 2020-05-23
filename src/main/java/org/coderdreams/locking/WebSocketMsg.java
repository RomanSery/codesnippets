package org.coderdreams.locking;

import java.io.Serializable;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

public class WebSocketMsg implements Serializable, IWebSocketPushMessage {

	private static final long serialVersionUID = 1L;
	private final String title;
	private final String msg;

	public WebSocketMsg(String title, String msg) {
		this.title = title;
		this.msg = msg;
	}	

	public String getMsg() {
		return msg;
	}

	public String getTitle() {
		return title;
	}
	
}
