/*
 * Atmosphere plugin for jQuery Stream
 * http://atmosphere.java.net/
 * http://code.google.com/p/jquery-stream/
 * 
 * Copyright 2011, Donghwan Kim 
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Compatible with jQuery Stream 1.2+
 */
(function($) {
        
        // Default configurations
        $.stream.setup({
                
                // Atmosphere runs on any Java based web server maintaining session using JSESSIONID cookie
                // and sets 'Access-Control-Allow-Origin' header to '*' unless 'enableAccessControl' option is not false
                enableXDR: true,
                
                // Atmosphere always prints junk padding ending with <!-- EOD --> wisely
                // and there is no identifier concept
                handleOpen: function(text, message) {
                        // 'Content-Type' header of the response must be set to 'text/plain'
                        // message.index = text.indexOf("<!-- EOD -->") + "<!-- EOD -->".length;
                        message.index = text.indexOf("<!-- EOD -->") + 12;
                },
                
                // This plugin assumes that you can send message in accord with the default message format
                // if not possible, you can parse the response chunks directly by implementing the following handler
                // handleMessage: function(text, message, stream) {},
                
                // Atmosphere doesn't need metadata
                handleSend: function(type) {
                        // All except send-type request will be canceled
                        if (type !== "send") {
                                return false;
                        }
                }
                
        });
        
})(jQuery);