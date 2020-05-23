package org.coderdreams.locking;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.coderdreams.service.UserService;
import org.coderdreams.webapp.JsonResponsePage;

import com.github.openjson.JSONException;
import com.github.openjson.JSONObject;

public class RemoveLockPage extends JsonResponsePage {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private UserService userService;
    @SpringBean
    private LockingService lockingService;

    public RemoveLockPage(final PageParameters pp) {
        super(pp);
    }

    @Override
    protected String sendResponse(PageParameters pp) {

        StringValue objIdPP = pp.get("objid");
        StringValue listenerIdPP = pp.get("lid");
        StringValue userLockIdPP = pp.get("ulId");
        if (objIdPP == null || objIdPP.isEmpty() || userLockIdPP == null || userLockIdPP.isEmpty()) {
            return getSuccessResponse();
        }

        int objId = objIdPP.toInt();
        int listenerId = listenerIdPP.toInt();
        int userLockId = userLockIdPP.toInt();


        lockingService.removeMyLock(userLockId, objId, userService.getCurrUserId(), listenerId);

        return getSuccessResponse();
    }


    private String getSuccessResponse() {
        JSONObject json = new JSONObject();
        try {
            json.put("success", 1);
        } catch (JSONException e) {
        }
        return json.toString();
    }

}
