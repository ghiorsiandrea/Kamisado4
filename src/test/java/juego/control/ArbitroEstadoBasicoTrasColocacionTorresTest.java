package juego.control;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
 * Pruebas sobre el estado inicial del árbitro 
 * una vez colocadas las torres y antes de iniciar la partida.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * @see Tablero#obtenerCoordenadasEnNotacionAlgebraica(Celda)
 */
@DisplayName("Tests básicos sobre el estado inicial una vez colocadas las torres.")
public class ArbitroEstadoBasicoTrasColocacionTorresTest {

	/** Tamaño en filas y columnas. */
	private static final int TAMAÑO = 8;

	/** Arbitro. */
	private Arbitro arbitro;

	/** Tablero. */
	private Tablero tablero;

	/**
	 * Proveedor de torres blancas en fila superior.
	 * 
	 * @return coordenadas y torres en fila superior de torres blancas
	 */
	static Stream<Arguments> coordenadasAndTorreBlancaProvider() {
		return Stream.of(arguments(0, 0, new TorreSimple(Turno.BLANCO, Color.NARANJA)),
				arguments(0, 1, new TorreSimple(Turno.BLANCO, Color.AZUL)),
				arguments(0, 2, new TorreSimple(Turno.BLANCO, Color.PURPURA)),
				arguments(0, 3, new TorreSimple(Turno.BLANCO, Color.ROSA)),
				arguments(0, 4, new TorreSimple(Turno.BLANCO, Color.AMARILLO)),
				arguments(0, 5, new TorreSimple(Turno.BLANCO, Color.ROJO)),
				arguments(0, 6, new TorreSimple(Turno.BLANCO, Color.VERDE)),
				arguments(0, 7, new TorreSimple(Turno.BLANCO, Color.MARRON)));
	}

	/**
	 * Proveedor de torres negras en fila inferior.
	 * 
	 * @return coordenadas y torres en fila inferior de torres negras
	 */
	static Stream<Arguments> coordenadasAndTorreNegraProvider() {
		return Stream.of(arguments(7, 0, new TorreSimple(Turno.NEGRO, Color.MARRON)),
				arguments(7, 1, new TorreSimple(Turno.NEGRO, Color.VERDE)),
				arguments(7, 2, new TorreSimple(Turno.NEGRO, Color.ROJO)),
				arguments(7, 3, new TorreSimple(Turno.NEGRO, Color.AMARILLO)),
				arguments(7, 4, new TorreSimple(Turno.NEGRO, Color.ROSA)),
				arguments(7, 5, new TorreSimple(Turno.NEGRO, Color.PURPURA)),
				arguments(7, 6, new TorreSimple(Turno.NEGRO, Color.AZUL)),
				arguments(7, 7, new TorreSimple(Turno.NEGRO, Color.NARANJA)));
	}

	/**
	 * Inicialización con colocación de torres sobre el tablero.
	 */
	@BeforeEach
	void colocarTorres() {
		tablero = new Tablero();
		arbitro = new ArbitroSimple(tablero);
		arbitro.colocarTorres();
	}

	/**
	 * Comprueba que se colocan 8 torres negras en la parte inferior del tablero.
	 * 
	 * @param fila    fila
	 * @param columna columna
	 * @param torre   torre
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	//  @formatter:off
	/* Partimos de un tablero que debe tener este estado.
	*   a  b  c  d  e  f  g  h  
	* 8 BN BZ BP BS BA BR BV BM 
	* 7 -- -- -- -- -- -- -- --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- -- -- -- -- --
	* 4 -- -- -- -- -- -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- -- -- -- -- -- -- -- 
	* 1 NM NZ NR NA NS NP NA NN
	*/
	// @formatter:on
	@DisplayName("Torres negras colocadas en fila inferior.")
	@ParameterizedTest
	@MethodSource("coordenadasAndTorreNegraProvider")
	void comprobarTorresPrimeraFila(int fila, int columna, Torre torre) throws CoordenadasIncorrectasException {
		Celda celda = tablero.obtenerCelda(fila, columna);
		assertAll("Comprueba el estado correcto en fila con torres negras.",
				() -> assertThat("Celda no debería estar vacía.", celda.estaVacia(), is(false)),
				() -> assertThat("Turno de torre colocada incorrecta.", celda.obtenerTurnoDeTorre(), is(Turno.NEGRO)),
				() -> assertThat("Color de torre colocada incorrecta.", celda.obtenerColorDeTorre(),
						is(torre.obtenerColor())));

	}

	/**
	 * Comprueba que se colocan 8 torres blancas en la parte superior del tablero.
	 * 
	 * @param fila    fila
	 * @param columna columna
	 * @param torre   torre
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero
	 */
	//  @formatter:off
	/* Partimos de un tablero que debe tener este estado.
	*   a  b  c  d  e  f  g  h  
	* 8 BN BZ BP BS BA BR BV BM 
	* 7 -- -- -- -- -- -- -- --
	* 6 -- -- -- -- -- -- -- --
	* 5 -- -- -- -- -- -- -- --
	* 4 -- -- -- -- -- -- -- -- 
	* 3 -- -- -- -- -- -- -- --
	* 2 -- -- -- -- -- -- -- -- 
	* 1 NM NZ NR NA NS NP NA NN
	*/
	// @formatter:on
	@DisplayName("Torres blancas colocadas en la fila superior")
	@ParameterizedTest
	@MethodSource("coordenadasAndTorreBlancaProvider")
	void comprobarTorresUltimaFila(int fila, int columna, Torre torre) throws CoordenadasIncorrectasException {
		Celda celda = tablero.obtenerCelda(fila, columna);
		assertAll("Comprueba el estado correcto en fila con torres blancas.",
				() -> assertThat("Celda no debería estar vacía.", celda.estaVacia(), is(false)),
				() -> assertThat("Turno de torre colocada incorrecta", celda.obtenerTurnoDeTorre(), is(Turno.BLANCO)),
				() -> assertThat("Color de torre incorrecto", celda.obtenerColorDeTorre(), is(torre.obtenerColor())));
	}

	/**
	 * Comprueba el estado inicial con las torres colocadas.
	 */
	@DisplayName("Estado inicial con torres colocadas")
	@Test
	void comprobarEstadoInicialConTableroConTorresColocadas() {
		assertAll("Estado inicial con torres colocadas.",
				() -> assertThat("El número de jugadas inicial debería ser cero.", arbitro.obtenerNumeroJugada(), is(0)),
				() -> assertThat("Número de torres negras incorrecto.", tablero.obtenerNumeroTorres(Turno.NEGRO), is(TAMAÑO)),
				() -> assertThat("Número de torres blancas incorrecto.", tablero.obtenerNumeroTorres(Turno.BLANCO),
						is(TAMAÑO)),
				() -> assertThat("Turno inicial siempre es de negras.", arbitro.obtenerTurno(), is(Turno.NEGRO))
				);
	}

	/**
	 * Comprueba el estado inicial respecto a situación de no finalización y
	 * ausencia de bloqueo mutuo o deadlock.
	 */
	@DisplayName("Estado inicial sin estar finalizada ni con bloqueo mutuo o deadlock.")
	@Test
	void comprobarEstadoInicialArbitroConTorresColocadas() {
		assertAll("Sin finalizar ni bloqueo mutuo o deadlock al iniciarse la partida.",
				() -> assertThat("La ronda no puede iniciarse ya finalizada.", arbitro.estaAcabadaRonda(), is(false)),
				() -> assertThat("La partida no puede iniciarse ya finalizada.", arbitro.estaAcabadaPartida(), is(false)),
				() -> assertThat("No puede estar bloqueado el turno actual al iniciarse con torres.", arbitro.estaBloqueadoTurnoActual(), is(false)),
				() -> assertThat("La partida no puede iniciarse en bloqueo mutuo o deadlock.", arbitro.hayBloqueoMutuo(), is(false)));
	}

	/**
	 * Comprueba el cambio de turno sucesivo. Ya se han colocado las torres y el
	 * turno debería estar inicializado a NEGRO.
	 */
	@DisplayName("Cambio de turno.")
	@Test
	void comprobarCambioTurno() {
		// Realizan varios cambios de turno comprobando el correcto cambio...
		assertThat("Turno inicial deberia ser de torres negras", arbitro.obtenerTurno(), is(Turno.NEGRO));
		((ArbitroSimple)arbitro).cambiarTurno();
		assertThat("Turno pasa a torres blancas", arbitro.obtenerTurno(), is(Turno.BLANCO));
		((ArbitroSimple)arbitro).cambiarTurno();
		assertThat("Turno pasa a torres negras", arbitro.obtenerTurno(), is(Turno.NEGRO));
	}
}
