package Graphe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import Exceptions.*;

/**
 * Created by thibault on 15/10/16.
 */

//TODO: Creer l'algorithme de MIS avec ce qui à été implémenté

public class Graphe {
    private int size;
    private Set<Vertex> vertexesSet;
    private Queue<Vertex> vertexesQueue;

    public Graphe() {
        size = 0;
        vertexesQueue = new PriorityQueue<Vertex>();
        vertexesSet = new HashSet<Vertex>();
    }

    public Graphe(String path) throws FilePathException, FileNotFoundException {
        int cpt = 0;
        if ('.' == path.charAt(0))
            throw new FilePathException("Empty File name Error.");
        while(cpt < path.length() && '.' != path.charAt(cpt)){
            if (' ' != path.charAt(cpt) || '/' != path.charAt(cpt)  || '\\' != path.charAt(cpt)
                    || '!' != path.charAt(cpt) || '?' != path.charAt(cpt))
                cpt++;
            else
                throw new FilePathException("File path syntax Error.");
        }
        if('.' != path.charAt(cpt))
            throw new FilePathException("File path extension Error.");
        else
            cpt++;

        StringBuilder builder = new StringBuilder();
        while(cpt < path.length()){
            builder.append(path.charAt(cpt));
            cpt++;
        }

        if (!builder.toString().equals("graphe"))
            throw new FilePathException("File path extension Error.");

        Path source = Paths.get(path);

        if (Files.exists(source)) {
            try {
                BufferedReader br = Files.newBufferedReader(source);
                String line = br.readLine();
                int lineCpt = 0;
                int vertexCpt = 0;
                vertexesQueue = new PriorityQueue<Vertex>();
                vertexesSet = new HashSet<Vertex>();

                while (line != null){
                    if (0 < line.length()){ // Ligne vide
                        if (0 == lineCpt){ // ligne avec la taille
                            size = Integer.parseInt(String.valueOf(line.charAt(0)));
                            if (0 == size)
                                throw new FileSyntaxException("Graphe size definition is 0.");
                        } else { // Ligne renseignement voisins
                            int i = 0;
                            builder = new StringBuilder();
                            while (i < line.length() && ':' != line.charAt(i)) {
                                if (' ' != line.charAt(i) || '-' != line.charAt(i) || '/' != line.charAt(i)
                                        || '!' != line.charAt(i) || '?' != line.charAt(i) || '\\' != line.charAt(i))
                                    builder.append(line.charAt(i));
                                else
                                    throw new FileSyntaxException("Vertex label syntax Error.");
                                i++;
                            }
                            Vertex vertex;
                            if (0 == builder.length())
                                throw new FileSyntaxException("Empty vertex label Error.");
                            else {
                                vertex = getVertex(builder.toString());
                                if (null == vertex){
                                    vertex = new Vertex(builder.toString());
                                    vertexesSet.add(vertex);
                                }
                            }
                            if( i+1 < line.length() && ':' == line.charAt(i) && ' ' == line.charAt(i+1))
                                i = i+2;
                            else
                                throw new FileSyntaxException("Vertex:"+vertex.getLabel()+" -> Vertex defintion syntax Error.");

                            int j=0;
                            while(i+j < line.length() && ']' != line.charAt(i+j)){
                                if (0 == j){ // Definition des voisins commence par un crochet ouvrant
                                    if ('[' != line.charAt(i+j))
                                        throw new FileSyntaxException("Vertex:"+vertex.getLabel()+" -> Vertex defintion syntax Error.");
                                    else
                                        j++;
                                } else {
                                    if (' ' != line.charAt(i)){
                                        if (']' == line.charAt(i+1))
                                            throw new FileSyntaxException("Vertex:"+vertex.getLabel()+" -> No vertex neighbor Error.");
                                        else
                                            if (1 != j)
                                                j++;
                                    }
                                    builder = new StringBuilder();
                                    while( ',' != line.charAt(i+j) && ']' != line.charAt(i+j)){
                                        if (' ' != line.charAt(i+j) || '-' != line.charAt(i+j) || '/' != line.charAt(i+j)
                                                || '!' != line.charAt(i+j) || '?' != line.charAt(i+j) || '\\' != line.charAt(i+j))
                                            builder.append(line.charAt(i+j));
                                        else
                                            throw new FileSyntaxException("Vertex:"+vertex.getLabel()+" -> Vertex label syntax Error.");
                                        j++;
                                    }
                                    Vertex neighbor;
                                    if (0 == builder.length())
                                        throw new FileSyntaxException("Vertex:"+vertex.getLabel()+" -> Empty vertex neighbor label Error.");
                                    else {
                                        neighbor = getVertex(builder.toString());
                                        if (null == neighbor) {
                                            neighbor = new Vertex(builder.toString());
                                            vertexesSet.add(neighbor);
                                        }
                                        vertex.addNeighbor(neighbor);
                                    }
                                }
                            }
                            i+=j;
                            while(i < line.length()){
                                i++;
                            }
                            vertexCpt++;
                            vertexesQueue.add(vertex);
                        }
                        if (size < vertexCpt)
                            throw new FileSyntaxException("Size definition is lower than the number of vertex read.");
                    }
                    line = br.readLine();
                    lineCpt++;
                }
                if (vertexCpt < size)
                    throw new FileSyntaxException("Size definition is higher than the number of vertex read.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            throw new FileNotFoundException();
        }

    }

    public void addVertex(Vertex v){
        if (!this.vertexesSet.contains(v)){
            size++;
            vertexesSet.add(v);
            vertexesQueue.add(v);
        }
    }

    public Vertex getVertex(String label){
        for (Vertex v: vertexesSet) {
            if (label.equals(v.getLabel())){
                return v;
            }
        }
        return null;
    }

    public Queue<Vertex> getVertexesQueue(){
        return this.vertexesQueue;
    }

    public int getSize(){
        return vertexesQueue.size();
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Graphe size :").append(this.vertexesQueue.size()).append("\n");
        for (Vertex v: vertexesQueue) {
            builder.append(v.toString()).append("\n");
        }
        return builder.toString();
    }

    public void toDot() throws IOException {
        //to launch a .dot graph use this command line:
        //dot -Tx11 graphe.dot
        List<String> lines = new ArrayList<>();
        lines.add("strict graph {");
        for (Vertex v: vertexesQueue){
            lines.add("\t"+v.toDot());
        }
        lines.add("}");
        Path file = Paths.get("target/graphe.dot");
        Files.write(file,lines, Charset.forName("UTF-8"));
    }

    public void toDot(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("strict graph {");
        for (Vertex v: vertexesQueue){
            lines.add("\t"+v.toDot());
        }
        lines.add("}");
        Path file = Paths.get("target/"+fileName+".dot");
        Files.write(file,lines, Charset.forName("UTF-8"));
    }

    public int nbCC() {
        int nbCC = 0;
        Vertex tete;
        LinkedList<Vertex> fifo = new LinkedList<Vertex>();
        for (Vertex v: vertexesQueue){
            v.setReached(false);
        }
        for (Vertex v: vertexesQueue){
            if (!v.isReached()){
                fifo.add(v);
                nbCC++;
                v.setReached(true);
                while (!fifo.isEmpty()){
                    tete = fifo.removeFirst();
                    for (Vertex neighbour: tete.getEdges()){
                        if (!neighbour.isReached()){
                            neighbour.setReached(true);
                            fifo.add(neighbour);
                        }
                    }
                }
            }
        }
        return nbCC;
    }

    public Graphe getCC(){
        Graphe graphe = new Graphe();
        Vertex tete;
        LinkedList<Vertex> fifo = new LinkedList<Vertex>();
        for (Vertex v: vertexesQueue){
            v.setReached(false);
        }
        Vertex v = this.vertexesQueue.peek();
        fifo.add(v);
        v.setReached(true);
        while (!fifo.isEmpty()) {
            tete = fifo.removeFirst();
            graphe.addVertex(tete);
            for (Vertex neighbour : tete.getEdges()) {
                if (!neighbour.isReached()) {
                    neighbour.setReached(true);
                    fifo.add(neighbour);
                }
            }
        }
        return graphe;
    }

    public Vertex findDominance() {
        for (Vertex v : this.vertexesQueue) {
            for (Vertex w : v.getEdges()) {
                if (v.isDominant(w))
                    return v;
            }
        }
        return null;
    }

    public boolean removeVertex(Vertex v){
        if (null != v) {
            if (!v.getEdges().isEmpty()) {
                for (Vertex vNeighbor : v.getEdges()) {
                    vNeighbor.removeNeighbor(v);
                }
                this.size--;
                this.vertexesQueue.remove(v);
                this.vertexesSet.remove(v);
                return true;
            }
        }
        return false;
    }

    public boolean isClique() {
        for (Vertex v1: vertexesQueue){
            for (Vertex v2: vertexesQueue){
                if (v1 != v2){
                    if (!v1.N().contains(v2)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Graphe getMirrors(Vertex v) {
        Graphe g = new Graphe();
        for (Vertex neighbour2: vertexesQueue){
            if (neighbour2 != v && neighbour2.isMirror(v)){
                g.addVertex(neighbour2);
            }
        }
        return g;
    }

    public Vertex getMaxDegree() {
        Vertex max = null;
        int size = 0;
        for (Vertex v: vertexesQueue){
            if (v.size() > size){
                size = v.size();
                max = v;
            }
        }
        return max;
    }

    public Vertex trouverPliable(){
        for(Vertex v: this.getVertexesQueue()){
            if(pliable(v)){
                return v;
            }
        }
        return null;
    }

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

    public Graphe clone() {
        Graphe clone = new Graphe();
        for (Vertex v: this.vertexesQueue){
            clone.addVertex(v.clone());
        }
        return clone;
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
