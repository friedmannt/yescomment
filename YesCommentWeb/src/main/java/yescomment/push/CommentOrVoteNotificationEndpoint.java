package yescomment.push;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.constraints.NotNull;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/commentsandvotes/{articleId}")
public class CommentOrVoteNotificationEndpoint {

	private static final Logger logger = Logger.getLogger("CommentOrVoteNotificationEndpoint");
	static Map<Session, String> subscriptions = new ConcurrentHashMap<Session, String>();

	@OnOpen
	public void open(Session session, EndpointConfig c, @PathParam("articleId") String articleId) {
		subscriptions.put(session, articleId);
		logger.log(Level.INFO, String.format("Connection opened for article %s.", articleId));
	}

	@OnClose
	public void closedConnection(Session session) {

		subscriptions.remove(session);
		logger.log(Level.INFO, "Connection closed.");
	}

	@OnError
	public void error(Session session, Throwable t) {
		subscriptions.remove(session);
		logger.log(Level.INFO, t.toString());
		logger.log(Level.INFO, "Connection error.");
	}

	public static void commentNotify(@NotNull String articleId) {

		try {
			/* Send updates to all open WebSocket sessions */
			for (Entry<Session, String> subscription : subscriptions.entrySet()) {
				if (subscription.getValue().equals(articleId)) {
					Session session = subscription.getKey();
					session.getBasicRemote().sendText(articleId);
				}

			}
		} catch (IOException e) {
			logger.log(Level.INFO, e.toString());
		}

	}
}
