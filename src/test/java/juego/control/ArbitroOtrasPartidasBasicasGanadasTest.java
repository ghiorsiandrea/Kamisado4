package juego.control;

import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.ROJO;
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

import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas sobre el árbitro ganando la partida tras una serie de movimientos básicos.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos con partida ganada.")
public class ArbitroOtrasPartidasBasicasGanadasTest {

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_1 = { 
			"c1c3", // NEGRAS torre roja a celda marrón en vertical
			"h8h2", // BLANCAS torre marrón a celda roja en vertical
			"c3h8" // NEGRAS torre roja a celda marron en diagonal -> GANA PARTIDA
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = { 
			{ MARRON, null }, 
			{ MARRON, ROJO },
			{ MARRON, ROJO } };


	/** Tablero para testing. */
	private Tablero tablero;

	/** Arbitro. */
	private Arbitro arbitro;

	/**
	 * Inicialización del tablero antes de cada test.
	 */
	@BeforeEach
	void inicializar() {
		tablero = new Tablero();
		arbitro = new ArbitroSimple(tablero);
		arbitro.colocarTorres(); // colocamos torres
	}

	/**
	 * Proveedor de argumentos con jugadas y ganador.
	 * 
	 * @return coordenadas y torres en fila inferior de torres negras
	 */
	static Stream<Arguments> jugadasYGanadorProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, Turno.NEGRO));
	}

	/**
	 * Prueba la finalización de partida una vez realizados unos movimientos básicos.
	 * 
	 * @param jugadas        jugadas
	 * @param ultimosColores estados de colores en últimos movimientos para torres
	 *                       negras y blancas
	 * @param ganador        turno ganador al finalizar la partida
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Partidas ganadas con movimientos básicos.")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarPartidasBasicas(String[] jugadas, Color[][] ultimosColores, Turno ganador) throws CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		// Realizamos movimientos...
		final int numeroJugadas = jugadas.length;
		int cont = 0;
		for (String jugada : jugadas) {
			Celda origen = tablero.obtenerCeldaOrigenEnJugada(jugada);
			Celda destino = tablero.obtenerCeldaDestinoEnJugada(jugada);
			assertThat("La jugada " + jugada + " no es legal", arbitro.esMovimientoLegalConTurnoActual(origen, destino),
					is(true));
			arbitro.moverConTurnoActual(origen, destino);
			final int auxCont = cont;
			// Comprobamos estados intermedios de la partida...
			assertAll("Jugadas legales hasta ganar partida",					
					() -> assertThat("No debería haber situación de bloqueo mutuo.", arbitro.hayBloqueoMutuo(), is(false)),
					() -> assertThat("Último color de turno negro incorrecto en movimiento " + (auxCont + 1) + ".",
							arbitro.obtenerUltimoMovimiento(Turno.NEGRO), is(ultimosColores[auxCont][0])),
					() -> assertThat("Último color de turno blanco incorrecto en movimiento " + (auxCont + 1) + ".",
							arbitro.obtenerUltimoMovimiento(Turno.BLANCO), is(ultimosColores[auxCont][1])));
			cont++;
		}
	
		// Comprobamos el estado final con partida ganada...
		assertAll("Estado actual correcto tras partida ganada",
				() -> assertThat("No encuentra torre en fila contraria.", arbitro.estaAlcanzadaUltimaFilaPor(ganador), is(true)),
				() -> assertThat("El turno ganador es incorrecto.", arbitro.consultarGanadorPartida(), is(ganador)),
				() -> assertThat("La ronda debería estar acabada.", arbitro.estaAcabadaRonda(), is(true)),
				() -> assertThat("La partida debería estar acabada.", arbitro.estaAcabadaPartida(), is(true)),
				() -> assertThat("No debería haber situación de bloqueo mutuo.", arbitro.hayBloqueoMutuo(), is(false)),
				() -> assertThat("El número de jugadas finales es incorrecto", arbitro.obtenerNumeroJugada(),
						is(numeroJugadas)));
	}

}
