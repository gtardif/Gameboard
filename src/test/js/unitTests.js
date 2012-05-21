require("./libs/jshamcrest-0.6.7.js");
require("./libs/jsmockito-1.0.4.js");
JsHamcrest.Integration.JsTestDriver();
JsMockito.Integration.importTo(GLOBAL);

var jsdom = require('jsdom');
testWithDom = function(testName, html, testFn) {
	test(testName, function(done) {
		jsdom.env({
			html : html,
			scripts : [ 'http://code.jquery.com/jquery-1.5.min.js' ]
		}, function(err, window) {
			$=window.jQuery;
			try {
				testFn();
				delete $;
				done();
			} catch(error){
				delete $;
				done(error);
			}
		});
	});
}
