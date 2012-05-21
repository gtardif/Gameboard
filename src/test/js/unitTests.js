require("./libs/jshamcrest-0.6.7.js");
require("./libs/jsmockito-1.0.4.js");
JsHamcrest.Integration.JsTestDriver();
JsMockito.Integration.importTo(GLOBAL);

var jsdom = require('jsdom');
testWithDom = function(testName, html, testFn) {
	test(testName, function(done) {
		jsdom.env({
			html : html,
			scripts : [ './libs/jquery-1.7.1.min.js' ]
		}, function(err, window) {
			if (err){
				done(err);
			}else {
				$=window.jQuery;
				try {
					testFn();
					delete $;
					done();
				} catch(error){
					delete $;
					done(error);
				}
			}
		});
	});
}
