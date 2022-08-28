# 如何处理启动失败

## 如果使用 mariadb

```
dyld[2672]: Library not loaded: /usr/local/opt/openssl/lib/libssl.1.0.0.dylib
```

```bash
brew install rbenv/tap/openssl@1.0
ln -sfn /usr/local/Cellar/openssl@1.0/1.0.2t /usr/local/opt/openssl
```

TODO：

1. 枚举-接口-注解相互映射和注入
