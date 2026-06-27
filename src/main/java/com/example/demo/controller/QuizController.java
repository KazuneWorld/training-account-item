package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.entity.Question;
import com.example.demo.repository.BokiMapper;
import com.example.demo.session.QuizSession;

import lombok.RequiredArgsConstructor;

@Controller
@SessionAttributes("quizSession") // quizSession をセッションに保存する指定
@RequiredArgsConstructor
public class QuizController {

    //
    private final BokiMapper bokiMapper;

    // quizSession をセッションに保存するための初期化メソッド
    @ModelAttribute("quizSession")
    public QuizSession createQuizSession() {
        return new QuizSession();
    }

    // タイトル画面
    @GetMapping("/")
    public String showTitle() {
        return "title";
    }

    // クイズ開始処理
    @PostMapping("/start")
    public String startQuiz(@ModelAttribute("quizSession") QuizSession session) {
        // セッション初期化
        session.resetForStart();

        // 全問題IDを取得
        List<Question> allProblems = bokiMapper.findAll();
        //問題IDのリストを定義
        List<Integer> ids = new ArrayList<>();
        // 問題IDをリストに追加していく
        for (Question p : allProblems) {
            ids.add(p.getId());
        }

        // IDをランダムに並べ替え（シャッフル）
        Collections.shuffle(ids);

        // 出題数の設定:5問（足りなければあるだけ出題）
        int questionCount = 5;
        if (ids.size() < 5) {
            questionCount = ids.size();
        }

        // 出題順リスト（先頭からquestionCount件を採用）
        List<Integer> order = new ArrayList<>();
        // questionCount件分のIDを出題順リストに追加
        for (int i = 0; i < questionCount; i++) {
            order.add(ids.get(i));
        }
        // セッションに出題順を保存（確定）
        session.setQuestionOrder(order);

        // 1問目へ
        return "redirect:/quiz";
    }

    // クイズ画面表示
    @GetMapping("/quiz")
    public String showQuiz(@ModelAttribute("quizSession") QuizSession session, Model model) {

        // すべての問題を解き終わっていたらリザルトへ
        if (session.isFinished()) {
            return "redirect:/result";
        }

        // 現在の問題を取得
        int problemId = session.getCurrentProblemId();
        Question problem = bokiMapper.findById(problemId);

        System.out.println("DBから取得した問題: " + bokiMapper.findById(problemId));

        // 画面に渡すデータ
        model.addAttribute("problem", problem);
        // model.addAttribute("choices", Choices.FIXED_CHOICES);

        // 表示用（何問目 / 全何問）
        model.addAttribute("index", session.getDisplayQuestionNumber());
        model.addAttribute("total", session.getTotalQuestionCount());

        return "quiz";
    }

    // 回答処理
    @PostMapping("/quiz/answer")
    public String answer(@ModelAttribute("quizSession") QuizSession session,
                         @RequestParam("selectedAnswer") String selectedAnswer) {

        // 今の問題を取得
        int problemId = session.getCurrentProblemId();
        Question problem = bokiMapper.findById(problemId);

        // 正誤判定
        String correctAnswer = problem.getAnswer();
        boolean isCorrect = (selectedAnswer.equals(correctAnswer));

        // 正解なら正解数を増やす
        if (isCorrect) {
            session.setCorrectCount(session.getCorrectCount() + 1);
        }

        // 回答後表示に必要な情報をセッションへ保存
        session.setAnswered(true);
        session.setLastCorrect(isCorrect);
        session.setLastSelectedAnswer(selectedAnswer);
        session.setLastCorrectAnswer(correctAnswer);

        // 結果表示のため、同じ画面へ戻す
        return "redirect:/quiz";
    }

    // 次の問題へ進む処理
    @PostMapping("/quiz/next")
    public String next(@ModelAttribute("quizSession") QuizSession session) {

        // 次の問題へ進める
        session.setCurrentIndex(session.getCurrentIndex() + 1);

        // 次の問題用に「回答状態」だけ初期化
        session.resetAnswerState();

        // 終了していたらリザルトへ
        if (session.isFinished()) {
            return "redirect:/result";
        }
        // 次の問題へ
        return "redirect:/quiz";
    }

    // リザルト画面
    @GetMapping("/result")
    public String showResult(@ModelAttribute("quizSession") QuizSession session, Model model) {
    	
        model.addAttribute("correct", session.getCorrectCount());
        model.addAttribute("total", session.getTotalQuestionCount());
        
        return "result";
    }

    //タイトルに戻るボタン：
    // セッション上の quizSession を破棄して、次回は新規スタートになるようにします。
    @PostMapping("/reset")
    public String reset(SessionStatus status) {
        status.setComplete(); // @SessionAttributes の内容を破棄
        return "redirect:/";
    }
}