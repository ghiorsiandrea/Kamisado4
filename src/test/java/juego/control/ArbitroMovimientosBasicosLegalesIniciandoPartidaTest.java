package juego.control;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import juego.modelo.Celda;
import juego.modelo.Tablero;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas sobre movimientos básicos al inicio de la partida,
 * una vez colocadas las torres y sin haber realizado movimiento alguno.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos de movimientos con torres en punto inicial.")
public class ArbitroMovimientosBasicosLegalesIniciandoPartidaTest {

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
		arbitro.colocarTorres();
	}

	/**
	 * Comprueba movimientos ilegales de torres negras al iniciar partida.
	 *
	 * @param textoOrigen coordenadas celda origen
	 * @param textoDestino coordenadas celda destino
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Movimientos ilegales de una torre negra al iniciar la partida")
	@ParameterizedTest
	@CsvSource({
		// @formatter:off
		"a1, a8", "a1, b3", "a1, b4", "a1, c2", "a1, d5", "a1, e3", "a1, h7",  // torre marrón
		"b1, a1", "b1, a3", "b1, c4", "b1, d4", "b1, d5", "b1, e7", "b1, f8",  // torre verde
		"c1, d1", "c1, a2", "c1, b3", "c1, a4", "c1, b5", "c1, d6", "c1, e7",  // torre roja
		"d1, b1", "d1, b2", "d1, c3", "d1, e3", "d1, e4", "d1, f5", "d1, g7",  // torre amarilla
		"e1, c1", "e1, d3", "e1, d5", "e1, f6", "e1, g4", "e1, h5", "e1, g7",  // torre rosa
		"f1, d1", "f1, c5", "f1, d5", "f1, e6", "f1, g7", "f1, h2", "f1, h7",  // torre púrpura
		"g1, h1", "g1, a5", "g1, b5", "g1, c4", "g1, e7", "g1, f7", "g1, h6",  // torre azul
		"h1, e1", "h1, b6", "h1, c7", "h1, d4", "h1, e6", "h1, g3", "h1, g7",  // torre naranja
	// 	@formatter:on
	})
	void movimientoIlegalTorreNegraAlInicio(String textoOrigen, String textoDestino) throws CoordenadasIncorrectasException {
		Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
		Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);
		assertThat("Jugada ilegal para una torre negra al inicio de partida se reconoce como legal", 
				arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(false));
	}
	
	/**
	 * Comprueba movimientos ilegales de torres blancas al iniciar partida.
	 * 
	 * @param textoOrigen coordenadas celda origen
	 * @param textoDestino coordenadas celda destino
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Movimientos ilegales de una torre blanca al iniciar la partida")
	@ParameterizedTest
	@CsvSource({
		// @formatter:off
		"a8, e1", "a8, b6", "a8, c7", "a8, d4", "a8, e6", "a8, g3", "a8, g7",  // torre naranja	
		"b8, a1", "b8, a3", "b8, c4", "b8, d4", "b8, d5", "b8, e7", "b8, f8",  // torre azul
		"c8, d1", "c8, a2", "c8, b3", "c8, a4", "c8, b5", "c8, d6", "c8, e7",  // torre purpura
		"d8, b1", "d8, b2", "d8, c3", "d8, e3", "d8, e4", "d8, f5", "d8, g7",  // torre rosa
		"e8, c1", "e8, d3", "e8, d5", "e8, f6", "e8, g4", "e8, h6", "e8, g7",  // torre amarilla
		"f8, d1", "f8, c3", "f8, d5", "f8, e6", "f8, g8", "f8, h2", "f8, h7",  // torre roja
		"g8, h1", "g8, a5", "g8, b5", "g8, c3", "g8, e7", "g8, f6", "g8, h6",  // torre verde
		"h8, e1", "h8, b6", "h8, c7", "h8, d3", "h8, e6", "h8, g3", "h8, g6",  // torre marron
		// 	@formatter:on
	})
	void movimientoIlegalTorreBlancaAlInicio(String textoOrigen, String textoDestino) throws CoordenadasIncorrectasException {
		// para que pueda mover el blanco suponemos que con negras se puede mover...
		Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
		Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);	
		((ArbitroSimple) arbitro).cambiarTurno(); // para poder mover blancas
		assertThat("Jugada ilegal para una torre blanca al inicio de partida se reconoce como legal", 
				arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(false));
	}

	
	/**
	 * Comprueba la legalidad de movimiento de torres negras en la fila de inicio.
	 * 
	 * @param textoOrigen coordenadas celda origen
	 * @param textoDestino coordenadas celda destino
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	/* @formatter:off
	*   a  b  c  d  e  f  g  h  
	* 8 BN BZ BP BS BA BR BV BM
	* 7 -- -- -- -- -- -- -- --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- -- -- -- -- --
	* 4 -- -- -- -- -- -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- -- -- -- -- -- -- --
	* 1 NM NV NR NA NS NP NZ NN
	*  @formatter:on
	*/
	@DisplayName("Movimiento legales en fila de inicio con torres negras")
	@ParameterizedTest
	@CsvSource({
		// @formatter:off
		"a1, a2", "a1, a4", "a1, a7", "a1, b2", "a1, e5", "a1, g7", 							  // torre marrón
		"b1, b2", "b1, b3", "b1, a2", "b1, c2", "b1, e4", "b1, g6", "b1, h7", 					  // torre verde
		"c1, c3", "c1, c6", "c1, b2", "c1, a3", "c1, d2", "c1, e3", "c1, f4", "c1, g5", "c1, h6", // torre roja
		"d1, d2", "d1, d4", "d1, c2", "d1, b3", "d1, a4", "d1, e2", "d1, f3", "d1, g4", "d1, h5", // torre amarilla
		"e1, e3", "e1, e4", "e1, d2", "e1, c3", "e1, b4", "e1, a5", "e1, f2", "e1, g3", "e1, h4", // torre rosa
		"f1, f2", "f1, f5", "f1, e2", "f1, d3", "f1, c4", "f1, b5", "f1, a6", "f1, g2", "f1, h3", // torre púrpura
		"g1, g3", "g1, g6", "g1, f2", "g1, e3", "g1, d4", "g1, c5", "g1, b6", "g1, a7", "g1, h2", // torre azul
		"h1, h2", "h1, h7", "h1, g2", "h1, f3", "h1, e4", "h1, d5", "h1, c6", "h1, b7", "h1, h2", // torre naranja	
		// 	@formatter:on
	})
	void movimientoLegalTorresNegras(String textoOrigen, String textoDestino) throws CoordenadasIncorrectasException {
		// movimiento
		Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
		Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);
		assertThat("El movimiento no es legal con la torre " + origen.obtenerTorre().toString(),
				arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(true));
	}
	
	
	/**
	 * Comprueba la legalidad de movimiento de torre blancas en la fila de inicio.
	 * 
	 * @param textoOrigen coordenadas celda origen
	 * @param textoDestino coordenadas celda destino
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	/* @formatter:off
	*   a  b  c  d  e  f  g  h  
	* 8 BN BZ BP BS BA BR BV BM
	* 7 -- -- -- -- -- -- -- --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- -- -- -- -- --
	* 4 -- -- -- -- -- -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- -- -- -- -- -- -- --
	* 1 NM NV NR NA NS NP NZ NN
	*  @formatter:on
	*/
	@DisplayName("Movimiento legales en fila de inicio con torre blancas")
	@ParameterizedTest
	@CsvSource({
		// @formatter:off
		"a8, a2", "a8, a4", "a8, a7", "a8, b7", "a8, e4", "a8, g2", 					          // torre marrón
		"b8, b2", "b8, b3", "b8, a7", "b8, c7", "b8, e5", "b8, g3", "b8, h2", 					  // torre verde
		"c8, c3", "c8, c6", "c8, b7", "c8, a6", "c8, d7", "c8, e6", "c8, f5", "c8, g4", "c8, h3", // torre roja
		"d8, d2", "d8, d4", "d8, c7", "d8, b6", "d8, a5", "d8, e7", "d8, f6", "d8, g5", "d8, h4", // torre amarilla
		"e8, e3", "e8, e4", "e8, d7", "e8, c6", "e8, b5", "e8, a4", "e8, f7", "e8, g6", "e8, h5", // torre rosa
		"f8, f2", "f8, f5", "f8, e7", "f8, d6", "f8, c5", "f8, b4", "f8, a3", "f8, g7", "f8, h6", // torre púrpura
		"g8, g3", "g8, g6", "g8, f7", "g8, e6", "g8, d5", "g8, c4", "g8, b3", "g8, a2", "g8, h7", // torre azul
		"h8, h2", "h8, h7", "h8, g7", "h8, f6", "h8, e5", "h8, d4", "h8, c3", "h8, b2", "h8, h6", // torre naranja	
		// 	@formatter:on
	})
	void movimientoLegalTorresBlancas(String textoOrigen, String textoDestino) throws CoordenadasIncorrectasException {
		// para que pueda mover turno blanco suponemos que con torres negras se puede mover...
		// movimiento
		Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
		Celda destino = tablero.obtenerCeldaParaNotacionAlgebraica(textoDestino);
		((ArbitroSimple) arbitro).cambiarTurno(); // cambiamos turno para mover con torres blancas
		assertThat("El movimiento no es legal con la torre " + origen.obtenerTorre().toString(),
				arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(true));
	}
	
	/**
	 * Comprueba movimientos ilegales con celdas vacías en origen.
	 *
	 * @param textoOrigen coordenadas celda origen
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	@DisplayName("Movimientos ilegales con celdas vacías en origen")
	@ParameterizedTest
	@CsvSource({
		// @formatter:off
		"a3", "b2", "c6", "d5", "e4", "f6", "h3", "g5" // se toman celdas del medio del tablero
	// 	@formatter:on
	})
	void movimientoIlegalConCeldaOrigenVacia(String textoOrigen) throws CoordenadasIncorrectasException {
		Celda origen = tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
		assertNotNull(origen, "La celda debería existir.");
		for (Celda destino : tablero.obtenerCeldas()) {
			assertThat("Jugada ilegal para una celda origen que está vacía.", 
					arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(false));
		}		
	}
}
