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

//TODO: EnvThibault

public class Graphe {
    private int size;
    private Set<Vertex> vertexesSet;
    private Queue<Vertex> vertexesQueue;

    public Graphe() {
        size = 0;
        vertexesQueue = new PriorityQueue<Vertex>();
        vertexesSet = new HashSet<Vertex>();
    }

    public Graphe(String path) throws FilePathExcpetion, FileNotFoundException {
        int cpt = 0;
        if ('.' == path.charAt(0))
            throw new FilePathExcpetion("Empty File name Error.");
        while(cpt < path.length() && '.' != path.charAt(cpt)){
            if (' ' != path.charAt(cpt) || '/' != path.charAt(cpt)  || '\\' != path.charAt(cpt)
                    || '!' != path.charAt(cpt) || '?' != path.charAt(cpt))
                cpt++;
            else
                throw new FilePathExcpetion("File path syntax Error.");
        }
        if('.' != path.charAt(cpt))
            throw new FilePathExcpetion("File path extension Error.");
        else
            cpt++;

        StringBuilder builder = new StringBuilder();
        while(cpt < path.length()){
            builder.append(path.charAt(cpt));
            cpt++;
        }

        if (!builder.toString().equals("graphe"))
            throw new FilePathExcpetion("File path extension Error.");

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

    public Vertex getVertex(String label){
        for (Vertex v: vertexesSet) {
            if (label.equals(v.getLabel())){
                return v;
            }
        }
        return null;
    }

    public int size(){
        return vertexesQueue.size();
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Graphe size :"+this.vertexesQueue.size()+"\n");
        for (Vertex v: vertexesQueue) {
            builder.append(v.toString()+"\n");
        }
        return builder.toString();
    }

    public void toDot() throws IOException {
        //to launch a .dot graph use this command line:
        //dot -Tx11 graphe.dot
        List<String> lines = new ArrayList<String>();
        lines.add("strict graph {");
        for (Vertex v: vertexesQueue){
            lines.add("\t"+v.toDot());
        }
        lines.add("}");
        Path file = Paths.get("graphe.dot");
        Files.write(file,lines, Charset.forName("UTF-8"));
    }

    public int nbCC(){return 0;}

}
