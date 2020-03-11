package br.com.tama.projekt.configs;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Classe de configurações adicionais do sistema
 *
 * @autor Wilson Ferreira
 */
@Configuration
public class WebConfig implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext applicationContext;

    /**
     * Contexto padrão da aplicação
     * @param applicationContext padrão do Spring
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Método para incluir o Thymeleaf como processador de views do sistema.
     *
     * @return objeto com as configurações do view resolver
     */
    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(SpringTemplateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }


    /** Configurações da template engine
     * Adiciona o LayoutDialect ao sistema para gerenciamento dos layouts
     * Adiciona o Java8TimeDialect para processar dados temporais dentro das views
     * Adicionar o DataAttributeDialect para extender e posibilitar o uso do atributo 'data:' no Thymeleaf
     *
     * @return objeto com configurações da engine.
     */
    @Bean
    public SpringTemplateEngine SpringTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.addDialect(new LayoutDialect());
        engine.addDialect(new Java8TimeDialect());
        engine.addDialect(new DataAttributeDialect());
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    /**
     * Configurações adicionais do template resolver.
     * Adicionando o contexto da aplicação, definindo o local onde os templates estão salvos,
     * o sufixo dos arquivos (HTML) e o modo do template (HTML).
     * @return objeto com o resolver do template.
     */
    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }
    
    /** 
     * Configuração para processamento correto dos acentos na view
     * @return Objeto Filter com configuração necessária
     */
    
    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
