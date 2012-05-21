require("./unitTests.js");
var Games = require("../../main/webapp/js/games.js");

games = new Games();
games.ws = mock(Object);
games.ws.send = mockFunction();

test("can call join WS", function() {
	games.joinGame("1");
	
	verify(games.ws.send)("join 1");
});

test("can call create WS", function() {
	games.createGame();
	
	verify(games.ws.send)("create");
});
