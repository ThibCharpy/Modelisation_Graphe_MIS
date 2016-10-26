package Main;

import Graphe.*;
import Exceptions.*;
import java.io.IOException;

/**
 * Created by thibault on 17/10/16.
 */
public class Main {
    public static int algo_MIS(Graphe graphe){
        if (graphe.getSize() <= 1){
            return graphe.getSize();
        }else {
            if (1 < graphe.nbCC()) {
                Graphe c = graphe.getCC();
                for (Vertex v : c.getVertexesQueue()) {
                    graphe.removeVertex(v);
                }
                return algo_MIS(c) + algo_MIS(graphe);
            } else {
                Vertex dominant = graphe.findDominance();
                if (null != dominant) {
                    graphe.removeVertex(dominant);
                    return algo_MIS(graphe);
                }else{
                    Vertex pliable = graphe.trouverPliable();
                    if(null != pliable){
                        return 1 + algo_MIS(graphe.pliage(pliable));
                    }else {
                        Vertex max = graphe.getMaxDegree();
                        Graphe clone = graphe.clone();
                        Graphe iterator = clone.clone();
                        for (Vertex v : iterator.getVertex(max.getLabel()).getEdges()){
                            clone.removeVertex(clone.getVertex(v.getLabel()));
                        }
                        for (Vertex v : iterator.getMirrors(iterator.getVertex(max.getLabel())).getVertexesQueue()){
                            graphe.removeVertex(graphe.getVertex(v.getLabel()));
                        }
                        graphe.removeVertex(graphe.getVertex(max.getLabel()));
                        return Math.max(algo_MIS(graphe),1+algo_MIS(clone));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Graphe g = new Graphe("src/main/ressources/g1.graphe");
            System.out.println(g.toString());
            g.toDot();
            algo_MIS(g);
        } catch (FilePathException | IOException e) {
            e.printStackTrace();
        }
    }
}
