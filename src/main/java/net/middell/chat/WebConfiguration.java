package net.middell.chat;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
@Configuration
@EnableWebMvc()
@ComponentScan(
        basePackageClasses = {ChatWebAppInitializer.class},
        useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ChatService chatService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ChatChannel.ToStringConverter());
        registry.addConverter(new ChatChannel.FromStringConverter(chatService));
        registry.addConverter(new ChatUser.ToStringConverter());
        registry.addConverter(new ChatUser.FromStringConverter(chatService));
    }

    @Override
    public void configureResourceHandling(ResourceConfigurer configurer) {
        configurer.addResourceLocation("/WEB-INF/static/");
        configurer.addPathMapping("/static/**");
    }

    @Override
    public void configureViewControllers(ViewControllerConfigurer configurer) {
        configurer.mapViewName("/", "start");
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.GERMAN);
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        final Properties settings = new Properties();
        settings.setProperty("auto_include", "/head.ftl");
        settings.setProperty("default_encoding", "UTF-8");
        settings.setProperty("output_encoding", "UTF-8");
        settings.setProperty("url_escaping_charset", "UTF-8");
        settings.setProperty("strict_syntax", "true");
        settings.setProperty("whitespace_stripping", "true");

        final FreeMarkerConfigurer c = new FreeMarkerConfigurer();
        c.setTemplateLoaderPath("/WEB-INF/freemarker/");
        c.setFreemarkerSettings(settings);
        return c;
    }

    @Bean
    public ViewResolver viewResolver() {
        final FreeMarkerViewResolver vr = new FreeMarkerViewResolver();
        vr.setContentType("text/html;charset=utf-8");
        vr.setPrefix("");
        vr.setSuffix(".ftl");
        return vr;
    }
}
