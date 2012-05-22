require("./unitTests.js");

Browser = require("zombie");
expect = require("expect.js");
serverBase = "http://localhost:" + process.env.PORT;
home = serverBase + "/games.html";
reset = serverBase + "/game/reset";

testWith2Browsers = function(name, scenario) {
	var execWith2Browsers = function(scenarioFunction) {
		return function(done) {
			Browser.visit(reset, function(e, browser1) {
				browser1.visit(home, function(e, browser2){
					Browser.visit(home, function(e, browser1) {
						scenarioFunction(done, browser1, browser2);
					});
				})
			});
		}
	};
	test(name, execWith2Browsers(scenario));
}

testWith1Browser = function(name, scenario) {
	var execWith1Browser = function(scenarioFunction) {
		return function(done) {
			Browser.visit(reset, function(e, browser1) {
				browser1.visit(home, function(e, browser) {
						scenarioFunction(done, browser);
				});
			});
		}
	};
	test(name, execWith1Browser(scenario));
}

allAsync = function() {
	//Do nothing on sync callback
};