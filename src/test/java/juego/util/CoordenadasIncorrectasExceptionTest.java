package juego.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias sobre la excepción CoordenadasIncorrectasException.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20201118
 * 
 */
@DisplayName("Tests sobre CoordenadasIncorrectasException")
public class CoordenadasIncorrectasExceptionTest {
	
	/**
	 * Correcta definición de cláusula de herencia.
	 */
	@DisplayName("Comprobar que la cláusula extends de la excepción es correcta.")
	@Test
	public void probarCorrectaHerencia() {
		assertAll("correcta herencia",
			() -> assertThat("La clase CoordendasIncorrectasException debe heredar de Exception.", 
					Exception.class.isAssignableFrom(CoordenadasIncorrectasException.class), is(true)),
			
			() -> assertThat("La clase CoordendasIncorrectasException NO debe heredar de RuntimeException.",
					RuntimeException.class.isAssignableFrom(CoordenadasIncorrectasException.class), is(false))
			);			
	} 
}

