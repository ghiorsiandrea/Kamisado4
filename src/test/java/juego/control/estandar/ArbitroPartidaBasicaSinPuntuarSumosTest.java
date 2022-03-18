package juego.control.estandar;

import static juego.modelo.Color.AMARILLO;
import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.PURPURA;
import static juego.modelo.Color.ROJO;
import static juego.modelo.Color.VERDE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import juego.control.Arbitro;
import juego.control.ArbitroEstandar;
import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.TorreSumoUno;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas unitarias sobre una partida ganada solo puntuando torres sin dientes.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * 
 */
@DisplayName("Tests del árbitro estándar con victoria sin puntuar con sumos.")
public class ArbitroPartidaBasicaSinPuntuarSumosTest {

	/** Coordenadas de partida básica 1. */
	static final String[] PARTIDA_BASICA_1 = {
			//  @formatter:off
			"d1d4", // NEGRAS torre amarila a celda marrón en vertical
			"h8h5", // BLANCAS torre marrón a celda amarilla en vertical
			"d4h8", // NEGRAS torre amarilla a celda marrón -> GANA RONDA
			"reiniciarRonda",
			"d8d3", // BLANCAS torre rosa a celda púrpura en vertical
			"f1f6", // NEGRAS torre purpura a celda marrón en vertical
			"h8h7", // BLANCAS torre marrón a celda púrpura en vertical
			"f6h8", // NEGRAS torre púrpura a celda marrón -> GANA RONDA
			"reiniciarRonda",
			"b8b4", // BLANCAS torre azul a celda roja en vertical
			"c1c3", // NEGRAS torre roja a celda marrón en vertical
			"h8h2", // BLANCAS torre marrón a celda roja en vertical
			"c3h8", // NEGRAS torre roja a celda marrón en diagonal -> GANA RONDA Y PARTIDA
			//  @formatter:on
	};

	/** Últimos colores de partida básica 1. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = {
			//  @formatter:off
			{ MARRON, null },
			{ MARRON, AMARILLO},
			{ MARRON, AMARILLO},
			{ null, null },
			{ null, PURPURA },
			{ MARRON, PURPURA },
			{ MARRON, PURPURA },
			{ MARRON, PURPURA },
			{ null, null},
			{ null, ROJO },
			{ MARRON, ROJO },
			{ MARRON, ROJO },
			{ MARRON, ROJO }			
			//  @formatter:on

	};

	/** Coordenadas de partida básica 2. */
	static final String[] PARTIDA_BASICA_2 = {
			//  @formatter:off
			"e1e3", // NEGRAS torre rosa a celda roja en vertical	
			"f8f6", // BLANCAS torre roja a celda marrón en vertical
			"a1a7", // NEGRAS torre marrón a celda roja en vertical
			"f6a1", // BLANCAS torre roja a celda marrón en diagonal -> GANA RONDA
			"reiniciarRonda",
			"e1e2", // NEGRAS torre rosa a celda verde en vertical
			"g8g7", // BLANCAS torre verde a celda marrón en vertical
			"a1a6", // NEGRAS torre marrón a celda verde en vertical
			"g7a1", // BLANCAS torre verde a celda marrón en diagonal -> GANA RONDA
			"reiniciarRonda",
			"e1e6", // NEGRAS torre rosa a celda púrpura en vertical
			"c8c3", // BLANCAS torre purpura a celda marron en vertical
			"a1a2", // NEGRAS torre marrón a celda púrpura en vertical
			"c3a1"  // BLANCAS torre púrpura a celda marrón -> GANAD RONDA Y PARTIDA
			//  @formatter:on
	};

	/** Últimos colores de partida básica 2. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = {
			//  @formatter:off
			{ ROJO, null },
			{ ROJO, MARRON },
			{ ROJO, MARRON },
			{ ROJO, MARRON},
			{ null, null },
			{ VERDE, null},
			{ VERDE, MARRON },
			{ VERDE, MARRON },
			{ VERDE, MARRON },
			{ null, null },
			{ PURPURA, null },
			{ PURPURA , MARRON },
			{ PURPURA, MARRON },
			{ PURPURA, MARRON }			
			//  @formatter:on

	};

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
		arbitro = new ArbitroEstandar(tablero);
		arbitro.colocarTorres();
	}

	/**
	 * Proveedor de argumentos con jugadas y ganador.
	 * 
	 * @return argumentos con jugadas, colores, ganador y puntuaciones
	 */
	static Stream<Arguments> jugadasYGanadorProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, Turno.NEGRO, 3, 0),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, Turno.BLANCO, 0, 3));
	}

	/**
	 * Comprueba partidas básicas completas ganadas sin puntuar sumos.
	 * 
	 * @param jugadas jugadas
	 * @param ultimosColores últimos colores
	 * @param ganador ganador
	 * @param puntuacionNegro puntuación de turno negro
	 * @param puntuacionBlanco puntuación de turno blanco
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Comprueba partidas básicas completas ganadas sin puntuar con sumos")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarPartidasBasicas(String[] jugadas, Color[][] ultimosColores, Turno ganador, int puntuacionNegro,
			int puntuacionBlanco) throws CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		int cont = 0;
		Celda ultimaCelda = null;
		for (String jugada : jugadas) {
			if (jugada.equals("reiniciarRonda")) {
				assertAll("Estado antes de reiniciar ronda",
						() -> assertThat("La ronda no está ganada antes de reiniciar ronda.",
								arbitro.estaAcabadaRonda(), is(true)),
						() -> assertThat("No hay ganador de la ronda antes de reiniciar ronda.",
								arbitro.consultarGanadorRonda(), is(notNullValue())));
				arbitro.reiniciarRonda();
			} else {
				String textoOrigen = jugada.substring(0, 2);
				String textoDestino = jugada.substring(2, 4);
				Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
				Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);
				assertThat("Moviendo de " + textoOrigen + " a " + textoDestino,
						arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(true));
				arbitro.moverConTurnoActual(origen, destino);
				if (arbitro.estaBloqueadoTurnoActual()) {
					// arbitro.cambiarTurno();
				}
				assertThat(
						"Último color de turno negro incorrecto en movimiento " + (cont + 1) + " con " + jugada + ".",
						arbitro.obtenerUltimoMovimiento(Turno.NEGRO), is(ultimosColores[cont][0]));
				assertThat(
						"Último color de turno blanco incorrecto en movimiento " + (cont + 1) + " con " + jugada + ".",
						arbitro.obtenerUltimoMovimiento(Turno.BLANCO), is(ultimosColores[cont][1]));
				ultimaCelda = destino;
			}
			cont++;
		}
		final Celda celda = ultimaCelda;
		// Asertos
		assertAll("Estado de ronda y partida ganada.",
				() -> assertThat("La torre debería ser una torre sumo de un diente.", celda.obtenerTorre(),
						instanceOf(TorreSumoUno.class)),
				() -> assertThat("Puntuación de turno negro incorrecta.", arbitro.obtenerPuntuacionTurnoNegro(),
						is(puntuacionNegro)),
				() -> assertThat("Puntuación de turno blanco incorrecta.", arbitro.obtenerPuntuacionTurnoBlanco(),
						is(puntuacionBlanco)),
				() -> assertThat("El turno ganador de la ronda es incorrecto.", arbitro.consultarGanadorRonda(),
						is(ganador)),
				() -> assertThat("La ronda está ganada", arbitro.estaAcabadaRonda(), is(true)),
				() -> assertThat("La partida está ganada", arbitro.estaAcabadaPartida(), is(true)),
				() -> assertThat("El turno ganador de la partida es incorrecto.", arbitro.consultarGanadorPartida(),
						is(ganador)));
	}

}
