package juego.modelo;

public class TorreSimple extends TorreAbstracta{

       /**
     * Una torre se crea con un turno y color que no cambia a lo largo de la partida. Inicialmente una
     * torre no está “colocada” sobre el tablero y no tendrá celda asignada.
     */
    public TorreSimple(Turno turno, Color color) {
        super(turno, color);
    }


    @Override
    public int obtenerNumeroDientes() {
        return 0;
    }

    @Override
    public int obtenerNumeroPuntos() {
        return 1;
    }

    @Override
    public int obtenerMaximoAlcance() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int obtenerNumeroMaximoTorresAEmpujar() {
        return 0;
    }

    public String toString() {
        String turnoTxt = String.valueOf(obtenerTurno().toChar());
        String colorTxt = String.valueOf(obtenerColor().toChar());
        return turnoTxt  + colorTxt;
    }
}
