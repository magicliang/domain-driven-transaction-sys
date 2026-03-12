-- Testcontainers 初始化脚本：授予 test 用户全局权限，使其可以创建额外的数据库
GRANT ALL PRIVILEGES ON *.* TO 'test'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
