/*
 * 
 *  * ========================================================= *
 *  *************************************************************
 *  *      _______. _______  _______ .___________.____    ____  *
 *  *     /       ||   ____||   ____||           |\   \  /   /  *
 *  *    |   (----`|  |__   |  |__   `---|  |----` \   \/   /   *
 *  *     \   \    |   __|  |   __|      |  |       \_    _/    *
 *  * .----)   |   |  |____ |  |____     |  |         |  |      *
 *  * |_______/    |_______||_______|    |__|         |__|      *
 *  *                                                           *
 *  *************************************************************
 *  * ========================================================= *
 *           _____  _   _      _   
 *     /\   |  __ \| \ | |    | |  
 *    /  \  | |  | |  \| | ___| |_ 
 *   / /\ \ | |  | | . ` |/ _ \ __|
 *  / ____ \| |__| | |\  |  __/ |_ 
 * /_/    \_\_____/|_| \_|\___|\__|
 * 
 * 
 * 
 * NOTICE:
 * 
 * ##################################################################################
 * #                                                                                #
 * # This file is part of the Seety project.                                        #
 * # All information contained herein is, remains the property of PagesJaunes Group #
 * # The intellectual and technical concepts contained herein are proprietary to    #
 * # PagesJaunes Group and may be covered by Patents, patents in process, and are   #
 * # protected by trade secret or copyright law.                                    #
 * #                                                                                #
 * # Dissemination of this information or reproduction of this material is strictly #
 * # forbidden unless prior written permission is obtained from PagesJaunes Group.  #
 * #                                                                                #
 * # All Right Reserved                                                             #
 * # Copyright (c) 2011 , PagesJaunes Group                                         #
 * #                                                                                #
 * ##################################################################################
 */
package gtardif.web;

import gtardif.GameWebSocketServlet;
import gtardif.sample.ChatServlet;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.inject.servlet.GuiceFilter;

public class GameWebServer {
	private final int port;
	private Server server;

	public GameWebServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		server = new Server(port);

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setResourceBase("src/main/webapp/");
		webapp.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		webapp.addServlet(ChatServlet.class, "/chat");
		webapp.addServlet(GameWebSocketServlet.class, "/gameMessage");
		webapp.addEventListener(new LimeServletListener());

		server.setHandler(webapp);
		server.start();
	}

	public void startAndJoin() throws Exception {
		start();
		server.join();
	}

	public boolean isRunning() {
		return server.isRunning();
	}

	public void stop() throws Exception {
		server.stop();
	}

	public static void main(String[] args) throws Exception {
		GameWebServer gameServer = new GameWebServer(1080);
		gameServer.startAndJoin();
	}
}
