package juego.control;

import juego.modelo.*;
import juego.modelo.Color;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Message;
import juego.util.Sentido;

import java.util.ArrayList;
import java.util.List;

public class ArbitroEstandar extends ArbitroAbstracto {

    private int numeroPuntosTurnoNegro;

    private int numeroPuntosTurnoBlanco;

    // private List<TorreSumoUno> listaTorresSumoUno; SOLO PARA SOLUCION 2 RE REINICIAR RONDA

    public ArbitroEstandar(Tablero tablero) {
        super(tablero);
        numeroPuntosTurnoNegro = 0;
        numeroPuntosTurnoBlanco = 0;
        // listaTorresSumoUno = new ArrayList<TorreSumoUno>(); SOLO PARA SOLUCION 2 RE REINICIAR RONDA
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
     *                                         Las torres sumo uno tienen unas reglas de movimiento adicionales:
     *                                         • Solo pueden desplazarse un máximo de una distancia de 5 celdas en cualquiera de los sentidos básicos (vertical o diagonal).
     *                                         • Pueden “empujar” una posición hacia delante a torres del contrario que la bloqueen (denominado
     *                                         “empujón sumo”), pero solo en sentido vertical:
     *                                         ◦ Solo pueden empujar una torre del turno contrario.
     *                                         ◦ Detrás de esa torre empujada, debe haber una celda vacía. No se puede “empujar” o echar torres del turno
     *                                         contrario fuera del tablero.
     *                                         ◦ No se puede empujar a otra “torre sumo uno” del contrario, solo a una torre simple.
     *                                         ◦ Cuando se produce un “empujón sumo”, el turno contrario pierde turno y vuelve a mover el
     *                                         turno que realizó el empujón.
     *                                         ◦ El color de la torre a mover, tras el empujón, se obtiene del color de la celda donde ha quedado situada la
     *                                         torre del contrario.
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
        this.numeroJugada++;
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

    private void sumarPuntosPorTurno(Torre torreGanadora) {
        int puntosASumar = torreGanadora.obtenerNumeroPuntos();

        if (torreGanadora.obtenerTurno() == Turno.BLANCO) {
            numeroPuntosTurnoBlanco = numeroPuntosTurnoBlanco + puntosASumar;
        } else {
            numeroPuntosTurnoNegro = numeroPuntosTurnoNegro + puntosASumar;
        }

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


        // Metodo 1, saco todas las torres del tablero y las guardo en una lista
        Turno turnoGanadorRonda = consultarGanadorRonda();
        try {
            List<Torre> listaTorres = new ArrayList<>();
            for (int fila = 0; fila < tablero.obtenerNumeroFilas(); fila++) {
                for (int columna = 0; columna < tablero.obtenerNumeroColumnas(); columna++) {
                    Celda celda = tablero.obtenerCelda(fila, columna);
                    if (!celda.estaVacia()) {
                        listaTorres.add(celda.obtenerTorre());
                        celda.eliminarTorre();
                    }
                }
            }
            // Debo colocar todas las torres en sus lugares iniciales
            for (Torre torre : listaTorres) {
                // Esto es un operador ternario que sustituye al if
                int fila = torre.obtenerTurno() == Turno.BLANCO ? 0 : tablero.obtenerNumeroFilas() - 1;
                for (int columna = 0; columna < tablero.obtenerNumeroColumnas(); columna++) {
                    Celda celda = tablero.obtenerCelda(fila, columna);
                    if (celda.obtenerColor() == torre.obtenerColor()) {
                        tablero.colocar(torre, celda);
                    }
                }
            }

            //Establezco el nuevo estado del arbitro
            this.colorCeldaUltimoMovimiento = null;
            this.colorPenultimoMovimiento = null;
            this.turnoActual = turnoGanadorRonda == Turno.BLANCO ? Turno.NEGRO : Turno.BLANCO;
            this.numeroJugada = 0;
            this.ultimoMovimientoEsCero = false;

        } catch (CoordenadasIncorrectasException e) {
            System.out.println(e.getMessage());
        }

        //Metodo 2: Creo una lista de torres sumos uno, luego asigno esas torres al tablero mediante un for
        //TUVIMOS QUE BORRAR ESTO PORQUE PARA EL TEST SE NECESITABA QUE SEA EL MISMO TABLERO!


//        Turno turnoGanadorRonda = consultarGanadorRonda();
//
//        this.tablero = new Tablero();
//        colocarTorres();
//        for (TorreSumoUno torreSumoUno : listaTorresSumoUno) {
//            Turno turnoSumoUno = torreSumoUno.obtenerTurno();
//            Color colorSumoUno = torreSumoUno.obtenerColor();
//            Celda celdaTorreSumoUno = tablero.buscarTorre(turnoSumoUno, colorSumoUno);
//            celdaTorreSumoUno.eliminarTorre();
//            celdaTorreSumoUno.establecerTorre(torreSumoUno);
//
//            //Establezco el nuevo estado del arbitro
//            this.colorCeldaUltimoMovimiento = null;
//            this.colorPenultimoMovimiento = null;
//            this.turnoActual = turnoGanadorRonda == Turno.BLANCO ? Turno.NEGRO : Turno.BLANCO;
//            this.numeroJugada = 0;
//            this.ultimoMovimientoEsCero = false;
//        }

    }


    //TODO: ACA COMIENZO A DISCRIMINAR MÈTODOS


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
        try {
            if (esEmpujonSumoLegal(celdaTurnoActualUtimoMovimiento)) {
                return false;
            }
        } catch (CoordenadasIncorrectasException e) {
            // Esto no deberia pasar. Si pasa es un error de programacion
            throw new RuntimeException("Error de programacion.");
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
        if (estaAcabadaRonda()) {
            Torre torreDestino = destino.obtenerTorre();
            sumarPuntosPorTurno(torreDestino);
            chequearSeSeCreaTorreSumoUno(torreDestino);
        }

    }

    private void chequearSeSeCreaTorreSumoUno(Torre torreGanadora) {
        if (torreGanadora instanceof TorreSimple) {
            Celda celdaTorreGanadora = torreGanadora.obtenerCelda();
            celdaTorreGanadora.eliminarTorre();
            TorreSumoUno newTorreSumoUno = new TorreSumoUno(torreGanadora.obtenerTurno(), torreGanadora.obtenerColor());
            celdaTorreGanadora.establecerTorre(newTorreSumoUno);
            // listaTorresSumoUno.add(newTorreSumoUno); SOLO PARA SOLUCION 2 RE REINICIAR RONDA
        }
    }

}




