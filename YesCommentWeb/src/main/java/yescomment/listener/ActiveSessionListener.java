package yescomment.listener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener()
public class ActiveSessionListener implements HttpSessionListener {

	static final Map<String, SessionData> activeSessions = new HashMap<String, SessionData>();

	public static Map<String, SessionData> getActivesessions() {
		return activeSessions;
	}

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		String sessionId = hse.getSession().getId();
		long creationTime = hse.getSession().getCreationTime();

		SessionData sessionData = new SessionData(sessionId, creationTime);
		activeSessions.put(sessionId, sessionData);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		String sessionId = hse.getSession().getId();
		activeSessions.remove(sessionId);
	}

	public static class SessionData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final String sessionId;

		private final long creationTime;

		public String getSessionId() {
			return sessionId;
		}

		public long getCreationTime() {
			return creationTime;
		}

		public SessionData(String sessionId, long creationTime) {
			super();
			this.sessionId = sessionId;
			this.creationTime = creationTime;
		}

		@Override
		public String toString() {
			return "SessionData [sessionId=" + sessionId + ", creationTime=" + creationTime + "]";
		}

	}

}
