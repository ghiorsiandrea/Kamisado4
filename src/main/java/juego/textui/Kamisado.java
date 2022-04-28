package juego.textui;

import com.diogonunes.jcolor.Attribute;
import juego.control.Arbitro;
import juego.control.ArbitroAbstracto;
import juego.control.ArbitroEstandar;
import juego.control.ArbitroSimple;
import juego.modelo.Celda;
import juego.modelo.Color;
import juego.modelo.Tablero;
import juego.modelo.Turno;
import juego.util.CoordenadasIncorrectasException;

import java.util.Objects;
import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

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

        //args = new String[]{"simple"};
        //System.out.println(extraerModoArbitro(args));

        mostrarMensajeBienvenida();
        inicializarPartida("simple");
        String inicioJuego;
        do {
            inicioJuego = scanner.nextLine().toLowerCase();
            switch (inicioJuego) {
                case "reglas" -> {
                    reglas();
                    mostrarMensajeBienvenida2();
                }
                case "texto" -> {
                    mostrarTableroEnFormatoTexto();
                    mostrarMensajeBienvenida2();
                }
                case "color" -> {
                    mostrarTableroEnPantalla();
                    mostrarMensajeBienvenida2();
                }
                case "elegir" -> {
                    elegirModoJuego();
                    decidirSiJugarNuevamente();
                }
                case "salir" -> System.out.println("El usuario ha decidido salir del juego. Vuelva pronto");
                default -> defaultAction();
            }

        }
        while (!inicioJuego.equals("salir"));

    }

    private static void elegirModoJuego() {
        do {
            String modoJuego = recogerInicioJuego().toLowerCase();

            switch (modoJuego) {
                case "simple" -> {
                    inicializarPartida("simple");
                    jugarPartidaSimple();
                }

                case "estandar" -> {
                    inicializarPartida("estandar");
                    jugarPartidaEstandar();
                }
                default -> defaultAction();
            }
        } while ((arbitro.consultarGanadorPartida() == null));
        finalizarPartida();
    }

    private static void defaultAction() {
        if (arbitro.consultarGanadorRonda() == null) {
            mostrarErrorEnFormatoDeEntrada();
        }
    }

    private static void jugarPartidaSimple() {
        System.out.println("Disfrute de la partida en modo simple");
        mostrarTableroEnPantalla();
        do {
            String jugada = recogerJugadaSimple().toLowerCase();

            if (validarFormato(jugada)) {
                realizarJugada((jugada));
                mostrarTableroEnPantalla();
                mostrarInformacionUltimoMovimiento();
            } else defaultAction();
        } while (arbitro.consultarGanadorRonda() == null);
    }

    private static void jugarPartidaEstandar() {

        System.out.println("Disfrute de la partida en modo estandar");
        mostrarTableroEnPantalla();
        inicializarPartida("estandar");
        do {
            String jugada = recogerJugada().toLowerCase();
            if (validarFormato(jugada)) {
                realizarJugada((jugada));
                mostrarTableroEnPantalla();
                mostrarInformacionUltimoMovimiento();
            } else if (!comprobarSiFinalizaPartida()) {
                defaultAction();
            } else if (comprobarSiFinalizaPartida()) {
                comprobarSiFinalizaPartida();
            }
            mostrarPuntuaciones();
        } while (!comprobarSiFinalizaPartida());

        if (comprobarSiFinalizaPartida()) {
            arbitro.reiniciarRonda();
        }
    }

    private static void decidirSiJugarNuevamente() {
        System.out.println("¿Desea continuar jugando?");
        System.out.println("Para salir del menú de opciones introduzca \"salir\".");
        System.out.println("Para seguir jugando y escoger el modo juego introduzca \"elegir\".\n");

        String decidirSiSeguirONo = scanner.nextLine().toLowerCase();
        switch (decidirSiSeguirONo) {
            case "salir" -> System.out.println("El usuario ha decidido salir del juego. Vuelva pronto");
            case "elegir" -> elegirModoJuego();
        }
    }

    private static void reglas() {

        System.out.println("\nPara las reglas generales introduzca \"generales\".");
        System.out.println("Para las reglas de la partida simple introduzca \"simple\".");
        System.out.println("Para las reglas de la partida estandar introduzca \"estandar\".\n");

        String reglas = scanner.nextLine().toLowerCase();
        switch (reglas) {
            case "generales" -> System.out.println("Reglas generales\n" +
            "Kamisado es un juego abstracto de tablero de 8x8 celdas, para dos jugadores. Las celdas tienen un color\n" +
            "asignado fijo, de entre solo ocho colores posibles (i.e. amarillo, azul, marrón, naranja, púrpura, rojo, \n" +
            "rosa, y verde). Sobre dicho tablero se colocan 8 torres1 blancas en la fila superior del tablero, y 8\n" +
            "torres negras en la fila inferior del tablero. A partir de ahora diremos que las torres tienen un turno ,\n" +
            "blanco o negro, según corresponda.\n" +
            "A cada jugador, se le asignan sus 8 torres correspondientes. Adicionalmente, cada torre además del\n" +
            "turno (blanco o negro), tiene asignado un color de entre esos ocho. La colocación de las torres de\n" +
            "cada turno, al inicio de la partida, coincide con el color de la celda. Por ejemplo, la torre negra de\n" +
            "color amarillo, se colocará en la fila inferior, en la celda de color amarillo.\n" +
            "Por simplificacion, siempre comienza la partida el jugador con turno negro. En este primer turno, puede\n" +
            "mover discrecionalmente una de sus torres a otra celda vacía. El color de la celda donde se coloca la \n" +
            "torre, determina que en el siguiente turno, el jugador contrario está obligado a mover su torre de dicho\n" +
            "color.\n" +
            "Por lo tanto, solo es discrecional el primer movimiento de salida en cada ronda. El resto de movimientos\n" +
            "están siempre condicionados por el color de la celda a donde movió en último lugar el jugador contrario.\n" +
                    "___________________________FIN REGLAS GENERALES___________________________\n");

            case "simple" -> System.out.println("Partida simple\n" +
            "Los movimientos de las torres están limitados por las siguientes reglas:\n" +
            "• Solo se puede mover una torre del turno actual hacia la fila de inicio del contrario, solo en sentido\n" +
            "vertical o diagonal (i.e. solo se puede avanzar hacia la fila de partida del contrario, pero nunca \n" +
            "retroceder).\n" +
            "• No se puede saltar sobre otras torres, independientemente del turno que tengan (ni siquiera sobre\n" +
            "torres propias).\n" +
            "• No se puede ocupar una celda que contenga otra torre. En este juego no se “comen” o eliminan torres \n" +
            "del contrario (ni propias).\n" +
            "• El jugador con turno está obligado a mover siempre que haya algún movimiento legal. No puede pasar turno.\n" +
            "La partida simple finaliza en la primera ronda, cuando uno de los jugadores consigue colocar una\n" +
            "de sus torres en la fila de salida del turno contrario sumando 1 punto. A la partidas simple se las\n" +
            "considera de ronda única, finalizando al conseguir un punto.\n" +
            "Para indicar las celdas del tablero, los jugadores utilizarán la misma notación utilizada en el ajedrez, \n" +
            "denominada “notación algebraica”. Por ejemplo y según se muestra en la Ilustración 1, la celda [0][0]\n" +
            "sería \"a8\", la celda [3][4] sería \"e5\" y la celda [6][5] sería \"f2\". Cuando se quiere mencionar \n" +
            "una jugada completa, moviendo una torre de una celda origen a una celda destino, se indicarán las dos " +
            "celdas seguidas. \n" +
            "Por ejemplo: \"a1c3\" sería una jugada donde se mueve la torre desde la celda [7][0] a la celda en [5][2].\n" +
            "Si un jugador está obligado a mover una torre, y dicha torre está bloqueada (según las reglas), se \n" +
            "considera que hace un movimiento de “distancia cero”, colocando su torre en la misma celda en la que \n" +
            "estaba, y por lo tanto el jugador contrario ahora tendrá que mover su torre del color de dicha celda \n" +
            "en la que ha quedado bloqueado el contrario.\n" +
            "Si se diese la situación de que el bloqueo se da en ambos jugadores, denominado bloqueo mutuo o\n" +
            "deadlock, se considera finalizada, dando como perdedor al jugador que provocó dicha situación con un\n" +
            "movimiento de torre. Es decir, pierde el jugador que hizo el último movimiento que no fuera de \n" +
            "“distancia cero”.\n" +
                    "___________________________FIN REGLAS PARTIDA SIMPLE___________________________\n");
            case "estandar" -> System.out.println("""
             Partida estándar
             En esta segunda versión de la práctica, se amplía con la posiblidad de jugar partidas estándar, con\s
             varias rondas.
             La partida se inicia igual que una partida simple. En una partida estándar las torres que alcanzan la\s
             fila del contrario suman 1 punto al turno correspondiente y además dicha torre se transforma en una \s
             "torre sumo uno" (en algunas versiones del juego se añade un “diente de dragón”), finalizando la ronda
             y reiniciando las torres a la posición inicial para iniciar otra ronda. Se colocan de nuevo las torres
             en la posición de partida, incluyendo ahora la nueva “torre sumo uno” en su color correspondiente. Inicia
              la nueva ronda el turno que perdió la ronda previa.
             Si se alcanza la fila del contrario con una torre sumo uno se consiguen 3 puntos.
             Se considera que se finaliza la partida cuando un turno consigue 3 o más puntos, al sumar los puntos\s
             acumulados en distintas rondas con torres simples o torres sumo uno.
             Las torres sumo uno tienen unas reglas de movimiento adicionales:
             • Solo pueden desplazarse un máximo de una distancia de 5 celdas en cualquiera de los sentidos
             básicos (vertical o diagonal).
             • Pueden “empujar” una posición hacia delante a torres del contrario que la bloqueen (denominado
             “empujón sumo”), pero solo en sentido vertical:
             ◦ Solo pueden empujar una torre del turno contrario.
             ◦ Detrás de esa torre empujada, debe haber una celda vacía. No se puede “empujar” o echar
             torres del turno contrario fuera del tablero.
             ◦ No se puede empujar a otra “torre sumo uno” del contrario, solo a una torre simple.
             ◦ Cuando se produce un “empujón sumo”, el turno contrario pierde turno y vuelve a mover el
             turno que realizó el empujón.
             ◦ El color de la torre a mover, tras el empujón, se obtiene del color de la celda donde ha quedado\s
             situada la torre del contrario.
             ___________________________FIN REGLAS PARTIDA ESTANDAR___________________________\s
             """);
            }
        }


    /**
     * Recoge modo juego del teclado.
     */
    private static String recogerInicioJuego() {
        System.out.print("Introduzca el modo del juego deseado: Simple o Estandar \n");
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
        System.out.println("A continuación se mostrará el estado del tablero en formato texto");
        System.out.println();
        System.out.println(tablero.toString());
        System.out.println("Se desplegará nuevamente el menú de opciones para que pueda escoger si desea o no continuar en el juego");
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
                        System.out.println("Bloqueada la torre " + Objects.requireNonNull(destino).obtenerColor() + " del jugador con turno "
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
                    assert origen != null;
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
        System.out.println("\n__________Bienvenido al juego del Kamisado__________\n");
        System.out.println("Para mostrar las reglas del juego introduzca \"reglas\".");
        System.out.println("Para mostrar el estado del tablero en formato texto introduzca \"texto\".");
        System.out.println("Para mostrar el estado del tablero en formato color introduzca \"color\".");
        System.out.println("Para escoger el modo del juego (Simple o Estandar) introduzca \"elegir\".");
        System.out.println("Para interrumpir partida introduzca \"salir\".\n");
    }

    private static void mostrarMensajeBienvenida2() {
        System.out.println("\n___Bienvenido nuevamente al menú de opciones del juego del Kamisado___\n");
        System.out.println("Para mostrar las reglas del juego introduzca \"reglas\".");
        System.out.println("Para mostrar el estado del tablero en formato texto introduzca \"texto\".");
        System.out.println("Para mostrar el estado del tablero en formato color introduzca \"color\".");
        System.out.println("Para escoger el modo del juego (Simple o Estandar) introduzca \"elegir\".");
        System.out.println("Para interrumpir partida introduzca \"salir\".\n");
    }

    /**
     * Mostrar al usuario información de error en el formato de entrada, mostrando
     * ejemplos.
     */
    private static void mostrarErrorEnFormatoDeEntrada() {
        System.out.println();
        System.out.println("Error en el formato de entrada.");
        System.out.println("El formato debe ser letra numero letra numero para mover torre, por ejemplo a7a5 o g1f3");
        System.out.println("El formato debe ser letra numero para realizar empujón con torre sumo, por ejemplo a7 o g1");
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
        if (arbitro.consultarGanadorPartida() != null) {
            System.out.printf("""
                    Partida finalizada ganando las torres de turno %s.
                    """, arbitro.consultarGanadorPartida());
        } else {
            System.out.print("""
                    Partida finalizada.
                    """);
        }
    }

    /**
     * Muestra información del último movimiento.
     */
    private static void mostrarInformacionUltimoMovimiento() {
        Color color;
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
        return switch (color) {
            case AMARILLO -> TEXT_COLOR(223, 227, 12); // BRIGHT_YELLOW_TEXT();
            case AZUL -> BRIGHT_BLUE_TEXT();
            case MARRON -> TEXT_COLOR(110, 44, 0);
            case NARANJA -> TEXT_COLOR(248, 162, 65);
            case ROJO -> BRIGHT_RED_TEXT();
            case ROSA -> BRIGHT_MAGENTA_TEXT();
            case PURPURA -> TEXT_COLOR(155, 89, 182);
            case VERDE -> BRIGHT_GREEN_TEXT();
        };
    }

    /**
     * Elige el color de fondo.
     *
     * @param color color
     * @return color de fondo
     */
    private static Attribute elegirColorFondo(Color color) {
        return switch (color) {
            case AMARILLO -> BRIGHT_YELLOW_BACK();
            case AZUL -> BRIGHT_BLUE_BACK();
            case MARRON -> BACK_COLOR(110, 44, 0);
            case NARANJA -> BACK_COLOR(248, 162, 65);
            case ROJO -> BRIGHT_RED_BACK();
            case ROSA -> BRIGHT_MAGENTA_BACK();
            case PURPURA -> BACK_COLOR(155, 89, 182);
            case VERDE -> BRIGHT_GREEN_BACK();
        };
    }

    /**
     * Inicializa el estado de los elementos de la partida.
     *
     * @param configuracion configuración
     */
    private static void inicializarPartida(String configuracion) {
        tablero = new Tablero();
        if ("estandar".equals(configuracion)) {
            arbitro = new ArbitroEstandar(tablero);
        } else {
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
//        boolean estado = true;
//        if (jugada.length() != TAMAÑO_JUGADA || esLetraInvalida(jugada.charAt(0)) || esLetraInvalida(jugada.charAt(2))
//                || esNumeroInvalido(jugada.charAt(1)) || esNumeroInvalido(jugada.charAt(3))) {
//            estado = false;
//        }
//        return estado;
        return jugada.length() == TAMAÑO_JUGADA && !esLetraInvalida(jugada.charAt(0)) && !esLetraInvalida(jugada.charAt(2))
                && !esNumeroInvalido(jugada.charAt(1)) && !esNumeroInvalido(jugada.charAt(3));
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
//       opcion 1
//       boolean estado = true;
//		if (jugada.length() != TAMAÑO_EMPUJON_SUMO || esLetraInvalida(jugada.charAt(0)) // RMS
//				|| esLetraInvalida(jugada.charAt(2))) {

//        opcion 2
//        boolean estado = true;
//        if (jugada.length() != TAMAÑO_EMPUJON_SUMO || esLetraInvalida(jugada.charAt(0))
//                || esNumeroInvalido(jugada.charAt(1))) {
//            estado = false;
//        }

//        opcion 3
//        boolean estado = jugada.length() == TAMAÑO_EMPUJON_SUMO && !esLetraInvalida(jugada.charAt(0))
//                && !esNumeroInvalido(jugada.charAt(1));
//        return estado;

        // opcion 4
        return jugada.length() == TAMAÑO_EMPUJON_SUMO && !esLetraInvalida(jugada.charAt(0))
                && !esNumeroInvalido(jugada.charAt(1));
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
