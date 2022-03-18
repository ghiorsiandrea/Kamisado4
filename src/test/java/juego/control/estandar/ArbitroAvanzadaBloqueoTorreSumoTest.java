package juego.control.estandar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
import juego.modelo.Torre;
import juego.modelo.TorreSimple;
import juego.modelo.TorreSumoUno;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Pruebas unitarias sobre el árbitro con movimientos de empujon sumo.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena Sánchez</a>
 * @version 1.0 20210811
 * 
 */
@DisplayName("Tests del árbitro estándar con empujón sumo sin finalizar partida.")
public class ArbitroAvanzadaBloqueoTorreSumoTest {
	
	/** Torres de partida básica 1. */
	//  @formatter:off
	// Colocamos una torre sumo negra delante de tres torres simples blancas (al final del tablero)
	static final Torre[] TORRES_PARTIDA_BASICA_1 = {
			new TorreSumoUno(Turno.NEGRO, Color.VERDE),
			new TorreSimple(Turno.BLANCO, Color.NARANJA),
			new TorreSimple(Turno.BLANCO, Color.AZUL),
			new TorreSimple(Turno.BLANCO, Color.PURPURA)			
	};
	
	/** Coordenadas de partida básica 1. */
	/*
	 * BBB-----
	 * -N------
	 * --------
	 * --------
	 * --------
	 * --------
	 * --------
	 * --------
	 */
	static final String[] COORDENADAS_PARTIDA_BASICA_1 = {
			"b7",
			"a8",
			"b8",
			"c8"
	};	
	
	/** Torres de partida básica 2. */
	// colocamos una torre sumo negrra delante de cuatro torres simples blancas a mitad de tablero
		static final Torre[] TORRES_PARTIDA_BASICA_2 = {
				new TorreSumoUno(Turno.NEGRO, Color.VERDE),
				new TorreSimple(Turno.BLANCO, Color.AMARILLO),
				new TorreSimple(Turno.BLANCO, Color.NARANJA),
				new TorreSimple(Turno.BLANCO, Color.AZUL),
				new TorreSimple(Turno.BLANCO, Color.PURPURA)			
		};
	
	/** Coordenadas de partida básica 2. */
	/* 
	 * --------
	 * -B------
	 * BBB-----
	 * -N------
	 * --------
	 * --------
	 * --------
	 * --------
	 */
	static final String[] COORDENADAS_PARTIDA_BASICA_2 = {
			"b5", // negra
			"b7",
			"a6",
			"b6",
			"c6"
	};	
	
	/** Torres de partida básica 3. */
	// Colocamos una torre sumo blanca delante de tres torres simples negras (al final del tablero)
	static final Torre[] TORRES_PARTIDA_BASICA_3 = {
			new TorreSumoUno(Turno.BLANCO, Color.VERDE),
			new TorreSimple(Turno.NEGRO, Color.ROSA),
			new TorreSimple(Turno.NEGRO, Color.PURPURA),
			new TorreSimple(Turno.NEGRO, Color.AZUL)			
	};
	
	/** Coordenadas de partida básica 3. */
	/*
	 * --------
	 * --------
	 * --------
	 * --------
	 * --------
	 * --------
	 * -----B--
	 * ----NNN-
	 */
	static final String[] COORDENADAS_PARTIDA_BASICA_3 = {
			"f2", // blanca
			"e1",
			"f1",
			"g1"
	};	
	
	/** Torres de partida básica 4. */
	// Colocamos una torre sumo blanca delante de cuatro torres simples negras a mitad de tablero
	static final Torre[] TORRES_PARTIDA_BASICA_4 = {
		new TorreSumoUno(Turno.BLANCO, Color.VERDE),
		new TorreSimple(Turno.NEGRO, Color.AMARILLO),
		new TorreSimple(Turno.NEGRO, Color.NARANJA),
		new TorreSimple(Turno.NEGRO, Color.AZUL),
		new TorreSimple(Turno.NEGRO, Color.PURPURA)			
	};
	
	/** Torres de partida básica 4. */
	/* 
	 * --------
	 * --------
	 * --------
	 * --------
	 * --------
	 * ----B---
	 * ---NNN--
	 * ----N---
	 * --------
	 */
	static final String[] COORDENADAS_PARTIDA_BASICA_4 = {
			"e4", // blanca
			"d3",
			"e3",
			"f3",
			"e2"
	};	
	
	//  @formatter:off

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
		
	}

	/**
	 * Proveedor de argumentos con torres, coordenadas, colores y turno.
	 * 
	 * @return argumentos con torres, coordenadas, colores y turno
	 */
	static Stream<Arguments> datosProveedor() {
		return Stream.of(
				// jugadas, cambios de colores y turno ganador
				arguments(TORRES_PARTIDA_BASICA_1, COORDENADAS_PARTIDA_BASICA_1, Color.PURPURA,
						Color.VERDE, Turno.NEGRO),
				arguments(TORRES_PARTIDA_BASICA_2, COORDENADAS_PARTIDA_BASICA_2, Color.PURPURA,
						Color.VERDE, Turno.NEGRO),
				arguments(TORRES_PARTIDA_BASICA_3, COORDENADAS_PARTIDA_BASICA_3, Color.VERDE,
						Color.PURPURA, Turno.BLANCO),
				arguments(TORRES_PARTIDA_BASICA_4, COORDENADAS_PARTIDA_BASICA_4, Color.VERDE,
						Color.PURPURA, Turno.BLANCO)
				);
	}

	/**
	 * Comprueba partidas con empujón sumo bloqueado.
	 * 
	 * @param torres torres
	 * @param coordenadas coordenadas
	 * @param ultimoColorTurnoNegro último color de turno negro
	 * @param ultimoColorTurnoBlanco último color de turno blanco
	 * @param turno turno 
	 * @throws CoordenadasIncorrectasException si las coordenadas son incorrectas
	 */
	@DisplayName("Comprueba partidas con empujón sumo bloqueado.")
	@ParameterizedTest
	@MethodSource("datosProveedor")
	void probarPartidasBasicas(Torre[] torres, String[] coordenadas, Color ultimoColorTurnoNegro, 
			Color ultimoColorTurnoBlanco, Turno turno) throws CoordenadasIncorrectasException {
		
		arbitro.colocarTorres(torres, coordenadas, ultimoColorTurnoNegro, ultimoColorTurnoBlanco, turno);
		
		Color color = (turno == Turno.NEGRO) ? ultimoColorTurnoBlanco : ultimoColorTurnoNegro;
		Celda origen = tablero.buscarTorre(turno, color);
		assertThat("El empujón sumo no es legal.", arbitro.esEmpujonSumoLegal(origen), is(false));
		assertThat("Debería estar bloqueado el turno actual.", arbitro.estaBloqueadoTurnoActual(), is(true));
	}

}
