package com.example.demo.repository;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Question;

@Mapper
public interface BokiMapper {
	
	// ランダムに指定件数のidを取得します。
	List<Integer> findRandomIds(@Param("limit") int limit);
	
	//全てのクイズを取得します。
	List<Question> findAll();
	
	//指定されたIDに対応するクイズを取得します。
	Question findById(@Param("id") Integer id);
}