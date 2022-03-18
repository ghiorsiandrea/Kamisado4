package juego.modelo;

/**
 * • Contiene los dos turnos en la partida: BLANCO y NEGRO.
 * • Permite almacenar y consultar el correspondiente carácter asociado a cada turno: 'B' y 'N'
 * respectivamente a los valores previos.
 */
public enum Turno {
    BLANCO('B'),
    NEGRO('N');

    private char representacionC;

    Turno(char representacionC) {
        this.representacionC = representacionC;
    }

    public char toChar() {
        return representacionC;
    }
}
