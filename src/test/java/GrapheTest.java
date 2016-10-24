import Graphe.Graphe;
import Briques.*;
import junit.framework.TestCase;

/**
 * Created by thibault on 17/10/16.
 */
public class GrapheTest extends TestCase {

    public void testGraphe() throws Exception{
        Graphe g = new Graphe("src/main/ressources/g2.graphe");

        //System.out.println(Pliage.pliable(g.getVertex("0")));
        Pliage.pliage(g, g.getVertex("0"));
        System.out.println(g.toString());
    }
}
