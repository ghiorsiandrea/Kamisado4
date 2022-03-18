package juego.control;

import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
import static juego.modelo.Color.ROSA;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.MultipleFailuresError;

import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas comprobando que no se pueden retroceder las torres
 * una vez han avanzado.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos de movimientos ilegales retrocediendo torres.")
public class ArbitroMovimientosBasicosIlegalesRetrocediendoTest extends ArbitroMovimientosBasicosIlegales {

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_1 = { "e1e5", // NEGRAS torre rosa a celda marrón en vertical
			"h8h4", // BLANCAS torre marrón a celda rosa en vertical
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = { { MARRON, null }, { MARRON, ROSA } };

	/** Movimientos ilegales. */
	static final String[] PARTIDA_BASICA_1_ILEGALES = { "e5e3", // NEGRAS movimiento ilegal no se puede retroceder en
																// vertical
			"e5g3", // NEGRAS movimiento ilegal no se puede retroceder en diagonal sureste
			"e5b2", // NEGRAS movimiento ilegal no se puede retroceder en diagonal suroeste
			"e5d3", // NEGRAS movimiento ilegal no se puede retroceder con un movimiento de caballo
					// del ajedrez
			"e5g4", // NEGRAS movimiento ilegal no se puede retroceder con un movimiento de caballo
					// del ajedrez
	};

	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_2 = { "a1a5", // NEGRAS torre marrón a celda rosa en vertical
			"d8d5", // BLANCAS torre rosa a celda naranja en vertical
			"h1h4", // NEGRAS torre naranja a celda rosa en vertical
	};

	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = { { ROSA, null }, { ROSA, NARANJA }, { ROSA, NARANJA } };

	/** Movimientos ilegales. */
	static final String[] PARTIDA_BASICA_2_ILEGALES = { "d5d7", // BLANCAS movimiento ilegal no se puede retroceder en
																// vertical
			"d5f7", // BLANCAS movimiento ilegal no se puede retroceder en diagonal noreste
			"d5b6", // BLANCAS movimiento ilegal no se puede retroceder en diagonal noroeste
			"d5c7", // BLANCAS movimiento ilegal no se puede retroceder con un movimiento de caballo
					// del ajedrez
			"d5f6" // BLANCAS movimiento ilegal no se puede retroceder con un movimiento de caballo
					// del ajedrez
	};

	/**
	 * Proveedor de argumentos con jugadas y ganador.
	 * 
	 * @return coordenadas y torres en fila inferior de torres negras
	 */
	static Stream<Arguments> jugadasYGanadorProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, PARTIDA_BASICA_1_ILEGALES),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, PARTIDA_BASICA_2_ILEGALES));
	}

	/**
	 * Prueba los movimientos ilegales retrocediendo las torres
	 * una vez realizados unos mínimos movimientos legales.
	 * 
	 * @param jugadasLegales  jugadas iniciales
	 * @param ultimosColores  estados de colores en últimos movimientos para torres
	 *                        negras y blancas
	 * @param jugadasIlegales jugadas ilegales
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 * @throws MultipleFailuresError si hay multiples fallos
	 */
	@DisplayName("Partidas con movimientos ilegales retrocediendo")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarMovimientosIlegales(String[] jugadasLegales, Color[][] ultimosColores, String[] jugadasIlegales) throws MultipleFailuresError, CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(), is(0));
		// realizar movimientos legales iniciales
		final int numeroJugadas = jugadasLegales.length;
		realizarJugadasLegales(jugadasLegales, ultimosColores);
		assertThat("El número de jugadas finales tras movimientos legales es incorrecto", arbitro.obtenerNumeroJugada(),
				is(numeroJugadas));
		// realizar movimiento ilegales
		intentarJugadasIlegales(jugadasIlegales);
		assertThat("El número de jugadas finales tras movimientoes ilegales es incorrecto",
				arbitro.obtenerNumeroJugada(), is(numeroJugadas));
	}

}
