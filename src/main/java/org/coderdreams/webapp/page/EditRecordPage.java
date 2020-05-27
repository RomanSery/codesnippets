package org.coderdreams.webapp.page;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.protocol.ws.api.registry.IKey;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.locking.LockablePage;
import org.coderdreams.locking.LockingService;
import org.coderdreams.locking.RecordAccess;
import org.coderdreams.webapp.components.DisplayLocksPanel;


public class EditRecordPage extends WebPage implements LockablePage {

	@SpringBean private LockingService lockingService;

	private boolean isRecordLocked = false;
    private boolean showedNotification = false;
    private boolean multipleViewers = false;
	private final RecordAccess access;
	private final int recordId;
    private final String applicationName;
    private final String sessionId;
    private final IKey key;
    private final int currUserId;

    private DisplayLocksPanel displayLocksPanel;


	public EditRecordPage(final PageParameters params) {
		super(params);
        applicationName = Application.get().getName();
        Session.get().bind();
        sessionId = this.getSession().getId();
        key = new PageIdKey(this.getPageId());

        currUserId = params.get("userId").isEmpty() ? 0 : params.get("userId").toInt();
        recordId = params.get("id").isEmpty() ? 0 : params.get("id").toInt();

        if(recordId == 0 || currUserId == 0) {
        	throw new IllegalArgumentException("missing query string parameters");
		}


		WebClientInfo clientInfo = WebSession.get().getClientInfo();
		access = lockingService.getRecordLock(getRecordId(), clientInfo.getProperties().getRemoteAddress(), clientInfo.getUserAgent(), this, currUserId);
		isRecordLocked = access.isRecordLocked();
		multipleViewers = !access.isInitialViewer();

		displayLocksPanel = new DisplayLocksPanel("displayLocksPanel", access.getListenerId(), getRecordId());
		add(displayLocksPanel.setOutputMarkupId(true));

		addWebSocketBehavior(this, lockingService);

		add(new Label("heading", String.format("Editing record %d as user %d", recordId, currUserId)));
		add(new Label("lockMsg", access.isRecordLocked() ? "Record Locked" : "Record Un-locked"));
	}


	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		renderHeadLockingScripts(response);
	}

	@Override public RecordAccess getRecordAccess() { return access; }
	@Override public int getRecordId() { return recordId; }
	@Override public int getUserId() { return currUserId; }
	@Override public String applicationName() { return applicationName; }
	@Override public String sessionId() { return sessionId; }
	@Override public IKey key() { return key; }
	@Override public boolean showedNotification() { return showedNotification; }
	@Override public void setShowedNotification(boolean showedNotification) { this.showedNotification = showedNotification; }
	@Override public boolean isMultipleViewers() { return multipleViewers; }
	@Override public void setMultipleViewers(boolean multipleViewers) { this.multipleViewers = multipleViewers; }
	@Override public Panel displayLocksPanel() { return displayLocksPanel; }
}