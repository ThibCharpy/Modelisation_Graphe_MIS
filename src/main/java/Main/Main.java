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
                for (Vertex v : graphe.getVertexesQueue()) {
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
                        return 1 + algo_MIS(Graphe.pliage(graphe, pliable));
                    }else {
                        Vertex max = graphe.getMaxDegree();
                        Graphe clone = graphe.clone();
                        System.out.println("clone: "+clone);
                        System.out.println("max: "+max);
                        for (Vertex v : max.getEdges()){
                            System.out.println("label: "+v.getLabel());
                            clone.removeVertex(v);
                        }
                        for (Vertex v : graphe.getMirrors(max).getVertexesQueue()){
                            graphe.removeVertex(v);
                        }
                        graphe.removeVertex(max);
                        return Math.max(algo_MIS(graphe),algo_MIS(clone));
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
