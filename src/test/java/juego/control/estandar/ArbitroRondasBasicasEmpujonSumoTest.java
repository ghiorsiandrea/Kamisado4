package juego.control.estandar;

import static juego.modelo.Color.AMARILLO;
import static juego.modelo.Color.AZUL;
import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.PURPURA;
import static juego.modelo.Color.ROJO;
import static juego.modelo.Color.ROSA;
import static juego.modelo.Color.VERDE;
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
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas unitarias sobre el árbitro con movimientos de empujon sumo.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.1 20211129
 * 
 */
@DisplayName("Tests del árbitro estándar con empujón sumo sin finalizar partida.")
public class ArbitroRondasBasicasEmpujonSumoTest {

	/** Coordenadas de partida básica 1. */
	static final String[] PARTIDA_BASICA_1 = {
		//  @formatter:off
		"d1d4", // NEGRAS torre amarilla a celda marrón en vertical
		"h8h5", // BLANCAS torre marrón a celda amarilla en vertical
		"d4h8", // NEGRAS torre amarilla a celda marrón -> GANA RONDA Y PROMOCIONA A SUMO
		"reiniciarRonda",  // REINICIAMOS RONDA
		"e8h5", // BLANCAS torre amarilla a celda amarilla en diagonal
		"d1d6", // NEGRAS torre sumo amarilla a celda roja
		"f8g7", // BLANCAS torre roja a celda marrón en diagonal
		"a1a5", // NEGRAS torre marrón a celda rosa en vertical
		"d8d7", // BLANCAS torre rosa a celda verde en vertical				
		"b1b5", // NEGRAS torre verde a celda púrpura en vertical
		"c8c2", // BLANCAS torre púrpura a celda amarilla en vertical
		"d6"	// NEGRAS torre SUMO amarilla EMPUJA a torre rosa a celda rosa
		//  @formatter:on
	};

	/**  Últimos colores de partida básica 1. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = {
		//  @formatter:off
		{ MARRON, null }, 
		{ MARRON, AMARILLO }, 
		{ MARRON, AMARILLO },
		{ null, null},
		{ null, AMARILLO }, 	// e8h5
		{ ROJO, AMARILLO },		// d1d6
		{ ROJO, MARRON },		// f8g7
		{ ROSA, MARRON }, 		// a1a5
		{ ROSA, VERDE }, 		// d8d7		
		{ PURPURA, VERDE },		// b1b5
		{ PURPURA, AMARILLO },	// c8c2
		{ PURPURA, ROSA}		// d6 empujón sumo
		//  @formatter:on

	};

	/** Coordenadas de partida básica 2. */
	static final String[] PARTIDA_BASICA_2 = {
		//  @formatter:off
		"d1d4", // NEGRAS torre amarilla a celda marrón en vertical
		"h8h5", // BLANCAS torre marrón a celda amarilla en vertical
		"d4h8", // NEGRAS torre amarilla a celda marrón -> GANA RONDA Y PROMOCIONA A SUMO
		"reiniciarRonda",  // REINICIAMOS RONDA
		"g8g7", // BLANCAS torre verde a celda marrón en vertical
		"a1a6", // NEGRAS torre marrón a celda verde en vertical
		"g7a1", // BLANCAS torre verde a celda marrón -> GANA RONDA Y PROMOCIONA A SUMO
		"reiniciarRonda",
		"h1h3", // NEGRAS torre naranja a celda verde vertical
		"g8g4", // BLANCAS torre verde a celda purpura en vertical
		"f1f2", // NEGRAS torre purpura a celda rosa en vertical
		"d8e7", // BLANCAS torre rosa a celda azul en diagonal
		"g1g3", // NEGRAS torre azul a celda rosa en vertical
		"e7f6", // BLANCAS torre rosa a celda marrón en diagonal
		"a1a6", // NEGRAS torre marrón a celda verde en vertical
		"g4" // BLANCAS torre SUMO verde EMPUJA a torre azul a celda naranja	
		//  @formatter:on
	};

	/** Últimos colores de partida básica 2. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = {
		//  @formatter:off
		{ MARRON, null }, 		// d1d4
		{ MARRON, AMARILLO },   // h8h5
		{ MARRON, AMARILLO },   // d4h8
		{ null, null},			// reiniciarRonda
		{ null, MARRON }, 		// g8g7
		{ VERDE, MARRON },		// a1a6
		{ VERDE, MARRON }, 		// g7a1
		{ null, null},			// reiniciarRonda
		{ VERDE, null },		// h1h3
		{ VERDE, PURPURA }, 	// g8g4
		{ ROSA, PURPURA }, 	 	// f1f2
		{ ROSA, AZUL },     	// d8e7
		{ ROSA, AZUL },     	// g1g3
		{ ROSA, MARRON },   	// e7f6
		{ VERDE, MARRON },  	// a1a6
		{ NARANJA, MARRON}		// d4 empujón sumo
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
	 * @return argumentos con jugadas, colores, ganador, puntuaciones, celdas con 
	 * torres desplazadas y colores correspondientes
	 */
	static Stream<Arguments> jugadasYGanadorProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, null, 1, 0, "d8", "d7", Color.ROSA,
						Color.AMARILLO),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, null, 1, 1, "g3", "g2",
						Color.VERDE, Color.AZUL));
	}

	/**
	 * Comprueba partidas con empujón sumo.
	 * 
	 * @param jugadas jugadas
	 * @param ultimosColores últimos colores
	 * @param ganador ganador
	 * @param puntuacionNegro puntuación de turno negro
	 * @param puntuacionBlanco puntuación de turno blanco
	 * @param textoDesplazada notación algebraica de celda con torre desplazada
	 * @param textoSumo notación algebraica de celda con torre sumo
	 * @param colorDesplazada color de celda desplazada
	 * @param colorSumo color de sumo
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Comprueba partidas con empujón sumo")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarPartidasBasicas(String[] jugadas, Color[][] ultimosColores, Turno ganador, int puntuacionNegro,
			int puntuacionBlanco, String textoDesplazada, String textoSumo, Color colorDesplazada, Color colorSumo) throws CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		int cont = 0;
		for (String jugada : jugadas) {			
			if (jugada.equals("reiniciarRonda")) {
				assertAll("Estado antes de reiniciar ronda",
						() -> assertThat("La ronda no está ganada antes de reiniciar ronda.", arbitro.estaAcabadaRonda(),is(true)),
						() -> assertThat("No hay ganador de la ronda antes de reiniciar ronda.", arbitro.consultarGanadorRonda(), is(notNullValue())));
				arbitro.reiniciarRonda();
			} else if (jugada.length() == 2) {
				String textoOrigen = jugada.substring(0, 2);
				Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
				assertThat(arbitro.esEmpujonSumoLegal(origen), is(true));
				arbitro.empujarSumo(origen);
				assertThat("Último color de turno negro incorrecto en movimiento " + (cont + 1) + " con " + jugada + ".",
						arbitro.obtenerUltimoMovimiento(Turno.NEGRO), is(ultimosColores[cont][0]));
				assertThat("Último color de turno blanco incorrecto en movimiento " + (cont + 1) + " con " + jugada + ".",
						arbitro.obtenerUltimoMovimiento(Turno.BLANCO), is(ultimosColores[cont][1]));	
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
			}
			cont++;
		}
		Celda celdaDesplazada = tablero.obtenerCeldaParaNotacionAlgebraica(textoDesplazada);
		Celda celdaSumo = tablero.obtenerCeldaParaNotacionAlgebraica(textoSumo);
		// Asertos
		assertAll("Empujón sumo",
				() -> assertThat("Torre de turno contrario desplazada incorrectamente.",
						celdaDesplazada.obtenerColorDeTorre(), is(colorDesplazada)),
				() -> assertThat("Torre sumo de turno actual que empuja incorrectamente desplazada.",
						celdaSumo.obtenerColorDeTorre(), is(colorSumo)),
				() -> assertThat("Número de torres blancas incorrecto después de empujón sumo.",
						tablero.obtenerNumeroTorres(Turno.BLANCO), is(8)),
				() -> assertThat("Número de torres negras incorrecto después de empujón sumo.",
						tablero.obtenerNumeroTorres(Turno.NEGRO), is(8)));

		assertAll("Estado de ronda ganada.",
				() -> assertThat("Puntuación de turno negro incorrecta.", arbitro.obtenerPuntuacionTurnoNegro(),
						is(puntuacionNegro)),
				() -> assertThat("Puntuación de turno blanco incorrecta.", arbitro.obtenerPuntuacionTurnoBlanco(),
						is(puntuacionBlanco)),
				() -> assertThat("El turno ganador de la ronda es incorrecto (no ha finalizado la ronda).",
						arbitro.consultarGanadorRonda(), is(ganador)),
				() -> assertThat("El turno ganador de la partida es incorrecto (no ha finalizado la partida).",
						arbitro.consultarGanadorPartida(), is(ganador)));
	}

}
