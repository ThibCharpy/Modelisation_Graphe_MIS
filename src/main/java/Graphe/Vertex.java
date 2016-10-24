package Graphe;

import java.util.*;

/**
 * Created by thibault on 15/10/16.
 */
public class Vertex implements Comparable<Vertex>{
    private String label;
    private boolean reached;
    private Set<Vertex> edges;

    public Vertex(String label) {
        this.label = label;
        edges = new HashSet<Vertex>();
    }

    public int size(){
        return edges.size();
    }

    public void addNeighbor(Vertex vertex){
        if (null != vertex)
            edges.add(vertex);
    }

    public String getLabel() {
        return label;
    }

    public Set<Vertex> getEdges() { return edges;}

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Vertex> N(){
        Set<Vertex> s = new HashSet<Vertex>();
        s.add(this);
        s.addAll(edges);
        return s;
    }

    public Set<Vertex> N2(){
        Set<Vertex> s = new HashSet<Vertex>();
        for (Vertex v : edges) {
            s.addAll(v.edges);
        }
        return s;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(label+": [");
        Iterator<Vertex> i = edges.iterator();
        while(i.hasNext()){
            builder.append(i.next().label);
            if (i.hasNext()){
                builder.append(',');
            }
        }
        builder.append(']');
        return builder.toString();
    }

    public String toDot() {
        StringBuilder builder = new StringBuilder();
        builder.append(label+" -- {");
        int cpt=0;
        for (Vertex v : edges) {
            builder.append(v.label+";");
            if (cpt < edges.size()-1)
                builder.append(" ");
            cpt++;
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public int compareTo(Vertex o) {
        if (this.edges.size() < o.edges.size()){
            return -1;
        }else{
            if (this.edges.size() > o.edges.size()){
                return 1;
            }else{
                return 0;
            }
        }
    }

    public boolean isReached() {
        return reached;
    }

    public boolean isMirror(Vertex v) {
        if (this.N2().contains(v)){
            Graphe g = new Graphe();
            v.N().forEach(g::addVertex);
            this.N().forEach(g::removeVertex);
            return g.isClique();
        }
        return false;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
