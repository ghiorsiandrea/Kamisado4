package juego.control;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
import juego.modelo.Torre;
import juego.modelo.TorreSimple;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas comprobando situaciones de movimiento legal en
 * diagonal entre torres contrarias.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos de movimientos legales en diagonal entre torres contrarias sin finalizar partida.")
public class ArbitroMovimientosBasicosLegalesEnDiagonalEntreTorresContrariasTest {

	//  @formatter:off
	/** Torres a colocar inicialmente. */
	// Con tres torres blancas moviendo una negra entre ellas
	static final Torre[] TORRES_PARTIDA_1 = {
			new TorreSimple(Turno.BLANCO, Color.AMARILLO),
			new TorreSimple(Turno.BLANCO, Color.ROJO),
			new TorreSimple(Turno.BLANCO, Color.AZUL),
			new TorreSimple(Turno.NEGRO, Color.MARRON)			
	};
	
	/*
	 * --------
	 * --------
	 * --------
	 * --------
	 * --------
	 * -----B--
	 * ----BNB-
	 * --------
	 */
	/** Coordenadas donde colocar las torres. */
	static final String[] COORDENADAS_INICIO_PARTIDA_1 =  {
		"f3",
		"e2",
		"g2",
		"f2", // TORRE NEGRA			
	};
	
	/** Celdas destino a donde intentar mover la torre. */
	static final String[] DESTINOS_PARTIDA_1 = {
		"e3",
		"g3",
		"c5",
		"h4"
	};
	
	/** Torres a colocar inicialmente. */
	// Con tres torres negras moviendo una blanca entre ellas
	static final Torre[] TORRES_PARTIDA_2 = {
			new TorreSimple(Turno.NEGRO, Color.AMARILLO),
			new TorreSimple(Turno.NEGRO, Color.ROJO),
			new TorreSimple(Turno.NEGRO, Color.AZUL),
			new TorreSimple(Turno.BLANCO, Color.MARRON)			
	};
	
	/*
	 * --------
	 * --------
	 * -NBN----
	 * --N-----
	 * --------
	 * --------
	 * --------
	 * --------
	 */
	/** Coordenadas donde colocar las torres. */
	static final String[] COORDENADAS_INICIO_PARTIDA_2 =  {
		"b6",
		"d6",
		"c5",
		"c6", // TORRE BLANCA			
	};
	
	/** Celdas destino a donde intentar mover la torre. */
	static final String[] DESTINOS_PARTIDA_2 = {
		"b5",
		"d5",
		"a4",
		"f3"
	};
//  @formatter:on

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
	}

	/**
	 * Proveedor de argumentos con torres, coordenadas, colores y turno.
	 * 
	 * @return argumentos con torres, coordenadas iniciales, jugadas, color ultimo
	 *         movimiento de torres negras, color de último movimiento de torres
	 *         blancas y turno
	 */
	static Stream<Arguments> datosProveedor() {
		return Stream.of(
				arguments(TORRES_PARTIDA_1, COORDENADAS_INICIO_PARTIDA_1, DESTINOS_PARTIDA_1, Color.AMARILLO,
				Color.MARRON, Turno.NEGRO),
				arguments(TORRES_PARTIDA_2, COORDENADAS_INICIO_PARTIDA_2, DESTINOS_PARTIDA_2, Color.MARRON,
						Color.ROJO, Turno.BLANCO));
	}

	/**
	 * Comprueba los movimientos en diagonal entre torres contrarias, que NO bloquean.
	 * 
	 * @param torres torres a colocar
	 * @param coordenadasIniciales coordenadas iniciales de las torres
	 * @param destinos destino a mover la torre del turno actual
	 * @param ultimoColorTurnoNegro último color de celda de turno negro
	 * @param ultimoColorTurnoBlanco último color de celda de turno blanco
	 * @param turno turno actual
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Movimientos legales en diagonal entre torres contrarias.")
	@ParameterizedTest
	@MethodSource("datosProveedor")
	void probarJugadasLegales(Torre[] torres, String[] coordenadasIniciales, String[] destinos,
			Color ultimoColorTurnoNegro, Color ultimoColorTurnoBlanco, Turno turno) throws CoordenadasIncorrectasException {

		arbitro.colocarTorres(torres, coordenadasIniciales, ultimoColorTurnoNegro, ultimoColorTurnoBlanco, turno);

		Color color = (turno == Turno.NEGRO) ? ultimoColorTurnoBlanco : ultimoColorTurnoNegro;
		Celda origen = tablero.buscarTorre(turno, color);
		for (String textoDestino : destinos) {
			Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);
			assertThat("Moviendo de " + tablero.obtenerCoordenadasEnNotacionAlgebraica(origen) + " a " + textoDestino
					+ " debería ser legal.", arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(true));
		}
	}

}
