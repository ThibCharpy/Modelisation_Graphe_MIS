package Graphe;

import java.util.*;

import static Main.Main.isClique2;

/**
 * Created by thibault on 15/10/16.
 */
public class Vertex implements Comparable<Vertex>{
    private String label;
    //private boolean reached;
    private Set<Vertex> edges;

    public Vertex(String label) {
        this.label = label;
        edges = new HashSet<Vertex>();
    }

    public Set<Vertex> getEdges(){
        return this.edges;
    }

    public int size(){
        return edges.size();
    }

    public void addNeighbor(Vertex vertex){
        if (null != vertex){
            edges.add(vertex);
            vertex.edges.add(this);
        }
    }

    public void removeNeighborByLabel(Vertex vertex){
        Iterator<Vertex> it = this.getEdges().iterator();
        while (it.hasNext()){
            if (it.next().getLabel().equals(vertex.getLabel())){
                it.remove();
            }
        }
    }

    public boolean removeNeighbor(Vertex vertex){
        return this.edges.remove(vertex);
    }

    public String getLabel() {
        return label;
    }

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
        builder.append(label).append(": [");
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
        builder.append(label).append(" -- {");
        int cpt=0;
        for (Vertex v : edges) {
            builder.append(v.label).append(";");
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

    /*public boolean isReached() {
        return reached;
    }*/

    public Vertex clone2(){
        Vertex vClone = new Vertex(this.getLabel());
        for (Vertex neighbor : this.getEdges()){
            Vertex vCloneNeighbor = new Vertex(neighbor.getLabel());
            vClone.addNeighbor(vCloneNeighbor);
        }
        return vClone;
    }

    public boolean isMirror2(Vertex v){
        if (v.N2().contains(this)){
            //obligé car sinon on utilise pas une copie
            Set<Vertex> c = new HashSet<Vertex>();
            for (Vertex vNeighbor : v.getEdges()){
                c.add(vNeighbor.clone2());
            }
            Iterator<Vertex> it = null;
            for (Vertex vU : this.getEdges()){
                it = c.iterator();
                while(it.hasNext()){
                    if (it.next().getLabel().equals(vU.getLabel()))
                        it.remove();
                }
            }
            if (isClique2(c))
                return true;
        }
        return false;
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

    /*public void setReached(boolean reached) {
        this.reached = reached;
    }*/

    public boolean isDominant(Vertex w){
        for (Vertex wNeighbor : w.N()) {
            if (!this.N().contains(wNeighbor)) {
                return false;
            }
        }
        return true;
    }

    public Vertex clone() {
        Vertex clone = new Vertex(this.label);
        for (Vertex v : this.getEdges()){
            if (!this.getEdges().contains(v)){
                clone.addNeighbor(v.clone());
            }
        }
        return clone;
    }

    public boolean is2pliable(){
        if (2 == this.getEdges().size()){
            Set<Vertex> neighbors = this.clone2().getEdges();
            Vertex u1 = neighbors.iterator().next();
            neighbors.remove(u1);
            Vertex u2 = neighbors.iterator().next();
            if (!u1.getEdges().contains(u2))
                return true;
        }
        return false;
    }
}
