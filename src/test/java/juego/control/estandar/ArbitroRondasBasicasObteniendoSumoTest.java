package juego.control.estandar;

import static juego.modelo.Color.AMARILLO;
import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.ROJO;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
 * Pruebas unitarias de promoción de torre a torre sumo sin finalizar partida.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * 
 */
@DisplayName("Tests del árbitro estándar con rondas simples y promoción a sumo sin finalizar partida.")
public class ArbitroRondasBasicasObteniendoSumoTest {

	/** Coordenadas de partida básica 1. */
	static final String[] PARTIDA_BASICA_1 = { 
		//  @formatter:off
		"c1c3", // NEGRAS torre roja a celda marrón en vertical
		"h8h2", // BLANCAS torre marrón a celda roja en vertical
		"c3h8" // NEGRAS torre roja a celda marrón -> GANA RONDA Y PROMOCIONA A SUMO
		//  @formatter:on
	};

	/** Últimos colores de partida básica 1. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = { 
		//  @formatter:off
		{ MARRON, null }, 
		{ MARRON, ROJO }, 
		{ MARRON, ROJO }
		//  @formatter:on

	};

	/** Coordenadas de partida básica 3. */
	static final String[] PARTIDA_BASICA_3 = { 
		//  @formatter:off
		"c1c3", // NEGRAS torre roja a celda marrón en vertical
		"h8h2", // BLANCAS torre marrón a celda roja en vertical
		"c3h8", // NEGRAS torre roja a celda marrón -> GANA RONDA Y PROMOCIONA A SUMO
		"reiniciarRonda",  // REINICIAMOS RONDA
		"e8e4", // BLANCAS torre amarilla a celda naranja en vertical
		"h1h5", // NEGRAS torre naranja a celda amarilla en vertical,
		"e4h1" // BLANCAS torre amarilla a celda naranja en diagonal -> GANA RONDA Y PROMOCION A SUMO
		//  @formatter:on
	};

	/** Últimos colores de partida básica 3. */
	static final Color[][] PARTIDA_BASICA_3_ULTIMOS_COLORES = {
		//  @formatter:off
		{ MARRON, null }, 
		{ MARRON, ROJO }, 
		{ MARRON, ROJO },
		{ null, null }, 
		{ null, NARANJA }, 
		{ AMARILLO, NARANJA }, 
		{ AMARILLO, NARANJA }
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
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, Turno.NEGRO, 1, 0),
				arguments(PARTIDA_BASICA_3, PARTIDA_BASICA_3_ULTIMOS_COLORES, Turno.BLANCO, 1, 1));
	}

	/**
	 * Comprueba partidas básicas con ronda ganada obteniendo sumo.
	 * 
	 * @param jugadas jugadas
	 * @param ultimosColores últimos colores
	 * @param ganador ganador
	 * @param puntuacionNegro puntuación de turno negro
	 * @param puntuacionBlanco puntuación de turno blanco
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Comprueba partidas básicas con ronda ganada obteniendo sumo")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarPartidasBasicas(String[] jugadas, Color[][] ultimosColores, Turno ganador, int puntuacionNegro,
			int puntuacionBlanco) throws CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		final int numeroJugadas = jugadas.length;
		int cont = 0;
		Celda ultimaCelda = null;
		for (String jugada : jugadas) {
			if (jugada.equals("reiniciarRonda")) {
				assertAll("Estado antes de reiniciar ronda",
						() -> assertThat("La ronda no está ganada antes de reiniciar ronda.", arbitro.estaAcabadaRonda(),is(true)),
						() -> assertThat("No hay ganador de la ronda antes de reiniciar ronda.", arbitro.consultarGanadorRonda(), is(notNullValue())));
				arbitro.reiniciarRonda();
			} else {
				String textoOrigen = jugada.substring(0, 2);
				String textoDestino = jugada.substring(2, 4);
				Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
				Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);
				assertThat("Moviendo de " + textoOrigen + " a " + textoDestino,
						arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(true));
				arbitro.moverConTurnoActual(origen, destino);
				assertThat("Último color de turno negro incorrecto en movimiento " + (cont + 1) + " con " + jugada + ".",
						arbitro.obtenerUltimoMovimiento(Turno.NEGRO), is(ultimosColores[cont][0]));
				assertThat("Último color de turno blanco incorrecto en movimiento " + (cont + 1) + " con " + jugada + ".",
						arbitro.obtenerUltimoMovimiento(Turno.BLANCO), is(ultimosColores[cont][1]));					
				ultimaCelda = destino;
			}
			cont++;
		}
		final Celda celda = ultimaCelda;
		// Asertos
		assertAll("Estado de ronda ganada.",
				() -> assertThat("La torre debería ser una torre sumo de un diente.", celda.obtenerTorre(),
						instanceOf(TorreSumoUno.class)),
				() -> assertThat("Puntuación de turno negro incorrecta.", arbitro.obtenerPuntuacionTurnoNegro(),
						is(puntuacionNegro)),
				() -> assertThat("Puntuación de turno blanco incorrecta.", arbitro.obtenerPuntuacionTurnoBlanco(),
						is(puntuacionBlanco)),
				() -> assertThat("El turno ganador de la ronda es incorrecto.", arbitro.consultarGanadorRonda(),
						is(ganador)),
				() -> assertThat(
						"El turno ganador de la partida es incorrecto (no ha finalizado la partida todavía, no debe haber ganador).",
						arbitro.consultarGanadorPartida(), not(ganador)));
	}

}
