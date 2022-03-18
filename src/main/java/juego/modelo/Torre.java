package juego.modelo;

/**
 * Torre.
 * 
 * @author <a href="rmartico@ubu.es">Raúl Marticorena</a>
 * @version 1.0
 * @since 2.0
 */
public interface Torre {

	/**
	 * Obtiene el turno.
	 * 
	 * @return turno
	 */
	Turno obtenerTurno();

	/**
	 * Obtiene el color.
	 * 
	 * @return color
	 */
	Color obtenerColor();

	/**
	 * Obtiene la celda donde está colocada la torre.
	 * Si no está colocda en el tablero devolverá null.
	 * 
	 * @return celda
	 */
	Celda obtenerCelda();

	/**
	 * Establece la celda en la que se coloca dicha torre.
	 * 
	 * @param celda celda
	 */
	void establecerCelda(Celda celda);

	/**
	 * Devuelve el estado de la torre en formato texto.
	 * 
	 * @return texto asociado a la torre
	 */
	String toString();
	
	/**
	 * Obtiene el número de dientes.
	 * 
	 * @return número de dientes
	 */
	int obtenerNumeroDientes();
	
	/**
	 * Obtiene el número de puntos que suma la torre
	 * al llegar a la fila de origen del turno contrario.
	 * 
	 * @return número de puntos
	 */
	int obtenerNumeroPuntos();
	
	/**
	 * Obtiene la distancia máxima en celdas a la que se puede mover
	 * la torre de origen a destino.
	 * 
	 * @return distancia máxima permitida para mover la torre
	 */
	int obtenerMaximoAlcance();
	
	/**
	 * Obtiene el número máximo de torres que puede empujar.
	 * 
	 * @return número máximo de torres a empujar
	 */
	int obtenerNumeroMaximoTorresAEmpujar();

}