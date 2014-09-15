package pkg429hw3;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.*;

public class PorterStemmer {

    // a single consonant
    private static final String c = "[^aeiou]";
    // a single vowel
    private static final String v = "[aeiouy]";

    // a sequence of consonants; the second/third/etc consonant cannot be 'y'
    private static final String C = c + "[^aeiouy]*";
    // a sequence of vowels; the second/third/etc cannot be 'y'
    private static final String V = v + "[aeiou]*";

    // this regex pattern tests if the token has measure > 0 [at least one VC].
    private static final Pattern mGr0 = Pattern.compile("^(" + C + ")?" + V + C + "(.*)?");

    // this regex pattern tests if the token has a measure equal to 1
    private static final Pattern mEq1 = Pattern.compile(mGr0 + "(" + V + ")?$");

    // this regex pattern tests if the token has a measure greater than 1
    private static final Pattern mGr1 = Pattern.compile(mGr0 + V + C);

    // this regrex pattern tests if the token has a vowel after the first (optional) C
    private static final Pattern hasV = Pattern.compile("(.*)?" + V + "(.*)?");

    // this regrex patten tests if the token ends in a double consonant NOT *L *S *Z
    private static final Pattern doubleC = Pattern.compile("(.*)?([^aeioulsz])\\2$");

    // this regrx pattern tests if the token has a measure of 1 and the last c is not w, x, or y
    private static final Pattern cVCNotWXY = Pattern.compile("^(" + C + ")?" + v + "[^aeiouywxy]$");

    public static String processToken(String token) {
        if (token.length() < 3) {
            return token; // token must be at least 3 chars
        }
        token = step1a(token);
        token = step1b(token);
        token = step1c(token);
        token = step2(token);
        token = step3(token);
        token = step4(token);
        token = step5(token);
        System.out.println(token);

        return token;
    }

    public static String step1a(String token) {
        final Map<String, String> step1a = createStep1a();

        for (int i = 0; i < step1a.size(); i++) {

            String prefix = (String) step1a.keySet().toArray()[i];
            String newPrefix = (String) step1a.values().toArray()[i];

            if (token.endsWith(prefix) && !token.endsWith("ss")) {
                token = token.substring(0, token.length() - prefix.length()) + newPrefix;
                break;
            }
        }
        return token;
    }

    public static String step1b(String token) {
        final Map<String, String> step1b = createStep1b();

        for (int i = 0; i < step1b.size(); i++) {

            String suffix = (String) step1b.keySet().toArray()[i];
            String newSuffix = (String) step1b.values().toArray()[i];
            Pattern[] patterns = {mGr0, hasV, hasV};

            if (token.endsWith(suffix)) {
                if (patterns[i].matcher(token.substring(0, token.length() - suffix.length())).matches()) {
                    token = token.substring(0, token.length() - suffix.length()) + newSuffix;
                    if (i > 0) {
                        token = step1bStar(token);
                    }
                }
                break;
            }
        }
        return token;
    }

    public static String step1bStar(String token) {
        final Map<String, String> step1bStar = createStep1bStar();

        for (int i = 0; i < step1bStar.size(); i++) {

            String suffix = (String) step1bStar.keySet().toArray()[i];
            String newSuffix = (String) step1bStar.values().toArray()[i];

            if (token.endsWith(suffix)) {
                token = token.substring(0, token.length() - suffix.length()) + newSuffix;
                return token;
            }
        }
        if (doubleC.matcher(token).matches()) {
            token = token.substring(0, token.length() - 1);
            System.out.println(token);
        } else if (mEq1.matcher(token).matches() && cVCNotWXY.matcher(token).matches()) {
            token += "e";
        }
        return token;
    }

    public static String step1c(String token) {
        if (token.endsWith("y") && hasV.matcher(token).matches()) {
            token = token.substring(0, token.length() - 1) + "i";
        }
        return token;
    }

    public static String step2(String token) {
        final Map<String, String> step2 = createStep2();

        for (int i = 0; i < step2.size(); i++) {

            String suffix = (String) step2.keySet().toArray()[i];
            String newSuffix = (String) step2.values().toArray()[i];

            if (token.endsWith(suffix)) {
                if (mGr0.matcher(token.substring(0, token.length() - suffix.length())).matches()) {
                    token = token.substring(0, token.length() - suffix.length()) + newSuffix;
                }
                break;
            }
        }
        return token;
    }

    public static String step3(String token) {
        final Map<String, String> step3 = createStep3();

        for (int i = 0; i < step3.size(); i++) {

            String suffix = (String) step3.keySet().toArray()[i];
            String newSuffix = (String) step3.values().toArray()[i];

            if (token.endsWith(suffix)) {
                if (mGr0.matcher(token.substring(0, token.length() - suffix.length())).matches()) {
                    token = token.substring(0, token.length() - suffix.length()) + newSuffix;
                }
                break;
            }
        }
        return token;
    }

    public static String step4(String token) {
        final Map<String, String> step4 = createStep4();

        for (int i = 0; i < step4.size(); i++) {

            String suffix = (String) step4.keySet().toArray()[i];
            String newSuffix = (String) step4.values().toArray()[i];

            if (token.endsWith(suffix)) {
                if (mGr1.matcher(token.substring(0, token.length() - suffix.length())).matches()) {
                    token = token.substring(0, token.length() - suffix.length()) + newSuffix;
                }
                break;
            }
        }
        return token;
    }
    
    public static String step5(String token) {

        if(token.endsWith("e") && mGr1.matcher(token.substring(0, token.length() - 1)).matches()){
            return token.substring(0, token.length() - 1);     
        }
        else if(token.endsWith("e") && mEq1.matcher(token.substring(0, token.length() - 1)).matches()
                && cVCNotWXY.matcher(token.substring(0, token.length() - 1)).matches()){
            return token.substring(0, token.length() - 1);     
        }
        else if(token.endsWith("ll") && mGr1.matcher(token.substring(0, token.length() - 1)).matches()){
            return token.substring(0, token.length() - 1);     
        }
        return token;
    }

    private static Map<String, String> createStep1a() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("sses", "ss");
        stem.put("ies", "i");
        stem.put("s", "");
        return Collections.unmodifiableMap(stem);
    }

    private static Map<String, String> createStep1b() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("eed", "ee");
        stem.put("ed", "");
        stem.put("ing", "");
        return Collections.unmodifiableMap(stem);
    }

    private static Map<String, String> createStep1bStar() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("at", "ate");
        stem.put("bl", "ble");
        stem.put("iz", "ize");
        return Collections.unmodifiableMap(stem);
    }

    private static Map<String, String> createStep2() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("ational", "ate");
        stem.put("iveness", "ive");
        stem.put("fulness", "ful");
        stem.put("ization", "ize");
        stem.put("ousness", "ous");
        stem.put("tional", "tion");
        stem.put("biliti", "ble");
        stem.put("entli", "ent");
        stem.put("ousli", "ous");
        stem.put("ation", "ate");
        stem.put("alism", "al");
        stem.put("aviti", "ive");
        stem.put("aliti", "al");
        stem.put("enci", "ence");
        stem.put("anci", "ance");
        stem.put("izer", "ize");
        stem.put("alli", "al");
        stem.put("bli", "ble");
        stem.put("eli", "e");
        return Collections.unmodifiableMap(stem);
    }

    private static Map<String, String> createStep3() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("icate", "ic");
        stem.put("ative", "");
        stem.put("alize", "al");
        stem.put("iciti", "ic");
        stem.put("ical", "ic");
        stem.put("ness", "");
        stem.put("ful", "");
        return Collections.unmodifiableMap(stem);
    }

    private static Map<String, String> createStep4() {
        Map<String, String> stem = new LinkedHashMap<>();
        stem.put("ement", "");
        stem.put("tion", "t");
        stem.put("sion", "s");
        stem.put("ible", "");
        stem.put("able", "");
        stem.put("ence", "");
        stem.put("ment", "");
        stem.put("ance", "");
        stem.put("ant", "");
        stem.put("ent", "");
        stem.put("ism", "");
        stem.put("ate", "");
        stem.put("iti", "");
        stem.put("ous", "");
        stem.put("ive", "");
        stem.put("ize", "");
        stem.put("al", "");
        stem.put("er", "");
        stem.put("ou", "");
        return Collections.unmodifiableMap(stem);
    }
}
