package juego.modelo;

import java.util.Random;

//¿Qué es un ENUM? En su forma más simple, una enumeración es una lista de constantes con nombre que definen un
// nuevo tipo de datos. Un objeto de un tipo de enumeración solo puede contener los valores definidos por la lista.
// Por lo tanto, una enumeración le brinda una manera de definir con precisión un nuevo tipo de datos que tiene
// un número fijo de valores válidos.


/**
 * • Contiene 8 valores: AMARILLO, AZUL, MARRON, NARANJA, PURPURA, ROJO, ROSA y VERDE.
 * • Permite almacenar y consultar el correspondiente carácter asociado a cada color: 'A', 'Z', 'M',
 * 'N', 'P', 'R', 'S' y 'V' respectivamente a los valores previos.
 * • El método estático de utilidad obtenerColorAleatorio genera un color aleatoriamente de entre
 * los ocho disponibles en la enumeración.
 */
public enum Color {
    MARRON('M'),
    VERDE('V'),
    ROJO('R'),
    AMARILLO('A'),
    ROSA('S'),
    PURPURA('P'),
    AZUL('Z'),
    NARANJA('N');

    private char representacion;

    Color(char representacion) {
        this.representacion = representacion;
    }

    public char toChar() {
        return representacion;
    }


    public static Color obtenerColorAleatorio() {

        Color[] colores = Color.values();

        Random random = new Random();

        int randomNumber = random.nextInt(colores.length);

        return colores[randomNumber];
    }
}
