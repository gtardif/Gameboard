Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

test("User can create game", function(done){
	Browser.visit(home, function(e, browser) {
		browser.clickLink("Create Game", function() {
		});

		setTimeout(function() {
			expect(browser.text("#gameList #first.WAITING")).to.be("first (WAITING) join game");
			done();
		}, 20);
	});
});

test("Page can display game list when opening game page", function(done) {
	Browser.visit(home, function(e, browser) {
		expect(browser.text("#gameList #first.WAITING")).to.be("first (WAITING) join game");
		done();
	});
});
