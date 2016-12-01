package Main;

import Graphe.*;
import Exceptions.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by thibault on 17/10/16.
 */
public class Main {

    public static boolean isClique2(Set<Vertex> C){
        for (Vertex vC : C){
            for (Vertex vC2 : C) {
                if (vC != vC2 && (!vC.getEdges().contains(vC2) || !vC2.getEdges().contains(vC))){
                    return false;
                }
            }
        }
        return true;
    }

    public static int algo_MIS(Graphe graphe){
        if (graphe.getSize() <= 1){
            return graphe.getSize();
        }else {
            int nbCC = graphe.nbCC2();
            if (1 < nbCC) {
                Graphe c = graphe.getCC2();
                graphe.removeCC(c);
                /*while(it.hasNext()){
                    Vertex v = it.next();
                    it.remove();
                }
                for (Vertex v : graphe.getVertexesQueue()) {
                    graphe.removeVertex(v);
                }*/
                return algo_MIS(c) + algo_MIS(graphe);
            } else {
                Vertex dominant = graphe.findDominance();
                if (null != dominant) {
                    graphe.removeVertex(dominant);
                    return algo_MIS(graphe);
                }else{
                    Vertex pliable = graphe.trouverPliable();
                    if(null != pliable){
                        graphe.pliage(pliable);
                        return 1 + algo_MIS(graphe);
                        //return 1 + algo_MIS(Graphe.pliage(graphe, pliable));
                    }else {
                        Vertex max = graphe.getMaxDegree();
                        Graphe clone = graphe.clone2();
                        for (Vertex v : max.N()){
                            clone.removeVertexByLabel(v);
                        }
                        Set<Vertex> mirrors = graphe.getMirrors2(max);
                        for (Vertex mirror : mirrors){
                            graphe.removeVertex(mirror);
                        }
                        /*for (Vertex v : graphe.getMirrors(max).getVertexesQueue()){
                            graphe.removeVertex(v);
                        }
                        graphe.removeVertex(max);*/
                        return Math.max(algo_MIS(graphe),1+algo_MIS(clone));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            String source = args[0];
            try {
                //Graphe g = new Graphe("src/main/ressources/g1.graphe");
                Graphe g = new Graphe(source);
                System.out.println(g.toString());
                System.out.println(algo_MIS(g));
            } catch (FilePathException ef) {
                System.out.println("FilePathException :");
                System.out.println(ef.getMessage());
                System.out.println("Expected: existing filename.graphe");
            } catch (FileSyntaxException es){
                System.out.println("FileSyntaxException :");
                System.out.println(es.getMessage());
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Input Error:");
            System.out.println("Expected : java -jar Modelisation_Graphe_MIS-1.0.jar filename.graphe");
        }

    }
}
