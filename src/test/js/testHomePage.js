Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

test("User can create game", function(done) {
	Browser.visit(home, function(e, browser2) {
		Browser.visit(home, function(e, browser) {
			browser.clickLink("Create Game", function() {
			});

			setTimeout(function() {
				expect(browser.text("#gameList #first")).to.be("first (WAITING) join game");
				expect(browser.query("#gameList #first").className).to.contain("WAITING");
				expect(browser2.text("#gameList #first")).to.be("first (WAITING) join game");
				expect(browser2.query("#gameList #first").className).to.contain("WAITING");
				done();
			}, 50);
		});
	});
});

test("User cannot create the same game twice", function(done) {
	Browser.visit(home, function(e, browser2) {
		Browser.visit(home, function(e, browser) {
		browser.clickLink("Create Game", function() {
		});

		setTimeout(function() {
			console.log(browser.errors);
			expect(browser.text("#messages li:last-child")).to.be("Error : Game with id first already exist.");
			expect(browser.query("#messages").children.length).to.be(1);
			expect(browser2.query("#messages").children.length).to.be(0);
			done();
		}, 50);
	});
	});
});

test("Page can display game list when opening game page", function(done) {
	Browser.visit(home, function(e, browser) {
		expect(browser.text("#gameList #first.WAITING")).to.be("first (WAITING) join game");
		expect(browser.query("#gameList #first").className).to.contain("WAITING");
		done();
	});
});

test("User can join a game", function(done) {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#gameList #first a", function() {
		});

		setTimeout(function() {
			expect(browser.text("#gameList #first")).to.be("first (STARTED) join game");
			expect(browser.query("#gameList #first").className).to.contain("STARTED");
			done();
		}, 100);
	});
});

test("Game is started automatically", function() {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#gameList #first a", function() {
		});

		setTimeout(function() {
			expect(browser.query("#currentGame").className).to.contain("RUNNING");
			done();
		}, 50);
	});
})
