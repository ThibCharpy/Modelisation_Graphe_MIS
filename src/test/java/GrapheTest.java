import Exceptions.FilePathException;
import Graphe.Graphe;
import junit.framework.TestCase;

import java.io.FileNotFoundException;

/**
 * Created by thibault on 17/10/16.
 */
public class GrapheTest extends TestCase {

    public void testGraphe() throws FileNotFoundException, FilePathException {
        Graphe g = new Graphe("src/main/ressources/g1.graphe");
        assertNotNull(g);
    }

    public void testnbCC() throws FileNotFoundException, FilePathException {
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
}
