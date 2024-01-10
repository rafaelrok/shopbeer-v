package br.com.rafaelvieira.shopbeer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource(value = { "file://${HOME}/.shopbeer-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {

	private final Environment env;

	public MailConfig(Environment env) {
		this.env = env;
	}

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.sendgrid.net");
		mailSender.setPort(587);
		mailSender.setUsername(env.getProperty("shopbeer.mail.username"));
		mailSender.setPassword(env.getProperty("SENDGRID_PASSWORD"));
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.debug", false);
		props.put("mail.smtp.connectiontimeout", 10000);

		mailSender.setJavaMailProperties(props);
		
		return mailSender;
	}
	
}
