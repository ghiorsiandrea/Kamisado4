package juego.control;

import static juego.modelo.Color.AMARILLO;
import static juego.modelo.Color.MARRON;
import static juego.modelo.Color.NARANJA;
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
 * Pruebas comprobando que no se puede saltar sobre otras torres.
 *
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos de movimientos ilegales con saltos sobre torres.")
public class ArbitroMovimientosBasicosIlegalesSaltandoSobrePiezasTest extends ArbitroMovimientosBasicosIlegales {
	
	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_1 = {
			"e1e5", // NEGRAS torre rosa a celda marrón en vertical
			"h8g7", // BLANCAS torre marrón a celda marrón en diagonal
	};	
	
	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_1_ULTIMOS_COLORES = {
			{ MARRON, null },
			{ MARRON, MARRON }			
	};
	
	/** Movimientos ilegales. */
	static final String[] PARTIDA_BASICA_1_ILEGALES = {
			"a1f6", // NEGRAS movimiento ilegal no se puede saltar sobre una pieza aunque sea del mismo color
			"a1h8", // NEGRAS movimiento ilegal no se puede saltar sobre varias piezas
			"a1e5", // NEGRAS movimiento ilegal no se puede colocar sobre una pieza aunque sea del mismo color
			"a1g7", // NEGRAS movimiento ilegal no se puede colocar sobre una pieza del color contrario saltando sobre una del mismo color			
			"b1b7",  // NEGRAS movimiento ilegal no se puede mover torre de otro color (verde) que no sea marrón
			"h1g2",  // NEGRAS movimiento ilegal no se puede mover torre de otro color (naranja) que no sea marrón
	};
	
	
	/** Movimientos iniciales. */
	static final String[] PARTIDA_BASICA_2 = {
			"g1g6", // NEGRAS torre azul a celda amarilla en vertical
			"e8c6", // BLANCAS torre amarilla a celda naranja en diagonal
			"h1e4", // NEGRAS torre naranja a celda naranja en diagonal
	};	
	
	/** Estado de colores en últimos movimientos legales para negras y blancas. */
	static final Color[][] PARTIDA_BASICA_2_ULTIMOS_COLORES = {
			{ AMARILLO, null },
			{ AMARILLO, NARANJA },
			{ NARANJA, NARANJA}
	};
	
	/** Movimientos ilegales. */
	static final String[] PARTIDA_BASICA_2_ILEGALES = {
			"a8d5", // BLANCAS movimiento ilegal no se puede saltar sobre una pieza aunque sea del mismo color
			"a8f3", // BLANCAS movimiento ilegal no se puede saltar sobre varias piezas
			"a8c6", // BLANCAS movimiento ilegal no se puede colocar sobre una pieza aunque sea del mismo color
			"a8e4", // BLANCAS movimiento ilegal no se puede colocar sobre una pieza del color contrario saltando sobre una del mismo color			
			"c8c7",  // BLANCAS movimiento ilegal no se puede mover torre de otro color (púrpura) que no sea naranja
			"g8f7",  // BLANCAS movimiento ilegal no se puede mover torre de otro color (verde) que no sea naranja
	};

	
	/**
	 * Proveedor de argumentos con jugadas y ganador.
	 * 
	 * @return coordenadas y torress en fila inferior de torres negras
	 */
	static Stream<Arguments> jugadasYGanadorProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(PARTIDA_BASICA_1, PARTIDA_BASICA_1_ULTIMOS_COLORES, PARTIDA_BASICA_1_ILEGALES),
				arguments(PARTIDA_BASICA_2, PARTIDA_BASICA_2_ULTIMOS_COLORES, PARTIDA_BASICA_2_ILEGALES)
				);
	}
	
	/**
	 * Prueba los movimientos ilegales saltando sobre torres, una vez realizados 
	 * unos mínimos movimientos legales.
	 * 
	 * @param jugadasLegales  jugadas iniciales
	 * @param ultimosColores  estados de colores en últimos movimientos para torres
	 *                        negras y blancas
	 * @param jugadasIlegales jugadas ilegales
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 * @throws MultipleFailuresError si hay múltiples fallos
	 */
	@DisplayName("Partidas con movimientos ilegales saltando sobre otras torres")
	@ParameterizedTest
	@MethodSource("jugadasYGanadorProveedor")
	void probarMovimientosIlegales(String[] jugadasLegales, Color[][] ultimosColores, String[] jugadasIlegales) throws MultipleFailuresError, CoordenadasIncorrectasException {
		assertThat("El número de jugadas iniciales debería ser 0", arbitro.obtenerNumeroJugada(),is(0));
		// realizar movimientos legales iniciales
		final int numeroJugadas = jugadasLegales.length;
		realizarJugadasLegales(jugadasLegales, ultimosColores);
		assertThat("El número de jugadas finales tras movimientos legales es incorrecto", arbitro.obtenerNumeroJugada(),is(numeroJugadas));
		// realizar movimiento ilegales
		intentarJugadasIlegales(jugadasIlegales);		
		assertThat("El número de jugadas finales tras movimientoes ilegales es incorrecto", arbitro.obtenerNumeroJugada(),is(numeroJugadas));		
	}
	
}
