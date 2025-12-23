package com.mule.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mule.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层接口 (User Mapper)
 * <p>
 * 这个接口负责与数据库进行直接交互。
 * </p>
 * 
 * <h3>核心原理：</h3>
 * <p>
 * 1. 继承 {@link BaseMapper}：
 *    MyBatis Plus 会自动为这个接口生成通过的 CRUD 方法实现，
 *    如 insert, deleteById, updateById, selectById, selectList 等。
 *    所以我们不需要写任何 SQL 就能操作数据库。
 * </p>
 * <p>
 * 2. {@code @Mapper} 注解：
 *    告诉 MyBatis 这是一个 Mapper 接口，启动时 Spring 会为它生成一个代理对象（Bean）放入容器。
 * </p>
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 如果将来有特别复杂的 SQL (MyBatis Plus 搞不定的)，
    // 我们可以在这里定义方法，并在 XML 文件里写原生 SQL。
}