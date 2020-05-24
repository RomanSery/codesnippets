package org.coderdreams.locking;


import org.apache.wicket.Application;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.registry.IKey;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.coderdreams.locking.msg.LockPublishMsg;
import org.coderdreams.locking.msg.UpdateDisplayLocksMsg;
import org.coderdreams.locking.msg.WebSocketMsg;
import org.coderdreams.util.Utils;
import org.redisson.api.listener.MessageListener;

public interface LockablePage extends MessageListener<LockPublishMsg> {

    RecordAccess getRecordAccess();
    int getRecordId();
    int getUserId();

    String applicationName();
    String sessionId();
    IKey key();
    boolean showedNotification();
    void setShowedNotification(boolean showedNotification);
    boolean isMultipleViewers();
    void setMultipleViewers(boolean multipleViewers);

    default void renderHeadLockingScripts(IHeaderResponse response) {

        Url url = RequestCycle.get().mapUrlFor(RemoveLockPage.class, new PageParameters()
                .add("recordId", getRecordId())
                .add("userId", getUserId())
                .add("ulId", getRecordAccess().getUserLockId())
                .add("lid", getRecordAccess().getListenerId()));

        String baseUrl = Utils.getVariable("BASE_URL");
        String urlStr = baseUrl + url;
        response.render(JavaScriptHeaderItem.forScript("remove_lock_page = '"+ urlStr +"'; ", "remove_lock_page"));

        response.render(OnDomReadyHeaderItem.forScript("window.onbeforeunload = function (e) { " +
                " Wicket.WebSocket.close(); " +
                " var requestXhr = new XMLHttpRequest(); " +
                "requestXhr.open('GET', remove_lock_page, false); " +
                "requestXhr.send(null); " +
                " }"));
    }


    default void onMessage(CharSequence channel, LockPublishMsg msg) {
        Application application = Application.get(applicationName());
        WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
        IWebSocketConnectionRegistry webSocketConnectionRegistry = webSocketSettings.getConnectionRegistry();
        IWebSocketConnection connection = webSocketConnectionRegistry.getConnection(application, sessionId(), key());

        if(connection != null && connection.isOpen()) {

            boolean isLocked = msg.getNumLocks() > 1;
            boolean origVal = isMultipleViewers();
            boolean isChanged = isMultipleViewers() != isLocked;
            if(isChanged) {
                setMultipleViewers(isLocked);
                if(getRecordAccess().isInitialViewer()) {
                    if(!showedNotification()) {
                        connection.sendMessage(new WebSocketMsg("Collaborative Editing", "Another user has started viewing this record.  Finish and save your changes and leave the page to allow other users to make changes."));
                        setShowedNotification(true);
                    }
                }

                if(origVal == true && !isMultipleViewers()) {
                    connection.sendMessage(new WebSocketMsg("Single-user Editing", "You are now the only user.  Reload the record to start single-user editing."));
                }
            } else {
                connection.sendMessage(new UpdateDisplayLocksMsg());
            }
        }
    }

}
