package com.owl.aipartner.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.owl.aipartner.model.user.UserAuthority;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);

		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			HandlerMappingIntrospector introspector) throws Exception {

		MvcRequestMatcher.Builder builder = new MvcRequestMatcher.Builder(introspector);

		MvcRequestMatcher user = builder.pattern("/users/**");
		MvcRequestMatcher getUsers = builder.pattern(HttpMethod.GET, "/users");
		MvcRequestMatcher postUser = builder.pattern(HttpMethod.POST, "/users");
		MvcRequestMatcher auth = builder.pattern(HttpMethod.POST, "/auth/**");

		return http
				.authorizeHttpRequests(requests -> requests
						.requestMatchers(getUsers).hasAuthority(UserAuthority.ADMIN.name())
						.requestMatchers(user).authenticated()
						.requestMatchers(postUser).permitAll()
						.requestMatchers(auth).permitAll()
						.requestMatchers(PathRequest.toH2Console()).permitAll()
						.anyRequest().permitAll())

				.headers(
						headersConfigurer -> headersConfigurer
								// 設置 frameOptions 為 sameOrigin，用於防止點擊劫持攻擊
								// 需開啟才能夠正常顯示 /h2-console 頁面
								.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

				.csrf(csrfConfigurer -> csrfConfigurer
						// 設置 CSRF 忽略的請求匹配器
						// 必須開啟才能夠正常使用 Postman（get 似乎不影響）、/h2-console，否則會 403 forbidden 存取拒絕
						.ignoringRequestMatchers(
								user,
								postUser,
								auth,
								PathRequest.toH2Console()))

				.formLogin(Customizer.withDefaults()) // 使用表單登錄進行身份驗證
				.httpBasic(Customizer.withDefaults()) // 使用 HTTP 基本驗證（JSON Basic Auth）進行身份驗證
				.build();
	}
}
