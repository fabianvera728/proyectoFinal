/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mundo;

import java.util.ArrayList;

/**
 *
 * @author Fabian
 */
public class Recorrido {
    Nodo cab;
    Grafo a;
    public Recorrido(){
        this.a = a;
        cab = new Nodo("cab",-1,-1,-1);
        cab.sigfil = null;
    }
    
    
    //un camino donde todas las aristas son diferentes se denomina recorrido
    
    
    
    //un camino simple es un camino donde todos los vertices son diferentes
    
    
    //un ciclo es un camino donde en el nodo de inicio es el nodo de fin 
    //teniendo en cuenta que todos los nodos son distintos
    
    
    

    public void agregar(Nodo nuevo){
        Nodo aux = cab;
        while(aux.sigfil != null){
            aux = aux.sigfil;
        }
        nuevo.sigfil = null;
        aux.sigfil = nuevo;
    }
    public void agregarRecorrido(Recorrido r){
        Nodo aux = cab;
        while(aux.sigfil != cab){
            aux = aux.sigfil;
        }
        aux.sigfil = r.cab.sigfil;
    }
    public void imprimirRecorrido(){
        Nodo aux = cab.sigfil;
        System.out.println("---------Camino mas corto entre dos punto------------");
        while(aux != null){
            System.out.println("Name: "+aux.name);
            aux = aux.sigfil;
        }
    }
}
