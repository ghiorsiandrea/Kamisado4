package juego.control;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import juego.modelo.Tablero;

/**
 * Pruebas unitarias sobre el lanzamiento de excepciones en métodos no
 * permitidos a invocar en un arbitro simple.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 */
@DisplayName("Tests básicos sobre el lanzamiento de excepciones en métodos no permitidos en arbitro simple")
public class ArbitroEstadoBasicoSimpleLanzandoExcepcionesTests {

	/** Árbitro de testing. */
	private Arbitro arbitro;

	/** Tablero de testing. */
	private Tablero tablero;

	/** Generación del árbitro para testing. */
	@BeforeEach
	void inicializar() {
		// Inyección de tablero para testing...
		tablero = new Tablero();
		arbitro = new ArbitroSimple(tablero);
		// NO colocamos torres
	}

	/**
	 * Comprueba el lanzamiento de excepción al comprobar la legalidad de un empujón
	 * sumo.
	 */
	@DisplayName("Lanzamiento de excepción al comprobar legalidad de empujón sumo")
	@Test
	void comprobarLanzamientoAlConsultarEmpujonSumoLegal() {
		assertThrows(UnsupportedOperationException.class, () -> arbitro.empujarSumo(tablero.obtenerCelda(0, 0)),
				"No se debe consultar la legalidad de empujón sumo en un arbitro simple");
	}
	
	/**
	 * Comprueba el lanzamiento de excepción al reiniciar ronda.
	 */
	@DisplayName("Lanzamiento de excepción al reiniciar la ronda")
	@Test
	void comprobarLanzamientoAlReiniciarRonda() {
		assertThrows(UnsupportedOperationException.class, () -> arbitro.reiniciarRonda(),
				"No se debe poder reiniciar ronda en un arbitro simple");
	}

}
