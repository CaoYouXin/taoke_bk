package com.taoke.miquaner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.taoke.miquaner.fltr.AdminInterceptor;
import com.taoke.miquaner.fltr.IdentityInterceptor;
import com.taoke.miquaner.fltr.VersionInterceptor;
import com.taoke.miquaner.serv.IInitServ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.util.UrlPathHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class MiquanerApplication {

    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
//        Calendar instance = Calendar.getInstance();
//        instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1, 0, 0, 0);
//        System.out.println(DEFAULT_DATE_FORMAT.format(instance.getTime()));

//        try {
//            JdTokenType jdTokenType = DEFAULT_OBJECT_MAPPER.readValue("{ \"access_token\": \"f0fa32a8-73e4-4567-9d9f-ee1b1699ed24\", \"code\": 0, \"expires_in\": 86399, \"refresh_token\": \"1967783a-d0df-47aa-9522-f942a080bc12\", \"time\": \"1511329860375\", \"token_type\": \"bearer\", \"uid\": \"6019658493\", \"user_nick\": \"陈洋1121\" }", JdTokenType.class);
//            System.out.println(DEFAULT_DATE_FORMAT.format(DateUtils.add(new Date(Long.parseLong(jdTokenType.getTime())), Calendar.SECOND, (int) (jdTokenType.getExpiresIn() - 1000))));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        LinkedList<Integer> stack = new LinkedList<>();
//        stack.push(1);
//        stack.push(2);
//        System.out.println(stack.pollFirst());
//        System.out.println(stack.pollFirst());

        ConfigurableApplicationContext context = SpringApplication.run(MiquanerApplication.class, args);

        IInitServ initServ = context.getBean(IInitServ.class);
        initServ.init(context);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("auth", "platform", "content-type")
                        .exposedHeaders("auth", "platform", "Content-Disposition");
            }

            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                urlPathHelper.setUrlDecode(true);
                configurer.setUrlPathHelper(urlPathHelper);
                super.configurePathMatch(configurer);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(getVersionInterceptor());
                registry.addInterceptor(getIdentityInterceptor());
                registry.addInterceptor(getAdminInterceptor());
            }
        };
    }

    @Bean
    VersionInterceptor getVersionInterceptor() {
        return new VersionInterceptor();
    }

    @Bean
    AdminInterceptor getAdminInterceptor() {
        return new AdminInterceptor();
    }

    @Bean
    IdentityInterceptor getIdentityInterceptor() {
        return new IdentityInterceptor();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        DEFAULT_OBJECT_MAPPER.setDateFormat(DEFAULT_DATE_FORMAT);
        DEFAULT_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new MappingJackson2HttpMessageConverter(DEFAULT_OBJECT_MAPPER);
    }

}
