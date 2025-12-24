package com.mule.demo.vo;
import lombok.Data;

@Data
public class ProjectMemberVO {
        private Long userId;
        private String username;
        private String avatar;
        private String role;
    }