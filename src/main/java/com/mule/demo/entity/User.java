package com.mule.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类 (User Entity)
 */
@Data // Lombok 注解：自动生成 Getter, Setter, toString, equals, hashCode 方法
@TableName("sys_user") // MyBatis Plus 注解：指定该实体类对应的数据库表名
public class User {

    /**
     * 主键 ID
     * <p>
     * IdType.AUTO 表示使用数据库的自增主键策略
     * </p>
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     * <p>
     * 对应数据库 username 字段，这是登录用的账号，具有唯一性
     * </p>
     */
    private String username;

    /**
     * 密码
     * <p>
     * 对应数据库 password 字段。
     * 注意：这里通常存储加密后的哈希值（如 BCrypt），而不是明文。
     * </p>
     */
    private String password;

    /**
     * 电子邮箱
     * <p>
     * 对应数据库 email 字段，用于接收通知
     * </p>
     */
    private String email;

    /**
     * 创建时间
     * <p>
     * 记录该用户是何时注册的
     * </p>
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     * <p>
     * 记录该用户信息最后一次被修改的时间
     * </p>
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     * <p>
     * @TableLogic 是核心注解。
     * 0 表示未删除 (正常)，1 表示已删除。
     * 当我们调用 removeById(id) 时，MyBatis Plus 不会真的发送 DELETE 语句，
     * 而是发送 UPDATE sys_user SET deleted=1 WHERE id=?
     * 这样数据还在库里，方便恢复，但查询时会自动过滤掉 deleted=1 的数据。
     * </p>
     */
    @TableLogic
    private Integer deleted;
}