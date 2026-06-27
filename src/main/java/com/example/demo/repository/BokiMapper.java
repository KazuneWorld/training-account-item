package com.example.demo.repository;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Question;

//
@Mapper
public interface BokiMapper {
	
	//全てのクイズを取得します。
	List<Question> selectAll();
	
	//指定されたIDに対応するクイズを取得します。
	Question selectById(@Param("id") Integer id);
}