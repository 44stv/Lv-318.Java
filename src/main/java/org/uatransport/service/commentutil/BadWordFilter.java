package org.uatransport.service.commentutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BadWordFilter {

    private static Map<String, String[]> wordsMap = new HashMap<>();
    public static Map<String, String[]> wordsToFilterMap = new HashMap<>();
    private static  Map<Character , String[]> tranlitMap = new HashMap<>();

    private static int largestWordLength = 0;

    public static void loadConfigs() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("files/badWords.txt").getInputStream()));
            BufferedReader transliterationReader = new BufferedReader(new InputStreamReader(new ClassPathResource("files/transliteration.txt").getInputStream()));
            String line;
            int counter = 0;
            log.debug("Inside bad word");
            while ((line = reader.readLine()) != null) {
                String[] content;
                try {
                    content = line.split(",");
                    if (content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore_in_combination_with_words = new String[]{};
                    if (content.length > 1) {
                        ignore_in_combination_with_words = content[1].split("_");
                    }

                    if (word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    wordsMap.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            while ((line = transliterationReader.readLine()) != null) {
                String[] letters = line.split("=");
                String[] translittedLetters = letters[1].trim().split(",");
                tranlitMap.put(letters[0].toCharArray()[0], translittedLetters);
            }

            for (String word : wordsMap.keySet()) {
                char [] letters = word.toCharArray();
                for (char letter: letters) {
                    if(tranlitMap.containsKey(letter)){
                       String []  traslits = tranlitMap.get(letter);
                       for ( String translit : traslits ) {
                           wordsToFilterMap.put(word.replace(String.valueOf(letter), translit), null);
                       }
                    }
                }
            }


            System.out.println("Loaded " + counter + " words to filter out");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).

     */
    public static ArrayList<String> filterBadWords(String input) {
        if (input == null) {
            return new ArrayList<>();
        }

        ArrayList<String> badWords = new ArrayList<>();
        input = input.toLowerCase().replaceAll("[^a-zA-Zа-яА-ЯёЁіІ]", "");

        // iterate over each letter in the word
        for (int start = 0; start < input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
            for (int offset = 1; offset < (input.length() + 1 - start) && offset <= largestWordLength; offset++) {
                String wordToCheck = input.substring(start, start + offset);
                if (wordsToFilterMap.containsKey(wordToCheck)) {
                    // for example, if you want to say the word bass, that should be possible.
                    String[] ignoreCheck = wordsToFilterMap.get(wordToCheck);
                    boolean ignore = false;
                    for (int s = 0; s < ignoreCheck.length; s++) {
                        if (input.contains(ignoreCheck[s])) {
                            ignore = true;
                            break;
                        }
                    }
                    if (!ignore) {
                        badWords.add(wordToCheck);
                    }
                }
            }
        }

        return badWords;

    }

}
