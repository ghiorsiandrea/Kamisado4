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

//       forma larga
//        if (tablero.hayTorreColorContrario(turno)) {
//            return true;
//        }
//        return false;


        return tablero.hayTorreColorContrario(turno);
    }

    protected Turno obtenerTurnoSiguiente() {
        if (turnoActual == Turno.BLANCO) {
            return Turno.NEGRO;
        } else {
            return Turno.BLANCO;
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

    /**
     * Retorna la celda que contiene la torre del turno y color indicado.
     *
     * @param turno turno
     * @param color color
     * @return celda
     */
    @Override
    public Celda buscarCeldaConTorreDeColor(Turno turno, Color color) {
        return tablero.buscarTorre(turno, color);
    }

    @Override
    public boolean hayBloqueoMutuo() {

//        no puedo usar este metodo pues consultar ganador usa al metodo hay bloqueo mutuo
//        if (consultarGanador() != null) {
//            return false;
//        }

// forma larga
//        if (ultimoMovimientoEsCero && estaBloqueadoTurnoActual()) {
//            return true;
//        } else return false;

        return ultimoMovimientoEsCero && estaBloqueadoTurnoActual();
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

        //Este es el metodo largo, pero aplicando el polimorfismo sabemos que la torre se va a comportar diferente segun
        // si sea una instancia de sumo uno o simple, y el metodo corto ya piensa a futuro
//        int distancia = tablero.obtenerDistancia(origen, destino);
//
//        if (origen.obtenerTorre()instanceof TorreSumoUno && distancia > origen.obtenerTorre().obtenerMaximoAlcance()) {
//           return false;
//        }

        //Mètodo corto
        int distancia = tablero.obtenerDistancia(origen, destino);

        if (distancia > origen.obtenerTorre().obtenerMaximoAlcance()) {
            return false;
        }
        return turnoActual == origen.obtenerTurnoDeTorre();
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
     *  El método colocarTorres() inicializa el tablero asignado en el constructor, con todas las torres
     * de ambos jugadores en sus filas correspondientes.
     */
    @Override
    public void colocarTorres() {

        // esta es la opcion larga, solo queda de ejemplo
        // tablero.colocar(new Torre(Turno.BLANCO, Color.NARANJA), 0, 0);
        // tablero.colocar(new Torre(Turno.BLANCO, Color.NARANJA), 0, 0);

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
}
