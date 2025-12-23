-- 插入默认管理员账号 (使用 IGNORE 避免重复插入报错)
INSERT IGNORE INTO `sys_user` (`username`, `password`, `email`) VALUES ('admin', '123456', 'admin@example.com');
