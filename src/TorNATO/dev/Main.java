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
 * A main method for my Scrabble program. Basically, what this program allows you to do is look up
 * if a given word is a valid scrabble word, or cheat based on the current characters that you hold
 * in your deck!
 *
 * @author Nathan Waltz
 * @version 0.1
 * @since 2020-12-30
 */
public class Main {

  // fields for storing the maximum possible input, a wordlist, and character values
  private static final int MAX_INPUT = 15; // size of scrabble board
  /** A dictionary of all the words that are available in scrabble */
  public static Map<String, Word> wordList;
  private static Map<Character, Integer> characterValues;

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
   *
   * @param args Unused command line arguments
   */
  public static void main(String[] args) {
    Scanner userIn = new Scanner(System.in);
    System.out.println("Welcome to my scrabble cheating program! Press (:C) to");
    System.out.println("cheat and (:L) to lookup. Also, press (:Q) to quit. Enjoy!");
    boolean stop = false;
    do {
      String firstInput = userIn.nextLine();
      if (firstInput.equalsIgnoreCase(":C")) {
        cheat(userIn);
      } else if (firstInput.equalsIgnoreCase(":L")) {
        lookup(userIn);
      } else if (firstInput.equalsIgnoreCase(":Q")) {
        stop = true;
      } else {
        System.out.println("Your input could not be understood!");
      }
      System.out.println();
    } while (!stop);
  }

  /**
   * readDictionary method: Takes in a filename and reads the words quickly using a BufferedReader,
   * and afterwards stores the words in a dictionary for O(1) access time
   *
   * @param filename A filename which contains the path to the dictionary
   * @return A HashMap containing a dictionary which consists of the word as a String and the
   *     corresponding Word object
   * @throws IOException If something goes wrong with reading the file, it throws this exception
   */
  public static Map<String, Word> readDictionary(String filename) throws IOException {
    BufferedReader br;
    Map<String, Word> wordMap = new TreeMap<>();
    try {
      br = new BufferedReader(new FileReader(filename));
      String currentLine;
      while ((currentLine = br.readLine()) != null) {
        validateWordlistLine(currentLine, wordMap);
      }
      br.close();
    } catch (FileNotFoundException exception) {
      exception.printStackTrace();
    }
    return wordMap;
  }

  /**
   * A helper method to verify that the input from the wordlist is indeed a valid word that fits
   * within the constraints of this program
   *
   * @param line A line that is provided by the client that they wish to be validated
   * @param wordMap A word map that acts as a dictionary-like data structure
   */
  private static void validateWordlistLine(String line, Map<String, Word> wordMap) {
    String toUpperCase = line.toUpperCase();
    if (toUpperCase.length() <= MAX_INPUT && isAlphabetic(toUpperCase)) {
      wordMap.put(toUpperCase, new Word(toUpperCase, score(toUpperCase)));
    }
  }

  /**
   * readDictionary method: Takes in a filename and reads the characters quickly using a
   * BufferedReader, and afterwards stores the words in a dictionary for O(1) access time
   *
   * @param filename A filename which contains the path to the dictionary
   * @return A HashMap containing a dictionary which consists of a character as well as score of
   *     that character
   * @throws IOException If something goes wrong with reading the file, it throws this exception
   */
  public static Map<Character, Integer> readCharacterValues(String filename) throws IOException {
    BufferedReader br;
    Map<Character, Integer> valueOfCharacter = new TreeMap<>();
    try {
      br = new BufferedReader(new FileReader(filename));
      String currentLine;
      while ((currentLine = br.readLine()) != null) {
        parseCharacterValues(currentLine, valueOfCharacter);
      }
      br.close();
    } catch (FileNotFoundException exception) {
      exception.printStackTrace();
    }
    return valueOfCharacter;
  }

  /**
   * Parses the character file to obtain the scores for those characters
   *
   * @param line The line passed by a client which is intended to be parsed
   * @param valueOfCharacter A dictionary-like data structure containing all of the characters in
   *     the file as well as their corresponding integer value
   */
  private static void parseCharacterValues(String line, Map<Character, Integer> valueOfCharacter) {
    String[] values = line.split(",");
    Character character = values[0].trim().charAt(0);
    Integer val = Integer.parseInt(values[1].trim());
    valueOfCharacter.put(character, val);
  }

  /**
   * Cheat method: Allows the user to cheat given a sequence of characters
   *
   * @param userIn A scanner parameter which allows for the user to enter a character sequence
   */
  public static void cheat(Scanner userIn) {
    boolean stop = false;
    do {
      System.out.println("Please enter a character sequence (Type :E to exit)");
      String input = userIn.nextLine().toUpperCase();
      if (input.equalsIgnoreCase(":E")) {
        stop = true;
      } else if (!isAlphabetic(input) || input.length() > MAX_INPUT) {
        System.out.println("That is not a valid input!");
      } else if (input.length() <= 10) {
        System.out.println(input.toCharArray());
        long time1 = System.currentTimeMillis();
        Queue<Word> combos = genCombos(input.toCharArray());
        long time2 = System.currentTimeMillis();
        System.out.println(
            "Time to execute with input of length "
                + input.length()
                + " = "
                + (time2 - time1)
                + " milliseconds");
        while (!combos.isEmpty()) {
          Word word = combos.remove();
          System.out.println("Word: " + word.getWord() + ", Score: " + word.getScore());
        }
      } else {
        System.out.println("Your input is too long!");
      }
    } while (!stop);
  }

  /**
   * Lookup method: Allows the user to see if a given String exists
   *
   * @param userIn A scanner parameter which allows for the user to enter a word
   */
  public static void lookup(Scanner userIn) {
    System.out.println("Please enter a word");
    String input = userIn.nextLine();
    String upperCaseInput = input.toUpperCase();
    if (wordList.containsKey(upperCaseInput)) {
      System.out.println(
          input
              + " is a valid word with a score of "
              + wordList.get(upperCaseInput).getScore()
              + "!");
    } else {
      System.out.println("Invalid word!");
    }
  }

  /**
   * Checks to see if all of the character in a String are alphabetic
   *
   * @param input A given String provided by the client
   * @return Boolean representing whether or not all of the characters in the input are alphabetic
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
   * Generates a combinations of characters from a given character array
   *
   * @param word A character array which contains characters for which we wish to find combinations
   * @return Combination of all the characters that are contained in the passed word character array
   */
  public static Queue<Word> genCombos(char[] word) {
    Queue<Word> subsets = new PriorityQueue<>();
    if (word.length == 0) {
      return subsets;
    }
    int[] characterCount = characterCount(word);
    genCombos(word, subsets, new Stack<>(), new TreeSet<>(), characterCount, new int[26]);
    return subsets;
  }

  /**
   * A helper method which does the heavy lifting and generates the combinations of the power set
   *
   * @param word A character array which contains characters for which we wish to find a combination
   * @param subsets A set of subsets of the word parameter
   * @param temp A stack of characters which stores values obtained throughout the execution of this
   *     method
   * @param words A set which contains the Strings generated through the duration of this method's
   *     recursion
   */
  private static void genCombos(
      char[] word,
      Queue<Word> subsets,
      Stack<Character> temp,
      Set<String> words,
      int[] wordCharacterCount,
      int[] tempCharacterCount) {
    // Uses hashset to maintain O(1) time in checking validity
    // NOTE: I cannot simply use a TreeSet, becomes I am comparing
    // objects based on score
    String result = toString(new ArrayList<>(temp));
    if (wordList.containsKey(result) && !words.contains(result)) {
      subsets.add(wordList.get(result));
      words.add(result);
    }

    // loops through the word and recursively backtracks
    // in a functional paradigm manner
    IntStream.range(0, word.length)
        .forEach(
            i -> {
              char c = word[i];
              if (wordCharacterCount[characterToInt(c)] > tempCharacterCount[characterToInt(c)]) {
                temp.push(c);
                tempCharacterCount[characterToInt(c)] += 1;
                genCombos(word, subsets, temp, words, wordCharacterCount, tempCharacterCount);
                tempCharacterCount[characterToInt(temp.pop())] -= 1;
              }
            });
  }

  private static int characterToInt(char c) {
    return c - 65;
  }

  /**
   * Converts a list of characters to a String
   *
   * @param array A list of characters which will be converted to a string
   * @return A string representation of the passed array parameter
   */
  private static String toString(List<Character> array) {
    if (array.isEmpty()) {
      return "";
    }
    StringBuilder temp = new StringBuilder();
    array.forEach(temp::append);
    return temp.toString();
  }

  /**
   * Finds a score for a given word
   *
   * @param word A word passed by the client
   * @return A score corresponding to the cumulative score of that word
   */
  public static int score(String word) {
    return IntStream.range(0, word.length()).map(i -> characterValues.get(word.charAt(i))).sum();
  }

  private static int[] characterCount(char[] sequence) {
    int[] count = new int[26];
    for (char c : sequence) {
      count[characterToInt(c)] += 1;
    }
    return count;
  }
}
