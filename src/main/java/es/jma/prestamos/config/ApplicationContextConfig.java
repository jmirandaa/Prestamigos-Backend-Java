/**
 * 
 */
package es.jma.prestamos.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import es.jma.prestamos.dominio.DatosConexion;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.Recordar;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.servicios.IServiciosDeuda;
import es.jma.prestamos.servicios.IServiciosOperacion;
import es.jma.prestamos.servicios.IServiciosRecordar;
import es.jma.prestamos.servicios.IServiciosUsuario;
import es.jma.prestamos.servicios.impl.ServiciosDeuda;
import es.jma.prestamos.servicios.impl.ServiciosOperacion;
import es.jma.prestamos.servicios.impl.ServiciosRecordar;
import es.jma.prestamos.servicios.impl.ServiciosUsuario;
import es.jma.prestamos.utils.UtilFicheros;

/**
 * Clase de configuración del contexto de Spring
 * @author jmiranda
 *
 */
@Configuration
@ComponentScan("es.jma.prestamos")
@EnableWebMvc
@EnableTransactionManagement
public class ApplicationContextConfig extends WebMvcConfigurerAdapter{
	/**
	 * Ubicación de recursos estáticos
	 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/resources/**")
            .addResourceLocations("/resources/")
            .setCachePeriod(604800)
        ;
    }
    
	/**
	 * Ubicación de los jsp
	 * @return
	 */
	@Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
     
    /**
     * Configuración de la base de datos
     * @return
     */
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
    	BasicDataSource dataSource = new BasicDataSource();
    	DatosConexion datosConexion = UtilFicheros.leerBBDD();
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	dataSource.setUrl(datosConexion.getUrl());
    	dataSource.setUsername(datosConexion.getUser());
    	dataSource.setPassword(datosConexion.getPassword());
    	
    	return dataSource;
    }
    
    /**
     * Propiedades de Hibernate
     * @return
     */
    private Properties getHibernateProperties() {
    	Properties properties = new Properties();
    	properties.put("hibernate.show_sql", "true");
    	properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
    	//Propiedad para actualizar los campos automáticamente
    	properties.put("hibernate.hbm2ddl.auto", "update");
    	properties.put("hibernate.event.merge.entity_copy_observer", "allow");
    	properties.put("hibernate.enable_lazy_load_no_trans", "true");
    	//Permitir sesiones automáticas
    	properties.put("hibernate.current_session_context_class", "thread");
    	return properties;
    }
    
    /**
     * Clases a buscar por Hibernate
     * @param dataSource
     * @return
     */
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
    	LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
    	sessionBuilder.addProperties(getHibernateProperties());
    	sessionBuilder.addAnnotatedClasses(Deuda.class);
    	sessionBuilder.addAnnotatedClasses(Operacion.class);
    	sessionBuilder.addAnnotatedClasses(Usuario.class);
    	sessionBuilder.addAnnotatedClasses(Recordar.class);
    	return sessionBuilder.buildSessionFactory();
    }
    
	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				sessionFactory);

		return transactionManager;
	}
	
    @Autowired
    @Bean(name = "serviciosUsuario")
    public IServiciosUsuario getIServiciosBar(SessionFactory sessionFactory) {
    	ServiciosUsuario serviciosUsuario = ServiciosUsuario.getInstance(sessionFactory);
    	return serviciosUsuario;
    }
    
    @Autowired
    @Bean(name = "serviciosDeuda")
    public IServiciosDeuda getIServiciosDeuda(SessionFactory sessionFactory) {
    	ServiciosDeuda serviciosDeuda = ServiciosDeuda.getInstance(sessionFactory);
    	return serviciosDeuda;
    }
    
    @Autowired
    @Bean(name = "serviciosOperacion")
    public IServiciosOperacion getIServiciosOperacion(SessionFactory sessionFactory) {
    	ServiciosOperacion serviciosOperacion = ServiciosOperacion.getInstance(sessionFactory);
    	return serviciosOperacion;
    }
    
    @Autowired
    @Bean(name = "serviciosRecordar")
    public IServiciosRecordar getIServiciosRecordar(SessionFactory sessionFactory) {
    	ServiciosRecordar serviciosRecordar = ServiciosRecordar.getInstance(sessionFactory);
    	return serviciosRecordar;
    }    
    
}
