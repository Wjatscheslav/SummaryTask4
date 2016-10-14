package ua.nure.tarasenko.summary4.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * Listener for configuring Log4j.
 * 
 * @author Tarasenko
 *
 */
public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		PropertyConfigurator.configure(context.getRealPath("WEB-INF/log4j.properties"));
	}

}
