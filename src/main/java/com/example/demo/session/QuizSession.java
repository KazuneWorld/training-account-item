package com.example.demo.session;

import java.util.ArrayList;
import java.util.List;

/**
 * クイズ進行状況（セッションに保持するデータ）をまとめたクラスです。
 *
 * 目的：
 * - 「いま何問目か」
 * - 「どの問題を出題済みか（重複禁止）」
 * - 「正解数はいくつか」
 * - 「直前の回答の正誤や、正解はどれか」
 *
 * を覚えておき、画面遷移してもクイズの続きができるようにします。
 *
 * ※「分かりやすさ優先」で、必要な情報をそのままフィールドとして持ちます。
 */
public class QuizSession {

    // =========================
    // 出題順の管理（重複禁止）
    // =========================

    /**
     * 出題する問題IDの順番リスト（例：[3, 1, 5, 2, 6]）
     * /start でシャッフルして作り、ここに入れます。
     */
    private List<Integer> questionOrder = new ArrayList<>();

    /**
     * 現在が何問目か（0始まり）
     * 0なら1問目、1なら2問目... という意味です。
     */
    private int currentIndex = 0;

    // =========================
    // スコア
    // =========================

    /** 正解数 */
    private int correctCount = 0;

    // =========================
    // 「回答後の表示」のための情報
    // =========================

    /**
     * いま表示している問題に対して、回答済みかどうか。
     * - false：まだ回答していない（A〜Eボタンを表示する）
     * - true：回答済み（正誤・正解・次へボタンを表示する）
     */
    private boolean answered = false;

    /** 直前の回答が正解なら true、不正解なら false（未回答のときは意味を持たない） */
    private boolean lastCorrect = false;

    /**
     * 直前に選んだ選択肢（0..4）
     * 0=A, 1=B, 2=C, 3=D, 4=E
     */
    private int lastSelectedIndex = 0;

    /**
     * 正解の選択肢（0..4）
     * 0=A, 1=B, 2=C, 3=D, 4=E
     */
    private int lastCorrectIndex = 0;

    // =========================
    // getter / setter
    // =========================

    public List<Integer> getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(List<Integer> questionOrder) {
        this.questionOrder = questionOrder;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isLastCorrect() {
        return lastCorrect;
    }

    public void setLastCorrect(boolean lastCorrect) {
        this.lastCorrect = lastCorrect;
    }

    public int getLastSelectedIndex() {
        return lastSelectedIndex;
    }

    public void setLastSelectedIndex(int lastSelectedIndex) {
        this.lastSelectedIndex = lastSelectedIndex;
    }

    public int getLastCorrectIndex() {
        return lastCorrectIndex;
    }

    public void setLastCorrectIndex(int lastCorrectIndex) {
        this.lastCorrectIndex = lastCorrectIndex;
    }

    // =========================
    // 便利メソッド（Controllerから使う想定）
    // =========================

    /**
     * クイズを最初から開始するために、状態を初期化します。
     * /start で呼び出す想定です。
     */
    public void resetForStart() {
        this.questionOrder = new ArrayList<>();
        this.currentIndex = 0;
        this.correctCount = 0;

        resetAnswerState();
    }

    /**
     * 1問分の「回答状態」だけを初期化します。
     * 次の問題へ進むとき（/quiz/next）に呼び出す想定です。
     */
    public void resetAnswerState() {
        this.answered = false;
        this.lastCorrect = false;
        this.lastSelectedIndex = 0;
        this.lastCorrectIndex = 0;
    }

    /**
     * 現在の問題IDを返します。
     * ※questionOrderが空の場合は呼ばない想定（Controller側でチェック）
     */
    public int getCurrentProblemId() {
        return questionOrder.get(currentIndex);
    }

    /**
     * いまが最終問題まで終わった状態かどうかを返します。
     * - true なら /result へ
     * - false なら /quiz を続行
     */
    public boolean isFinished() {
        return currentIndex >= questionOrder.size();
    }

    /**
     * 現在が何問目（1始まり）かを返します。表示用。
     * 例：currentIndex=0 → 1問目
     */
    public int getDisplayQuestionNumber() {
        return currentIndex + 1;
    }

    /**
     * 全部で何問出題するか。表示用。
     *（通常5問）
     */
    public int getTotalQuestionCount() {
        return questionOrder.size();
    }
}