package juego.control;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import juego.modelo.Celda;
import juego.modelo.Tablero;
import juego.modelo.Turno;

/**
 * Pruebas unitarias sobre el estado inicial del árbitro sin torres. 
 * 
 * Este conjunto de pruebas se centra en verificar el estado del 
 * árbitro justo después de instanciar SIN colocar torres sobre el tablero.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos sobre el estado inicial sin torres colocadas")
public class ArbitroEstadoBasicoInicialSinTorresTest {

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
	 * Comprobacion de inicialización correcta del tablero, sin colocar ninguna
	 * torre, con un tablero vacío y sin turno incialmente.
	 */
	// @formatter:off
	 /* Partimos de un tablero vacío como el que se muestra:
	 *   a b c d e f g h  
	 * 8 - - - - - - - - 
	 * 7 - - - - - - - - 
	 * 6 - - - - - - - - 
	 * 5 - - - - - - - - 
	 * 4 - - - - - - - - 
	 * 3 - - - - - - - - 
	 * 2 - - - - - - - - 
	 * 1 - - - - - - - -
	 */
	 // @formatter:on
	@DisplayName("Estado inicial del tablero vacío")
	@Test
	void comprobarEstadoInicialConTablero() {
		assertAll("Estado inicial.",
				() -> assertThat("El número de jugadas debería ser cero", arbitro.obtenerNumeroJugada(),is(0)),
				() -> assertThat("No debería haber torres negras", tablero.obtenerNumeroTorres(Turno.BLANCO), is(0)),
				() -> assertThat("No debería haber torres blancas", tablero.obtenerNumeroTorres(Turno.NEGRO), is(0)),
				() -> assertNull(arbitro.obtenerTurno(), "El turno no se asigna hasta colocar las torres"),
				() -> assertThat("La ronda no puede estar ganada al iniciarse sin torres.", arbitro.estaAcabadaRonda(), is(false)),
				() -> assertThat("La partida no puede estar ganada al iniciarse sin torres.", arbitro.estaAcabadaPartida(), is(false)),
				() -> assertThat("No puede estar bloqueado el turno actual al iniciarse sin torres.", arbitro.estaBloqueadoTurnoActual(), is(false)));
	}

}
