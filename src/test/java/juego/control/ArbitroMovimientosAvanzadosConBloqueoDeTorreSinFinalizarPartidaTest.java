package juego.control;

import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.PURPURA;
import static juego.modelo.Color.ROJO;
import static juego.modelo.Color.VERDE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.MultipleFailuresError;

import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas sobre el árbitro comprobando situaciones de bloqueo sobre
 * el turno actual, sin finalizar partida.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests avanzados de bloqueos en turno actual sin finalizar partida.")
public class ArbitroMovimientosAvanzadosConBloqueoDeTorreSinFinalizarPartidaTest extends ArbitroMovimientosAvanzadosConBloqueos {

	/** Movimientos iniciales incluyendo bloqueo de torre negra. */
	static final String[] PARTIDA_BASICA_1 = { 
			"a1a7", // NEGRAS torre marrón a celda roja en vertical
			"f8f5", // BLANCAS torre roja a celda verde en vertical
			"b1b7", // NEGRAS torre verde a celda naranja en vertical -> TORRE NARANJA BLOQUEADA
					// pierde turno BLANCAS
			"h1h7" // NEGRAS torre naranja a celda púrpura, pasa turno a blancas y no seguimos
					// jugando...
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = { { ROJO, null }, { ROJO, VERDE }, { NARANJA, NARANJA },
			{ PURPURA, NARANJA } };

	/** Movimientos iniciales incluyendo bloqueo de torre blanca. */
	static final String[] PARTIDA_BASICA_2 = { "c1c3", // NEGRAS torre roja a celda marrón en vertical
			"h8h2", // BLANCAS torre marrón a celda roja en vertical
			"c3c4", // NEGRAS torre roja a celda verde en vertical

			"g8g2", // BLANCAS torre verde a celda naranja -> TORRE NARANJA BLOQUEADA
					// pierde turno NEGRAS
			"a8a7" // BLANCAS torre naranja a torre roja, pasa turno a negras y no seguimos jugando
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = { { MARRON, null }, { MARRON, ROJO }, { VERDE, ROJO },
			{ NARANJA, NARANJA }, { NARANJA, ROJO } };

	/**
	 * Inicialización del tablero antes de cada test.
	 */
	@BeforeEach
	void inicializar() {
		tablero = new Tablero();
		arbitro = new ArbitroSimple(tablero);
		arbitro.colocarTorres();
	}

	/**
	 * Proveedor de argumentos con jugadas y ganador.
	 * 
	 * @return coordenadas y torres en fila inferior de torres negras
	 */
	static Stream<Arguments> jugadasYGanadorProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, Turno.BLANCO),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, Turno.NEGRO));
	}

	/**
	 * Prueba partidas que incluyen bloqueos pero no finalizan.
	 * 
	 * @param jugadas		  jugadas incluyendo bloqueos
	 * @param ultimosColores  estados de colores en últimos movimientos para torres
	 *                        negras y blancas
	 * @param turnoFinal	  turno con el que deberían finalizar los movimientos
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 * @throws MultipleFailuresError si hay múltiples fallos
	 */
	@DisplayName("Partidas básicas con bloqueos")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarPartidasBasicas(String[] jugadas, Color[][] ultimosColores, Turno turnoFinal) throws MultipleFailuresError, CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		// Realizamos movimientos que incluyen bloqueos...
		final int numeroJugadas = jugadas.length;
		realizarJugadasConBloqueos(jugadas, ultimosColores);
		// Comprobar estado final tras bloqueos...
		assertAll("Estado tras bloqueos.",
				() -> assertThat("El turno final tras bloqueos es incorrecto.", arbitro.obtenerTurno(), is(turnoFinal)),
				() -> assertThat("El número de jugadas finales es incorrecto", arbitro.obtenerNumeroJugada(),
						is(numeroJugadas + 1)));

	}

	

}
