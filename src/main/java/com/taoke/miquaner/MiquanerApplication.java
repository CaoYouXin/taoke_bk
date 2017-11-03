package com.taoke.miquaner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EConfig;
import com.taoke.miquaner.data.ERole;
import com.taoke.miquaner.repo.ConfigRepo;
import com.taoke.miquaner.repo.RoleRepo;
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
import java.util.List;

@SpringBootApplication
@ServletComponentScan
public class MiquanerApplication {

	public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MiquanerApplication.class, args);

        ConfigRepo configRepo = context.getBean(ConfigRepo.class);
        EConfig config = configRepo.findByKeyEquals(EConfig.SERVER_TOKEN);
        if (null == config) {
            config = new EConfig();
            config.setKey(EConfig.SERVER_TOKEN);
        }
        config.setValue(StringUtil.toMD5HexString(DEFAULT_DATE_FORMAT.format(new Date())));
        configRepo.save(config);

        RoleRepo roleRepo = context.getBean(RoleRepo.class);
        ERole role = roleRepo.findByNameEquals(ERole.SUPER_ROLE_NAME);
        if (null == role) {
            role = new ERole();
            role.setName(ERole.SUPER_ROLE_NAME);
            roleRepo.save(role);
        } else {
            List<EAdmin> admins = role.getAdmins();
            if (admins.size() > 1) {
                context.stop();
            }
        }
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
