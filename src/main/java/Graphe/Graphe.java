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
    private Queue<Vertex> vertexesQueue;

    public Graphe() {
        size = 0;
        vertexesQueue = new PriorityQueue<Vertex>();
    }

    public Graphe(String path) throws FilePathException, FileSyntaxException{
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
                                } else { // lecture interieur des crochets
                                    if (' ' == line.charAt(i+j)){
                                        if (']' == line.charAt(i+j+1) && 1==j)
                                            j++;
                                        else throw new FileSyntaxException("Vertex:"+vertex.getLabel()+" -> Vertex neighbor syntax Error.");
                                    }else{
                                        builder = new StringBuilder();
                                        while (',' != line.charAt(i + j) && ']' != line.charAt(i + j)) {
                                            if (' ' != line.charAt(i + j) || '-' != line.charAt(i + j) || '/' != line.charAt(i + j)
                                                    || '!' != line.charAt(i + j) || '?' != line.charAt(i + j) || '\\' != line.charAt(i + j))
                                                builder.append(line.charAt(i + j));
                                            else
                                                throw new FileSyntaxException("Vertex:" + vertex.getLabel() + " -> Vertex label syntax Error.");
                                            j++;
                                        }
                                        Vertex neighbor;
                                        if (0 == builder.length())
                                            throw new FileSyntaxException("Vertex:" + vertex.getLabel() + " -> Empty vertex neighbor label Error.");
                                        else {
                                            neighbor = getVertex(builder.toString());
                                            if (null == neighbor) {
                                                neighbor = new Vertex(builder.toString());
                                            }
                                            vertex.addNeighbor(neighbor);
                                        }
                                        j++;
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
            throw new FilePathException("No file named \""+path+ "\" in this directory.");
        }

    }

    /**
     * if v not exist, add v to the queue and size+1
     * @param v
     */
    public void addVertex(Vertex v){
        if (!this.vertexesQueue.contains(v)){
                size++;
                vertexesQueue.add(v);
        }
    }

    /**
     * if vertex with label is already created re this vertex
     * @param label
     * @return
     */
    public Vertex getVertex(String label){
        for (Vertex vQueue: this.vertexesQueue) {
            if (vQueue.getLabel().equals(label))
                return vQueue;
            else
                for (Vertex vQueueNeighbor: vQueue.getEdges()) {
                    if (vQueueNeighbor.getLabel().equals(label))
                        return vQueueNeighbor;
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

    /*public void toDot() throws IOException {
        //to launch a .dot graph use this command line:
        //dot -Tx11 graphe.dot
        List<String> lines = new ArrayList<>();
        lines.add("strict graph {");
        for (Vertex v: this.vertexesQueue){
            lines.add("\t"+v.toDot());
        }
        lines.add("}");
        Path file = Paths.get("target/graphe.dot");
        Files.write(file,lines, Charset.forName("UTF-8"));
    }

    public void toDot(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("strict graph {");
        for (Vertex v: this.vertexesQueue){
            lines.add("\t"+v.toDot());
        }
        lines.add("}");
        Path file = Paths.get("target/"+fileName+".dot");
        Files.write(file,lines, Charset.forName("UTF-8"));
    }*/

    public int nbCC(){
        int nbCC = 0;
        Map<Vertex,Integer> states = new HashMap<Vertex, Integer>();
        Queue<Vertex> queue = new PriorityQueue<Vertex>();
        //non atteint = -1, atteint = 0, traité = 1
        for (Vertex v : this.vertexesQueue){
            states.put(v,-1);
        }
        for (Vertex v : this.vertexesQueue){
            if (-1 == states.get(v)){
                nbCC++;
                queue.add(v);
                states.replace(v,-1,0);
                while(!queue.isEmpty()){
                    Vertex head = queue.poll();
                    for (Vertex headNeighbor : head.getEdges()){
                        if (-1 == states.get(headNeighbor)) {
                            states.replace(headNeighbor, -1, 0);
                            queue.add(headNeighbor);
                        }
                    }
                    states.replace(head,0,1);
                }
            }
        }
        return nbCC;
    }

    public Graphe getCC(){
        List<Graphe> ccs = new ArrayList<Graphe>();
        Queue<Vertex> queue = new PriorityQueue<Vertex>();
        Graphe g;
        for (Vertex v : this.vertexesQueue){
            g = new Graphe();
            queue.add(v);
            while(!queue.isEmpty()){
                Vertex head = queue.poll();
                for (Vertex headNeighbor : head.getEdges()){
                    head.addNeighbor(headNeighbor);
                    queue.add(headNeighbor);
                }
                g.addVertex(head);
            }
            ccs.add(g);
        }
        return ccs.get(0);
    }

    public void removeCC(Graphe cc){
        for (Vertex vCC: cc.vertexesQueue) {
            this.removeVertex(vCC);
        }
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

    public boolean removeVertexByLabel(Vertex v){
        if (null != v) {
            Vertex toRemove = this.getVertex(v.getLabel());
            if (null != toRemove) {
                if (!toRemove.getEdges().isEmpty()) {
                    for (Vertex toRemoveNeighbor : toRemove.getEdges()) {
                        toRemoveNeighbor.removeNeighborByLabel(v);
                    }
                }
                    this.vertexesQueue.remove(toRemove);
                    this.size--;
                    return true;
            }
        }
        return false;
    }

    public boolean removeVertex(Vertex v){
        if (null != v) {
            if (!v.getEdges().isEmpty()) {
                for (Vertex vNeighbor : v.getEdges()) {
                    vNeighbor.removeNeighbor(v);
                }
                this.size--;
                this.vertexesQueue.remove(v);
                return true;
            }
        }
        return false;
    }

    public boolean isClique() {
        for (Vertex v1: this.vertexesQueue){
            for (Vertex v2: this.vertexesQueue){
                if (v1 != v2){
                    if (!v1.N().contains(v2)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Set<Vertex> getMirrors(Vertex v){
        Set<Vertex> m = new HashSet<Vertex>();
        for (Vertex vGraphe : this.vertexesQueue){
            if (vGraphe.isMirror2(v)){
                m.add(vGraphe);
            }
        }
        return m;
    }

    public Vertex getMaxDegree() {
        Vertex max = null;
        int size = 0;
        for (Vertex v: this.vertexesQueue){
            if (v.size() > size){
                size = v.size();
                max = v;
            }
        }
        return max;
    }

    public Vertex trouverPliable(){
        for(Vertex v: this.vertexesQueue){
            if(v.is2pliable()){
                return v;
            }
        }
        return null;
    }

    public Graphe clone_bis(){
        Graphe clone = new Graphe();
        for (Vertex vQueue : this.vertexesQueue) {
            Vertex vClone = clone.getVertex(vQueue.getLabel());
            if (null == vClone)
                vClone = new Vertex(vQueue.getLabel());
            for (Vertex vQueueNeighbor : vQueue.getEdges()) {
                Vertex vCloneNeighbor = clone.getVertex(vQueueNeighbor.getLabel());
                if (null == vCloneNeighbor) {
                    vCloneNeighbor = new Vertex(vQueueNeighbor.getLabel());
                    clone.addVertex(vCloneNeighbor);
                }
                vClone.addNeighbor(vCloneNeighbor);
            }
            clone.addVertex(vClone);
        }
        return clone;
    }

    public void pliage(Vertex v){
        boolean cpt = true;
        Vertex u1 = null;
        Vertex u2 = null;
        for (Vertex vNeighbor : v.getEdges()){
            if (cpt) {
                u1 = vNeighbor;
                cpt = false;
            }else{
                u2 = vNeighbor;
            }
        }
        Vertex u12 = new Vertex(u1.getLabel()+u2.getLabel());
        for (Vertex vU1Neighbor: u1.getEdges()) {
            u12.addNeighbor(vU1Neighbor);
        }
        for (Vertex vU2Neighbor: u2.getEdges()) {
            if (!u12.getEdges().contains(vU2Neighbor)){
                u12.addNeighbor(vU2Neighbor);
            }
        }
        this.removeVertex(v);
        this.removeVertex(u1);
        this.removeVertex(u2);
        this.addVertex(u12);
    }
}
