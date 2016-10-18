package Exceptions;

/**
 * Created by thibault on 16/10/16.
 */
public class FileSyntaxException extends Exception {
    public FileSyntaxException(String message) {
        super(message+"\n Expected format of Vertex definition:\nline 0: graphe size\nline1-(size-1): vertexLabel: [vertexNeighbor1Label, vertexNeighbor2Label, ...]");
    }
}
