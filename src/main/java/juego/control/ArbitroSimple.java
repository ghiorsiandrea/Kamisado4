package juego.control;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Sentido;

public class ArbitroSimple extends ArbitroAbstracto {
    public ArbitroSimple(Tablero tablero) {
        super(tablero);
    }

    @Override
    public Celda buscarCeldaConTorreDeColor(Turno turno, Color color) {
        return null;
    }

    @Override
    public Turno consultarGanadorPartida() {
        return null;
    }

    @Override
    public Turno consultarGanadorRonda() {
        return null;
    }

    @Override
    public void empujarSumo(Celda origen) throws CoordenadasIncorrectasException {

    }

    @Override
    public boolean estaAcabadaPartida() {
        return false;
    }

    @Override
    public boolean estaAcabadaRonda() {
        return false;
    }


    @Override
    public Turno obtenerTurno() {
        return null;
    }

    @Override
    public int obtenerNumeroJugada() {
        return 0;
    }

    @Override
    public Color obtenerUltimoMovimiento(Turno turno) {
        return null;
    }

    @Override
    public boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException {
        return false;
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

    }

    @Override
    public boolean estaAlcanzadaUltimaFilaPor(Turno turno) {
        return false;
    }

    //TODO: ACA COMIENZO A DISCRIMINAR MÈTODOS

    /**
     *  El método colocarTorres() inicializa el tablero asignado en el constructor, con todas las torres
     * de ambos jugadores en sus filas correspondientes.
     */
    @Override
    public void colocarTorres() {
        //        esta es la opcion larga, solo queda de ejemplo
        //        tablero.colocar(new Torre(Turno.BLANCO, Color.NARANJA), 0, 0);
        //        tablero.colocar(new Torre(Turno.BLANCO, Color.NARANJA), 0, 0);

        for (int i = 0; i < tablero.obtenerNumeroColumnas(); i++) {
            try {
                tablero.colocar(new TorreSimple(Turno.BLANCO, tablero.obtenerCelda(0, i).obtenerColor()), 0, i);
            } catch (CoordenadasIncorrectasException e) {
                e.printStackTrace();
            }
            try {
                tablero.colocar(new TorreSimple(Turno.NEGRO, tablero.obtenerCelda(7, i).obtenerColor()), 7, i);
            } catch (CoordenadasIncorrectasException e) {
                e.printStackTrace();
            }
        }
        this.turnoActual = Turno.NEGRO;

    }

    /**
     *  El método colocarTorres(Torre[], String[], Color, Color, Turno) permite inicializar el tablero con una
     * configuración diferente a la inicial, pasando un array de torres, un array de coordenadas en notacion algebraica
     * donde colocar las torres, el color del último movimiento del jugador con turno negro, el color del último
     * movimiento del jugador con turno blanco y el jugador con turno actual.
     * ◦ Nota: este método se implementa para ser utilizados en los tests automáticos y para facilitar al alumnado
     * las pruebas y depuración del código.
     * Se puede sustituir temporalmente la invocación al método colocarTorres() por este método para cargar partidas
     * más simples de probar, sin tener que realizar tantos movimientos.
     */
    @Override
    public void colocarTorres(Torre[] torres, String[] coordenadas, Color ultimoColorTurnoNegro,
                              Color ultimoColorTurnoBlanco, Turno turnoActual) throws CoordenadasIncorrectasException {

        if (torres.length != coordenadas.length || torres.length == 0) {
            return;
        }
        for (int i = 0; i < torres.length; i++) {
//            Esta es una forma mas larga y ordenada de hacerlo
//            Torre torre = torres[i];
//            String coordenada = coordenadas[i];
//            tablero.colocar(torre, coordenada);

            tablero.colocar(torres[i], coordenadas[i]);
        }
        this.turnoActual = turnoActual;
        if (turnoActual == Turno.NEGRO) {
            colorCeldaUltimoMovimiento = ultimoColorTurnoBlanco;
            colorPenultimoMovimiento = ultimoColorTurnoNegro;
        } else {
            colorCeldaUltimoMovimiento = ultimoColorTurnoNegro;
            colorPenultimoMovimiento = ultimoColorTurnoBlanco;

        }
    }

    /**
     *  El método consultarGanador retorna el turno del ganador de la partida, bien por alcanzar la fila  de salida
     * del jugador contrario, o bien por existir bloqueo mutuo. Si no hay ganador devuelve null.
     */
    public Turno consultarGanador() {

        if (estaAlcanzadaUltimaFilaPor(Turno.BLANCO)) {
            return Turno.BLANCO;
        }
        if (estaAlcanzadaUltimaFilaPor(Turno.NEGRO)) {
            return Turno.NEGRO;
        }
        if (hayBloqueoMutuo()) {
            return obtenerTurnoSiguiente();
        }
        return null;
    }

    /**
     *  El método esMovimientoLegalConTurnoActual dadas la celda origen y destino, devuelve true si es legal
     * realizar el movimiento con el turno actual, o false en caso contrario.
     */
    @Override
    public boolean esMovimientoLegalConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException {

        if (origen.estaVacia() || !destino.estaVacia()) {
            return false;
        }
        if (!tablero.estanVaciasCeldasEntre(origen, destino)) {
            return false;
        }
        if (colorCeldaUltimoMovimiento != null && origen.obtenerColorDeTorre() != colorCeldaUltimoMovimiento) {
            return false;
        }

        if (turnoActual == Turno.NEGRO && origen.obtenerFila() < destino.obtenerFila()) {
            return false;
        }
        if (turnoActual == Turno.BLANCO && origen.obtenerFila() > destino.obtenerFila()) {
            return false;
        }
        return turnoActual == origen.obtenerTurnoDeTorre();
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

            int filaPosibleMovimiento = celdaTurnoActualUtimoMovimiento.obtenerFila() +
                    sentido.obtenerDesplazamientoEnFilas();
            int columnaPosibleMovimiento = celdaTurnoActualUtimoMovimiento.obtenerColumna() +
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

    /**
     *  El método hayBloqueoMutuo devuelve true si ninguno de los jugadores puede mover la torre
     * que corresponde, false en caso contrario.
     */
    @Override
    public boolean hayBloqueoMutuo() {

//        no puedo usar este metodo pues consultar ganador usa al metodo hay bloqueo mutuo
//        if (consultarGanador() != null) {
//            return false;
//        }

        if (ultimoMovimientoEsCero && estaBloqueadoTurnoActual()) {
            return true;
        } else return false;
    }
    protected Sentido[] sentidosDeTurno(Turno turno) {

        if (turno == Turno.BLANCO) {
            return new Sentido[]{Sentido.DIAGONAL_SO, Sentido.VERTICAL_S, Sentido.DIAGONAL_SE};

        } else {
            return new Sentido[]{Sentido.DIAGONAL_NO, Sentido.VERTICAL_N, Sentido.DIAGONAL_NE};
        }
    }


    /**
     *  El método moverConTurnoActualBloqueado realizar un movimiento de “distancia cero” para el
     * jugador con turno actual. Se supone que previamente se ha comprobado la situación de bloqueo
     * del jugador y no es necesario volver a comprobarlo. Debe ajustar el color de último movimiento
     * para el turno actual y cambiar el turno, teniendo en cuenta que se ha finalizado una jugada.
     */
    @Override
    public void moverConTurnoActualBloqueado() {
        Celda celdaTorreConMovimientoCero = tablero.buscarTorre(turnoActual, colorCeldaUltimoMovimiento);
        colorPenultimoMovimiento = colorCeldaUltimoMovimiento;
        colorCeldaUltimoMovimiento = celdaTorreConMovimientoCero.obtenerColor();

        this.numeroJugada++;
        this.cambiarTurno();
        ultimoMovimientoEsCero = true;
    }

}
