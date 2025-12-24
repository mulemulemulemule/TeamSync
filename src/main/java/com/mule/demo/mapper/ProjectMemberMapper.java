package com.mule.demo.mapper;

import com.mule.demo.entity.ProjectMember;
import com.mule.demo.vo.ProjectMemberVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {
@Select("SELECT project_id FROM project_member WHERE user_id = #{userId} AND status = #{status}")
List<Long> selectProjectIdsByUserIdAndStatus(Long userId, Integer status);
@Select("SELECT u.id as userId, u.username, u.avatar, pm.role " 
+"FROM project_member pm " 
+"JOIN sys_user u ON pm.user_id = u.id " 
+"WHERE pm.project_id = #{projectId} AND pm.status = 1")
List<ProjectMemberVO> listProjectMembers(Long projectId);
}
