package Parcours;

import java.util.LinkedList;
import Graphe.*;

/**
 * Created by benoit-windows on 20/10/2016.
 */
public class Parcours {

    // Version pour récupérer  N² (les sommets à distance 2 de 'in')
    public static LinkedList<Vertex> parcoursLargeur(Graphe g, Vertex in, int prof) {
        LinkedList<Vertex> voisinsN = new LinkedList<>();
        LinkedList<Vertex> sommetsVisites = new LinkedList<>();
        if(0 == prof){
            return voisinsN;
        }else{
            if(1 == prof){
                voisinsN.addAll(in.getEdges());
                return voisinsN;
            }else{ // profondeur de 2 pour N²
                for(Vertex v: in.getEdges()){
                    for(Vertex v2: v.getEdges()){
                        if(!voisinsN.contains(v2) && !sommetsVisites.contains(v2)){
                            voisinsN.add(v2);
                        }
                        sommetsVisites.add(v2);
                    }
                    sommetsVisites.add(v);
                }
                for(Vertex v: in.getEdges()){
                    if(voisinsN.contains(v)){
                        voisinsN.remove(v);
                    }
                }
                if(voisinsN.contains(in)){
                    voisinsN.remove(in);
                }
                return voisinsN;
            }
        }
    }

    // Version non fonctionelle (manque des sommets)
    /*
    public static LinkedList<Vertex> parcoursLargeur(Graphe g, Vertex e, int prof){
        LinkedList<Vertex> voisinsN = new LinkedList<Vertex>();
        if(0 == prof){
            return voisinsN;
        }else{
            Queue<Vertex> sommets = new LinkedList<Vertex>();
            LinkedList<Vertex> sommetsVisites = new LinkedList<Vertex>();
            int profN = 1;

            sommetsVisites.add(e);
            sommets.add(e);

            while(!sommets.isEmpty()){
                Vertex v = sommets.poll();
                for(Vertex voisin: v.getEdges()) {
                    if (!sommetsVisites.contains(voisin)) {
                        sommetsVisites.add(voisin);
                        sommets.add(voisin);
                    }
                    if (prof == profN) {
                        voisinsN.add(voisin);
                    }
                }
                profN++;
            }
            return voisinsN;
        }
    }
    */

}
