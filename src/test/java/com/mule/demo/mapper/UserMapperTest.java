package com.mule.demo.mapper;

import com.mule.demo.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单元测试类
 * @SpringBootTest: 启动 Spring 上下文，让我们可以使用 @Autowired
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional // 加上这个注解，测试运行完会自动回滚事务，不会在数据库里留下垃圾数据！
    public void testInsertAndSelect() {
        // 1. 准备数据
        System.out.println("开始测试：准备插入用户...");
        User user = new User();
        user.setUsername("test_user_001");
        user.setPassword("123456");
        user.setEmail("test@company.com");

        // 2. 执行功能 (插入)
        int result = userMapper.insert(user);
        
        // 断言：验证受影响行数是否为 1
        // 如果 result 不等于 1，测试就会变红（失败），并报错
        Assertions.assertEquals(1, result, "插入数据失败");
        System.out.println("插入成功，生成的 ID 为: " + user.getId());

        // 3. 执行功能 (查询)
        User foundUser = userMapper.selectById(user.getId());

        // 4. 验证结果
        Assertions.assertNotNull(foundUser, "未查询到刚刚插入的用户");
        Assertions.assertEquals("test_user_001", foundUser.getUsername(), "用户名不匹配");
        System.out.println("查询验证成功！");
    }
}
