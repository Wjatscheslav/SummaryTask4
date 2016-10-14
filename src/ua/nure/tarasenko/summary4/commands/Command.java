package ua.nure.tarasenko.summary4.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides method for execution command.
 * 
 * @author Tarasenko
 */
public interface Command {

	/**
	 * Execute command from servlet.
	 * 
	 * @param req
	 *            Servlet request
	 * @param resp
	 *            Servlet response
	 * @return String value (page path).
	 */
	public String execute(HttpServletRequest req, HttpServletResponse resp);

}
