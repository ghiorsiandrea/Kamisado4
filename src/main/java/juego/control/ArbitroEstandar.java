package juego.control;

import juego.modelo.*;
import juego.modelo.Color;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Message;
import juego.util.Sentido;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArbitroEstandar extends ArbitroAbstracto {

    private int numeroPuntosTurnoNegro;

    private int numeroPuntosTurnoBlanco;

    private List<TorreSumoUno> listaTorresSumoUno;

    public ArbitroEstandar(Tablero tablero) {
        super(tablero);
        numeroPuntosTurnoNegro = 0;
        numeroPuntosTurnoBlanco = 0;
        listaTorresSumoUno = new ArrayList<TorreSumoUno>();
    }

    @Override
    public Turno consultarGanadorPartida() {
        if (numeroPuntosTurnoNegro >= 3) {
            return Turno.NEGRO;
        }
        if (numeroPuntosTurnoBlanco >= 3) {
            return Turno.BLANCO;
        }
        return null;
    }

    @Override
    public Turno consultarGanadorRonda() {
        return consultarGanador();
    }
// TODO

    /**
     * Realiza un empujón sumo con la torre en la celda de origen.
     * Si la celda está vacía, no se realiza ninguna operación.
     *
     * @param origen celda con la torre sumo que empuja
     * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen son incorrectas
     *
     * Las torres sumo uno tienen unas reglas de movimiento adicionales:
     * • Solo pueden desplazarse un máximo de una distancia de 5 celdas en cualquiera de los sentidos
     * básicos (vertical o diagonal).
     * • Pueden “empujar” una posición hacia delante a torres del contrario que la bloqueen (denominado
     * “empujón sumo”), pero solo en sentido vertical:
     * ◦ Solo pueden empujar una torre del turno contrario.
     * ◦ Detrás de esa torre empujada, debe haber una celda vacía. No se puede “empujar” o echar torres del turno
     * contrario fuera del tablero.
     * ◦ No se puede empujar a otra “torre sumo uno” del contrario, solo a una torre simple.
     * ◦ Cuando se produce un “empujón sumo”, el turno contrario pierde turno y vuelve a mover el
     * turno que realizó el empujón.
     * ◦ El color de la torre a mover, tras el empujón, se obtiene del color de la celda donde ha quedado situada la
     * torre del contrario.
     */
    @Override
    public void empujarSumo(Celda origen) throws CoordenadasIncorrectasException {

        if (!tablero.estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())) {
            throw new CoordenadasIncorrectasException(String.format(Message.ERROR_CELDA_FUERA_TABLERO,
                    origen.obtenerFila(), origen.obtenerColumna()));
        }

        Celda celdaAMover;
        Celda celdaObjetivo;

        if (turnoActual == Turno.BLANCO) {
            celdaAMover = tablero.obtenerCelda(origen.obtenerFila() + 1, origen.obtenerColumna());
            celdaObjetivo = tablero.obtenerCelda(origen.obtenerFila() + 2, origen.obtenerColumna());
        } else {
            celdaAMover = tablero.obtenerCelda(origen.obtenerFila() - 1, origen.obtenerColumna());
            celdaObjetivo = tablero.obtenerCelda(origen.obtenerFila() - 2, origen.obtenerColumna());
        }

        tablero.moverTorre(celdaAMover, celdaObjetivo);
        tablero.moverTorre(origen, celdaAMover);

        //Establezco el nuevo estado del arbitro

        this.colorPenultimoMovimiento = colorCeldaUltimoMovimiento;
        this.colorCeldaUltimoMovimiento = celdaObjetivo.obtenerColor();
        this.numeroJugada ++;
        this.ultimoMovimientoEsCero = false;

    }

    @Override
    public boolean estaAcabadaPartida() {
        if (numeroPuntosTurnoNegro >= 3 || numeroPuntosTurnoBlanco >= 3) {
            return true;
        }
        return false;
    }

    @Override
    public boolean estaAcabadaRonda() {
        return consultarGanador() != null;
    }

    /**
     * Comprueba si es legal realizar un empujón sumo con la torre colocada en la celda de origen.
     *
     * @param origen celda de origen
     * @return true si es legal el empujón sumo, false en caso contrario
     * @throws CoordenadasIncorrectasException si las coordenadas de la celda origen son incorrectas
     */
    // TODO
    @Override

    public boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException {

        if (!tablero.estaEnTablero(origen.obtenerFila(), origen.obtenerColumna())) {
            throw new CoordenadasIncorrectasException(String.format(Message.ERROR_CELDA_FUERA_TABLERO,
                    origen.obtenerFila(), origen.obtenerColumna()));
        }
        if (!(origen.obtenerTorre() instanceof TorreSumoUno)) {
            return false;
        }

        Celda celdaAMover;
        Celda celdaObjetivo;


        try {
            if (turnoActual == Turno.BLANCO) {
                celdaAMover = tablero.obtenerCelda(origen.obtenerFila() + 1, origen.obtenerColumna());
                celdaObjetivo = tablero.obtenerCelda(origen.obtenerFila() + 2, origen.obtenerColumna());
            } else {
                celdaAMover = tablero.obtenerCelda(origen.obtenerFila() - 1, origen.obtenerColumna());
                celdaObjetivo = tablero.obtenerCelda(origen.obtenerFila() - 2, origen.obtenerColumna());
            }

            if (celdaAMover.estaVacia() || celdaAMover.obtenerTurnoDeTorre() == turnoActual || !celdaObjetivo.estaVacia()) {
                return false;
            }
            if ((celdaAMover.obtenerTorre() instanceof TorreSumoUno)) {
                return false;
            }
        } catch (CoordenadasIncorrectasException e) {
            return false;
        }



        return true;
    }


    private void SumarPuntosPorTurno() {
        if (consultarGanador() == Turno.BLANCO) {
            numeroPuntosTurnoBlanco++;
        } else numeroPuntosTurnoNegro++;
    }

    @Override
    public int obtenerPuntuacionTurnoBlanco() {
        return numeroPuntosTurnoBlanco;
    }

    @Override
    public int obtenerPuntuacionTurnoNegro() {
        return numeroPuntosTurnoNegro;
    }

    @Override
    public void reiniciarRonda() {
        // Vamos a hacer dos métodos


//        // Metodo 1, saco todas las torres del tablero y las guardo en una lista
//        Turno turnoGanadorRonda = consultarGanadorRonda();
//        try {
//            List<Torre> listaTorres = new ArrayList<>();
//            for (int fila = 0; fila < tablero.obtenerNumeroFilas(); fila++) {
//                for (int columna = 0; columna < tablero.obtenerNumeroColumnas(); columna++) {
//                    Celda celda = tablero.obtenerCelda(fila, columna);
//                    if (!celda.estaVacia()) {
//                        listaTorres.add(celda.obtenerTorre());
//                        celda.eliminarTorre();
//                    }
//                }
//            }
//            // Debo colocar todas las torres en sus lugares iniciales
//            for (Torre torre : listaTorres) {
//                // Esto es un operador ternario que sustituye al if
//                int fila = torre.obtenerTurno() == Turno.BLANCO ? 0 : tablero.obtenerNumeroFilas() - 1;
//                for (int columna = 0; columna < tablero.obtenerNumeroColumnas(); columna++) {
//                    Celda celda = tablero.obtenerCelda(fila, columna);
//                    if (celda.obtenerColor() == torre.obtenerColor()) {
//                        tablero.colocar(torre, celda);
//                    }
//                }
//            }
//
//            //Establezco el nuevo estado del arbitro
//            this.colorCeldaUltimoMovimiento = null;
//            this.colorPenultimoMovimiento = null;
//            this.turnoActual = turnoGanadorRonda == Turno.BLANCO ? Turno.NEGRO : Turno.BLANCO;
//            this.numeroJugada = 0;
//            this.ultimoMovimientoEsCero = false;
//
//        } catch (CoordenadasIncorrectasException e) {
//            System.out.println(e.getMessage());
//        }

        //Metodo 2: Creo una lista de torres sumos uno, luego asigno esas torres al tablero mediante un for

        Turno turnoGanadorRonda = consultarGanadorRonda();

        this.tablero = new Tablero();
        colocarTorres();
        for (TorreSumoUno torreSumoUno : listaTorresSumoUno) {
            Turno turnoSumoUno = torreSumoUno.obtenerTurno();
            Color colorSumoUno = torreSumoUno.obtenerColor();
            Celda celdaTorreSumoUno = tablero.buscarTorre(turnoSumoUno, colorSumoUno);
            celdaTorreSumoUno.eliminarTorre();
            celdaTorreSumoUno.establecerTorre(torreSumoUno);

            //Establezco el nuevo estado del arbitro
            this.colorCeldaUltimoMovimiento = null;
            this.colorPenultimoMovimiento = null;
            this.turnoActual = turnoGanadorRonda == Turno.BLANCO ? Turno.NEGRO : Turno.BLANCO;
            this.numeroJugada = 0;
            this.ultimoMovimientoEsCero = false;
        }

    }


    //TODO: ACA COMIENZO A DISCRIMINAR MÈTODOS

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




