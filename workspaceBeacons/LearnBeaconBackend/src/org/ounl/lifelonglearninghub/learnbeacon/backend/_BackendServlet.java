/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.learnbeacon.backend;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class _BackendServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Welcome to the LearnBeacon Backend");
		resp.getWriter().println("... List here available becons in DB");
		resp.getWriter().println("... Make available to insert new beacon from web");
		resp.getWriter().println("... Esta va a ser la mobile authoring tool across platform que vas a ofrecer, lo otro va a ser el cliente para jugar");
		resp.getWriter().println("... En el formulario de insercion de beacons explica que tipo de feedback se tiene ue introducir basado en tu modelo.");
		
		resp.getWriter().println("(c) Bernardo Tabuenca");
	}
}
