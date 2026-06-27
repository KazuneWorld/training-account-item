package com.example.demo.session;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//クイズ進行状況をセッションに保持するためのクラス
@Getter
@Setter
@NoArgsConstructor
// クイズの進行状況をセッションに保持するためのクラス
public class QuizSession {

    //出題する問題のIDの順番
    private List<Integer> questionOrder = new ArrayList<>();
    // 現在の問題番号（0始まり）
    private int currentIndex = 0;
    // これまでの正解数
    private int correctCount = 0;
    // 直前の問題に回答済みかどうか
    private boolean answered = false;
    // 直前の回答が正解なら true、不正解なら false
    private boolean lastCorrect = false;
    // 直前の選択肢
    private String lastSelectedAnswer = null;
    // 直前の正解の選択肢
    private String lastCorrectAnswer = null;
    // エンドレスモードかどうか
    private boolean endlessMode = false;


    // クイズ開始時にセッションを初期化する
    public void resetForStart() {
        this.questionOrder = new ArrayList<>();
        this.currentIndex = 0;
        this.correctCount = 0;

        resetAnswerState();
    }

    //1問分の解答状態をリセット
    public void resetAnswerState() {
        this.answered = false;
        this.lastCorrect = false;
        this.lastSelectedAnswer = null;
        this.lastCorrectAnswer = null;
    }

    //現在の問題ID
    public int getCurrentProblemId() {
        return questionOrder.get(currentIndex);
    }

    //今の問題が最後の問題かどうかを返す
    public boolean isFinished() {
        return currentIndex >= questionOrder.size();
    }

    /// 現在の問題番号（1始まり）
    public int getDisplayQuestionNumber() {
        return currentIndex + 1;
    }

    /// 総出題数
    public int getTotalQuestionCount() {
        return questionOrder.size();
    }
}