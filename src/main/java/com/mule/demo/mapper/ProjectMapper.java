package com.mule.demo.mapper;
import com.mule.demo.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

}
