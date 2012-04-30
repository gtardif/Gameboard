Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/games.html";

test("page has title", function (done) {
    Browser.visit(home, function (e, browser) {
        expect(browser.text("title")).to.be("Games");
        done();
    });
});

test("page can display game list", function (done) {
    Browser.visit(home, function (e, browser) {
    	console.log("DIV : " + browser.query("#gameList"));
        expect(browser.query("#gameList .WAITING:contains('first')")).to.be.ok();
        expect(browser.query("#gameList .ready:contains('second')")).to.be.ok();
        done();
    });
});

