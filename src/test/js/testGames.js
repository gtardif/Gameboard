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

msg = function(jsonData){
	return {data : JSON.stringify(jsonData)};
};

testWithDom("can receive UserId", '<div id="userId"><div>', function(){
	games.receiveMessage(msg({"userId":"toto"}));
	
	assertThat($("#userId").html(), equalTo("Welcome toto"));
});

testWithDom("can update game list with new game", '<ul id="gameList"></ul>', function(){
	games.receiveMessage(msg({"updatedGame":{
		"name":"p4", 
		"status":"WAITING"}
	}));
	
	assertThat($("#gameList #game-p4").text(), equalTo("Game p4 (WAITING) join game"));
});

testWithDom("can update game list with updated game", '<ul id="gameList"><li id="game-someGame">previous content</li></ul>', function(){
	games.receiveMessage(msg({"updatedGame":{
		"name":"someGame", 
		"status":"STARTED"}
	}));
	
	assertThat($("#gameList #game-someGame").text(), equalTo("Game someGame (STARTED) join game"));
});

testWithDom("can display message", '<ul id="messages"><li>some previous message</li></ul>', function(){
	games.receiveMessage(msg({"message":"hello world"}));
	
	assertThat($("#messages").html(), equalTo('<li>some previous message</li><li>hello world</li>'));
});

testWithDom("can update current game when your turn", '<div id="currentGame"></div>', function(){
	games.receiveMessage(msg({"yourTurn":"true"}));
	
	assertThat($("#currentGame").attr("class"), equalTo('STARTED'));
});

