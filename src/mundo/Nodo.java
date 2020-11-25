package mundo;

public class Nodo {

    //Variables necesarias
    public String name;
    public String narista;
    public int info;
    public Nodo sigfil;
    public Nodo sigcol;
    public int col;
    public int fil;
    public int posX;
    public int posY;
    /**
     * @author Fabian
     * @param name
     * @param info
     * @param col
     * @apiNote Definir el metodo constructor
     * @param fil
     */
    public Nodo(String name,int info, int col, int fil) {
        this.name = name;
        this.col = col;
        this.fil = fil;
        this.info = info;
        this.posX = 0;
        this.posY = 0;
    }

    /**
     * *
     * @return devuelve el nombre del nodo
     */
    public String getName() {
        return name;
    }

    /**
     * da el valor de unna col
     *
     * @return devuelve el valor de la columna
	 *
     */
    public int getCol() {
        return col;
    }

    /**
     * da el valor de fil
     *
     * @return devuelve el valor de la fila
	 *
     */
    public int getFil() {
        return fil;
    }

    /**
     * da el valor del info
     *
     * @return int valor del info
	 *
     */
    public int getInfo() {
        return info;
    }

    /**
     * @param a
     * @param b
     * @apiNote Comparar si dos nodos son iguales
     * @return boolean true si son iguales o false si son diferentes
     */
    public boolean equalsNodo(Nodo a, Nodo b) {
        return (a.getName().equals(b.name) && a.getCol() == b.getCol() && a.getFil() == b.getFil());
    }

}
