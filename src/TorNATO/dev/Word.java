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

/**
 * A Word object which stores data about a given word and
 * implements the Comparable interface.
 *
 * @author Nathan Waltz
 * @version 0.1
 * @since 2020-12-30
 */
public class Word implements Comparable<Word> {

    // fields storing the String representation
    // of the word, and the score
    private final String word;
    private final int score;

    /**
     * Constructor for the Word object
     *
     * @param word The word which is inputted by
     *             the client as a String
     * @param score The score which corresponds to
     *              to the cumulative sum of all the character's scores
     *              in the word
     */
    public Word(String word, int score) {
        this.word = word;
        this.score = score;
    }

    /**
     * compareTo: Compares one word to another
     *
     * @param other Another Word object that we want to compare
     * @return An integer corresponding to the comparison between
     * the values of these two objects, namely their scores
     */
    @Override
    public int compareTo(Word other) {
        return other.score - this.score;
    }

    /**
     * getWord: Getter for the word
     *
     * @return A string representation of the word
     */
    public String getWord() {
        return word;
    }

    /**
     * getScore: Getter for the score
     *
     * @return An integer representation of the score
     */
    public int getScore() {
        return score;
    }
}
