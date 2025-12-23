package com.mule.demo.mapper;

import com.mule.demo.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}
