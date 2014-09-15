

package pkg429hw3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Cody
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PorterStemmer ps = new PorterStemmer();
        
        Scanner scan = new Scanner(System.in);

        String searchedWord = "";
        while (!"quit".equalsIgnoreCase(searchedWord)) {
            System.out.print("\nEnter a word to search: ");
            searchedWord = scan.next();
            if ("quit".equalsIgnoreCase(searchedWord)) {
                break;
            }
            ps.processToken(searchedWord);
        }
    }
    
}
