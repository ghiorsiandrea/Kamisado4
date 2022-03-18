package juego.control;

import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Torre;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Arbitro.
 * 
 * @author <a href="mailto:rmartico@ubu.es">Raúl Marticorena</a>
 * @version 4.0
 * @since JDK 16
 */
public interface Arbitro {

	/**
	 * Retorna la celda que contiene la torre del turno y color indicado.
	 * 
	 * @param turno turno
	 * @param color color
	 * @return celda
	 */
	Celda buscarCeldaConTorreDeColor(Turno turno, Color color);

	/**
	 * Coloca la configuración por defecto de torres al principio de partida.
	 *
	 */
	void colocarTorres();

	/**
	 * Alternativa para la inicializaciónde la partida con configuraciones
	 * prefabricadas. Solo se incluye para facilitar el testing automático.
	 * 
	 * @param torres                 torres
	 * @param coordenadas            coordenadas en notación algebraica
	 * @param ultimoColorTurnoNegro  último color del turno negro
	 * @param ultimoColorTurnoBlanco último color del turno blanco
	 * @param turno                  turno actual que mueve
	 * @throws CoordenadasIncorrectasException si alguna de las coordenadas son incorrectas
	 */
	void colocarTorres(Torre[] torres, String[] coordenadas, Color ultimoColorTurnoNegro, Color ultimoColorTurnoBlanco,
			Turno turno) throws CoordenadasIncorrectasException;

	/**
	 * Consulta el ganador de la partida.
	 * 
	 * @return ganador actual o null si no hay ganador
	 */
	Turno consultarGanadorPartida();

	/**
	 * Consulta el ganador de la ronda actual.
	 * 
	 * @return ganador actual o null si no hay ganador
	 */
	Turno consultarGanadorRonda();

	/**
	 * Realiza un empujón sumo con la torre en la celda de origen.
	 * Si la celda está vacía, no se realiza ninguna operación.
	 * 
	 * @param origen celda con la torre sumo que empuja
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen son incorrectas
	 */
	void empujarSumo(Celda origen) throws CoordenadasIncorrectasException;

	/**
	 * Comprueba si el movimiento es legal para el turno actual.
	 * 
	 * @param origen  origen
	 * @param destino destino
	 * @return true si el movimiento es legal o false en caso contrario
	 * @throws CoordenadasIncorrectasException si las coordenadas de alguna de las celdas son incorrectas
	 */
	boolean esMovimientoLegalConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException;

	/**
	 * Comprueba si está acabada la partida.
	 * 
	 * @return true si está acabada la partida, false en caso contrario
	 */
	boolean estaAcabadaPartida();

	/**
	 * Comprueba si está acabada la ronda.
	 * 
	 * @return true si está acabada la ronda, false en caso contrario
	 */
	boolean estaAcabadaRonda();

	/**
	 * Comprueba si está bloqueado el turno actual.
	 * 
	 * @return true si no puede realizar ningún movimiento, false en caso contrario.
	 */
	boolean estaBloqueadoTurnoActual();

	/**
	 * Comprueba si hay situación de bloqueo mutuo o deadlock.
	 * 
	 * @return true si hay bloqueo mutuo o deadlock, false en caso contrario
	 */
	boolean hayBloqueoMutuo();

	/**
	 * Mueve la torre de la celda origen al destino.
	 * 
	 * @param origen  origen
	 * @param destino destino
	 * @throws CoordenadasIncorrectasException si las coordenadas de alguna de las celdas son incorrectas
	 */
	void moverConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException;

	/**
	 * Obtiene el turno actual.
	 * 
	 * @return color del turno actual
	 */
	Turno obtenerTurno();

	/**
	 * Devuelve el número de jugadas realizadas hasta el momento.
	 * 
	 * @return número actual de jugada
	 */
	int obtenerNumeroJugada();

	/**
	 * Mueve la torre de color indicado del turno actual a su propia posición por
	 * estar bloqueada.
	 */
	void moverConTurnoActualBloqueado();

	/**
	 * Consulta el color de celda del último movimiento.
	 * 
	 * @param turno turno
	 * @return color de celda del último movimiento del turno
	 */
	Color obtenerUltimoMovimiento(Turno turno);

	/**
	 * Comprueba si es legal realizar un empujón sumo con la torre colocada en la
	 * celda de origen.
	 * 
	 * @param origen celda de origen
	 * @return true si es legal el empujón sumo, false en caso contrario
	 * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen son incorrectas
	 */
	boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException;

	/**
	 * Consulta la puntuación del jugador con turno blanco.
	 * 
	 * @return puntuación del jugador con turno blanco
	 */
	int obtenerPuntuacionTurnoBlanco();

	/**
	 * Consulta la puntuación del jugador con turno negro.
	 * 
	 * @return puntuación del jugador con turno negro
	 */
	int obtenerPuntuacionTurnoNegro();

	/**
	 * Reinicia la ronda.
	 */
	void reiniciarRonda();
	
	/**
	 * Comprueba si está alcanzada la última fila del contrario por el turno indicado.
	 * 
	 * @param turno turno
	 * @return true si hay torre del turno en la fila de partida del contrario,
	 *         false en caso contrario
	 * @since 4.0
	 */
	boolean estaAlcanzadaUltimaFilaPor(Turno turno);
}