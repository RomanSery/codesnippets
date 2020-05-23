package org.coderdreams.locking;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;


public abstract class LockingWebSocketBehavior extends WebSocketBehavior {
    private static final long serialVersionUID = 1L;

    private final LockingService lockingService;
    private final int lockObjId;
    private final int currUserId;
    private final RecordAccess access;

    public LockingWebSocketBehavior(LockingService lockingService, int lockObjId, int currUserId, RecordAccess access) {
        this.lockingService = lockingService;
        this.lockObjId = lockObjId;
        this.currUserId = currUserId;
        this.access = access;
    }

    protected abstract void updateDisplayLocksPanel(WebSocketRequestHandler handler);

    @Override
    protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
        super.onPush(handler, message);
        if(message instanceof WebSocketMsg) {
            WebSocketMsg m = (WebSocketMsg) message;
            showGritterFeedback(handler, m.getTitle(), m.getMsg());
            updateDisplayLocksPanel(handler);
        } else if(message instanceof UpdateDisplayLocksMsg) {
            updateDisplayLocksPanel(handler);
        }
    }

    @Override
    protected void onConnect(ConnectedMessage msg) {
        super.onConnect(msg);
        lockingService.publishToChannel(lockObjId, currUserId);
    }

    @Override
    protected void onClose(ClosedMessage message) {
        super.onClose(message);
        lockingService.removeMyLock(access.getUserLockId(), lockObjId, currUserId, access.getListenerId());
    }


    private void showGritterFeedback(WebSocketRequestHandler handler, String title, String messageText) {
        handler.appendJavaScript("showGritterFeedback('" + title + "', '" + StringEscapeUtils.escapeHtml4(messageText).replace("'", "\\'") +"');");
    }
}
