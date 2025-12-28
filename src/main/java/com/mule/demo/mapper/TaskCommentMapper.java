package com.mule.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mule.demo.entity.TaskComment;
import com.mule.demo.model.vo.TaskCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
@Mapper
public interface TaskCommentMapper extends BaseMapper<TaskComment>{
@Select("SELECT c.*, u.username, u.avatar " +"FROM task_comment c " +"LEFT JOIN sys_user u ON c.user_id = u.id " +"WHERE c.task_id = #{taskId} " +"ORDER BY c.create_time DESC")
    List<TaskCommentVO> selectCommentsByTaskId(Long taskId);
}
