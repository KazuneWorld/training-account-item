package com.example.demo.model;
import lombok.Value;


// 問題を表すクラス
// 問題ID、問題文、正解の選択肢を持ちます。
@Value
public class Problem {
	// 問題識別ID
    int id;
    // 問題文
    String questionText;
    // 答えの選択肢
    int correctIndex;
}