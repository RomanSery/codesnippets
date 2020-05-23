package org.coderdreams.webapp.page;

import org.apache.wicket.Application;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.registry.IKey;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.locking.LockablePage;
import org.coderdreams.locking.LockingService;
import org.coderdreams.locking.LockingWebSocketBehavior;
import org.coderdreams.locking.RecordAccess;
import org.coderdreams.locking.msg.LockPublishMsg;
import org.coderdreams.service.UserService;
import org.coderdreams.webapp.components.DisplayLocksPanel;
import org.redisson.api.listener.MessageListener;


public class EditRecordPage extends WebPage implements MessageListener<LockPublishMsg>, LockablePage {

	@SpringBean private UserService userService;
	@SpringBean private LockingService lockingService;

	protected boolean isRecordLocked = false;
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
        sessionId = this.getSession().getId();
        key = new PageIdKey(this.getPageId());
        currUserId = userService.getCurrUserId();

        recordId = params.get("id").isEmpty() ? 0 : params.get("id").toInt();

		WebClientInfo clientInfo = WebSession.get().getClientInfo();
		access = lockingService.getAccessToRecord(getLockObjId(), clientInfo.getProperties().getRemoteAddress(), clientInfo.getUserAgent(), this);
		isRecordLocked = access.isRecordLocked();
		multipleViewers = !access.isInitialViewer();

		buildForm(params);
	}


	private void buildForm(final PageParameters params) {

		displayLocksPanel = new DisplayLocksPanel("displayLocksPanel", access.getListenerId(), getLockObjId());
		add(displayLocksPanel.setOutputMarkupId(true));

		add(new LockingWebSocketBehavior(lockingService, getLockObjId(), currUserId, getRecordAccess()) {
			private static final long serialVersionUID = 1L;
			@Override protected void updateDisplayLocksPanel(WebSocketRequestHandler handler) { handler.add(displayLocksPanel); }
		});
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);

//		if (event.getPayload() instanceof GritterEvent) {
//			GritterEvent ev = ((GritterEvent)event.getPayload());
//			showGritterFeedback(ev.getAjaxRequestTarget(), ev.getTitle(), ev.getMsg());
//			return;
//		}

	}	

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);


		renderHeadLockingScripts(response);
	}



	@Override public RecordAccess getRecordAccess() { return access; }
	@Override public int getLockObjId() { return recordId; }
	@Override public String applicationName() { return applicationName; }
	@Override public String sessionId() { return sessionId; }
	@Override public IKey key() { return key; }
	@Override public boolean showedNotification() { return showedNotification; }
	@Override public void setShowedNotification(boolean showedNotification) { this.showedNotification = showedNotification; }
	@Override public boolean isMultipleViewers() { return multipleViewers; }
	@Override public void setMultipleViewers(boolean multipleViewers) { this.multipleViewers = multipleViewers; }

	@Override
	public void onMessage(CharSequence channel, LockPublishMsg msg) {
		onLockMessage(channel, msg);
	}

}