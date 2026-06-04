package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Problem;

//問題データを提供するサービスクラス
//全ての問題の保管場所
@Service
public class ProblemService {

    //全問題リスト
    private final List<Problem> problems;

    public ProblemService() {
        List<Problem> list = new ArrayList<>();

        // correctIndex は 0=A, 1=B, 2=C, 3=D, 4=E
        list.add(new Problem(1, "【問題1】正解はA〜Eのどれ？", 0)); // A
        list.add(new Problem(2, "【問題2】正解はA〜Eのどれ？", 1)); // B
        list.add(new Problem(3, "【問題3】正解はA〜Eのどれ？", 2)); // C
        list.add(new Problem(4, "【問題4】正解はA〜Eのどれ？", 3)); // D
        list.add(new Problem(5, "【問題5】正解はA〜Eのどれ？", 4)); // E
        list.add(new Problem(6, "【問題6】正解はA〜Eのどれ？", 0)); // A

        this.problems = list;
    }

    //全問題を取得
    public List<Problem> findAll() {
    	//
        return problems;
    }

    /**
     * IDで問題を1件取得します。
     *
     * 「分かりやすさ優先」で、まずはシンプルに for 文で探します。
     * 問題数が増えて効率が気になったら、Map化（id→Problem）にすると高速になります。
     */
    public Problem findById(int id) {
        for (Problem p : problems) {
            if (p.getId() == id) {
                return p;
            }
        }
        // 今回は例外で落として「データがおかしい」ことが分かるようにしています
        throw new IllegalArgumentException("Problem not found. id=" + id);
    }
}