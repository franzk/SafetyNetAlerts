package net.safety.alerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AlertsApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AlertsApplication.class, args);
		context.getBean(DataInitializer.class).importJsonData();
	}

}
