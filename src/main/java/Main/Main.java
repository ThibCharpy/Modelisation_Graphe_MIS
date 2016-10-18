package Main;

import Graphe.Graphe;
import Exceptions.*;

import java.io.FileNotFoundException;

/**
 * Created by thibault on 17/10/16.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Graphe g = new Graphe("src/main/ressources/g1.graphe");
            System.out.println(g.toString());
        } catch (FilePathExcpetion filePathExcpetion) {
            filePathExcpetion.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
