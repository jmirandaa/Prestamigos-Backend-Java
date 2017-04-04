package es.jma.prestamos.junit.daos;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Probar todos los DAOS
 * @author jmiranda
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TestDAODeuda.class, TestDAOOperacion.class,
		TestDAOUsuario.class })
public class TestsDAOS {

}
