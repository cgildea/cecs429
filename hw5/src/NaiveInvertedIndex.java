import java.util.*;

public class NaiveInvertedIndex {
   private HashMap<String, List<Integer>> mIndex;
   
   public NaiveInvertedIndex() {
      mIndex = new HashMap<>();
   }
   
   public void addTerm(String term, int documentID) {
       if(mIndex.containsKey(term)){
           if(this.getPostings(term).contains(documentID)){
               return;
           }
           else{
               this.getPostings(term).add(documentID);
           }
       }
       else{
           ArrayList<Integer> docID = new ArrayList();
           docID.add(documentID);
           mIndex.put(term, docID);
       }
   
   }
   
   public List<Integer> getPostings(String term) {
      return mIndex.get(term);
   }
   
   public int getTermCount() {      
      return mIndex.size();
   }
   
   public String[] getDictionary() {
      String[] hashKeys;
      hashKeys = mIndex.keySet().toArray(new String[0]);
      Arrays.sort(hashKeys);
      
      return hashKeys;
   }
   
   public List<String> getContainingDocuments(String word, List<String> fileNames){
       List<Integer> documentIDs = new ArrayList();
       List<String> documentNames = new ArrayList();
       
       documentIDs = this.getPostings(word);
       if(documentIDs == null){
           return documentNames;
       }
        
       for (Integer documentID : documentIDs) {
           documentNames.add(fileNames.get(documentID));
       }
       return documentNames;
   }
}
