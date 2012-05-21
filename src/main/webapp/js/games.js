function Games(){
	var self = this;
	self.userId;
	self.ws;
	
	this.newGameLine = function(game) {
		return "<li id='game-"+game.name+"' class='"+ game.status + "'>Game " + game.name
				+ " (" + game.status + ") <a href=# onCLick='games.joinGame(\"" + game.name.toString()
				+ "\")'>join game</a></li>";
	}

	this.init = function() {
		var location = "ws://" + document.location.host + "/gameMessage";
		self.ws = new WebSocket(location, "game");
		self.ws.onmessage = self.receiveMessage;
	}
	
	this.receiveMessage = function(message) {
		var jsonMsg = JSON.parse(message.data);
		if (jsonMsg.userId){
			self.userId=jsonMsg.userId;
			$("#userId").html(self.userId);
		}
		if (jsonMsg.updatedGame){
			$("#game-" + jsonMsg.updatedGame.name).remove();
			$("#gameList").append(self.newGameLine(jsonMsg.updatedGame));
		}
		if (jsonMsg.yourTurn){
			$("#currentGame").addClass("STARTED");
		}
		if (jsonMsg.message){
			$("#messages").append("<li>" + jsonMsg.message + "</li>");
		}
		console.log(self.userId + "-messages", $("#messages").html());
	}

	this.joinGame = function(gameId) {
		self.ws.send("join " + gameId);
	}

	this.createGame = function() {
		self.ws.send("create");
	}
	
	this.udpateGameList = function(){
		$.getJSON("/game/list", function(json) {
			$("#gameList").empty();
			json.forEach(function(game) {
				$("#gameList").append(self.newGameLine(game));
			});
		});		
	}
}

module.exports = Games;