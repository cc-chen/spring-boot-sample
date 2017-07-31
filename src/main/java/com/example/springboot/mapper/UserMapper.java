package com.example.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.springboot.model.User;
import com.example.springboot.model.UserExample;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}