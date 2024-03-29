package juego.modelo;

public abstract class TorreAbstracta implements Torre{

    /**
     * Una torre podría estar o no en una celda
     */
    protected Celda celda;

    /**
     * Una torre tendrá un solo color y un color podría repetirse en muchas torres
     */
    protected Color color;

    /**
     * Una torre pertenece a un turno (B o N) y el turno podría tener una o más torres.
     */
    protected Turno turno;

    /**
     * Una torre se crea con un turno y color que no cambia a lo largo de la partida. Inicialmente una
     * torre no está “colocada” sobre el tablero y no tendrá celda asignada.
     */
    public TorreAbstracta(Turno turno, Color color) {
        this.turno = turno;
        this.color = color;
        this.celda = null;
    }

    @Override
    public Turno obtenerTurno() {
        return turno;
    }

    @Override
    public Color obtenerColor() {
        return color;
    }

    @Override
    public Celda obtenerCelda() {
        return celda;
    }

    @Override
    public void establecerCelda(Celda celda) {
        this.celda = celda;

    }

    @Override
    public String toString() {
        String turnoTxt = String.valueOf(obtenerTurno().toChar());
        String colorTxt = String.valueOf(obtenerColor().toChar());
        return turnoTxt  + colorTxt;
    }
}
