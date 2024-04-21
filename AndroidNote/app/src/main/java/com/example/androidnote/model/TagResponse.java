package com.example.androidnote.model;

import java.util.List;

/**
 * {
 *     "results": [
 *         {
 *             "score": "0.2690834070687418",
 *             "word": "操作系统"
 *         },
 *         {
 *             "score": "0.26713747058514653",
 *             "word": "知识"
 *         }
 *     ],
 *     "log_id": "1782074151486105460"
 * }
 */
public class TagResponse {

    private List<Results> results;
    private String log_id;
    public void setResults(List<Results> results) {
        this.results = results;
    }
    public List<Results> getResults() {
        return results;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
    public String getLog_id() {
        return log_id;
    }

    public class Results {

        private String score;
        private String word;
        public void setScore(String score) {
            this.score = score;
        }
        public String getScore() {
            return score;
        }

        public void setWord(String word) {
            this.word = word;
        }
        public String getWord() {
            return word;
        }

    }
}