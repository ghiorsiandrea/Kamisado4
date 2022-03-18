package juego.modelo;

public class Celda {

    /**
     * Una celda solo puede tener un color, un color puede estar en muchas celdas
     */
    private Color color;

    /**
     * Una celda podría contener o no una torre
     */
    private Torre torre;

    private int fila;

    private int columna;

    /**
     * • Inicialmente toda celda estará vacía. Solo contiene las coordenadas con su posición en el tablero
     * y el color asignado.
     */
    public Celda(int fila, int columna, Color color) {
        this.color = color;
        this.fila = fila;
        this.columna = columna;
        this.torre = null;
    }

    /**
     * * • El método eliminarTorre elimina la torre de una celda previamente asignada.
     */
    public void eliminarTorre() {
        torre = null;
    }

    /**
     * * • El método establecerTorre asigna la torre a la celda actual.
     */
    public void establecerTorre(Torre torre) {
        this.torre = torre;
    }

    /***
     * • El método estaVacia consulta si la celda tiene o no torre asignada.
     */
    public boolean estaVacia() {
        return torre == null;
    }

    public int obtenerFila() {
        return fila;
    }

    public int obtenerColumna() {
        return columna;
    }

    public Color obtenerColor() {
        return color;
    }

    public Torre obtenerTorre() {
        return torre;
    }

    /***
     * • El método obtenerColorDeTorre obtiene el color de la torre asignada, o null si está vacía.
     */
    public Color obtenerColorDeTorre() {
        if (estaVacia()) {
            return null;
        } else {
            return torre.obtenerColor();
        }
    }

    /**
     * • El método obtenerTurnoDeTorre obtiene el turno de la torre asignada, o null si está vacía.
     */
    public Turno obtenerTurnoDeTorre() {
        if (estaVacia()) {
            return null;
        } else {
            return torre.obtenerTurno();
        }
    }

    /**
     * • El método tieneCoordenadasIguales devuelve true si la celda pasada como argumento tiene
     * * iguales coordenadas a la celda actual, o false en caso contrario, con independencia del resto de
     * * su estado.
     */

    public boolean tieneCoordenadasIguales(Celda celda) {
        return celda.obtenerFila() == obtenerFila() && celda.obtenerColumna() == obtenerColumna();
    }

    /**
     * • El método toString devuelve en formato texto el estado actual de la celda, con el siguiente formato:
     * [X][Y] Donde X es el n.º de fila,  Y el n.º de columna
     * Color: C, C es un color
     * Turno: T, T es el turno.
     * Torre: C.
     * Por ejemplo: "[4][2] Color: V Turno: B Torre: N" para la celda en las coordenadas del array [4][2],
     * con color verde y con una torre colocada del turno blanco con color naranja.
     * Si dicha celda estuviese vacía, el texto generado hubiese sido "[4][2] Color: V Turno: - Torre: -".
     */
    public String toString() {
        String filaTxt = "[" + obtenerFila() + "]";
        String columnaTxt = "[" + obtenerColumna() + "] ";
        String colorTxt = "Color: " + obtenerColor().toChar();
        String turnoTxt;
        String colorTorreTxt;
        if (estaVacia()) {
            turnoTxt = " Turno: -";
            colorTorreTxt = " Torre: -";
        } else {
            turnoTxt = " Turno: " + obtenerTurnoDeTorre().toChar();
            colorTorreTxt = " Torre: " + obtenerColorDeTorre().toChar();
        }
        return filaTxt + columnaTxt + colorTxt + turnoTxt + colorTorreTxt;
    }

}