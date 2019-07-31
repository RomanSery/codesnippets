package org.romansery.util;

import java.util.Iterator;
import java.util.Queue;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.AbstractRequestLogger;
import org.apache.wicket.request.ILoggableRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

import com.google.common.collect.EvictingQueue;

/**
 * Logger that keeps track of the past N request to include them in error logs to help reproduce errors.
 */
public class CustomRequestLogger extends AbstractRequestLogger {

	@Override
	protected void log(RequestData rd, SessionData sd) {		
		if(shouldSkip(rd)) {
			return;
		}
		EvictingQueue<String> q = (EvictingQueue<String>) Session.get().getAttribute("request_log");
		if (q == null) {
			q = EvictingQueue.create(getRequestsWindowSize());
		}
		String str = createRequestData(rd, sd);		
		q.add(str);
		Session.get().setAttribute("request_log", q);
	}
	
	private boolean shouldSkip(RequestData rd) {
		String url = rd.getRequestedUrl();
		if(url.contains("TasksNotificationsCountPage") || url.contains("LocksUpdatePage") || url.contains("displayLocksPanel")) {
			return true;
		}
		return false;
	}

	public void getRequests(StringBuilder sb) {
		Queue<String> q = (EvictingQueue<String>) Session.get().getAttribute("request_log");
		if (q != null) {
			synchronized (q) {
				Iterator<String> i = q.iterator(); 													
				while (i.hasNext()) {
					sb.append(i.next()).append('\n');
				}
			}
		}
	}

	public String createRequestData(RequestData rd, SessionData sd) {
		AppendingStringBuffer sb = new AppendingStringBuffer(150);

		sb.append("startTime=\"");
		sb.append(formatDate(rd.getStartDate()));
		sb.append("\",duration=");
		sb.append(rd.getTimeTaken());
		sb.append(",url=\"");
		sb.append(rd.getRequestedUrl());
		sb.append('"');
		sb.append(",event={");
		sb.append(getRequestHandlerString(rd.getEventTarget()));
		sb.append("},response={");
		sb.append(getRequestHandlerString(rd.getResponseTarget()));
		sb.append("},sessionid=\"");
		sb.append(rd.getSessionId());
		sb.append('"');
		sb.append(",sessionsize=");
		sb.append(rd.getSessionSize());
		if (rd.getSessionInfo() != null && !Strings.isEmpty(rd.getSessionInfo().toString())) {
			sb.append(",sessioninfo={");
			sb.append(rd.getSessionInfo());
			sb.append('}');
		}
		if (sd != null) {
			sb.append(",sessionstart=\"");
			sb.append(formatDate(sd.getStartDate()));
			sb.append("\",requests=");
			sb.append(sd.getNumberOfRequests());
			sb.append(",totaltime=");
			sb.append(sd.getTotalTimeTaken());
		}
		sb.append(",activerequests=");
		sb.append(rd.getActiveRequest());

		Runtime runtime = Runtime.getRuntime();
		long max = runtime.maxMemory() / 1000000;
		long total = runtime.totalMemory() / 1000000;
		long used = total - runtime.freeMemory() / 1000000;
		sb.append(",maxmem=");
		sb.append(max);
		sb.append("M,total=");
		sb.append(total);
		sb.append("M,used=");
		sb.append(used);
		sb.append('M');

		return sb.toString();
	}

	private String getRequestHandlerString(IRequestHandler handler) {
		AppendingStringBuffer sb = new AppendingStringBuffer(128);
		if (handler != null) {
			Class<? extends IRequestHandler> handlerClass = handler.getClass();
			sb.append("handler=");
			sb.append(handlerClass.isAnonymousClass() ? handlerClass.getName() : Classes.simpleName(handlerClass));
			if (handler instanceof ILoggableRequestHandler) {
				sb.append(",data=");
				sb.append(((ILoggableRequestHandler) handler).getLogData());
			}
		} else {
			sb.append("none");
		}
		return sb.toString();
	}

	private int getRequestsWindowSize() {
		int requestsWindowSize = 0;
		if (Application.exists()) {
			requestsWindowSize = Application.get().getRequestLoggerSettings().getRequestsWindowSize();
		}
		return requestsWindowSize;
	}
}
