import Exceptions.*;
import Graphe.*;
import junit.framework.TestCase;

/**
 * Created by thibault on 17/10/16.
 */
public class GrapheTest extends TestCase {

    public void testGraphe() throws FilePathException, FileSyntaxException {
        Graphe g = new Graphe("src/main/ressources/g1.graphe");
        assertNotNull(g);
        System.out.println(g.toString());
    }

    public void testnbCC() throws FileSyntaxException, FilePathException {
        Graphe g = new Graphe("src/main/ressources/g1.graphe");
        if (g.nbCC() == 1)
            System.out.println("oui1");
        else
            System.out.println("non1");
        g = new Graphe("src/main/ressources/g2.graphe");
        if (g.nbCC() == 2)
            System.out.println("oui2");
        else
            System.out.println("non2");
        g = new Graphe("src/main/ressources/g3.graphe");
        if (g.nbCC() == 4)
            System.out.println("oui3");
        else
            System.out.println("non3");
    }

    public void testFindDominance() throws FileSyntaxException, FilePathException {
        Graphe g = new Graphe("src/main/ressources/g4.graphe");
        Graphe g2 = new Graphe();
        int ancSize = g.getSize();
        Vertex toRemove = g.findDominance();
        Vertex toRemove2 = g2.findDominance();
        assertTrue(null == toRemove2);
        assertTrue(!g2.removeVertex(null));
        assertTrue(g.removeVertex(toRemove));
        assertTrue(ancSize-1 == g.getSize());
    }
    public void testMirror() throws FileSyntaxException, FilePathException {
        Graphe g = new Graphe("src/main/ressources/g2.graphe");
        if (g.getVertex("0").isMirror(g.getVertex("1"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
        if (g.getVertex("1").isMirror(g.getVertex("2"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
        if (g.getVertex("2").isMirror(g.getVertex("3"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
        if (g.getVertex("3").isMirror(g.getVertex("4"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
        if (g.getVertex("4").isMirror(g.getVertex("5"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
        if (g.getVertex("5").isMirror(g.getVertex("6"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
        if (g.getVertex("6").isMirror(g.getVertex("7"))) {
            System.out.println("oui");
        }else{
            System.out.println("non");
        }
    }
}
