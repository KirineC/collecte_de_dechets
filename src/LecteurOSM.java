import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class LecteurOSM {

    public static Graphe chargerFichier(String chemin) throws Exception {

        Graphe graphe = new Graphe();

        File fichier = new File(chemin);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichier);

        doc.getDocumentElement().normalize();

        Set<String> listeIds = new HashSet<>();

        NodeList listeWays = doc.getElementsByTagName("way");

        for (int i = 0; i < listeWays.getLength(); i++) {
            Element way = (Element) listeWays.item(i);
            NodeList nds = way.getElementsByTagName("nd");
            for (int j = 0; j < nds.getLength(); j++) {
                Element nd = (Element) nds.item(j);
                listeIds.add(nd.getAttribute("ref"));
            }
        }

        for (String id : listeIds) {
            graphe.ajouterNoeud(new Noeud(id, 0, 0));
        }

        for (int i = 0; i < listeWays.getLength(); i++) {
            Element way = (Element) listeWays.item(i);

            String nomRue = "Rue sans nom";
            NodeList tags = way.getElementsByTagName("tag");
            for (int t = 0; t < tags.getLength(); t++) {
                Element tag = (Element) tags.item(t);
                if (tag.getAttribute("k").equals("name")) {
                    nomRue = tag.getAttribute("v");
                }
            }

            List<String> sequence = new ArrayList<>();
            NodeList nds = way.getElementsByTagName("nd");

            for (int j = 0; j < nds.getLength(); j++) {
                Element nd = (Element) nds.item(j);
                sequence.add(nd.getAttribute("ref"));
            }

            for (int s = 0; s < sequence.size() - 1; s++) {
                Noeud a = graphe.getNoeud(sequence.get(s));
                Noeud b = graphe.getNoeud(sequence.get(s + 1));

                graphe.ajouterArete(a, b, 1.0, nomRue);
            }
        }

        System.out.println("Noeuds chargés (" + graphe.getNoeuds().size() + ") :");
        for (String id : graphe.getNoeuds().keySet()) {
            System.out.print(id + " ");
        }
        System.out.println("\n----------------------------------------");

        return graphe;
    }
}
