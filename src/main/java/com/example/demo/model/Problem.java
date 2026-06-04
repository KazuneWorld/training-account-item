package com.example.demo.model;

/**
 * 1問分の問題データを表します。
 *
 * このアプリでは選択肢（A/B/C/D/E）は全問題で共通なので、
 * Problemが持つのは「問題文」と「正解がどれか（0〜4）」だけです。
 */
public class Problem {

    // 問題識別ID（重複しない想定）
    private final int id;

    // 問題文
    private final String questionText;

    //正解の選択肢番号
    //0:A, 1:B, 2:C, 3:D, 4:E
    private final int correctIndex;

    //コンストラクタ
    public Problem(int id, String questionText, int correctIndex) {
        this.id = id;
        this.questionText = questionText;
        this.correctIndex = correctIndex;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}