import java.util.*;

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;


    /**
     *
     * @param terms   - A list of words to form terms 
     * @param weights - A corresponding list of weights, for the terms
     * 
     */
    public HashListAutocomplete(String[] terms, double[] weights) {
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        } else if (terms.length != weights.length) {
            throw new IllegalArgumentException("terms and weights are not the same length");
        }

        initialize(terms, weights);
    }


    /**
     * Returns the top k matching terms. If there
     * are fewer than k matches, return all matching terms. If there are no matches, return an empty list.
     */
    @Override
    public List<Term> topMatches(String prefix, int k) {

        if (prefix == null) {
            throw new NullPointerException(); //throwing error if no prefix is passed
        }

        if(prefix.length() >= MAX_PREFIX){
            prefix = prefix.substring(0, MAX_PREFIX); //shortening prefix if it is longer than the maximum prefix length
        }

        if(myMap.get(prefix) != null){
            List<Term> all = myMap.get(prefix); //getting List<Term> object that corresponds with prefix key
            List<Term> topMatch = all.subList(0, Math.min(k, all.size())); //taking appropriate amount of ordered Terms needed
            return topMatch;
        }
        else{
            return new ArrayList<Term>();
        }


    }

    /**
     * Method used in implementing
     * constructors
     */
    @Override
    public void initialize(String[] terms, double[] weights) {

        //TODO: implement for myMap

        myMap = new HashMap<>();
        

        for (int i = 0; i < terms.length; i++) {
            for (int j = 0; j <= MAX_PREFIX; j++) {

                try {
                    String currentPrefix = terms[i].substring(0, j);
                    Term currentTerm = new Term(terms[i], weights[i]);


                    myMap.putIfAbsent(currentPrefix, new ArrayList<Term>()); //adding prefix to map if not present

                    if (currentTerm.getWord().substring(0, j).equals(currentPrefix)) {
                        myMap.get(currentPrefix).add(currentTerm); //adding current term to map if prefix matches
                    }
                } catch (StringIndexOutOfBoundsException e) { // catching exception and skipping iteration if substring goes out of bounds
                    continue;
                }
            }

        }


        for (List<Term> list : myMap.values()) {
        	Collections.sort(list,Comparator.comparing(Term::getWeight).reversed()); 

        }


    }

    /**
     *
     * @return number of bytes used after initialization
     */
    @Override
    public int sizeInBytes() {

        //TODO: fix code here for hashmap implementation

        if (mySize == 0) { //checking if mySize has not been calculated

            for (String key : myMap.keySet()) { // iterating through each key of myMap

                mySize += BYTES_PER_CHAR * key.length(); // adding length of each key

                for (Term t : myMap.get(key)) { // iterating through each Term object in List<Term> object in myMap
                    mySize += BYTES_PER_CHAR * t.getWord().length(); // adding length of each string in Term object
                    mySize += BYTES_PER_DOUBLE; // adding length of each double value in Term object
                }
            }
        }
        return mySize;


    }
}
