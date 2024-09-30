
package org.sportradar.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Repository implements IRepository {
    private List<String> words;
    private Random random;

    public Repository() {
        words = new ArrayList<>();
        random = new Random();

        try (InputStream is = getClass().getResourceAsStream("/correctWords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Resource file not found!");
        }
    }

    @Override
    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("Word list is empty");
        }
        int index = random.nextInt(words.size());
        return words.get(index);
    }
}