/**
 * 
 */
package es.jma.prestamos.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Clase de configuración para Spring MVC
 * @author jmiranda
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "es.jma.prestamos")
public class SpringConfiguration {

}
