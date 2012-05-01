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
        expect(browser.text("#gameList .WAITING")).to.be("first");
        expect(browser.text("#gameList .ready")).to.be("second");
        done();
    });
});

