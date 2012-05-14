Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

test("User can create game", function(done) {
	Browser.visit(home, function(e, browser2) {
		Browser.visit(home, function(e, browser) {
			browser.clickLink("Create Game", function() {
			});

			setTimeout(function() {
				expect(browser.text("#gameList #game-1")).to.be("Game 1 (WAITING) join game");
				expect(browser.query("#gameList #game-1").className).to.contain("WAITING");
				expect(browser2.text("#gameList #game-1")).to.be("Game 1 (WAITING) join game");
				expect(browser2.query("#gameList #game-1").className).to.contain("WAITING");
				
				done();
			}, 50);
		});
	});
});

test("User cannot join its own game", function(done) {
	Browser.visit(home, function(e, browser2) {
		Browser.visit(home, function(e, browser) {
			browser.clickLink("Create Game", function() {
			});
	
			setTimeout(function() {
				console.log(browser.text("#userId") + ": join game");
				browser.evaluate("joinGame(2)");
				//TODO pb when clicking link : creates a new unwantd browser
			}, 50);

			setTimeout(function() {
				console.log(browser.errors);
				console.log(browser.text("#userId") + " : " + browser.html("#messages"));
				expect(browser.text("#messages li:last-child")).to.be("Error : player has already joined the game");
				expect(browser.query("#messages").children.length).to.be(3);
				expect(browser2.query("#messages").children.length).to.be(2);
				done();
			}, 100);
		});
	});
});

test("Page can display game list when opening game page", function(done) {
	Browser.visit(home, function(e, browser) {
		expect(browser.text("#gameList #game-1.WAITING")).to.be("Game 1 (WAITING) join game");
		expect(browser.query("#gameList #game-1").className).to.contain("WAITING");
		done();
	});
});

test("User can join a game", function(done) {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#gameList #game-1 a", function() {
		});

		setTimeout(function() {
			expect(browser.text("#gameList #game-1")).to.be("Game 1 (STARTED) join game");
			expect(browser.query("#gameList #game-1").className).to.contain("STARTED");
			done();
		}, 100);
	});
});

test("Game is started automatically", function() {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#gameList #game-1 a", function() {
		});

		setTimeout(function() {
			expect(browser.query("#currentGame").className).to.contain("RUNNING");
			done();
		}, 50);
	});
})
