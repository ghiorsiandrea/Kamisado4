package juego.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias sobre la torre.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20181008
 * 
 */
@DisplayName("Tests sobre Torre")
public class TorreTest {

	/**
	 * Test del constructor.
	 */
	@DisplayName("Constructor con estado inicial correcto")
	@Test
	void constructor() {
		Torre torre = new TorreSimple(Turno.BLANCO, Color.AMARILLO);
		assertAll(() -> assertThat("Color mal inicializado", torre.obtenerColor(), is(Color.AMARILLO)),
				() -> assertThat("Turno mal inicializado", torre.obtenerTurno(), is(Turno.BLANCO)),
				() -> assertNull(torre.obtenerCelda(), "La torre inicialmente no debe estar colocada en una celda."));
	}

	/**
	 * Coloca la torre en la celda.
	 */
	@DisplayName("Coloca la torre en la celda")
	@Test
	void colocarEnCelda() {
		Torre torre = new TorreSimple(Turno.BLANCO, Color.ROJO);
		Celda celda = new Celda(0, 0, Color.AZUL);
		torre.establecerCelda(celda);
		assertAll(() -> assertThat("Torre mal asociada a celda", torre.obtenerCelda(), is(celda)),
				() -> assertThat("Turno mal inicializado", torre.obtenerTurno(), is(Turno.BLANCO)),
				() -> assertThat("Color mal inicializado", torre.obtenerColor(), is(Color.ROJO)),
				() -> assertThat("Celda no debería estar asociada a la torre", celda.obtenerTorre(), is(nullValue())));
	}

	/**
	 * Prueba del método toString.
	 */
	@DisplayName("Formato de texto")
	@Test
	void probarToString() {
		Torre torre = new TorreSimple(Turno.BLANCO, Color.ROJO);
		assertThat("Texto mal construido o estado incorrecto", torre.toString().replaceAll("\\s", ""),
				is ("BR"));

		torre = new TorreSimple(Turno.BLANCO, Color.MARRON);
		assertThat("Texto mal construido o estado incorrecto", torre.toString().replaceAll("\\s", ""),
				is ("BM"));

		Celda celda = new Celda(3, 4, Color.VERDE);
		torre = new TorreSimple(Turno.NEGRO, Color.NARANJA);
		torre.establecerCelda(celda);
		assertThat("Texto mal construido o estado incorrecto", torre.toString().replaceAll("\\s", ""),
				is ("NN"));
	}
	
}
