package mundo;

import interfaz.informacionGrafo;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class Grafo {

    // variables necesarias
    public Nodo cab;
    ArrayList<Integer> posY = new ArrayList<>();
    ArrayList<Integer> posX = new ArrayList<>();
    public ArrayList<String> enc;

    /**
     * Metodo constructor me inicializa el nodo encabezamiento
     */
    public Grafo() {
        this.cab = new Nodo("Encabezamiento", -1, 0, 0);
        cab.sigfil = cab;
        cab.sigcol = cab;
        enc = new ArrayList<>();
    }

    /**
     * agrega un vertice al grafo aqui filas y columnas son dinamicas
     *
     * @param name
     * @throws java.lang.Exception
     */
    public void agregarVertice(String name) throws Exception {
        if (existeElVertice(name) == false) {
            int fila = 1;
            Nodo aux = cab;
            Nodo aux2 = cab;
            while (aux.sigfil != cab) {
                fila += 1;
                aux = aux.sigfil;
                aux2 = aux2.sigcol;
            }
            int[] posicion = generarPosiciones();
            Nodo nuevo = new Nodo(name, 0, 0, fila);
            nuevo.posX = posicion[0];
            nuevo.posY = posicion[1];
            nuevo.sigfil = aux.sigfil;
            nuevo.sigcol = nuevo;
            aux.sigfil = nuevo;
        } else {
            throw new Exception("El nodo ya esta agregado");
        }
    }

    /**
     * Agrega una arista al grafo, si ya esta suma 1 En caso de ser un lazo un
     * vertice solo puede tener un lazo
     *
     * @param c1
     * @param c2
     * @throws Exception si no existe uno de los vertices
     */
    public void agregarArista(String c1, String c2) throws Exception {
        String a = c1 + ":" + c2;
        int fil = darPocision(c1);
        int col = darPocision(c2);
        if (existeElVertice(c1) && existeElVertice(c2)) {
            Nodo nuevo = new Nodo(c1, 0, col, fil);
            nuevo.narista = c2;
            Nodo nuevo2 = new Nodo(c2, 0, fil, col);
            nuevo2.narista = c1;
            Nodo sup = horizontal(fil, col);
            Nodo inf = horizontal(col, fil);
            boolean p = nuevo.col == sup.col;
            boolean q = nuevo2.col == inf.col;
            if (c1.toLowerCase().equals(c2.toLowerCase()) && nuevo.col == nuevo.fil) {
                nuevo.info = 2;
                nuevo.sigcol = sup.sigcol;
                sup.sigcol = nuevo;
            } else if (p || q) {
                sup.info += 1;
                inf.info += 1;
            } else if (!p && !q) {
                nuevo.info = 1;
                nuevo.sigcol = sup.sigcol;
                sup.sigcol = nuevo;
                nuevo2.info = 1;
                nuevo2.sigcol = inf.sigcol;
                inf.sigcol = nuevo2;
            }
        } else {
            throw new Exception("No se pudo agregar la arista: " + a + ". Uno de los vertices no existe");
        }
    }

    /**
     * metodo para eliminar un vertice y todas las aristas donde este presente
     *
     * @param name
     * @throws java.lang.Exception
     */
    public void eliminarVertice(String name) throws Exception {
        Nodo aux = buscar(name);
        if (existeElVertice(name)) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                String a = aux2.name;
                String b = aux2.narista;
                eliminarArista(a, b);
                aux2 = aux2.sigcol;
            }
            int a = aux.fil;
            Nodo anterior = horizontal(aux.fil - 1, 0);
            anterior.sigfil = aux.sigfil;
            Nodo res = cab.sigfil;
            while (res != cab) {
                Nodo r2 = res.sigcol;
                res.fil -= (res.fil > a) ? 1 : 0;
                while (r2 != res) {
                    r2.fil -= (r2.fil > a) ? 1 : 0;
                    r2.col -= (r2.col >= a) ? 1 : 0;
                    r2 = r2.sigcol;
                }
                res = res.sigfil;
            }
        } else {
            throw new Exception("El nodo que quiere eliminar no existe");
        }
    }

    /**
     * elimina todas las ocurrencias de una arista de forma simetrica
     *
     * @param name1
     * @param name2
     */
    public void eliminarArista(String name1, String name2) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                String a = aux2.name;
                String b = aux2.narista;
                boolean p = (a.toLowerCase().equals(name1.toLowerCase()) && b.toLowerCase().equals(name2.toLowerCase()));
                boolean q = (a.toLowerCase().equals(name2.toLowerCase()) && b.toLowerCase().equals(name1.toLowerCase()));
                if (p || q) {
                    Nodo bus = horizontal(aux2.col, aux2.fil - 1);
                    Nodo bus2 = horizontal(aux2.fil, aux2.col - 1);
                    bus.sigcol = bus.sigcol.sigcol;
                    bus2.sigcol = bus2.sigcol.sigcol;
                    break;
                }
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
    }

    /**
     * elimina todos los nodos del grafo
     */
    public void eliminarGrafo() {
        cab.sigfil = cab;
        cab.sigcol = cab;
        posX.removeAll(posX);
        posY.removeAll(posY);
    }

    /**
     * Nos dice si una matriz es simple o es compuesta
     *
     * @return boolean
     */
    public boolean esSimple() {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                if (aux2.getInfo() > 1) {
                    return false;
                }
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
        return true;
    }

    /**
     * Nos muestra los lazos existentes en un grafo
     *
     * @param panelOrigen
     */
    public void mostrarLazos(JPanel panelOrigen) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                if (aux2.getCol() == aux2.getFil()) {
                    informacionGrafo panel = new informacionGrafo();
                    panel.jLabel1.setText(aux2.name);
                    panel.jLabel2.setText(aux2.narista);
                    panel.jLabel3.setText("LAZO");
                    panelOrigen.add(new JSeparator());
                    panelOrigen.add(panel);
                    panelOrigen.add(new JSeparator());
                }
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
    }

    /**
     * Nos muestra cuales son las aristas multiples
     *
     * @param panelOrigen
     */
    public void mostrarAristasMultiples(JPanel panelOrigen) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                boolean p = aux2.getInfo() > 1;
                boolean q = aux2.getCol() > aux2.getFil();
                if (p && q) {
                    informacionGrafo panel = new informacionGrafo();
                    panel.jLabel1.setText(aux2.name);
                    panel.jLabel2.setText(aux2.narista);
                    panel.jLabel3.setText("MULTIARISTA");
                    panelOrigen.add(new JSeparator());
                    panelOrigen.add(panel);
                    panelOrigen.add(new JSeparator());
                }
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
    }

    /**
     * Mostrar aristas simples
     *
     * @param panelOrigen
     */
    public void mostrarAristasSimples(JPanel panelOrigen) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                boolean p = aux2.info == 1;
                boolean q = aux2.col > aux.fil;
                if (p && q) {
                    informacionGrafo panel = new informacionGrafo();
                    panel.jLabel1.setText(aux2.name);
                    panel.jLabel2.setText(aux2.narista);
                    panel.jLabel3.setText("ARISTA");
                    panelOrigen.add(new JSeparator());
                    panelOrigen.add(panel);
                    panelOrigen.add(new JSeparator());
                }
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
    }

    /**
     * Nos da el nodo emcabezamiento con los grados de los vertices
     *
     */
    public void gradoVertices() {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            aux.info = 0;
            while (aux2 != aux) {
                aux.info += aux2.getInfo();
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
    }

    /**
     * Metodo para comprobar el teorema1 donde dice que: SumadetodoslosGrados =
     * 2*#Aristas
     *
     * @return boolean true si se cumple el teorema en caso contrario false
     */
    public boolean teorema1() {
        Nodo aux = cab.sigfil;
        int suma = 0;
        gradoVertices();
        while (aux != cab) {
            suma += aux.getInfo();
            aux = aux.sigfil;
        }
        boolean p = (suma == contarAristas() * 2);
        if (p) {
            return true;
        }
        return false;
    }

    /**
     * Determina si el grafo contiene un vertice
     *
     * @param name
     * @return true || false
     */
    public boolean existeElVertice(String name) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            if (aux.name.toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
            aux = aux.sigfil;
        }
        return false;
    }

    /**
     * Busca un nodo y lo retorna en caso contrario retorna null
     *
     * @param name
     * @return
     */
    public Nodo buscar(String name) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            if (aux.name.toLowerCase().equals(name.toLowerCase())) {
                return aux;
            }
            aux = aux.sigfil;
        }
        return null;
    }

    /**
     * @apiNote nos lleva horizontalmente a la columna donde vamos a insertar el
     * nodo
     * @param fil
     * @param col
     * @return devuelve el nodo anterior a la columna donde queremos ingresar un
     * nodo
     */
    public Nodo horizontal(int fil, int col) {
        Nodo aux = cab.sigfil;
        while (aux != cab && aux.getFil() != fil) {
            aux = aux.sigfil;
        }
        Nodo res = aux;
        res.posY = aux.posY;
        Nodo p = aux;
        aux = aux.sigcol;
        while (aux.getCol() != p.col) {
            if (aux.getCol() <= col) {
                res = aux;
            }
            aux = aux.sigcol;
        }
        return res;
    }

    /**
     * da la posicion que tiene un vertice en la matriz
     *
     * @param name
     * @return posicion de name dentro del array si lo encontro si no return -1
     */
    public int darPocision(String name) {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            if (aux.name.toLowerCase().equals(name.toLowerCase())) {
                return aux.fil;
            }
            aux = aux.sigfil;
        }
        return -1;
    }

    /**
     * recorre hasta la ultima fila y da ese valor ya que va a ser el ultimo
     * vertice
     *
     * @return int con el numero de vertices que tiene un grafo
     *
     */
    public int contarVertices() {
        Nodo aux = cab.sigfil;
        while (aux.sigfil != cab) {
            aux = aux.sigfil;
        }
        return aux.fil;
    }

    /**
     * cuenta todas las aristas que contiene el grafo recorriendo todas las
     * filas de la matriz dinamica
     *
     * @return int devuelve el total de aristas
     */
    public int contarAristas() {
        Nodo aux = cab.sigfil;
        int res = 0;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                if (aux2.getCol() > aux.getFil()) {
                    res += aux2.info;
                } else if (aux2.getCol() == aux.getFil()) {
                    res += 1;
                }
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
        return res;
    }

    /**
     * * Imprimar los datos de las aristas
     */
    public void imprimirGrafo() {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            System.out.println(aux.getName() + "  ");
            while (aux2 != aux) {
                System.out.println("name: " + aux2.name + " vertice: " + aux2.narista + " info :" + aux2.info + " distancia: " + aux2.posX);
                aux2 = aux2.sigcol;
            }
            System.out.println();
            aux = aux.sigfil;
        }
    }

    /**
     * Metodo para generar las posiciones de los nodos
     *
     * @return
     */
    public int[] generarPosiciones() {
        int[] posiciones = new int[2];
        int x = 20;
        int y = 35;
        Random posA = new Random();
        int altx = posA.nextInt(600 - x) + x;
        int alty = posA.nextInt(500 - y) + y;
        if (!posY.contains(alty) && !posX.contains(altx)) {
            posY.add(alty);
            posX.add(altx);
            posiciones[0] = altx;
            posiciones[1] = alty;
        }
        return posiciones;

    }

    void imprimirAristas(Nodo a) {
        Nodo aux = a.sigcol;
        while (aux != a) {
            System.out.print("name: " + aux.name + "\t");
            aux = aux.sigcol;
        }
    }

    public void imVertices() {
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            System.out.println("vertice: " + aux.name + " posX: " + aux.posX + " posY: " + aux.posY);
            aux = aux.sigfil;
        }
    }

    /**
     * crear un grafo aleatorio
     *
     * @param n
     * @throws Exception
     */
    public void crearGrafoAleatorio(int n) throws Exception {
        eliminarGrafo();
        posY.clear();
        posX.clear();
        Random rnd = new Random();
        char name = 65;
        int num = 0;
        while (num < n) {
            agregarVertice(name + "" + num);
            num += 1;
        }
        generarPosiciones();
        num = 0;
        while (num < n) {
            int j = 0;
            while (j < 2) {
                agregarArista(name + "" + num, name + "" + rnd.nextInt(n));
                j++;
            }
            num += 1;
        }
    }

     /**
     * metodo para determinar si un grafo es conexo o no
     * @param aux
     * @return 
     */
    public boolean esConexo(Nodo aux){
        Nodo aux2 = aux.sigcol;
        enc.add(aux.name);
        while(aux2 != aux){
            if(!enc.contains(aux2.narista)){
                Nodo desp = horizontal(aux2.col,0);
                esConexo(desp);
            }
            aux2 = aux2.sigcol;
        }
        return enc.size()==contarVertices();
    }
    
    /**
     * Dar componentes componentes conexos
     */
    
    /**
     * metodo para hallar la distancia de una arista entre dos nodos
     *
     */
    public void actualizarDistancia() {
        int x1 = 0;
        Nodo aux = cab.sigfil;
        while (aux != cab) {
            Nodo aux2 = aux.sigcol;
            while (aux2 != aux) {
                Nodo p = cab.sigfil;
                while (p != cab) {
                    if (p.name.toLowerCase().equals(aux2.narista.toLowerCase())) {
                        x1 = p.posX;
                    }
                    p = p.sigfil;
                }
                aux2.posX = Math.abs(aux.posX - x1);
                aux2 = aux2.sigcol;
            }
            aux = aux.sigfil;
        }
    }
    
    public Nodo nodoCercano(Nodo aux, ArrayList<String> p) {
        Nodo aux2 = aux.sigcol;
        Nodo menor = aux2;
        while (aux2 != aux) {
            if (menor.posX > aux2.posX && aux2.col != aux2.fil && !p.contains(aux2.name)) {
                menor = aux2;
            }
            aux2 = aux2.sigcol;
        }
        return menor;
    }
    
    
    ArrayList<String> p = new ArrayList<>();
    public Recorrido caminoCercano(String a, String b) {
        Nodo aux = cab.sigfil;
        Recorrido res = new Recorrido();
        while (aux != cab) {
            if (aux.name.toLowerCase().equals(a.toLowerCase())) {
                Nodo bus = nodoCercano(aux, this.p);
                this.p.add(bus.narista);
                System.out.println("nombre: " + bus.name + " verticeCercano: " + bus.narista + " distancia: " + bus.posX);
                res.agregar(aux);
                res.agregar(bus);
                if(bus.narista.toLowerCase().equals(b.toLowerCase())){
                    return res;
                }else{
                    res.agregarRecorrido(caminoCercano(bus.narista,b));
                }
            }
            aux = aux.sigfil;
        }
        return null;
    }

}
