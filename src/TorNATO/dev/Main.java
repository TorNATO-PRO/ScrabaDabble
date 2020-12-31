/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020. Nathan Waltz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package TorNATO.dev;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * A main method for my Scrabble program. Basically, what this program allows you
 * to do is look up if a given word is a valid scrabble word, or cheat based on the
 * current characters that you hold in your deck!
 *
 * @author Nathan Waltz
 * @version 0.1
 * @since 2020-12-30
 */
public class Main {

    // fields for storing the maximum possible input, a wordlist, and character values
    private static final int MAX_INPUT = 10;
    private static Map<Character, Integer> characterValues;

    /**
     * A dictionary of all the words that are available in scrabble
     */
    public static Map<String, Word> wordList;

    // loads data from files
    static {
        try {
            characterValues = readCharacterValues("CharacterValues.txt");
            wordList = readDictionary("EnglishDictionary.txt");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Main method
     * @param args Unused command line arguments
     */
    public static void main(String[] args) {
        Scanner userIn = new Scanner(System.in);
        System.out.println("Welcome to my scrabble cheating program! Press (:C) to");
        System.out.println("cheat and (:L) to lookup. Also, press (:Q) to quit. Enjoy!");
        for (; ; ) {
            String firstInput = userIn.nextLine();
            if (firstInput.equalsIgnoreCase(":C")) {
                cheat(userIn);
            } else if (firstInput.equalsIgnoreCase(":L")) {
                lookup(userIn);
            } else if (firstInput.equalsIgnoreCase(":Q")) {
                break;
            } else {
                System.out.println("Your input could not be understood!");
            }
            System.out.println();
        }
    }

    /**
     * readDictionary method: Takes in a filename and reads the words quickly
     * using a BufferedReader, and afterwards stores the words in a
     * dictionary for O(1) access time
     *
     * @param filename A filename which contains the path to the dictionary
     * @return A HashMap containing a dictionary which consists of the word as a
     * String and the corresponding Word object
     * @throws IOException If something goes wrong with reading the file, it throws
     * this exception
     */
    public static Map<String, Word> readDictionary(String filename) throws IOException {
        BufferedReader br;
        HashMap<String, Word> wordMap = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(filename));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.length() < 8) {
                    boolean isAlphabetic = true;
                    currentLine = currentLine.toUpperCase();
                    char[] charArray = currentLine.toCharArray();
                    for (char c : charArray) {
                        if (!Character.isAlphabetic(c)) {
                            isAlphabetic = false;
                            break;
                        }
                    }
                    if (isAlphabetic) {
                        wordMap.put(currentLine, new Word(currentLine, score(currentLine)));
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return wordMap;
    }

    /**
     * readDictionary method: Takes in a filename and reads the characters quickly
     * using a BufferedReader, and afterwards stores the words in a
     * dictionary for O(1) access time
     *
     * @param filename A filename which contains the path to the dictionary
     * @return A HashMap containing a dictionary which consists of a character as
     * well as score of that character
     * @throws IOException If something goes wrong with reading the file, it throws
     * this exception
     */
    public static Map<Character, Integer> readCharacterValues(String filename)
            throws IOException {
        BufferedReader br;
        Map<Character, Integer> valueOfCharacter = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(filename));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] values = currentLine.split(",");
                Character character = values[0].trim().charAt(0);
                Integer val = Integer.parseInt(values[1].trim());
                valueOfCharacter.put(character, val);
            }
            br.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return valueOfCharacter;
    }

    /**
     * Cheat method: Allows the user to cheat given a sequence
     * of characters
     *
     * @param userIn A scanner parameter which allows
     *               for the user to enter a character sequence
     */
    public static void cheat(Scanner userIn) {
        System.out.println("Please enter a character sequence");
        String input = userIn.nextLine().toUpperCase();
        if (!isAlphabetic(input) || input.length() > MAX_INPUT) {
            System.out.println("That is not a valid input!");
        } else {
            Set<Word> powerSet = genSubsets(input.toCharArray());
            for (Word word : powerSet) {
                System.out.println("Word: " + word.getWord() + ", Score: " + word.getScore());
            }
        }
    }

    /**
     * Lookup method: Allows the user to see if a given String
     * exists
     *
     * @param userIn A scanner parameter which allows
     *               for the user to enter a word
     */
    public static void lookup(Scanner userIn) {
        System.out.println("Please enter a word");
        String input = userIn.nextLine().toUpperCase();
        if (wordList.containsKey(input)) {
            System.out.println(input + " is a valid word with a score of "
                    + wordList.get(input).getScore() + "!");
        } else {
            System.out.println("Invalid word!");
        }
    }

    /**
     * Checks to see if all of the character in a String
     * are alphabetic
     *
     * @param input A given String provided by the client
     * @return Boolean representing whether or not all of the
     * characters in the input are alphabetic
     */
    private static boolean isAlphabetic(String input) {
        char[] charArray = input.toCharArray();
        for (char c : charArray) {
            if (!Character.isAlphabetic(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a "power set" of characters from a given character
     * array
     *
     * @param word A character array which contains characters for which
     *             we wish to find a subset
     * @return A "power set" of all the characters that are
     * contained in the passed word character array
     */
    public static Set<Word> genSubsets(char[] word) {
        Set<Word> subsets = new TreeSet<>();
        if (word.length == 0) {
            return subsets;
        }
        genSubsets(word, subsets, new Stack<>(), 0);
        return subsets;
    }

    /**
     * A helper method which does the heavy lifting and generates the subsets
     * for the power set
     *
     * @param word A character array which contains characters for which
     *             we wish to find a subset
     * @param subsets A set of subsets of the word parameter
     * @param temp A stack of characters which stores values obtained throughout
     *             the execution of this method
     * @param index The current index
     */
    private static void genSubsets(char[] word, Set<Word> subsets,
                                   Stack<Character> temp, int index) {
        String result = toString(new ArrayList<>(temp));
        if (wordList.containsKey(result)) {
            subsets.add(wordList.get(result));
        }

        // loops through the word and recursively backtracks
        // in a functional paradigm manner
        IntStream.range(index, word.length).forEach(i -> {
            temp.push(word[i]);
            genSubsets(word, subsets, temp, i + 1);
            temp.pop();
        });
    }

    /**
     * Converts a list of characters to a String
     *
     * @param array A list of characters which will be converted to
     *              a string
     * @return A string representation of the passed array parameter
     */
    private static String toString(List<Character> array) {
        if (array.size() == 0) {
            return "";
        }
        StringBuilder temp = new StringBuilder();
        for (Character c : array) {
            temp.append(c);
        }
        return temp.toString();
    }

    /**
     * Finds a score for a given word
     *
     * @param word A word passed by the client
     * @return A score corresponding to the cumulative
     * score of that word
     */
    public static int score(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            score += characterValues.get(c);
        }
        return score;
    }

}