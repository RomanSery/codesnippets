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

public interface LockablePage {

    RecordAccess getRecordAccess();
    int getLockObjId();

    String applicationName();
    String sessionId();
    IKey key();
    boolean isShowedCollabortiveNotification();
    void setShowedCollabortiveNotification(boolean showedCollabortiveNotification);
    boolean isMultipleViewers();
    void setMultipleViewers(boolean multipleViewers);
    String getReloadUrl();

    default void renderHeadLockingScripts(IHeaderResponse response) {

        Url url = RequestCycle.get().mapUrlFor(RemoveLockPage.class, new PageParameters()
                .add("objid", getLockObjId())
                .add("ulId", getRecordAccess().getUserLockId())
                .add("lid", getRecordAccess().getListenerId()));

        response.render(JavaScriptHeaderItem.forScript("CD.remove_lock_page = '"+ url +"'; ", "remove_lock_page"));

        response.render(OnDomReadyHeaderItem.forScript("window.onbeforeunload = function (e) { " +
                " Wicket.WebSocket.close(); " +
                " var requestXhr = new XMLHttpRequest(); " +
                "requestXhr.open('GET', CD.remove_lock_page, false); " +
                "requestXhr.send(null); " +
                " }"));
    }


    default void onLockMessage(CharSequence channel, LockPublishMsg msg) {

        Application application = Application.get(applicationName());
        WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
        IWebSocketConnectionRegistry webSocketConnectionRegistry = webSocketSettings.getConnectionRegistry();
        IWebSocketConnection connection = webSocketConnectionRegistry.getConnection(application, sessionId(), key());

        if(connection != null && connection.isOpen()) {

            boolean isCollab = msg.getNumLocks() > 1;
            boolean origVal = isMultipleViewers();
            boolean isChanged = isMultipleViewers() != isCollab;
            if(isChanged) {
                setMultipleViewers(isCollab);
                if(getRecordAccess().isInitalViewer()) {
                    if(!isShowedCollabortiveNotification()) {
                        connection.sendMessage(new WebSocketMsg("Collaborative Editing", "Another user has started viewing this record.  Finish and save your changes and leave the page to allow other users to make changes."));
                        setShowedCollabortiveNotification(true);
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
