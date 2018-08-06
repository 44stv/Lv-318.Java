package org.uatransport.service.commentutil;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class BadWordFilter {

    private static Map<String, String[]> wordsToFilterMap = BadWordFilterInit.wordsToFilterMap;
    private static int largestWordLength = BadWordFilterInit.largestWordLength;

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
                    for (String s : ignoreCheck) {
                        if (input.contains(s)) {
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
