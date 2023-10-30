# 如何处理启动失败

## 如果使用 mariadb

```
dyld[2672]: Library not loaded: /usr/local/opt/openssl/lib/libssl.1.0.0.dylib
```

```bash
brew install rbenv/tap/openssl@1.0

# 方法1：https://stackoverflow.com/questions/77219126/dyld45923-library-not-loaded-usr-local-opt-libavif-lib-libavif-15-dylib
ln -sfn /usr/local/Cellar/openssl@1.0/1.0.2t /usr/local/opt/openssl
sudo ln -sfn /opt/homebrew/Cellar/openssl@1.0/1.0.2u /usr/local/opt/openssl

# 暂时在 m1 上不生效：
https://stackoverflow.com/questions/69896059/is-it-possible-to-get-mariadb4j-to-work-on-an-m1-mac
https://github.com/MariaDB4j/MariaDB4j/issues/497

# 方法2：https://stackoverflow.com/questions/71500911/library-not-loaded-libssl-1-1-dylib-loaderror-when-trying-to-run-rails-server
brew unlink openssl@1.1
brew link openssl@1.0 --force

```

TODO：

1. 枚举-接口-注解相互映射和注入
