package com.octopus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class OctopusLoginApplication {

    public static void main( String[] args ) {
        ConfigurableApplicationContext ctx = SpringApplication.run( OctopusLoginApplication.class, args );

        Environment env = ctx.getBean( Environment.class );
        String portValue = env.getProperty( "server.port" );

        log.info( "Tomcat Server Port :: {}", portValue );

        //String[] beanNames = ctx.getBeanDefinitionNames();
        //Arrays.stream( beanNames ).forEach( name -> log.info( "Bean Name :: {}", name ) );
    }
}
