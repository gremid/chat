package net.middell.chat;

import com.google.common.base.Preconditions;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatWebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        final AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
        appCtx.register(ApplicationConfiguration.class);
        appCtx.getEnvironment().setDefaultProfiles("production");

        servletContext.addListener(new ContextLoaderListener(appCtx));


        final AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext();
        webCtx.register(WebConfiguration.class);
        webCtx.getEnvironment().setDefaultProfiles("production");

        final ServletRegistration.Dynamic appServlet = servletContext.addServlet("dispatcher", new DispatcherServlet(webCtx));
        appServlet.setLoadOnStartup(1);
        final Set<String> conflicts = appServlet.addMapping("/");
        Preconditions.checkState(conflicts.isEmpty(), "'dispatcher' could not be mapped to '/' due to an existing mapping. This is a known issue under Tomcat versions <= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");

        final FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", new CharacterEncodingFilter());
        characterEncoding.setInitParameter("encoding", "UTF-8");
        characterEncoding.setInitParameter("forceEncoding", "true");
        characterEncoding.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "dispatcher");
    }
}
