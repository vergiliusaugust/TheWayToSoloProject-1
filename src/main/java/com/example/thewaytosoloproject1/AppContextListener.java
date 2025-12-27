package com.example.thewaytosoloproject1;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info(">>> Application started. ServletContext initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("<<< Application is stopping. ServletContext destroyed");
    }
}
