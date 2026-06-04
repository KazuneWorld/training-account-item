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

import com.example.demo.model.Choices;
import com.example.demo.model.Problem;
import com.example.demo.service.ProblemService;
import com.example.demo.session.QuizSession;

/**
 * 画面の遷移と、回答処理を担当するコントローラです。
 *
 * 画面：
 * - タイトル：GET /
 * - 問題：GET /quiz
 * - リザルト：GET /result
 *
 * 操作：
 * - 開始：POST /start
 * - 回答：POST /quiz/answer
 * - 次へ：POST /quiz/next
 * - リセット：POST /reset
 */
@Controller
@SessionAttributes("quizSession") // quizSession をセッションに保存する指定
public class QuizController {

    private final ProblemService problemService;

    public QuizController(ProblemService problemService) {
        this.problemService = problemService;
    }

    /**
     * セッションに quizSession が無いとき、新規作成するためのメソッドです。
     * （最初にアクセスしたときなど）
     */
    @ModelAttribute("quizSession")
    public QuizSession createQuizSession() {
        return new QuizSession();
    }

    // =========================
    // タイトル画面
    // =========================

    @GetMapping("/")
    public String showTitle() {
        return "title";
    }

    /**
     * スタートボタン押下時：
     * - 出題順（問題IDリスト）をランダムに決める（重複なし）
     * - セッションを初期化
     * - 1問目の画面へ
     */
    @PostMapping("/start")
    public String startQuiz(@ModelAttribute("quizSession") QuizSession session) {
        // セッション初期化
        session.resetForStart();

        // 全問題IDを取得
        List<Problem> allProblems = problemService.findAll();
        List<Integer> ids = new ArrayList<>();
        for (Problem p : allProblems) {
            ids.add(p.getId());
        }

        // ランダムに並べ替え（シャッフル）
        Collections.shuffle(ids);

        // 今回は5問出題（ただし問題が5件未満ならある分だけ）
        int questionCount = 5;
        if (ids.size() < 5) {
            questionCount = ids.size();
        }

        // 出題順リスト（先頭からquestionCount件を採用）
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < questionCount; i++) {
            order.add(ids.get(i));
        }
        session.setQuestionOrder(order);

        // 1問目へ
        return "redirect:/quiz";
    }

    // =========================
    // 問題画面
    // =========================

    /**
     * 問題画面表示
     * - 未開始ならタイトルへ戻す
     * - 終了済みならリザルトへ
     * - それ以外は現在の問題を表示
     */
    @GetMapping("/quiz")
    public String showQuiz(@ModelAttribute("quizSession") QuizSession session, Model model) {

        // クイズが開始されていない（出題順がない）場合はタイトルへ
        if (session.getQuestionOrder() == null || session.getQuestionOrder().isEmpty()) {
            return "redirect:/";
        }

        // すべての問題を解き終わっていたらリザルトへ
        if (session.isFinished()) {
            return "redirect:/result";
        }

        // 現在の問題を取得
        int problemId = session.getCurrentProblemId();
        Problem problem = problemService.findById(problemId);

        // 画面に渡すデータ
        model.addAttribute("problem", problem);
        model.addAttribute("choices", Choices.FIXED_CHOICES);

        // 表示用（何問目 / 全何問）
        model.addAttribute("index", session.getDisplayQuestionNumber());
        model.addAttribute("total", session.getTotalQuestionCount());

        return "quiz";
    }

    /**
     * 回答処理：
     * - すでに回答済みなら無視（リロード対策）
     * - 正誤判定してセッションに保存
     * - 同じ /quiz を表示（結果エリアに切り替える）
     */
    @PostMapping("/quiz/answer")
    public String answer(@ModelAttribute("quizSession") QuizSession session,
                         @RequestParam("selectedIndex") int selectedIndex) {

        // 未開始ならタイトルへ（通常は起きないが保険）
        if (session.getQuestionOrder() == null || session.getQuestionOrder().isEmpty()) {
            return "redirect:/";
        }

        // 二重回答防止：回答済みなら何もしない
        if (session.isAnswered()) {
            return "redirect:/quiz";
        }

        // 今の問題を取得
        int problemId = session.getCurrentProblemId();
        Problem problem = problemService.findById(problemId);

        // 正誤判定
        int correctIndex = problem.getCorrectIndex();
        boolean isCorrect = (selectedIndex == correctIndex);

        // 正解なら正解数を増やす
        if (isCorrect) {
            session.setCorrectCount(session.getCorrectCount() + 1);
        }

        // 回答後表示に必要な情報をセッションへ保存
        session.setAnswered(true);
        session.setLastCorrect(isCorrect);
        session.setLastSelectedIndex(selectedIndex);
        session.setLastCorrectIndex(correctIndex);

        // 結果表示のため、同じ画面へ戻す
        return "redirect:/quiz";
    }

    /**
     * 次の問題へ：
     * - 回答前に押されたらそのまま（安全策）
     * - 回答済みなら次へ進める
     * - 最後まで終わったらリザルトへ
     */
    @PostMapping("/quiz/next")
    public String next(@ModelAttribute("quizSession") QuizSession session) {

        // 未開始ならタイトルへ
        if (session.getQuestionOrder() == null || session.getQuestionOrder().isEmpty()) {
            return "redirect:/";
        }

        // 回答していないのに次へは押せない想定（保険）
        if (!session.isAnswered()) {
            return "redirect:/quiz";
        }

        // 次の問題へ進める
        session.setCurrentIndex(session.getCurrentIndex() + 1);

        // 次の問題用に「回答状態」だけ初期化
        session.resetAnswerState();

        // 終了していたらリザルトへ
        if (session.isFinished()) {
            return "redirect:/result";
        }

        return "redirect:/quiz";
    }

    // =========================
    // リザルト画面
    // =========================

    @GetMapping("/result")
    public String showResult(@ModelAttribute("quizSession") QuizSession session, Model model) {

        // 未開始ならタイトルへ
        if (session.getQuestionOrder() == null || session.getQuestionOrder().isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("correct", session.getCorrectCount());
        model.addAttribute("total", session.getTotalQuestionCount());

        return "result";
    }

    // =========================
    // リセット（タイトルに戻る）
    // =========================

    /**
     * タイトルに戻るボタン：
     * セッション上の quizSession を破棄して、次回は新規スタートになるようにします。
     */
    @PostMapping("/reset")
    public String reset(SessionStatus status) {
        status.setComplete(); // @SessionAttributes の内容を破棄
        return "redirect:/";
    }
}