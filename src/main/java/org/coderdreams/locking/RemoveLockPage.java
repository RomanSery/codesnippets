package org.coderdreams.locking;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.coderdreams.webapp.JsonResponsePage;

public class RemoveLockPage extends JsonResponsePage {
    private static final long serialVersionUID = 1L;

    @SpringBean private LockingService lockingService;

    public RemoveLockPage(final PageParameters pp) {
        super(pp);
    }

    @Override
    protected String sendResponse(PageParameters params) {

        int recordId = params.get("recordId").isEmpty() ? 0 : params.get("recordId").toInt();
        int listenerId = params.get("lid").isEmpty() ? 0 : params.get("lid").toInt();
        int userLockId = params.get("ulId").isEmpty() ? 0 : params.get("ulId").toInt();
        int userId = params.get("userId").isEmpty() ? 0 : params.get("userId").toInt();

        lockingService.removeMyLock(userLockId, recordId, userId, listenerId);
        return sendResponse("OK", "OK");
    }

}
