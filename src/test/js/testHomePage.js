Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

test("page has title", function(done) {
	Browser.visit(home, function(e, browser) {
		expect(browser.text("title")).to.be("Games");
		done();
	});
});

test("page can display game list", function(done) {
	Browser.visit(home, function(e, browser) {
		expect(browser.text("#gameList #first.WAITING")).to.be("first join");
		expect(browser.text("#gameList .ready[id='second']")).to
				.be("second join");
		done();
	});
});

messageReceived = function(window) {
	return window.document.querySelector("#messages li");
}

test("user can join game", function(done) {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#first a", function() {
		});
		
		setTimeout(function() {
			expect(browser.html("#messages")).to.contain("join");
			done();
		}, 500);
	});
});
