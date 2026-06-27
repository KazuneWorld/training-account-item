-- テーブルが存在したら削除する
DROP TABLE IF EXISTS boki;

-- テーブルの作成
CREATE TABLE boki (
	-- id(することID):主キー、自動採番
	id INT PRIMARY KEY AUTO_INCREMENT,
	-- 問題文
	question VARCHAR(255),
	-- 正解の勘定科目
	answer VARCHAR(255)
);