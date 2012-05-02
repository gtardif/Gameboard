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
		console.log("errors : " + browser.errors);
		expect(browser.text("#gameList #first.WAITING")).to.be("first join");
		expect(browser.text("#gameList .ready[id='second']")).to
				.be("second join");
		done();
	});
});

messageReceived = function(window) {
	console.log(window.document.querySelector("#messages"));
	return window.document.querySelector("#messages li");
}

test("user can join game", function(done) {
	Browser.visit(home, function(e, browser) {
		browser.clickLink("#first a", function() {
		});
		browser.onalert(function(message) {
			console.log("alert " + message);
		});
		console.log("errors : ");
		console.log(browser.errors);

		setTimeout(function() {
			console.log("errors : ");
			console.log(browser.errors);
			expect(browser.html("#messages")).to.contain("join");
			done();
		}, 500);
	});
});
