package juego.control;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Sentido;

public class ArbitroSimple extends ArbitroAbstracto {
    public ArbitroSimple(Tablero tablero) {
        super(tablero);
    }

    /**
     * Consulta el ganador de la partida.
     *
     * @return ganador actual o null si no hay ganador
     */
    @Override
    public Turno consultarGanadorPartida() {
        return consultarGanador();
    }

    /**
     * Consulta el ganador de la ronda actual.
     *
     * @return ganador actual o null si no hay ganador
     */
    @Override
    public Turno consultarGanadorRonda() {
        return consultarGanadorPartida();
    }

    @Override
    public void empujarSumo(Celda origen) throws CoordenadasIncorrectasException {
        throw new UnsupportedOperationException();
    }

    /**
     * Comprueba si está acabada la partida.
     *
     * @return true si está acabada la partida, false en caso contrario
     */
    @Override
    public boolean estaAcabadaPartida() {
        return consultarGanador() != null;
    }

    /**
     * Comprueba si está acabada la ronda.
     *
     * @return true si está acabada la ronda, false en caso contrario
     */
    @Override
    public boolean estaAcabadaRonda() {
        return estaAcabadaPartida();
    }

    @Override
    public boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int obtenerPuntuacionTurnoBlanco() {
        return 0;
    }

    @Override
    public int obtenerPuntuacionTurnoNegro() {
        return 0;
    }

    @Override
    public void reiniciarRonda() {
        throw new UnsupportedOperationException();
    }


    //TODO: TRY CATCH

    /**
     *  El método estaBloqueadoTurnoActual devuelve true si el jugador con turno actual no puede mover la torre que
     * corresponde, o false en caso contrario.
     */
    @Override
    public boolean estaBloqueadoTurnoActual() {
        if (colorCeldaUltimoMovimiento == null) {
            return false;
        }
        Celda celdaTurnoActualUtimoMovimiento = tablero.buscarTorre(turnoActual, colorCeldaUltimoMovimiento);

        for (Sentido sentido : sentidosDeTurno(turnoActual)) {

            int filaPosibleMovimiento = celdaTurnoActualUtimoMovimiento.obtenerFila() + sentido.obtenerDesplazamientoEnFilas();
            int columnaPosibleMovimiento = celdaTurnoActualUtimoMovimiento.obtenerColumna() + sentido.obtenerDesplazamientoEnColumnas();
            Celda celdaPosibleMovimiento = null;
            try {
                celdaPosibleMovimiento = tablero.obtenerCelda(filaPosibleMovimiento, columnaPosibleMovimiento);
            } catch (CoordenadasIncorrectasException ignored) {
                //Si al obtener celda, uno de los posibles sentidos para el turno actual se encuentra bloqueado, no
                // deberia detenerse el juego o aparecer la ruta del error durante la partida
                continue;
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
     *  El método moverConTurnoActual realiza el movimiento de la torre desde la celda origen a la
     * celda destino. Se supone que previamente se ha comprobado la legalidad de la jugada y no es
     * necesario volver a comprobarla. Debe ajustar el color de último movimiento para el turno actual y
     * cambiar el turno, teniendo en cuenta que se ha finalizado una jugada.
     */
    @Override
    public void moverConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException {

        tablero.moverTorre(origen, destino);
        this.numeroJugada++;
        this.cambiarTurno();
        colorPenultimoMovimiento = colorCeldaUltimoMovimiento;
        colorCeldaUltimoMovimiento = destino.obtenerColor();
        ultimoMovimientoEsCero = false;
    }


}
