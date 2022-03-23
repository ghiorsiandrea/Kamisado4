package juego.control;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Sentido;

public abstract class ArbitroAbstracto implements Arbitro {

    protected Color colorCeldaUltimoMovimiento;

    protected Color colorPenultimoMovimiento;

    protected Tablero tablero;

    protected Turno turnoActual;

    protected int numeroJugada;

    protected boolean ultimoMovimientoEsCero;

    public ArbitroAbstracto(Tablero tablero) {
        this.colorCeldaUltimoMovimiento = null;
        this.colorPenultimoMovimiento = null;
        this.tablero = tablero;
        this.turnoActual = null;
        this.numeroJugada = 0;
        this.ultimoMovimientoEsCero = false;
    }

    /**
     *  El método cambiarTurno cambia el turno al otro jugador.
     * ◦ Nota: este método es amigable, y se limita su acceso a clases del mismo paquete. (PACKAGE PRIVATE)
     */
    void cambiarTurno() {

        if (turnoActual == Turno.NEGRO) {
            turnoActual = Turno.BLANCO;
        } else {
            turnoActual = Turno.NEGRO;
        }
    }

    // TODO: ACÀ REVISAR CADA MÈTODO Y REUBICARLOS

    //TODO: TRY CATCH  -creo que esta es igual en ambos

    /**
     *  El método estaAlcanzadaUltimaFilaPor devuelve true si el jugador con el turno pasado ha alcanzado la fila
     * de salida del jugador contrario, o false en caso contrario.
     */
    public boolean estaAlcanzadaUltimaFilaPor(Turno turno) {

        if (turno == null) {
            return false;
        }
        int ultimaFila = 0;
        if (turno == Turno.BLANCO) {
            ultimaFila = tablero.obtenerNumeroFilas() - 1;
        }

        for (int i = 0; i < tablero.obtenerNumeroColumnas(); i++) {
            try {
                if (tablero.obtenerCelda(ultimaFila, i).obtenerTurnoDeTorre() == turno) {
                    return true;
                }
            } catch (CoordenadasIncorrectasException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    protected Turno obtenerTurnoSiguiente() {
        if (turnoActual == Turno.BLANCO) {
            return Turno.NEGRO;
        } else {
            return Turno.BLANCO;
        }
    }

//todo> aca en realidad quiero que los sentidos sean para cada tipo de partida, peero no se como hacerlo llamar al
// metodo de un hijo en la superclase *porque si lo pongo en estandar y simple no se como llamarlo en abstracto, y
// si lo necesito en abstracto, deberia crear una logica unificada?*

    protected Sentido[] sentidosDeTurno(Turno turno) {

        if (turno == Turno.BLANCO) {
            return new Sentido[]{Sentido.DIAGONAL_SO, Sentido.VERTICAL_S, Sentido.DIAGONAL_SE};

        } else {
            return new Sentido[]{Sentido.DIAGONAL_NO, Sentido.VERTICAL_N, Sentido.DIAGONAL_NE};
        }
    }

    //TODO: TRY CATCH

    /**
     * Este mètodo no se està usando ya que al usar el turno siguiente habia una referencia circular con Consultar
     * ganador y bloqueo mutuo (porque el ganador es el turno que no genera el bloqueo y porque si hay un ganador,
     * entonces no hay bloqueo mutuo a futuro (pero en el pasado si lo hubo).
     */

    private boolean estaBloqueadoTurnoSiguienteConTurnoActualBloqueado() {
        // este if no es necesario ya que se evalua en el metodo establoqueadoturnoactual
        if (colorCeldaUltimoMovimiento == null) {
            return false;
        }
        Celda celdaTurnoActualUtimoMovimiento = tablero.buscarTorre(turnoActual, colorCeldaUltimoMovimiento);
        Celda celdaTorreSiguienteMovimiento = tablero.buscarTorre(obtenerTurnoSiguiente(), celdaTurnoActualUtimoMovimiento.obtenerColor());

        for (Sentido sentido : sentidosDeTurno(obtenerTurnoSiguiente())) {

            int filaPosibleMovimiento = celdaTorreSiguienteMovimiento.obtenerFila() +
                    sentido.obtenerDesplazamientoEnFilas();
            int columnaPosibleMovimiento = celdaTorreSiguienteMovimiento.obtenerColumna() +
                    sentido.obtenerDesplazamientoEnColumnas();
            Celda celdaPosibleMovimiento = null;
            try {
                celdaPosibleMovimiento = tablero.obtenerCelda(filaPosibleMovimiento, columnaPosibleMovimiento);
            } catch (CoordenadasIncorrectasException e) {
                e.printStackTrace();
            }

            // Aca reutilizamos código pero la carga de trabajo es significativamente mayor a la solución siguiente
            // if (esMovimientoLegalConTurnoActual(celdaTurnoActualUtimoMovimiento, celdaPosibleMovimiento)) {
            // return false;
            //
            //}

            if (celdaPosibleMovimiento != null && celdaPosibleMovimiento.estaVacia()) {
                return false;
            }

        }

        return true;
    }


    /**
     *  El método obtenerNumeroJugada consulta el número de jugadas finalizadas en la partida. Todos
     * los movimientos cuentan, tanto de torre a otra celda, y de “distancia cero”.
     */
    public int obtenerNumeroJugada() {
        return numeroJugada;
    }

    /**
     *  El método obtenerTurno consulta qué jugador tiene el turno actualmente.
     */
    public Turno obtenerTurno() {
        return turnoActual;
    }

    /**
     *  El método obtenerUltimoMovimiento devuelve el color de la celda donde realizó su último
     * movimiento la torre del turno indicado.
     */
    public Color obtenerUltimoMovimiento(Turno turno) {
        if (turno == turnoActual) {
            return colorPenultimoMovimiento;
        }
        return colorCeldaUltimoMovimiento;

    }


}
