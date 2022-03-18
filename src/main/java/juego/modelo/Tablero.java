package juego.modelo;

import juego.util.Sentido;

import static juego.modelo.Color.*;

public class Tablero {

    private static final int TAMANHO_POR_DEFECTO = 8;

    private static final Color[][] COLORES_POR_DEFECTO = {
            {NARANJA, AZUL, PURPURA, ROSA, AMARILLO, ROJO, VERDE, MARRON},
            {ROJO, NARANJA, ROSA, VERDE, AZUL, AMARILLO, MARRON, PURPURA},
            {VERDE, ROSA, NARANJA, ROJO, PURPURA, MARRON, AMARILLO, AZUL},
            {ROSA, PURPURA, AZUL, NARANJA, MARRON, VERDE, ROJO, AMARILLO}
    };

    private Celda[][] matriz;

    public Tablero() {
        // Lógica de constructor por defecto

        matriz = new Celda[TAMANHO_POR_DEFECTO][TAMANHO_POR_DEFECTO];

        for (int i = 0; i < TAMANHO_POR_DEFECTO; i++) {
            for (int j = 0; j < TAMANHO_POR_DEFECTO; j++) {
                if (i < TAMANHO_POR_DEFECTO / 2) {
                    matriz[i][j] = new Celda(i, j, COLORES_POR_DEFECTO[i][j]);
                } else {
                    matriz[i][j] = new Celda
                            (i, j, COLORES_POR_DEFECTO[(TAMANHO_POR_DEFECTO - 1) - i][(TAMANHO_POR_DEFECTO - 1) - j]);
                }
            }
        }
    }

    /*
     * Un tablero se considera como un conjunto de celdas, cada una en una posición (fila,columna).
     * Suponiendo que el tablero es de 8 filas x 8 columnas, entonces tenemos: [0][0] las coordenadas
     * de la esquina superior izquierda, [0][7] las coordenadas de la esquina superior derecha, [7][0]
     * las coordenadas de la esquina inferior izquierda y [7][7] las coordenadas de la esquina inferior
     * derecha. Se numera de izquierda a derecha y en sentido descendente.
     *
     *  El conjunto de celdas de un tablero debe implementarse con un array3
     *  de celdas de dos
     * dimensiones. Al instanciar un tablero se crean y asignan las correspondientes celdas vacías, con
     * sus correspondientes coordenadas y colores.
     */


    /**
     * El método buscarTorre obtiene la celda que contiene la torre del turno y color indicada.
     */
    public Celda buscarTorre(Turno turno, Color color) {
        for (int i = 0; i < TAMANHO_POR_DEFECTO; i++) {
            for (int j = 0; j < TAMANHO_POR_DEFECTO; j++) {
                if (!matriz[i][j].estaVacia() &&
                        matriz[i][j].obtenerTurnoDeTorre() == turno &&
                        matriz[i][j].obtenerColorDeTorre() == color) {
                    return matriz[i][j];
                }
            }
        }
        return null;
    }

    /**
     * El método colocar(Torre, Celda) coloca la torre en la celda indicada (método sobrecargado).
     */
    public void colocar(Torre torre, Celda celda) {
        torre.establecerCelda(celda);
        celda.establecerTorre(torre);
    }

    /**
     *  El método colocar(Torre, String) coloca la torre en la celda indicada en notación algebraica.
     * Si la celda en notación algebraica es incorrecta en sintaxis, no se hace nada (método
     * sobrecargado).
     * ◦ Nota: todos los métodos sobrecargados colocar deben realizar el doble enganche entre torre y
     * celda.
     */
    public void colocar(Torre torre, String notacionAlgebraica) {
        Celda celda = obtenerCeldaParaNotacionAlgebraica(notacionAlgebraica);
        // tengo dos opciones, usar el mismo codigo de colocar original o llamar al metodo.
        // torre.establecerCelda(celda);
        // celda.establecerTorre(torre);
        colocar(torre, celda);
    }

    /**
     * El método colocar(Torre, int, int) coloca la torre en la fila y columna indicada. Si los
     * valores de fila y columna son incorrectos (no están en los límites del tablero), no se hace nada
     * (método sobrecargado)
     */
    public void colocar(Torre torre, int fila, int columna) {
        Celda celda = obtenerCelda(fila, columna);
        colocar(torre, celda);
    }

    /**
     * El método estanVaciasCeldasEntre devuelve true si las celdas entre el origen y destino no contienen torres,
     * es decir están vacías, o false en caso contrario
     * Si las dos celdas están consecutivas sin celdas entre medias se devuelve true.
     * Si las celda origen y destino no están alineadas en alguno de los sentidos definidos en la enumeración Sentido,
     * se devuelve false.
     * No se tiene en cuenta el estado de las celdas origen y destino, solo el de las celdas entre medias  para
     * comprobar si hay torres
     */
    public boolean estanVaciasCeldasEntre(Celda origen, Celda destino) {

        Sentido sentido = obtenerSentido(origen, destino);
        if (sentido == null) {
            return false;
        }
//        int fila = origen.obtenerFila();
//        int columna = origen.obtenerColumna();
//        do {
//            fila = fila + sentido.obtenerDesplazamientoEnFilas();
//            columna = columna + sentido.obtenerDesplazamientoEnColumnas();
//            if ( !matriz[fila][columna].estaVacia() ) {
//                return false;
//            }
//        } while (destino.obtenerFila() != fila || destino.obtenerColumna() != columna);

        int fila = origen.obtenerFila() + sentido.obtenerDesplazamientoEnFilas();
        int columna = origen.obtenerColumna() + sentido.obtenerDesplazamientoEnColumnas();

        while (destino.obtenerFila() != fila || destino.obtenerColumna() != columna) {
            if (!matriz[fila][columna].estaVacia()) {
                return false;
            }
            fila = fila + sentido.obtenerDesplazamientoEnFilas();
            columna = columna + sentido.obtenerDesplazamientoEnColumnas();
        }

        return true;
    }

    /**
     * El método moverTorre mueve la torre de la celda origen a destino. Si no hay torre en origen, o la
     * celda destino no está vacía, no se hace nada.
     */
    public void moverTorre(Celda origen, Celda destino) {

        if (origen.estaVacia() || !destino.estaVacia()) {
            return;
        }
        Torre torre = origen.obtenerTorre();
        destino.establecerTorre(torre);
        origen.eliminarTorre();
    }

    /**
     * El método obtenerCelda, devuelve la referencia a la celda del tablero.
     */
    public Celda obtenerCelda(int fila, int columna) {
        if (estaFueraDeRango(fila, columna))
            return null;
        Celda celda = matriz[fila][columna];
        return celda;
    }

    private boolean estaFueraDeRango(int fila, int columna) {
        return fila > (TAMANHO_POR_DEFECTO - 1) || columna > (TAMANHO_POR_DEFECTO - 1)
                || fila < 0 || columna < 0;
    }

    /**
     * El método obtenerCeldaDestinoEnJugada, devuelve la referencia a la celda destino en la jugada
     * introducida en notación algebraica (e.g. con "a1c3" retorna la celda en [5][2]). Si el formato
     * de texto es incorrecto retorna null.
     */
    public Celda obtenerCeldaDestinoEnJugada(String textoJugada) {

//        char[] caracteres = textoJugada.toLowerCase().toCharArray();
//        if (caracteres.length != 4) {
//            return null;
//        }
//
//        char filaChar = caracteres[3];
//        char columnaChar = caracteres[2];
//
//        return obtenerCeldaParaNotacionAlgebraica("" + columnaChar + filaChar);


        if (textoJugada.length() != 4) {
            return null;
        }

        return obtenerCeldaParaNotacionAlgebraica(textoJugada.substring(2, 4));

    }

    /**
     * El método obtenerCeldaOrigenEnJugada, devuelve la referencia a la celda origen en la jugada
     * introducida en notación algebraica (e.g. con "a1c3" retorna la celda en [7][0]). Si el formato
     * de texto es incorrecto retorna null.
     */
    public Celda obtenerCeldaOrigenEnJugada(String textoJugada) {
        char[] caracteres = textoJugada.toLowerCase().toCharArray();
        if (caracteres.length != 4) {
            return null;
        }

        char filaChar = caracteres[1];
        char columnaChar = caracteres[0];

        return obtenerCeldaParaNotacionAlgebraica("" + columnaChar + filaChar);
    }

    /**
     * El método obtenerCeldaParaNotacionAlgebraica, devuelve la referencia a la celda en notación
     * algebraica (e.g. con "a1" retorna la celda [7][0]). Si el formato de texto es incorrecto retorna
     * null.
     */

    //ejemplo SWITCH
    // switch (filaChar) {
    //case '1':
    //fila = 7;
    //break;

    // EJEMPLO MAPAS
    //Map<Character, Integer> mapaFilas = new HashMap<>();
    //mapaFilas.put('1', 7);
    //mapaFilas.put('2', 6);
    //int fila = mapaFilas.get(filaChar);
    public Celda obtenerCeldaParaNotacionAlgebraica(String texto) {
        char[] caracteres = texto.toLowerCase().toCharArray();
        if (caracteres.length != 2) {
            return null;
        }

        char filaChar = caracteres[1];
        char columnaChar = caracteres[0];
        int fila = 56 - filaChar;
        int columna = columnaChar - 97;
        if (estaFueraDeRango(fila, columna)) {
            return null;
        }
        return matriz[fila][columna];
    }

    /**
     * El método obtenerCeldas devuelve un array de una dimensión con todas las celdas del tablero.
     * Se recorren las celdas de arriba a abajo, y de izquierda a derecha. Este método se puede
     * utilizar en bucles for-each para recorrer todas las celdas del tablero de forma simplificada.
     */
    public Celda[] obtenerCeldas() {
        Celda[] resultado = new Celda[TAMANHO_POR_DEFECTO * TAMANHO_POR_DEFECTO];
        int pos = 0;
        for (int j = 0; j < TAMANHO_POR_DEFECTO; j++) {

            for (int i = 0; i < TAMANHO_POR_DEFECTO; i++) {
                Celda celda = matriz[i][j];
                resultado[pos] = celda;
                pos++;
            }
        }
        return resultado;
    }

    /**
     * El método obtenerCoordenadasEnNotacionAlgebraica, devuelve el texto correspondiente a la
     * celda en notación algebraica (e.g. con la celda [7][0] se retorna "a1"). Si la celda no pertenece
     * al tablero se retorna "--".
     */
    public String obtenerCoordenadasEnNotacionAlgebraica(Celda celda) {
        if (estaFueraDeRango(celda.obtenerFila(), celda.obtenerColumna())) {
            return "--";
        }
        char fila = (char) (56 - celda.obtenerFila());
        char columna = (char) (celda.obtenerColumna() + 97);
        String resultado = "" + columna + fila;
        return resultado;
    }

    /**
     * El método obtenerJugadaEnNotacionAlgebraica, devuelve el texto correspondiente a un par de
     * celdas origen y destino en notación algebraica (e.g. con la celda origen [7][0] y la celda destino
     * [5][2] se retorna "a1c3"). Si alguna celda no pertenece al tablero, su texto correspondiente es
     * "--".
     */
    public String obtenerJugadaEnNotacionAlgebraica(Celda origen, Celda destino) {
        return obtenerCoordenadasEnNotacionAlgebraica(origen) + obtenerCoordenadasEnNotacionAlgebraica(destino);
    }

    public int obtenerNumeroColumnas() {
        return TAMANHO_POR_DEFECTO;
    }

    public int obtenerNumeroFilas() {
        return matriz.length;
    }

    /**
     * El método obtenerNumeroTorres(Color) devuelve el número de torres de un determinado color
     * en el tablero (método sobrecargado).
     */
    public int obtenerNumeroTorres(Color color) {
        int resultado = 0;
        for (Celda celda : obtenerCeldas()) {

            if (celda.obtenerColorDeTorre() == color) {
                resultado++;
            }
        }
        return resultado;

    }

    /**
     * El método obtenerNumeroTorres(Turno) devuelve el número de torres de un determinado turno
     * en el tablero (método sobrecargado).
     */
    public int obtenerNumeroTorres(Turno turno) {
        int resultado = 0;
        for (Celda celda : obtenerCeldas()) {

            if (celda.obtenerTurnoDeTorre() == turno) {
                resultado++;
            }
        }
        return resultado;

    }

    /**
     * El método obtenerSentido obtiene el sentido de movimiento desde una celda origen a destino.
     * Si las celda origen y destino no están alineadas en alguno de los sentidos definidos en la
     * enumeración Sentido, se devuelve null.
     */
    public Sentido obtenerSentido(Celda origen, Celda destino) {

        Sentido x = null;

        int fOrigen = origen.obtenerFila();
        int colOrigen = origen.obtenerColumna();
        int fDestino = destino.obtenerFila();
        int colDestino = destino.obtenerColumna();

        int difFilas = Math.abs(fOrigen - fDestino);
        int difColumnas = Math.abs(colOrigen - colDestino);

        if (fOrigen == fDestino) {
            if (colOrigen < colDestino) {
                x = Sentido.HORIZONTAL_E;
            } else if (colOrigen > colDestino) {
                x = Sentido.HORIZONTAL_O;
            }
        } else if (colOrigen == colDestino) {
            if (fOrigen < fDestino) {
                x = Sentido.VERTICAL_S;
            } else {
                x = Sentido.VERTICAL_N;
            }
        } else if (difFilas == difColumnas) {
            if (fOrigen < fDestino && colOrigen < colDestino) {
                x = Sentido.DIAGONAL_SE;
            } else if (fOrigen < fDestino) {
                x = Sentido.DIAGONAL_SO;
            } else if (colOrigen < colDestino) {
                x = Sentido.DIAGONAL_NE;
            } else {
                x = Sentido.DIAGONAL_NO;
            }
        }
        return x;


    }

    /**
     * El método toString devuelve el estado actual del tablero en formato cadena de texto tal y como se mostraría a un
     * jugador en plena partida. Ej: se muestra el tablero tras realizar algún movimiento de torres.
     * En cada celda se indica en sus cuatros esquinas la letra con su color y en el centro, el turno y color de la
     * torre (e.g. "BN" para turno blanco con torre verde, "NM" para turno negro con torre naranja, etc.) o bien un par
     * de guiones si está vacía.
     */
    @Override
    public String toString() {

        String resultado = "   a       b       c       d       e       f       g       h  \n" +
                "                                                              \n";

        for (int fila = 0; fila < TAMANHO_POR_DEFECTO; fila++) {

            String primeraYTerceraLinea = "  ";
            String segundaLinea = "" + ((char) (56 - fila)) + " ";

            for (int columna = 0; columna < TAMANHO_POR_DEFECTO; columna++) {

                primeraYTerceraLinea = primeraYTerceraLinea + crearCaracteres1ray3raLinea(matriz[fila][columna]);
                segundaLinea = segundaLinea + crearCaracteres2daLinea(matriz[fila][columna]);

            }
            primeraYTerceraLinea = primeraYTerceraLinea + "\n";
            segundaLinea = segundaLinea + "\n";

            String espacioFinalTablero = "                                                              \n" +
                    "                                                              \n";
            if (fila == (TAMANHO_POR_DEFECTO - 1)) {
                espacioFinalTablero = "";
            }

            resultado = resultado + primeraYTerceraLinea + segundaLinea + primeraYTerceraLinea + espacioFinalTablero;

        }
        return resultado;
    }

    private String crearCaracteres1ray3raLinea(Celda celda) {

        String espacioFinal = "    ";
        if (celda.obtenerColumna() == (TAMANHO_POR_DEFECTO - 1)) {
            espacioFinal = "";
        }
        char caracterColorCelda = celda.obtenerColor().toChar();
        return caracterColorCelda + ".." + caracterColorCelda + espacioFinal;

    }

    private String crearCaracteres2daLinea(Celda celda) {

        String espacioFinal = "    ";
        if (celda.obtenerColumna() == (TAMANHO_POR_DEFECTO - 1)) {
            espacioFinal = "";
        }
        if (!celda.estaVacia()) {
            return "-" + celda.obtenerTurnoDeTorre().toChar() + celda.obtenerColorDeTorre().toChar() + "-" +
                    espacioFinal;
        }
        return "----" + espacioFinal;
    }
    //TODO: AQUI COMIENZA EL CODIGO NUEVO


}