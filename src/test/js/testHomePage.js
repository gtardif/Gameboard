Browser = require("zombie");
expect = require("expect.js");
require("./libs/jshamcrest-0.6.7.js");
require("./libs/jsmockito-1.0.4.js");

JsHamcrest.Integration.JsTestDriver();
JsMockito.Integration.importTo(GLOBAL);

serverBase = "http://localhost:" + process.env.PORT;
home = serverBase + "/games.html";
reset = serverBase + "/game/reset";

testWith2Browsers = function(name, scenario) {
	var execWith2Browsers = function(scenarioFunction) {
		return function(done) {
			Browser.visit(reset, function(e, browser1) {
				browser1.visit(home, function(e, browser2){
					Browser.visit(home, function(e, browser1) {
						scenarioFunction(done, browser1, browser2);
					});
				})
			});
		}
	};
	test(name, execWith2Browsers(scenario));
}

testWith1Browser = function(name, scenario) {
	var execWith1Browser = function(scenarioFunction) {
		return function(done) {
			Browser.visit(reset, function(e, browser1) {
				browser1.visit(home, function(e, browser) {
						scenarioFunction(done, browser);
				});
			});
		}
	};
	test(name, execWith1Browser(scenario));
}

allAsync = function() {
	//Do nothing on sync callback
};

testWith2Browsers("User can create game", function(done, browser1, browser2) {
	browser1.clickLink("Create Game", allAsync);

	setTimeout(function() {
		assertThat(browser1.text("#gameList #game-1"), equalTo("Game 1 (WAITING) join game"));
		assertThat(browser1.query("#gameList #game-1").className, equalTo("WAITING"));
		assertThat(browser2.text("#gameList #game-1"), equalTo("Game 1 (WAITING) join game"));
		assertThat(browser2.query("#gameList #game-1").className, equalTo("WAITING"));

		done();
	}, 50);
});

testWith2Browsers("User cannot join its own game", function(done, browser1, browser2) {
	browser1.clickLink("Create Game", allAsync);

	setTimeout(function() {
		browser1.evaluate("games.joinGame(1)");
		// TODO pb when clicking link : creates a new unwantd browser
		//browser1.clickLink("#gameList #game-1 a", allAsync);
	}, 50);

	setTimeout(function() {
		assertThat(browser1.text("#messages li:last-child"), equalTo("Error : player has already joined the game"));
		assertThat(browser2.query("#messages").children.length, equalTo(2));
		assertThat(browser1.query("#messages").children.length, equalTo(3));
		done();
	}, 100);
});

testWith1Browser("Page can display game list when opening game page", function(done, browser1) {
	browser1.clickLink("Create Game", allAsync);
	
	setTimeout(function() {
		browser1.visit(home, function(e, browser2){
			assertThat(browser2.text("#gameList #game-1.WAITING"), equalTo("Game 1 (WAITING) join game"));
			assertThat(browser2.query("#gameList #game-1").className, equalTo("WAITING"));
			done();
		});
	}, 50);
});

testWith2Browsers("User can join a game", function(done, browser1, browser2) {
	browser1.clickLink("Create Game", allAsync);

	setTimeout(function() {
		browser2.clickLink("#gameList #game-1 a", allAsync);
	}, 50);

	setTimeout(function() {
		assertThat(browser1.text("#gameList #game-1"), equalTo("Game 1 (STARTED) join game"));
		assertThat(browser1.query("#gameList #game-1").className, equalTo("STARTED"));
		assertThat(browser1.query("#currentGame").className, equalTo("STARTED"));
		done();
	}, 100);
});