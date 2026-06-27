// package com.example.demo.service;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.example.demo.model.Problem;

// //問題データを提供するサービスクラス
// //全ての問題の保管場所
// @Service
// public class ProblemService {

//     //全問題リスト
//     private final List<Problem> problems;

//     public ProblemService() {
//         List<Problem> list = new ArrayList<>();

//         // correctIndex は 0=資産, 1=負債, 2=純資産, 3=収益, 4=費用 を表す
//         list.add(new Problem(1, "現金", 0));
//         list.add(new Problem(2, "買掛金", 1));
//         list.add(new Problem(3, "売掛金", 0));
//         list.add(new Problem(4, "受取利息", 3));
//         list.add(new Problem(5, "給料", 4));
//         list.add(new Problem(6, "繰越利益剰余金", 2));

//         this.problems = list;
//     }

//     //全問題を取得
//     public List<Problem> findAll() {
//     	//
//         return problems;
//     }

//     // IDで問題を1件取得
//     public Problem findById(int id) {
//         for (Problem p : problems) {
//             if (p.getId() == id) {
//                 return p;
//             }
//         }
        
//         throw new IllegalArgumentException("Problem not found. id=" + id);
//     }
// }