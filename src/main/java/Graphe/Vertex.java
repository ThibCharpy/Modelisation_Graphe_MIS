package Graphe;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by thibault on 15/10/16.
 */
public class Vertex implements Comparable<Vertex>{
    private String label;
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
        if (null != vertex)
            edges.add(vertex);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
