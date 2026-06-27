package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// クラスのフィールドに対して、ゲッター、セッター、toString、equals、hashCodeなどのメソッドを自動生成するためのLombokアノテーションです。
@Data
// 引数なしのコンストラクタを自動生成するためのLombokアノテーションです。
@NoArgsConstructor
// すべてのフィールドを引数に持つコンストラクタを自動生成するためのLombokアノテーションです。
@AllArgsConstructor
public class Question {
	//ID
	private Integer id;
	//問題文
	private String question;
	//正解番号
	private Integer answer;
}