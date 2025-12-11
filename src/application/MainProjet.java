package application;

import java.util.Scanner;

public class MainProjet {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n================= MENU PRINCIPAL =================");
            System.out.println("1 - Thème 1 ");
            System.out.println("2 - Thème 2 ");
            System.out.println("3 - Thème 3 ");
            System.out.println("4 - Quitter");
            System.out.print("Votre choix : ");

            String line = sc.nextLine().trim();
            int choix;
            try {
                choix = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide.");
                continue;
            }

            switch (choix) {
                case 1:

                    Main.main(new String[0]);
                    break;

                case 2:

                    MainTheme2.main(new String[0]);
                    break;


                case 3:
                    MainTheme3.main(null);
                    break;


                case 4:
                    System.out.println("Au revoir !");
                    sc.close();
                    return;

                default:
                    System.out.println("Choix inconnu.");
            }
        }
    }
}
