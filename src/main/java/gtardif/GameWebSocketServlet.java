package gtardif;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class GameWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;
	private final Set<GameWebSocket> webSockets = new CopyOnWriteArraySet<GameWebSocket>();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		System.out.println("REQUEST " + protocol);
		return new GameWebSocket();
	}

	private class GameWebSocket implements WebSocket.OnTextMessage {
		volatile Connection connection;

		@Override
		public void onOpen(Connection connection) {
			this.connection = connection;
			webSockets.add(this);
		}

		@Override
		public void onClose(int closeCode, String message) {
			webSockets.remove(this);
		}

		@Override
		public void onMessage(String message) {
			for (GameWebSocket webSocket : webSockets) {
				try {
					webSocket.connection.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}