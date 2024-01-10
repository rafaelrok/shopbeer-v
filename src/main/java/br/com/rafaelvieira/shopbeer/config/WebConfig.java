package br.com.rafaelvieira.shopbeer.config;

import br.com.rafaelvieira.shopbeer.security.UserPermissionChecker;
import java.util.Arrays;
import java.util.List;
import javax.cache.Caching;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableCaching
@EnableAsync
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public CacheManager cacheManager() throws Exception {
		return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager(
				getClass().getResource("/ehcache.xml").toURI(),
				getClass().getClassLoader()));
	}

	@Bean
	public UserPermissionChecker userPermissionChecker() {
		return new UserPermissionChecker(securityContextHolder(), allowedRoles());
	}

	@Bean
	public List<String> allowedRoles() {
		return Arrays.asList("ADMIN", "USER");
	}

	@Bean
	public SecurityContextHolder securityContextHolder() {
		return new SecurityContextHolder(); // bean default
	}
}