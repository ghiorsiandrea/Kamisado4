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
        return 0;
    }

    @Override
    public int obtenerMaximoAlcance() {
        return 0;
    }

    @Override
    public int obtenerNumeroMaximoTorresAEmpujar() {
        return 0;
    }
}
