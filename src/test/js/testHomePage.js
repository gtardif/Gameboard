Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

test("user can create game", function(done){
	Browser.visit(home, function(e, browser) {
		browser.clickLink("Create Game", function() {
			expect(browser.text("#gameList #first.WAITING")).to.be("first join");
			done();
		});
	});
});

test("page can display game list when opening game page", function(done) {
	Browser.visit(home, function(e, browser) {
		expect(browser.text("#gameList #first.WAITING")).to.be("first join");
		done();
	});
});

test("user can join game", function(done) {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#first a", function() {
		});

		setTimeout(function() {
			expect(browser.html("#messages")).to.contain("join first");
			done();
		}, 500);
	});
});
