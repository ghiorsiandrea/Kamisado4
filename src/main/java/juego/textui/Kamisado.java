package juego.textui;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.BACK_COLOR;
import static com.diogonunes.jcolor.Attribute.BLACK_BACK;
import static com.diogonunes.jcolor.Attribute.BLACK_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_BLUE_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_BLUE_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_GREEN_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_GREEN_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_MAGENTA_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_RED_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_RED_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_WHITE_BACK;
import static com.diogonunes.jcolor.Attribute.BRIGHT_WHITE_TEXT;
import static com.diogonunes.jcolor.Attribute.BRIGHT_YELLOW_BACK;
import static com.diogonunes.jcolor.Attribute.TEXT_COLOR;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import com.diogonunes.jcolor.Attribute;

import juego.control.Arbitro;
import juego.control.ArbitroEstandar;
import juego.control.ArbitroSimple;
import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

/**
 * Ajedrez en modo texto.
 * <p>
 * Se abusa del uso de static tanto en atributos como en métodos para comprobar
 * su similitud a variables globales y funciones globales de otros lenguajes.
 *
 * @author <a href="rmartico@ubu.es">Raúl Marticorena</a>
 * @version 4.0.0
 * @since JDK 16
 */
public class Kamisado {

    /**
     * Tamaño en caracteres de una jugada de mover torre.
     */
    private static final int TAMAÑO_JUGADA = 4;

    /**
     * Tamaño en caracteres de una jugada de empujón sumo.
     */
    private static final int TAMAÑO_EMPUJON_SUMO = 2;

    /**
     * Tablero.
     */
    private static Tablero tablero;

    /**
     * Arbitro.
     */
    private static Arbitro arbitro;

    /**
     * Lector por teclado.
     */
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Tipo de arbitro a instanciar.
     */
    private static String configuracion;

    /**
     * Método raíz.
     *
     * @param args argumentos de entrada
     */
    // TODO:
    public static void main(String[] args) {
        // COMPLETAR POR EL ALUMNADO
        // Algoritmo de kamisado en modo texto.
        // Revisar los métodos ya disponibles y reutilizarlos.

        //args = new String[]{"simple"};
        //System.out.println(extraerModoArbitro(args));


        mostrarMensajeBienvenida();
        String inicioJuego = scanner.nextLine();
        inicializarPartida(configuracion = "simple");

        switch (inicioJuego) {
            case "salir":
                mostrarInterrupcionPartida();
                System.exit(0);
                break;
            case "texto":
                mostrarTableroEnFormatoTexto();
                mostrarMensajeBienvenida();
            case "elegir":
                elegirModoJuego();
            default:
                mostrarErrorEnFormatoDeEntrada();

        }
        while (arbitro.consultarGanadorRonda() == null) ;
        finalizarPartida();

    }

    private static void elegirModoJuego() {
        do {
            String modoJuego = recogerModoJuego().toLowerCase();

            switch (modoJuego) {

                case "simple":
                    System.out.println("Disfrute de la partida en modo simple");
                    mostrarTableroEnPantalla();
                    do {
                        String jugada = recogerJugadaSimple().toLowerCase();

                        switch (jugada) {

                            case "salir":
                                mostrarInterrupcionPartida();
                                System.exit(0);
                                break;
                            case "texto":
                                mostrarTableroEnFormatoTexto();
                                break;
                            default:
                                if (validarFormato(jugada)) {
                                    realizarJugada((jugada));
                                    mostrarTableroEnPantalla();
                                    mostrarInformacionUltimoMovimiento();
                                } else {
                                    mostrarErrorEnFormatoDeEntrada();
                                }
                        }
                    } while (arbitro.consultarGanadorRonda() == null);
                    finalizarPartida();
                    break;
                case "estandar":
                    System.out.println("Disfrute de la partida en modo estandar");
                    mostrarTableroEnPantalla();
                    String jugada = recogerJugada().toLowerCase();
                    if (validarFormato(jugada)) {
                        realizarJugada((jugada));
                        mostrarTableroEnPantalla();
                        mostrarInformacionUltimoMovimiento();
                    }
                    break;
                default:
                    mostrarErrorEnFormatoDeEntrada();
            }
        } while ((arbitro.consultarGanadorPartida() == null));
        finalizarPartida();
    }

    /**
     * Recoge modo juego del teclado.
     */
    private static String recogerInicioJuego() {
        System.out.printf("Introduzca el modo del juego deseado: Simple o Estandar \n");
        return scanner.next();
    }

    /**
     * Recoge modo juego del teclado.
     */
    private static String recogerModoJuego() {
        System.out.printf("Introduzca el modo del juego deseado: Simple o Estandar \n");
        return scanner.next();
    }


    /**
     * Recoge jugada del teclado.
     *
     * @return jugada jugada en formato texto
     */
    private static String recogerJugadaSimple() {
        System.out.printf("Introduce jugada el jugador con turno %s (jugada columna fila columna fila): ", arbitro.obtenerTurno());
        return scanner.next();
    }

    /**
     * Muestra el mensaje de interrupción de partida.
     */
    private static void mostrarInterrupcionPartida() {
        System.out.println("Interrumpida la partida, se concluye el juego.");
    }

    /**
     * Muestra mensaje de error grave por error en el código del que no podemos
     * recuperarnos.
     *
     * @param ex excepción generada
     */
    private static void mostrarErrorInterno(RuntimeException ex) {
        System.err.println("Error interno en código a corregir por el equipo informático.");
        System.err.println("Mensaje asociado de error: " + ex.getMessage());
        System.err.println("Traza detallada del error:");
        ex.printStackTrace();
        // mejor solución mandar dicha informacion de la traza a un fichero de log
        // en lugar de a la consola, se verá en otras asignaturas
    }

    /**
     * Cierra el único recurso abierto.
     */
    private static void cerrarRecursos() {
        scanner.close();
    }

    /**
     * Muestra el estado actual del tablero en formato texto. Utilidad si hay
     * problemas con la visualización gráfica o con usuarios con daltonismo.
     */
    private static void mostrarTableroEnFormatoTexto() {
        System.out.println();
        System.out.println(tablero.toString());
    }

    /**
     * Extrae de los argumentos de ejecución el tipo de árbitro con el que jugamos.
     * No comprueba la corrección del texto introducido.
     *
     * @param args argumentos
     * @return texto con el tipo de árbitro a jugar, por defecto en seguro
     */
    private static String extraerModoArbitro(String[] args) {
        if (args.length >= 1) {
            return args[0].toLowerCase();
        }
        return "simple";

    }

    /**
     * Realiza la jugada introducida por teclado realizando las correspondientes
     * comprobaciones relativas a las reglas del juego. Se supone que la jugada en
     * cuanto al formato ya ha sido validada previamente.
     *
     * @param jugada jugada
     */
    private static void realizarJugada(String jugada) {
        assert validarFormato(jugada)
                : "El formato de la jugada y la corrección de las coordenadas de las celdas, deberían haber sido " +
                "validados previamente.";
        try {
            Celda origen = leerOrigen(jugada);
            Celda destino = leerDestino(jugada);

            if (arbitro.esMovimientoLegalConTurnoActual(origen, destino)) { // si el movimiento es legal
                arbitro.moverConTurnoActual(origen, destino);
                if (!arbitro.estaAcabadaRonda()) { // FIX 4.0
                    if (arbitro.estaBloqueadoTurnoActual()) {
                        System.out.println("Bloqueada la torre " + destino.obtenerColor() + " del jugador con turno "
                                + arbitro.obtenerTurno() + ".");
                        System.out.println("Se realiza un movimiento de distancia cero y pierde el turno.\n");
                        arbitro.moverConTurnoActualBloqueado();
                    }
                }
            } else {
                System.out.println("Movimiento ilegal.");
            }
        } catch (CoordenadasIncorrectasException ex) {
            throw new RuntimeException("Error interno accediendo a celdas ya validadas previamente. Corregir código de " +
                    "validación.", ex);
        }
    }

    /**
     * Realiza el empujon sumo introducido por teclado realizando las correspondientes comprobaciones relativas a las
     * reglas del juego. Se supone que la jugada en cuanto al formato ya ha sido validada previamente.
     *
     * @param jugada jugada
     */
    private static void realizarEmpujonSumo(String jugada) {
        assert validarFormatoEmpujonSumo(jugada)
                : "El formato de la jugada y la corrección de las coordenadas de la celda, deberían haber sido validados previamente.";
        try {
            Celda origen = leerOrigenSumo(jugada);

            if (arbitro.esEmpujonSumoLegal(origen)) { // si es empujón sumo legal
                arbitro.empujarSumo(origen);
                // tras empujon sumo sigue teniendo turno el turno actual
                if (arbitro.estaBloqueadoTurnoActual()) {
                    System.out.println("Bloqueada la torre " + origen.obtenerColor() + " del jugador con turno "
                            + arbitro.obtenerTurno() + ".");
                    System.out.println("Se realiza un movimiento de distancia cero y pierde el turno.\n");
                    arbitro.moverConTurnoActualBloqueado();
                }
            } else {
                System.out.println("Movimiento ilegal de empujón sumo.");
            }
        } catch (CoordenadasIncorrectasException ex) {
            throw new RuntimeException("Error interno accediendo a celdas ya validadas.", ex);
        }
    }

    /**
     * Muestra el mensaje de bienvenida con instrucciones para finalizar la partida.
     */
    private static void mostrarMensajeBienvenida() {
        System.out.println("Bienvenido al juego del Kamisado");
        System.out.println("Para interrumpir partida introduzca \"salir\".");
        System.out.println("Para mostrar el estado del tablero en formato texto introduzca \"texto\".");
        System.out.println("Para escoger el modo del juego (Simple o Estandar) introduzca \"elegir\".");
    }

    /**
     * Mostrar al usuario información de error en el formato de entrada, mostrando
     * ejemplos.
     */
    private static void mostrarErrorEnFormatoDeEntrada() {
        System.out.println();
        System.out.println("Error en el formato de entrada.");
        System.out.println("El formato debe ser letranumeroletranumero para mover torre, por ejemplo a7a5 o g1f3");
        System.out.println("El formato debe ser letranumero para realizar empujón con torre sumo, por ejemplo a7 o g1");
        System.out.println("Las letras deben estar en el rango [a,h] y los números en el rango [1,8]\n");
    }

    /**
     * Comprueba si se finaliza la partida, reiniciando la ronda si no se ha
     * finalizado.
     *
     * @return true si se ha finalizado la partida, false en caso contrario
     */
    private static boolean comprobarSiFinalizaPartida() {
        boolean resultado = false;
        if (arbitro.estaAcabadaRonda()) {
            if (arbitro.hayBloqueoMutuo()) {
                System.out.println("Situacion de bloqueo mutuo.");
                System.out.printf("Ganada la ronda por el jugador con turno %s,%n", arbitro.consultarGanadorRonda());
                System.out.println("porque no ha provocado el bloqueo.");
            }
            if (arbitro.estaAcabadaPartida()) {
                resultado = true;
            } else {
                System.out.println("Reiniciamos ronda...");
                arbitro.reiniciarRonda();
                mostrarTableroEnPantalla();
            }
        }
        return resultado;
    }

    /**
     * Finaliza la partida informando al usuario y cerrando recursos abiertos.
     */
    private static void finalizarPartida() {
        System.out.printf("Partida finalizada ganando las torres de turno %s.", arbitro.consultarGanadorPartida());
    }

    /**
     * Muestra información del último movimiento.
     */
    private static void mostrarInformacionUltimoMovimiento() {
        Color color = null;
        System.out.println();

        color = arbitro.obtenerUltimoMovimiento(Turno.NEGRO);
        imprimirColorUltimoTurno("negro", BRIGHT_WHITE_TEXT(), color);

        color = arbitro.obtenerUltimoMovimiento(Turno.BLANCO);
        imprimirColorUltimoTurno("blanco", BLACK_TEXT(), color);

        System.out.println();
    }

    /**
     * Muestra puntuaciones de ambos jugadores.
     */
    private static void mostrarPuntuaciones() {
        System.out.printf("Puntos de turno negro: %d\t", arbitro.obtenerPuntuacionTurnoNegro());
        System.out.printf("Puntos de turno blanco: %d%n", arbitro.obtenerPuntuacionTurnoBlanco());
    }

    /**
     * Imprime último color del turno.
     *
     * @param textoTurno texto del turno
     * @param colorTexto color del texto
     * @param color      color
     */
    private static void imprimirColorUltimoTurno(String textoTurno, Attribute colorTexto, Color color) {
        if (color != null) {
            System.out.print("Último color de turno " + textoTurno + ":");
            Attribute colorFondo = elegirColorFondo(color);
            System.out.print(colorize(color.toString(), colorTexto, colorFondo));
            System.out.println(" ");
        }
    }

    /**
     * Muestra el tablero en pantalla con su estado actual.
     */
    private static void mostrarTableroEnPantalla() {
        try {
            System.out.print("  ");
            for (int col = 0; col < tablero.obtenerNumeroColumnas(); col++) {
                char c = (char) (col + 'a');
                System.out.print("   " + c + "  ");
            }
            System.out.println();
            for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
                for (int cont = 0; cont < 3; cont++) {
                    if (cont == 1) {
                        System.out.print((tablero.obtenerNumeroFilas() - i) + " ");
                    } else {
                        System.out.print("  ");
                    }
                    for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
                        Celda celda = tablero.obtenerCelda(i, j);
                        if (cont == 1 && !celda.estaVacia()) {
                            mostrarLineaColorCeldaConTorre(celda);
                        } else {
                            mostrarLineaColor(celda.obtenerColor());
                        }
                    }
                    System.out.println();
                }
            }
        } catch (CoordenadasIncorrectasException ex) {
            throw new RuntimeException("Error interno accediendo a celdas.", ex);
        }
    }

    /**
     * Muestra la línea con color de fondo.
     *
     * @param color color
     */
    private static void mostrarLineaColor(Color color) {
        Attribute colorFondo = elegirColorFondo(color);
        System.out.print(colorize("      ", colorFondo));
    }

    /**
     * Muestra la línea con color de la torre contenida en celda.
     *
     * @param celda celda
     */
    private static void mostrarLineaColorCeldaConTorre(Celda celda) {
        Color colorCelda = celda.obtenerColor();
        Turno turno = celda.obtenerTurnoDeTorre();
        Color colorTorre = celda.obtenerColorDeTorre();

        Attribute colorFondo = elegirColorFondo(colorCelda);
        System.out.print(colorize("  ", colorFondo));

        Attribute colorTurno = turno == Turno.BLANCO ? BRIGHT_WHITE_BACK() : BLACK_BACK();
        Attribute colorTexto = elegirColorTexto(colorTorre);
        String sumo = celda.obtenerTorre().obtenerNumeroDientes() == 0 ? "." : "1";
        System.out.print(colorize(celda.obtenerColorDeTorre().toChar() + sumo, colorTexto, colorTurno));

        System.out.print(colorize("  ", colorFondo));
    }

    /**
     * Elige el color de texto.
     *
     * @param color color
     * @return color de texto
     */
    private static Attribute elegirColorTexto(Color color) {
        Attribute colorTexto = null;
        switch (color) {
            case AMARILLO:
                colorTexto = TEXT_COLOR(223, 227, 12); // BRIGHT_YELLOW_TEXT();
                break;
            case AZUL:
                colorTexto = BRIGHT_BLUE_TEXT();
                break;
            case MARRON:
                colorTexto = TEXT_COLOR(110, 44, 0);
                break;
            case NARANJA:
                colorTexto = TEXT_COLOR(248, 162, 65);
                break;
            case ROJO:
                colorTexto = BRIGHT_RED_TEXT();
                break;
            case ROSA:
                colorTexto = BRIGHT_MAGENTA_TEXT();
                break;
            case PURPURA:
                colorTexto = TEXT_COLOR(155, 89, 182);
                break;
            case VERDE:
                colorTexto = BRIGHT_GREEN_TEXT();
                break;
        }
        return colorTexto;
    }

    /**
     * Elige el color de fondo.
     *
     * @param color color
     * @return color de fondo
     */
    private static Attribute elegirColorFondo(Color color) {
        Attribute colorFondo = null;
        switch (color) {
            case AMARILLO:
                colorFondo = BRIGHT_YELLOW_BACK();
                break;
            case AZUL:
                colorFondo = BRIGHT_BLUE_BACK();
                break;
            case MARRON:
                colorFondo = BACK_COLOR(110, 44, 0);
                break;
            case NARANJA:
                colorFondo = BACK_COLOR(248, 162, 65);
                break;
            case ROJO:
                colorFondo = BRIGHT_RED_BACK();
                break;
            case ROSA:
                colorFondo = BRIGHT_MAGENTA_BACK();
                break;
            case PURPURA:
                colorFondo = BACK_COLOR(155, 89, 182);
                break;
            case VERDE:
                colorFondo = BRIGHT_GREEN_BACK();
                break;
        }
        return colorFondo;
    }

//    private static void elegirModoParaTexto() {
//        arbitro = new ArbitroSimple(tablero);
//    }

    /**
     * Inicializa el estado de los elementos de la partida.
     *
     * @param configuracion configuración
     */
    private static void inicializarPartida(String configuracion) {
        tablero = new Tablero();
        switch (configuracion) {
            case "simple":
                arbitro = new ArbitroSimple(tablero);
                break;
            case "estandar":
                arbitro = new ArbitroEstandar(tablero);
                break;
            default:
                arbitro = new ArbitroSimple(tablero);
        }
        // Abrimos la lectura desde teclado
        arbitro.colocarTorres();
        scanner = new Scanner(System.in);
    }

    /**
     * Recoge jugada del teclado.
     *
     * @return jugada jugada en formato texto
     */
    private static String recogerJugada() {
        System.out.print("Introduce jugada el jugador con turno " + arbitro.obtenerTurno()
                + " (jugada con columna fila columna fila para mover o columna fila para empujón sumo): ");
        return scanner.next();
    }

    /**
     * Valida la corrección del formato de la jugada. Solo comprueba la corrección
     * del formato de entrada en cuanto al tablero, no la validez de la jugada en
     * cuanto a las reglas del Kamisado. La jugada tiene que tener cuatro caracteres
     * y contener letras y números de acuerdo a las reglas de la notación
     * algebraica.
     * <p>
     * Otra mejor solución alternativa es el uso de expresiones regulares (se verán
     * en la asignatura de 3º Procesadores del Lenguaje).
     *
     * @param jugada a validar
     * @return true si el formato de la jugada es correcta según las coordenadas
     * disponibles del tablero
     */
    private static boolean validarFormato(String jugada) {
        boolean estado = true;
        if (jugada.length() != TAMAÑO_JUGADA || esLetraInvalida(jugada.charAt(0)) || esLetraInvalida(jugada.charAt(2))
                || esNumeroInvalido(jugada.charAt(1)) || esNumeroInvalido(jugada.charAt(3))) {
            estado = false;
        }
        return estado;
    }

    /**
     * Valida la corrección del formato de la jugada para empujon sumo. Solo
     * comprueba la corrección del formato de entrada en cuanto al tablero, no la
     * validez de la jugada en cuanto a las reglas del Kamisado. La jugada tiene que
     * tener dos caracteres y contener letra y número de acuerdo a las reglas de la
     * notación algebraica.
     * <p>
     * Otra mejor solución alternativa es el uso de expresiones regulares (se verán
     * en la asignatura de 3º Procesadores del Lenguaje).
     *
     * @param jugada a validar
     * @return true si el formato de la jugada es correcta según las coordenadas
     * disponibles del tablero
     */
    private static boolean validarFormatoEmpujonSumo(String jugada) {
        boolean estado = true;
//		if (jugada.length() != TAMAÑO_EMPUJON_SUMO || esLetraInvalida(jugada.charAt(0)) // RMS
//				|| esLetraInvalida(jugada.charAt(2))) {
        if (jugada.length() != TAMAÑO_EMPUJON_SUMO || esLetraInvalida(jugada.charAt(0))
                || esNumeroInvalido(jugada.charAt(1))) {
            estado = false;
        }
        return estado;
    }

    /**
     * Comprueba si la letra está fuera del rango [a,h].
     *
     * @param letra letra a comprobar
     * @return true si la letra no está en el rango, false en caso contrario
     */
    private static boolean esLetraInvalida(char letra) {
        return letra < 'a' || letra > 'h';
    }

    /**
     * Comprueba si el número (en formato letra) está fuera del rango [1,8].
     *
     * @param numero numero
     * @return true si el número no está en el rango, false en caso contrario
     */
    private static boolean esNumeroInvalido(char numero) {
        return numero < '1' || numero > '8';
    }

    /**
     * Obtiene la celda origen.
     *
     * @param jugada jugada en formato notación algebraica (e.g. a1)
     * @return celda origen o null si no es posible extraerla
     * @throws CoordenadasIncorrectasException si las coordenadas son incorrectas
     */
    private static Celda leerOrigen(String jugada) throws CoordenadasIncorrectasException {
        if (jugada.length() != TAMAÑO_JUGADA)
            return null;
        return extraerCelda(jugada);

    }

    /**
     * Obtiene la celda origen en empujón sumo.
     *
     * @param jugada jugada en formato notación algebraica (e.g. a1)
     * @return celda origen o null si no es posible extraerla
     * @throws CoordenadasIncorrectasException si las coordenadas son incorrectas
     */
    private static Celda leerOrigenSumo(String jugada) throws CoordenadasIncorrectasException {
        if (jugada.length() != TAMAÑO_EMPUJON_SUMO)
            return null;
        return extraerCelda(jugada);
    }

    /**
     * Extrae la celda del texto.
     *
     * @param texto texto de longitud dos
     * @return celda
     * @throws CoordenadasIncorrectasException si las coordenadas son incorrectas
     */
    private static Celda extraerCelda(String texto) throws CoordenadasIncorrectasException {
        String textoOrigen = texto.substring(0, 2);
        return tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
    }

    /**
     * Obtiene la celda destino.
     *
     * @param jugada jugada en formato notación algebraica (e.g. a1)
     * @return celda destino o null si no es posible extraerla
     * @throws CoordenadasIncorrectasException si las coordenadas son incorrectas
     */
    private static Celda leerDestino(String jugada) throws CoordenadasIncorrectasException {
        if (jugada.length() != TAMAÑO_JUGADA)
            return null;
        String textoOrigen = jugada.substring(2, 4);
        return tablero.obtenerCeldaParaNotacionAlgebraica(textoOrigen);
    }
}
