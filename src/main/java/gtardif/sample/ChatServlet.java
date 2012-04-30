package gtardif.sample;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class ChatServlet extends WebSocketServlet {
	private static final long serialVersionUID = 1L;

	private final Set<ChatWebSocket> webSockets = new CopyOnWriteArraySet<ChatWebSocket>();

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
		return new ChatWebSocket();
	}

	private class ChatWebSocket implements WebSocket.OnTextMessage {
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
		public void onMessage(String data) {
			for (ChatWebSocket webSocket : webSockets) {
				try {
					webSocket.connection.sendMessage(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
