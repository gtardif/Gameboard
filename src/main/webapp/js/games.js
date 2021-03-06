function Games() {
	var self = this;
	self.userId;
	self.ws;

	this.newGameLine = function(game) {
		return "<li id='game-" + game.name + "' class='" + game.status + "'>Game " + game.name + " (" + game.status
				+ ") <a href=# onCLick='games.joinGame(\"" + game.name.toString() + "\")'>join game</a></li>";
	};

	this.init = function() {
		var wsLocation = "ws://" + document.location.host + "/gameMessage";
		self.ws = new WebSocket(wsLocation, "game");
		self.ws.onmessage = self.receiveMessage;
	};

	this.receiveMessage = function(message) {
		var jsonMsg = JSON.parse(message.data);
		if (jsonMsg.userId) {
			self.userId = jsonMsg.userId;
			$("#userId").html("Welcome " + self.userId);
		}
		if (jsonMsg.updatedGame) {
			$("#game-" + jsonMsg.updatedGame.name).remove();
			$("#gameList").append(self.newGameLine(jsonMsg.updatedGame));
		}
		if (jsonMsg.startedGame) {
			$("#currentGame").addClass("STARTED");
			$("#currentGame").append(" : " + jsonMsg.startedGame.name);
			self.startGame();
		}
		if (jsonMsg.message) {
			$("#messages").append("<li>" + jsonMsg.message + "</li>");
		}
		console.log(self.userId + "-messages", $("#messages").html());
	};

	this.startGame = function() {
		if (document.getElementById("gameCanvas").getContext) {
			var p4 = new P4();
			p4.drawGrid();
		}
	};

	this.joinGame = function(gameId) {
		self.ws.send("join " + gameId);
	};

	this.createGame = function() {
		self.ws.send("create");
	};

	this.udpateGameList = function() {
		$.getJSON("/game/list", function(json) {
			$("#gameList li").remove();
			json.forEach(function(game) {
				$("#gameList").append(self.newGameLine(game));
			});
		});
	};
}

module.exports = Games;