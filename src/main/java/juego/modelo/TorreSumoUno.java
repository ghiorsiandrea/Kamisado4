package juego.modelo;

public class TorreSumoUno extends TorreAbstracta {


    /**
     * Una torre se crea con un turno y color que no cambia a lo largo de la partida. Inicialmente una
     * torre no está “colocada” sobre el tablero y no tendrá celda asignada.
     */
    public TorreSumoUno(Turno turno, Color color) {
        super(turno, color);
    }

    @Override
    public int obtenerNumeroDientes() {
        return 1;
    }

    @Override
    public int obtenerNumeroPuntos() {
        return 3;
    }

    @Override
    public int obtenerMaximoAlcance() {
        return 5;
    }

    @Override
    public int obtenerNumeroMaximoTorresAEmpujar() {
        return 1;
    }

    public String toString() {
        String turnoTxt = String.valueOf(obtenerTurno().toChar());
        String colorTxt = String.valueOf(obtenerColor().toChar());
        return turnoTxt  + colorTxt + obtenerNumeroDientes();
    }
}
