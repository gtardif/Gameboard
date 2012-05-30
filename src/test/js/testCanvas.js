require("./testFramework.js");

test("can use canvas", function(done) {
	Browser.visit("file:./src/test/js/testCanvas.html", function(e, browser) {
		console.log("errors", browser.errors);
		assertThat(browser.errors, empty());
		done();
	});
});
