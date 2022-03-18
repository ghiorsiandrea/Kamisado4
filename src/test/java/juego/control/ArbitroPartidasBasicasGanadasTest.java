package juego.control;

import static juego.modelo.Color.AMARILLO;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.PURPURA;
import static juego.modelo.Color.ROJO;
import static juego.modelo.Color.ROSA;
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
public class ArbitroPartidasBasicasGanadasTest {

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_1 = { "c1c6", // NEGRAS torre roja a celda naranja en vertical
			"a8a7", // BLANCAS torre naranja a celda roja en vertical
			"c6a8" // NEGRAS torre roja a celda naranja en diagonal -> GANA PARTIDA
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = { 
			{ NARANJA, null }, 
			{ NARANJA, ROJO },
			{ NARANJA, ROJO } };

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_2 = { "a1a7", // NEGRAS torre marrón a celda roja en vertical
			"f8f2", // BLANCAS torre roja a celda rosa en vertical
			"e1e3", // NEGRAS torre rosa a celda roja en vertical
			"f2e1" // BLANCAS torre roja a celda rosa en diagonal -> GANA PARTIDA
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = { 
			{ ROJO, null }, 
			{ ROJO, ROSA },
			{ ROJO, ROSA },
			{ ROJO, ROSA } };

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_3 = { "g1g5", // NEGRAS torre azul a celda roja en vertical
			"f8f2", // BLANCAS torre roja a celda rosa en vertical
			"e1e3", // NEGRAS torre rosa a celda roja en vertical
			"f2e1" // BLANCAS torre roja a celda rosa en diagonal -> GANA PARTIDA
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_3_ULTIMOS_COLORES = { 
			{ ROJO, null },
			{ ROJO, ROSA }, 
			{ ROJO, ROSA },
			{ ROJO, ROSA } };

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_4 = { "e1e6", // NEGRAS torre rosa a celda purpura en vertical
			"c8c2", // BLANCAS torre púrpura a celda amarilla en vertical
			"d1d6", // NEGRAS torre amarilla a celda roja en vertical
			"f8f2", // BLANCAS torre roja a celda rosa en vertical
			"e6c8" // NEGRAS torre rosa a celda púrpura -> GANA PARTIDA
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_4_ULTIMOS_COLORES = { 
			{ PURPURA, null }, 
			{ PURPURA, AMARILLO },
			{ ROJO, AMARILLO }, 
			{ ROJO, ROSA }, 
			{ PURPURA, ROSA } };

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
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, Turno.NEGRO),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, Turno.BLANCO),
				arguments(PARTIDA_BASICA_3, PARTIDA_BASICA_3_ULTIMOS_COLORES, Turno.BLANCO),
				arguments(PARTIDA_BASICA_4, PARTIDA_BASICA_4_ULTIMOS_COLORES, Turno.NEGRO));
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
					() -> assertThat("No hay bloqueos de turno.", arbitro.estaBloqueadoTurnoActual(), is(false)),
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
				() -> assertThat("El número de jugadas finales es incorrecto", arbitro.obtenerNumeroJugada(),
						is(numeroJugadas)));
	}

}
