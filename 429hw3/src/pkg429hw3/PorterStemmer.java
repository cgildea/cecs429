package pkg429hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.*;

public class PorterStemmer {

    private static final Map<String, String> STEM = createStem();

    // a single consonant
    private static final String c = "[^aeiou]";
    // a single vowel
    private static final String v = "[aeiouy]";

    // a sequence of consonants; the second/third/etc consonant cannot be 'y'
    private static final String C = c + "[^aeiouy]*";
    // a sequence of vowels; the second/third/etc cannot be 'y'
    private static final String V = v + "[aeiou]*";

    // this regex pattern tests if the token has measure > 0 [at least one VC].
    private static final Pattern mGr0 = Pattern.compile("^(" + C + ")?" + V + C);

    // this regex pattern tests if the token has a measure equal to 1
    private static final Pattern mEq1 = Pattern.compile(mGr0 + "(" + V + ")?$");

    // this regex pattern tests if the token has a measure greater than 1
    private static final Pattern mGr1 = Pattern.compile(mGr0 + V + C);

    // this regrex pattern tests if the token has a vowel after the first (optional) C
    private static final Pattern cThenV = Pattern.compile("^(" + C + ")?" + V);

    // this regrex patten tests if the token ends in a double consonant NOT *L *S *Z
    private static final Pattern doubleC = Pattern.compile("([^aeioulsz])\\1$");

    // this regrx pattern tests if the token has a measure of 1 and the last c is not w, x, or y
    private static final Pattern cVCNotWXY = Pattern.compile("^(" + C + ")?" + v + "[^aeiouywxy]$");

    public static String processToken(String token) {
        String prefix, newPrefix;

        if (token.length() < 3) {
            return token; // token must be at least 3 chars
        }
        // step 1a
//      if (token.endsWith("sses")) {
//         token = token.substring(0, token.length() - 2);
//      }

        for (int i = 0; i < STEM.size(); i++) {
            prefix = (String) STEM.keySet().toArray()[i];
            newPrefix = (String) STEM.values().toArray()[i];

            step1a(token);
            while (i < 2) {
                if (token.endsWith(prefix)) {
                    System.out.println("PREFIX -- " + prefix);
                    System.out.println("NEW PREFIX -- " + newPrefix);
                    token = token.substring(0, token.length() - prefix.length());
                    token += newPrefix;
                    System.out.println(token);
                    break;
                }
            }
        }
      // program the other steps in 1a. 
        // note that Step 1a.3 implies that there is only a single 's' as the 
        //	suffix; ss does not count. you may need a regex pattern here for 
        // "not s followed by s".

        // step 1b
//      boolean doStep1bb = false;
//      //		step 1b
//      if (token.endsWith("eed")) { // 1b.1
//         // token.substring(0, token.length() - 3) is the stem prior to "eed".
//         // if that has m>0, then remove the "d".
//         String stem = token.substring(0, token.length() - 3);
//         if (mGr0.matcher(stem).matches()) { // if the pattern matches the stem
//            token = stem + "ee";
//         }
//      }
//      // program the rest of 1b. set the boolean doStep1bb to true if Step 1b* 
//      // should be performed.
//
//      // step 1b*, only if the 1b.2 or 1b.3 were performed.
//      if (doStep1bb) {
//         if (token.endsWith("at") || token.endsWith("bl")
//          || token.endsWith("iz")) {
//
//            token = token + "e";
//         }
//         // use the regex patterns you wrote for 1b*.4 and 1b*.5
//      }
        // step 1c
        // program this step. test the suffix of 'y' first, then test the 
        // condition *v*.
        // step 2
        // program this step. for each suffix, see if the token ends in the 
        // suffix. 
        //		* if it does, extract the stem, and do NOT test any other suffix.
        //    * take the stem and make sure it has m > 0.
        //			* if it does, complete the step. if it does not, do not 
        //				attempt any other suffix.
        // you may want to write a helper method for this. a matrix of 
        // "suffix"/"replacement" pairs might be helpful. It could look like
        // string[][] step2pairs = {  new string[] {"ational", "ate"}, 
        //										new string[] {"tional", "tion"}, ....
        // step 3
        // program this step. the rules are identical to step 2 and you can use
        // the same helper method. you may also want a matrix here.
        // step 4
        // program this step similar to step 2/3, except now the stem must have
        // measure > 1.
        // note that ION should only be removed if the suffix is SION or TION, 
        // which would leave the S or T.
        // as before, if one suffix matches, do not try any others even if the 
        // stem does not have measure > 1.
        // step 5
        // program this step. you have a regex for m=1 and for "Cvc", which
        // you can use to see if m=1 and NOT Cvc.
        // all your code should change the variable token, which represents
        // the stemmed term for the token.
        return token;
    }

    public static String step1a(String token) {
        for (int i = 0; i < STEM.size(); i++) {

            String prefix = (String) STEM.keySet().toArray()[i];
            String newPrefix = (String) STEM.values().toArray()[i];

            if (token.endsWith(prefix)) {
                System.out.println("PREFIX -- " + prefix);
                System.out.println("NEW PREFIX -- " + newPrefix);
                token = token.substring(0, token.length() - prefix.length())+ newPrefix;
                System.out.println(token);
            }
        }
        return token;
    }

    private static Map<String, String> createStem() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("sses", "ss");
        stem.put("ies", "i");
        stem.put("s", "");
        return Collections.unmodifiableMap(stem);
    }
}
