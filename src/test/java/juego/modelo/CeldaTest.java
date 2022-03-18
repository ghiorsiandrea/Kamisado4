package juego.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


/**
 * Pruebas unitarias sobre la celda.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * 
 */
@DisplayName("Tests sobre Celda")
public class CeldaTest {
	
	/** Generador de aleatorios. */
	private static Random aleatorios = new SecureRandom();

	/**
	 * Test del constructor.
	 */
	@DisplayName("Constructor con estado inicial correcto")
	@Test
	void constructor() {
		final int TAMAÑO = 8;
		for (int fila = 0; fila < TAMAÑO; fila++) {
			for (int columna = 0; columna < TAMAÑO; columna++) {
				Celda celda = new Celda(fila, columna, Color.obtenerColorAleatorio());
				assertThat("Fila mal inicializada", celda.obtenerFila(), is(fila));
				assertThat("Columnna mal inicializada", celda.obtenerColumna(), is(columna));
				assertTrue("Inicialmente no está vacía.", celda.estaVacia());
				assertNull("Tiene torre cuando debería vale null.", celda.obtenerTorre());
			}
		}
	}

	/**
	 * Comprueba la colocación en celda.
	 */
	@DisplayName("Coloca una torre en una celda")
	@Test
	void colocarEnCelda() {
		TorreSimple torre = new TorreSimple(Turno.BLANCO, Color.AZUL);
		Celda celda = new Celda(0, 0, Color.AMARILLO);
		celda.establecerTorre(torre);
		assertThat("Color mal inicializado", celda.obtenerTorre(), is(torre));
	}

	/**
	 * Test del método toString.
	 */
	@DisplayName("Formato de texto sin torre")
	@Test
	void probarToStringSinTorre() {
		for (int fila = 0; fila < 10; fila++) {
			for (int columna = 0; columna < 10; columna++) {
				Celda celda = new Celda(fila, columna, Color.AMARILLO);
				String actual = celda.toString().replaceAll("\\s", "");
				assertThat("Texto incorrecto." + celda.toString(), actual, is("[" + fila + "][" + columna + "]Color:ATurno:-Torre:-"));
			}
		}
	}
	
	/**
	 * Proveedor de argumentos con color de celda, torres y cadena esperada.
	 * 
	 * @return coordenadas y torress en fila inferior de torres negras
	 */
	static Stream<Arguments> proveedorColorTorreYTexto() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(Color.AZUL,  new TorreSimple(Turno.NEGRO, Color.AMARILLO), "Color:ZTurno:NTorre:A"),
				arguments(Color.NARANJA, new TorreSimple(Turno.NEGRO, Color.VERDE), "Color:NTurno:NTorre:V"),
				arguments(Color.PURPURA,  new TorreSimple(Turno.BLANCO, Color.MARRON), "Color:PTurno:BTorre:M"),
				arguments(Color.ROSA, new TorreSimple(Turno.BLANCO, Color.ROJO), "Color:STurno:BTorre:R"));
	}
	
	/**
	 * Test del método toString.
	 * 
	 * @param color color
	 * @param torre torre
	 * @param texto texto
	 */
	@DisplayName("Formato de texto con torre")
	@ParameterizedTest
	@MethodSource("proveedorColorTorreYTexto")
	void probarToStringConTorre(Color color, Torre torre, String texto) {
		Celda celda = new Celda(0, 0, color);
		celda.establecerTorre(torre);
		String actual = celda.toString().replaceAll("\\s", "");
		assertThat("Texto incorrecto." + celda.toString(), actual, is("[0][0]"+texto));
	}	

	/** Elimina torre en celda ocupada. */
	@DisplayName("Elimina una torre en una celda ocupada")
	@Test
	void eliminarTorre() {
		Celda celda = new Celda(0, 0, Color.VERDE);
		assertTrue("La celda no está vacía.", celda.estaVacia());
		celda.establecerTorre(new TorreSimple(Turno.NEGRO, Color.MARRON));
		assertFalse("La celda está vacía.", celda.estaVacia());
		celda.eliminarTorre();
		assertNull(celda.obtenerTorre());
		assertTrue("La celda no está vacía.", celda.estaVacia());
	}

	/** Obtiene el color de celdas con torres. */
	@DisplayName("Obtiene el color de la torre que ocupa la celda")
	@Test
	void obtenerColorDeTorreEnCeldasOcupadas() {
		Celda celda = new Celda(0, 0, Color.PURPURA);
		for (Color color : Color.values()) {
			celda.establecerTorre(new TorreSimple(Turno.BLANCO, color));
			assertThat("Color incorrecto.", celda.obtenerColorDeTorre(), is(color));
		}
	}

	/** Obtiene color de celdas vacías. */
	@DisplayName("Obtiene color nulo de la torre de una celda vacía")
	@Test
	void obtenerColorDeTorreEnCeldasVacías() {
		Celda celda = new Celda(0, 0, Color.PURPURA);
		assertNull("Color incorrecto.", celda.obtenerColorDeTorre());
	}

	/** 
	 * Comprueba la igualdad de coordenadas entre celdas. 
	 * Se utilizan celdas de diferentes colores puesto que no debe afectar en la
	 * condición de igualdad de coordenadas. 
	 */
	@DisplayName("Tienen las mismas coordenadas celdas coincidentes en posición")
	@Test
	void comprobarIgualdadDeCoordenadas() {
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				Celda celda1 = new Celda(i, j, Color.AMARILLO);
				Celda celda2 = new Celda(i, j, Color.AZUL);
				// la operación de igualdad es simétrica...
				assertTrue(celda1.tieneCoordenadasIguales(celda2));
				assertTrue(celda2.tieneCoordenadasIguales(celda1));
			}
		}
	}

	/** Comprueba la desigualdad de coordenadas entre celdas. */
	@DisplayName("Tienen diferentes coordenadas celdas NO coincidentes en posición")
	@Test
	void comprobarDesigualdadDeCoordenadas() {
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				Celda celda1 = new Celda(i, j, Color.AMARILLO);
				int desplazamiento1 = aleatorios.nextInt(10) + 1;
				int desplazamiento2 = aleatorios.nextInt(10) + 1;
				Celda celda2 = new Celda(i + desplazamiento1, j + desplazamiento2, Color.AMARILLO);
				assertFalse(celda1.tieneCoordenadasIguales(celda2));
				assertFalse(celda2.tieneCoordenadasIguales(celda1));
				Celda celda3 = new Celda(i, j + desplazamiento2, Color.AMARILLO);
				assertFalse(celda1.tieneCoordenadasIguales(celda3));
				assertFalse(celda3.tieneCoordenadasIguales(celda1));
				Celda celda4 = new Celda(i + desplazamiento1, j, Color.AMARILLO);
				assertFalse(celda1.tieneCoordenadasIguales(celda4));
				assertFalse(celda4.tieneCoordenadasIguales(celda1));
			}
		}
	}

	/** Test del método equals. */
	@DisplayName("Comprobación de igualdad")
	@Test
	void probarEquals() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Celda celda = new Celda(i, j, Color.AMARILLO);
				Celda celda2 = new Celda(i, j, Color.AMARILLO);
				assertTrue("Comparación de igualdad incorrecta para " + celda.toString(), celda.tieneCoordenadasIguales(celda2));
				assertFalse("Es igual a nulo", celda == null);

			}
		}
	}

}
