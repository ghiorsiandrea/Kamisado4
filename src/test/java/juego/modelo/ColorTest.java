package juego.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias sobre el tipo enumerado.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * 
 */
@DisplayName("Tests sobre Color")
public class ColorTest {

	/**
	 * Comprueba el número de valores definidos.
	 */
	@Test
	@DisplayName("Numero de valores definido en la enumeración.")
	void numeroDeValoresEnColor() {
		assertThat("El número de valores es incorrecto.", Color.values().length, is(8));
	}

	/**
	 * Correcta inicialización de letras para cada valor.
	 */
	@DisplayName("Letras correctas para cada valor.")
	@Test
	public void probarLetra() {
		assertAll("letras correctas", () -> assertThat(Color.AMARILLO.toChar(), is('A')),
				() -> assertThat(Color.AZUL.toChar(), is('Z')), () -> assertThat(Color.MARRON.toChar(), is('M')),
				() -> assertThat(Color.NARANJA.toChar(), is('N')), () -> assertThat(Color.PURPURA.toChar(), is('P')),
				() -> assertThat(Color.ROJO.toChar(), is('R')), () -> assertThat(Color.ROSA.toChar(), is('S')),
				() -> assertThat(Color.VERDE.toChar(), is('V')));
	}

	/**
	 * Comprueba la correcta generación de colores aleatorios.
	 * 
	 * Se comprueba que se generan todos los colores con una frecuncia
	 * dentro de un cierto rango admitido, suponiendo que sigue
	 * una distribución uniforme.
	 */
	@DisplayName("Generación de colores aleatorios.")
	@Test
	void probarColorAleatorio() {
		final int ITERACIONES = 1000;
		int[] contador = new int[Color.values().length];
		for (int i = 0; i < ITERACIONES; i++) {
			Color color = Color.obtenerColorAleatorio();
			contador[color.ordinal()]++;
		}
		double frecuenciaEsperada = ITERACIONES / Color.values().length; // 125
		double error = frecuenciaEsperada / 4; // 31,25
		for (Color color : Color.values()) {
			assertAll("frecuencias correctas",
					() -> assertTrue(contador[color.ordinal()] <= frecuenciaEsperada + error,
							"Frecuencia incorrecta " + contador[color.ordinal()] + " para el color " + color +
							". Revisa si se generan todos los colores con igual probabilidad."),
					() -> assertTrue(contador[color.ordinal()] >= frecuenciaEsperada - error,
							"Frecuencia incorrecta " + contador[color.ordinal()] + " para el color " + color +
							". Revisa si se generan todos los colores con igual probabilidad."));
		}

	}
}
