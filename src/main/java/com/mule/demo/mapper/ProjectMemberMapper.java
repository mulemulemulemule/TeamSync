package com.mule.demo.mapper;

import com.mule.demo.entity.ProjectMember;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {
@Select("SELECT project_id FROM project_member WHERE user_id = #{userId}")
List<Long> selectProjectIdsByUserId(Long userId);
}
