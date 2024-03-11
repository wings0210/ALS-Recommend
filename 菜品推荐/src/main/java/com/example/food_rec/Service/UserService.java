package com.example.food_rec.Service;

import com.example.food_rec.domain.User;
import com.example.food_rec.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
/*
    public User selectUserById(String id){
        return userMapper.selectUserById(id);
    }

 */

    public boolean login(User user){
        String uid = user.getUid();
        //String password = user.getPwd();
        User u1 =  userMapper.selectUserById(uid);
        if(u1==null){
            return false;
        }else{
            //判断数据库查到的密码是否等于前台user对象的密码
            if(u1.getPwd().equals(user.getPwd())){
                return true;
            }else{
                return false;
            }
        }
    }

}
