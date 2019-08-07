package org.coderdreams.service;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.IRequestLogger.RequestData;
import org.apache.wicket.protocol.http.IRequestLogger.SessionData;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Bytes;
import org.coderdreams.util.CustomRequestLogger;
import org.coderdreams.util.Utils;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendErrorEmail(RequestCycle cycle, Exception ex, IRequestLogger logger) {
        try {
            String customer = Utils.getCustomer();
            Url url = cycle.getRequest().getUrl();
            Url clientUrl = cycle.getRequest().getClientUrl();
            String exStr = ExceptionUtils.getStackTrace(ex);
            String fullUrl = cycle.getUrlRenderer().renderFullUrl(url);
            int currUserId = 0;

            RequestData currRd = logger.getCurrentRequest();

            String currSessId = Session.get().getId();
            SessionData sd = null;
            if(currSessId != null) {
                SessionData[] sessions = logger.getLiveSessions();
                for(SessionData s : sessions) {
                    if(s.getSessionId().equals(currSessId)) {
                        sd = s;
                        break;
                    }
                }
            }

            String currReq = ((CustomRequestLogger)logger).createRequestData(currRd, sd);


            StringBuilder currSessStr = new StringBuilder();
            if(sd != null) {
                currSessStr.append("id:").append(sd.getSessionId()).append('\n');
                currSessStr.append("requestCount:").append(sd.getNumberOfRequests()).append('\n');
                currSessStr.append("requestsTime:").append(sd.getTotalTimeTaken()).append('\n');
                currSessStr.append("sessionSize:").append(Bytes.bytes(sd.getSessionSize())).append('\n');

                currSessStr.append("sessionInfo:").append(sd.getSessionInfo()).append('\n');
                currSessStr.append("startDate:").append(sd.getStartDate()).append('\n');
                currSessStr.append("lastRequestTime:").append(sd.getLastActive()).append('\n');

                currSessStr.append("numberOfRequests:").append(sd.getNumberOfRequests()).append('\n');
                currSessStr.append("totalTimeTaken:").append(sd.getTotalTimeTaken()).append("\n\n\n");
            }

            currSessStr.append("Requests: \n");
            ((CustomRequestLogger)logger).getRequests(currSessStr);


            String template = "param1: %s \n url: %s \n client url: %s \n user: %s \n\n "
                    + "curr req: %s \n\n\n curr session: %s \n\n\n stack trace: %s";

            String body = String.format(template, customer, fullUrl, clientUrl.toString(), currUserId, currReq, currSessStr.toString(), exStr);

            String subject = customer + " error";
            //TODO send email here
        } catch (Exception e) {
            //ignore
        }
    }
}
