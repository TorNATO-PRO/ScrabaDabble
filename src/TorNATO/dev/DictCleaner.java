/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2021.  $author
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

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class DictCleaner {

  public static void main(String[] args) throws IOException {
    System.out.println("Beginning process");
    Set<String> words = new TreeSet<>();
    long time1 = System.currentTimeMillis();
    readFile("american-english", words);
    readFile("british-english", words);
    long time2 = System.currentTimeMillis();
    System.out.println(
      "Time to read files: " + (time2 - time1) + " milliseconds"
    );
    System.out.println("Number of words: " + words.size());
    String filename = "Cleansed_English_Dictionary";
    System.out.println("Outputting new file, " + filename);
    outputFile(filename, words);
    long time3 = System.currentTimeMillis();
    System.out.println(
      "Total execution time: " + (time3 - time1) + " milliseconds"
    );
    File file = new File(filename);
    double fileSize = file.length() >> 10;
    System.out.println(
      "Output file size of " + filename + "= " + fileSize + " kilobytes"
    );
  }

  public static void readFile(String fileName, Set<String> words)
    throws IOException {
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(fileName));
      String currentLine;
      while ((currentLine = br.readLine()) != null) {
        if (isAllAlphabetic(currentLine) && currentLine.length() <= 15) {
          words.add(currentLine);
        }
      }
      br.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static void outputFile(String filename, Set<String> words)
    throws FileNotFoundException {
    BufferedOutputStream bos = new BufferedOutputStream(
      new FileOutputStream(filename)
    );
    try {
      for (String s : words) {
        bos.write(s.getBytes());
        bos.write(System.lineSeparator().getBytes());
      }
      bos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isAllAlphabetic(String currentLine) {
    for (Character c : currentLine.toCharArray()) {
      if (!Character.isAlphabetic(c)) {
        return false;
      }
    }
    return true;
  }
}
