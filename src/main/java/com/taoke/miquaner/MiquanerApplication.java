package com.taoke.miquaner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.repo.ConfigRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@ServletComponentScan
public class MiquanerApplication {

	public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MiquanerApplication.class, args);

        ConfigRepo configRepo = context.getBean(ConfigRepo.class);
        EConfig byKeyEquals = configRepo.findByKeyEquals(EConfig.SERVER_TOKEN);
        if (null == byKeyEquals) {
            byKeyEquals = new EConfig();
            byKeyEquals.setKey(EConfig.SERVER_TOKEN);
        }
        byKeyEquals.setValue(StringUtil.toMD5HexString(DEFAULT_DATE_FORMAT.format(new Date())));
        configRepo.save(byKeyEquals);
    }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedHeaders("auth", "content-type")
						.exposedHeaders("auth");
			}

			@Override
			public void configurePathMatch(PathMatchConfigurer configurer) {
				UrlPathHelper urlPathHelper = new UrlPathHelper();
				urlPathHelper.setUrlDecode(true);
				configurer.setUrlPathHelper(urlPathHelper);
				super.configurePathMatch(configurer);
			}
		};
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(DEFAULT_DATE_FORMAT);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return new MappingJackson2HttpMessageConverter(mapper);
	}

}
