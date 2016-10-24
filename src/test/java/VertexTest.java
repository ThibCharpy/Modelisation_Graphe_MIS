import Graphe.Vertex;
import junit.framework.TestCase;

/**
 * Created by thibault on 20/10/16.
 */
public class VertexTest extends TestCase {

    public void testN(){
        Vertex v0 = new Vertex("v0");
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");

        assertTrue(v0.N().size() == 1);

        v0.addNeighbor(v1);
        v0.addNeighbor(v2);

        assertTrue(v0.N().size() == 3);

        v0.addNeighbor(v3);
        v0.addNeighbor(v4);

        assertTrue(v0.N().size() == 5);
    }

    public  void testN2(){
        Vertex v0 = new Vertex("v0");
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");
        Vertex v5 = new Vertex("v5");

        assertTrue(v0.N2().size() == 0);

        v1.addNeighbor(v2);
        v0.addNeighbor(v1);
        v3.addNeighbor(v4);
        v3.addNeighbor(v5);
        v0.addNeighbor(v3);

        assertTrue(v0.N2().size() == 3);
    }
}
