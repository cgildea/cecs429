package pkg429.hw2;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very simple search engine. Uses an inverted index over a folder of TXT
 * files.
 */
public class SimpleEngine {

    public static void main(String[] args) throws IOException {
        final Path currentWorkingPath = Paths.get("").toAbsolutePath();

        // the inverted index
        final NaiveInvertedIndex index = new NaiveInvertedIndex();

        // the list of file names that were processed
        final List<String> fileNames = new ArrayList<String>();

        // This is our standard "walk through all .txt files" code.
        Files.walkFileTree(currentWorkingPath, new SimpleFileVisitor<Path>() {
            int mDocumentID = 0;

            public FileVisitResult preVisitDirectory(Path dir,
                    BasicFileAttributes attrs) {
                // make sure we only process the current working directory
                if (currentWorkingPath.equals(dir)) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.SKIP_SUBTREE;
            }

            public FileVisitResult visitFile(Path file,
                    BasicFileAttributes attrs) {
                // only process .txt files
                if (file.toString().endsWith(".txt")) {
               // we have found a .txt file; add its name to the fileName list,
                    // then index the file and increase the document ID counter.
                    //System.out.println("Indexing file " + file.getFileName());

                    fileNames.add(file.getFileName().toString());
                    indexFile(file.toFile(), index, mDocumentID);
                    mDocumentID++;
                }
                return FileVisitResult.CONTINUE;
            }

            // don't throw exceptions if files are locked/other errors occur
            public FileVisitResult visitFileFailed(Path file,
                    IOException e) {

                return FileVisitResult.CONTINUE;
            }

        });

        printResults(index, fileNames);

        Scanner scan = new Scanner(System.in);

        String searchedWord = "";
        while (!"quit".equalsIgnoreCase(searchedWord)) {
            System.out.print("\nEnter a word to search: ");
            searchedWord = scan.next();
            if ("quit".equalsIgnoreCase(searchedWord)) {
                break;
            }
            List<String> documentNames = new ArrayList();

            documentNames = index.getContainingDocuments(searchedWord, fileNames);
            if(documentNames.isEmpty())
                System.out.println("The word \""+ searchedWord + "\" was not found.");        
            else
                System.out.println("Word is found in: "+ documentNames.toString());
        }
    }

    /**
     * Indexes a file by reading a series of tokens from the file, treating each
     * token as a term, and then adding the given document's ID to the inverted
     * index for the term.
     *
     * @param file a File object for the document to index.
     * @param index the current state of the index for the files that have
     * already been processed.
     * @param docID the integer ID of the current document, needed when indexing
     * each term from the document.
     */
    private static void indexFile(File file, NaiveInvertedIndex index,
            int docID) {
        try {
            SimpleTokenStream tokenStream = new SimpleTokenStream(file);
            while(tokenStream.hasNextToken()){
                index.addTerm(tokenStream.nextToken(), docID);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void printResults(NaiveInvertedIndex index,
            List<String> fileNames) {
        int longestWord = 0;
        for (String dict : index.getDictionary()) {
            longestWord = Math.max(longestWord, dict.length());
        }
        for(String token : index.getDictionary()){
            System.out.format("%-" + longestWord + "s: %s\n", token, index.getContainingDocuments(token, fileNames).toString());
        }
       
    }
}
