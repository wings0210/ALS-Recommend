package com.example.food_rec.mapper;

import com.example.food_rec.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectUserById(String uid);
}
