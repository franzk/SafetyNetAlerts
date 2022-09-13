package net.safety.alerts;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="net.safety.alerts")
public class CustomProperties {

	private String dataUrl;
	
}
