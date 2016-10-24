package Briques;

import Graphe.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by benoit-windows on 23/10/2016.
 */
public class Pliage {

    public static boolean pliable(Vertex v){
        Set<Vertex> vEdges = v.getEdges();
        if(2 == v.getEdges().size()) {
            Vertex u1 = vEdges.iterator().next();
            vEdges.remove(u1);
            Vertex u2 = vEdges.iterator().next();
            return !u1.getEdges().contains(u2);
        }
        return false;
    }

    public static Graphe pliage(Graphe g, Vertex v){

        Set<Vertex> vEdges = v.getEdges();
        Graphe g2 = g;

        // Si le sommet a exactement 2 voisins
        if(2 == v.getEdges().size()){
            Vertex u1 = vEdges.iterator().next();
            vEdges.remove(u1);
            Vertex u2 = vEdges.iterator().next();

            // Si ses deux voisins ne sont eux memes pas voisins
            if(!u1.getEdges().contains(u2)){
                Vertex u12 = new Vertex(u1.getLabel()+"."+u2.getLabel()); // le . est censé représenter la fusion de deux sommets
                u1.getEdges().stream().filter(temp -> !u12.getEdges().contains(temp)).forEach(temp -> {
                    u12.addNeighbor(temp);
                });
                u2.getEdges().stream().filter(temp -> !u12.getEdges().contains(temp)).forEach(temp -> {
                    u12.addNeighbor(temp);
                });
                for(Vertex temp: u12.getEdges()){
                    if(!temp.getEdges().contains(u12)){
                        temp.addNeighbor(u12);
                    }
                    if(temp.getEdges().contains(u1)){
                        temp.getEdges().remove(u1);
                    }
                    if(temp.getEdges().contains(u2)){
                        temp.getEdges().remove(u2);
                    }
                }

                // Pour chaque sommet du graphe, si v était son voisin on retire v de cette liste
                g.getVertexesQueue().stream().filter(temp -> temp.getEdges().contains(v)).forEach(temp -> {
                    temp.getEdges().remove(v);
                });

                // On supprime v du graphe
                g.getVertexesQueue().remove(v);
                g.getVertexesQueue().remove(u1);
                g.getVertexesQueue().remove(u2);
                g.getVertexesQueue().add(u12);

                return g;
            }
        }
        return g;
    }
}
