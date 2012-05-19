package gtardif.web;

import gtardif.p4.P4Board;
import gtardif.p4.P4Player;

import com.google.common.collect.ImmutableMap;

class WebP4Player implements P4Player {
	private final String name;
	private final transient UserWebSocket ws;

	public WebP4Player(String name, UserWebSocket ws) {
		this.name = name;
		this.ws = ws;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void yourTurn(P4Board board) {
		ws.sendMessageMap(ImmutableMap.of("yourTurn", true, "board", board));
	}

	@Override
	public void youWin(P4Board board) {
	}

	@Override
	public void youLoose(P4Board board) {
	}
}