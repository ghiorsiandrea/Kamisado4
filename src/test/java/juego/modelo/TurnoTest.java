package juego.modelo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias sobre el tipo enumerado.
 *
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @version 1.0 20210811
 */
@DisplayName("Tests sobre Turno")
public class TurnoTest {
	
	/**
	 * Comprueba el número de valores definidos.
	 */
	@Test
	@DisplayName("Numero de valores definido en la enumeración.")
	void numeroDeValoresEnTurno() {
		assertThat("El número de valores es incorrecto.", Turno.values().length, is(2));
	}

	/**
	 * Comprueba la correcta inicialización de valores.
	 */
	@Test
	@DisplayName("Comprueba la inicialización de letras correcta en turno")
	void probarInicializacion() {
		assertAll(() -> assertThat("Letra incorrecta asignada a turno blanco.", Turno.BLANCO.toChar() , is('B')),
				() -> assertThat("Letra incorrecta asignada a turno negro.", Turno.NEGRO.toChar(), is('N')));
	}
}
