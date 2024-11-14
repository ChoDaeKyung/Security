package com.example.tobi.tokenproject.mapper;


import com.example.tobi.tokenproject.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void signUp(Member member);
    Member findByUserId(String userId);
}
