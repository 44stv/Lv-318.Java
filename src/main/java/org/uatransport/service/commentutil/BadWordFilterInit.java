package org.uatransport.service.commentutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BadWordFilterInit {

    private static Map<String, String[]> wordsMap = new HashMap<>();
    static Map<String, String[]> wordsToFilterMap = new HashMap<>();
    private static Map<Character, String[]> translitLettersMap = new HashMap<>();

    static int largestWordLength = 0;

    @PostConstruct
    public static void loadConfigs() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("files/badWords.txt").getInputStream()));
        BufferedReader transliterationReader = new BufferedReader(new InputStreamReader(new ClassPathResource("files/transliteration.txt").getInputStream()));
        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null) {

            String[] content = line.split(",");
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
            counter++;


        }

        while ((line = transliterationReader.readLine()) != null) {
            String[] letters = line.split("=");
            String[] translittedLetters = letters[1].split(",");
            translitLettersMap.put(letters[0].trim().toCharArray()[0], translittedLetters);
        }

        for (String word : wordsMap.keySet()) {
            char[] letters = word.toCharArray();
            for (char letter : letters) {
                if (translitLettersMap.containsKey(letter)) {
                    String[] traslits = translitLettersMap.get(letter);
                    for (String translit : traslits) {
                        wordsToFilterMap.put(word.replace(String.valueOf(letter).trim(), translit.trim()), null);
                    }
                }
            }
        }

        wordsToFilterMap.putAll(wordsMap);
        log.debug("Loaded " + counter + " to words to filter out");

    }
}
