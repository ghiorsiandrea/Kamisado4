package juego.control;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.opentest4j.MultipleFailuresError;

import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas avanzadas con bloqueos.
 * 
 * Clase abstracta con métodos comunes de utilidad 
 * que son heredados en descendientes concretos.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 */
public abstract class ArbitroMovimientosAvanzadosConBloqueos {
	
	/** Tablero. */
	protected Tablero tablero;
	
	/** Arbitro. */
	protected Arbitro arbitro;
	
	/**
	 * Realiza jugadas legales con bloqueos.
	 * 
	 * @param jugadas jugadas legales
	 * @param ultimosColores últimos colores en cada movimiento
	 * @throws MultipleFailuresError si hay algún error en los tests
	 * @throws CoordenadasIncorrectasException si las coordenadas están fuera del tablero 
	 */
	protected void realizarJugadasConBloqueos(String[] jugadas, Color[][] ultimosColores) throws MultipleFailuresError, CoordenadasIncorrectasException {
		int cont = 0;
		for (String jugada : jugadas) {
			Celda origen = tablero.obtenerCeldaOrigenEnJugada(jugada);
			Celda destino = tablero.obtenerCeldaDestinoEnJugada(jugada);
			assertThat("La jugada " + jugada + " no es legal para turno "
					+ arbitro.obtenerTurno(), arbitro.esMovimientoLegalConTurnoActual(origen, destino), is(true));

			arbitro.moverConTurnoActual(origen, destino);
			if (arbitro.estaBloqueadoTurnoActual()) {
				arbitro.moverConTurnoActualBloqueado();
			}
			final int auxCont = cont;
			assertAll("Estados de colores en últimos movimientos",
					() -> assertThat("Último color de turno negro incorrecto en movimiento " + (auxCont + 1) + ".",
							arbitro.obtenerUltimoMovimiento(Turno.NEGRO), is(ultimosColores[auxCont][0])),
					() -> assertThat("Último color de turno blanco incorrecto en movimiento " + (auxCont + 1) + ".",
							arbitro.obtenerUltimoMovimiento(Turno.BLANCO), is(ultimosColores[auxCont][1])));
			cont++;

		}
	}
}
