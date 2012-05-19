Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

testWith2Browsers = function(name, scenario) {
	var execWith2Browsers = function(scenarioFunction) {
		return function(done) {
			Browser.visit(home, function(e, browser2) {
				Browser.visit(home, function(e, browser) {
					scenarioFunction(done, browser, browser2);
				});
			});
		}
	};
	test(name, execWith2Browsers(scenario));
}

testWith1Browser = function(name, scenario) {
	var execWith1Browser = function(scenarioFunction) {
		return function(done) {
			Browser.visit(home, function(e, browser) {
					scenarioFunction(done, browser);
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
		expect(browser1.text("#gameList #game-1")).to.be("Game 1 (WAITING) join game");
		expect(browser1.query("#gameList #game-1").className).to.contain("WAITING");
		expect(browser2.text("#gameList #game-1")).to.be("Game 1 (WAITING) join game");
		expect(browser2.query("#gameList #game-1").className).to.contain("WAITING");

		done();
	}, 50);
});

testWith2Browsers("User cannot join its own game", function(done, browser1, browser2) {
	browser1.clickLink("Create Game", allAsync);

	setTimeout(function() {
		browser1.evaluate("joinGame(2)");
		// TODO pb when clicking link : creates a new unwantd browser
	}, 50);

	setTimeout(function() {
		expect(browser1.text("#messages li:last-child")).to.be("Error : player has already joined the game");
		expect(browser1.query("#messages").children.length).to.be(3);
		expect(browser2.query("#messages").children.length).to.be(2);
		done();
	}, 100);
});

testWith1Browser("Page can display game list when opening game page", function(done, browser) {
	expect(browser.text("#gameList #game-1.WAITING")).to.be("Game 1 (WAITING) join game");
	expect(browser.query("#gameList #game-1").className).to.contain("WAITING");
	done();
});

testWith2Browsers("User can join a game", function(done, browser1, browser2) {
	browser1.clickLink("Create Game", allAsync);

	setTimeout(function() {
		browser2.evaluate("joinGame(3)");
		// TODO pb when clicking link : creates a new unwantd browser
	}, 50);

	setTimeout(function() {
		expect(browser1.text("#gameList #game-3")).to.be("Game 3 (STARTED) join game");
		expect(browser1.query("#gameList #game-3").className).to.contain("STARTED");
		console.log("current", browser1.html("#currentGame"));
		expect(browser1.query("#currentGame").className).to.contain("STARTED");
		done();
	}, 150);
});