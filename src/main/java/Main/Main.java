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
            System.out.println("fin");
            return graphe.getSize();
        }else {
            System.out.println("brique1");
            if (1 < graphe.nbCC()) {
                Graphe c = graphe.getCC();
                for (Vertex v : c.getVertexesQueue()) {
                    if (graphe.getVertexesQueue().contains(v))
                        System.out.println("WTF: "+v);
                    graphe.removeVertex(v);
                }
                return algo_MIS(c) + algo_MIS(graphe);
            } else {
                System.out.println("brique2");
                Vertex dominant = graphe.findDominance();
                if (null != dominant) {
                    graphe.removeVertex(dominant);
                    return algo_MIS(graphe);
                }else{
                    System.out.println("brique3");
                    Vertex pliable = graphe.trouverPliable();
                    if(null != pliable){
                        return 1 + algo_MIS(graphe.pliage(pliable));
                    }else {
                        System.out.println("brique4");
                        Vertex max = graphe.getMaxDegree();
                        Graphe clone = graphe.cloner();
                        Graphe iterator = clone.cloner();
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
            System.out.println(algo_MIS(g));
        } catch (FilePathException | IOException e) {
            e.printStackTrace();
        }
    }
}
