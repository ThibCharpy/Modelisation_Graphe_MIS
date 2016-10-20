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

}
