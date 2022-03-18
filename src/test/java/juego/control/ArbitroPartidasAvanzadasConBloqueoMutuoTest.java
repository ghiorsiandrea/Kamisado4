package juego.control;

import static juego.modelo.Color.AZUL;
import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.PURPURA;
import static juego.modelo.Color.ROJO;
import static juego.modelo.Color.VERDE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
 * Pruebas de situación de bloqueo mutuo o deadlock, con partida finalizada.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests avanzados de detección del bloqueo mutuo o deadlock.")
public class ArbitroPartidasAvanzadasConBloqueoMutuoTest extends ArbitroMovimientosAvanzadosConBloqueos {

	// @formatter:off
	/** Movimientos iniciales provocando bloqueo mutuo o deadlock provocado por turno negro. */
	static final String[] PARTIDA_BASICA_1 = { 
			"a1a7", // NEGRAS torre marrón a celda roja en vertical
			"f8f5", // BLANCAS torre roja a celda verde en vertical
			"b1b7", // NEGRAS torre verde a celda naranja en vertical -> TORRE NARANJA BLANCA BLOQUEADA
					// pierde turno
			"h1h7", // NEGRAS torre naranja a celda púrpura en vertical,
			"c8c5", // BLANCAS torre púrpura a celda azul en vertical,
			"g1g7", // NEGRAS torre azul a celda marrón en vertical -> TORRE MARRON BLANCA BLOQUEADA
					// y TORRE MARRON NEGRA BLOQUEADA

	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = { 
			{ ROJO, null }, 
			{ ROJO, VERDE }, 
			{ NARANJA, NARANJA },
			{ PURPURA, NARANJA }, 
			{ PURPURA, AZUL }, 
			{ MARRON, MARRON } };
	
	/** Movimientos iniciales provocando bloqueo mutuo o deadlock provocado por turno blanco. */
	static final String[] PARTIDA_BASICA_2 = { 
			"d1d4", // NEGRAS torre amarilla a celda marrón en vertical
			"h8h2", // BLANCAS torre marrón a celda roja en vertical
			"c1c4", // NEGRAS torre roja a celda verde en vertical
			"g8g2", // BLANCAS torre verde a celda naranaja ->  TORRE NARANJA BLANCA BLOQUEADA pierde turno
			"a8a2", // BLANCAS torre naranja a celda púrpura vertical
			"f1f4", // NEGRAS torre negra a celda azul en vertical
			"b8b2", // BLANCAS torre azul a celda marrón en vertical -> TORRE MARRON NEGRA BLOQUEADA y TORRE MARRON BLANCA BLOQUEADA
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = { 
			{ MARRON, null }, 
			{ MARRON, ROJO }, 
			{ VERDE, ROJO },
			{ NARANJA, NARANJA }, 
			{ NARANJA, PURPURA },
			{ AZUL, PURPURA },
			{ MARRON, MARRON }
			};

	// @formatter:on

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
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, Turno.NEGRO),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, Turno.BLANCO));
	}

	/**
	 * Prueba la situación de bloqueo mutuo o deadlock.
	 * 
	 * @param jugadas        jugadas finalizando en bloqueo mutuo o deadlock
	 * @param ultimosColores estados de colores en últimos movimientos para torres
	 *                       negras y blancas
	 * @param turnoFinal     turno que realiza el último movimiento provocando el
	 *                       bloqueo mutuo o deadlock
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 * @throws MultipleFailuresError si hay múltiples fallos
	 */
	@DisplayName("Comprueba partidas finalizadas con bloqueo mutuo o deadlock.")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarPartidasBasicas(String[] jugadas, Color[][] ultimosColores, Turno turnoFinal) throws MultipleFailuresError, CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		// Realizamos movimientos que llevan a situación de bloqueo mutuo o deadlock...
		final int numeroJugadas = jugadas.length;
		realizarJugadasConBloqueos(jugadas, ultimosColores);
		// Comprobar estado final tras bloqueo mutuo o deadlock...
		assertAll("Estado final tras bloqueo mutuo o deadlock",
				() -> assertThat("No encuentra torre en fila contraria.", arbitro.estaAlcanzadaUltimaFilaPor(turnoFinal), is(false)),
				() -> assertThat("No se detecta la situación de bloqueo mutuo o deadlock.", arbitro.hayBloqueoMutuo(), is(true)),
				() -> assertThat("El turno actual es incorrecto.", arbitro.obtenerTurno(), is(turnoFinal)),
				() -> assertThat("El ganador de la ronda en el bloqueo mutuo o deadlock no es correcto.", arbitro.consultarGanadorRonda(),
						not(turnoFinal)),
				() -> assertThat("El ganador de la partida en el bloqueo mutuo o deadlock no es correcto.", arbitro.consultarGanadorPartida(),
						not(turnoFinal)),
				() -> assertThat("El número de jugadas finales es incorrecto", arbitro.obtenerNumeroJugada(),
						is(numeroJugadas + 2)));

	}

}
