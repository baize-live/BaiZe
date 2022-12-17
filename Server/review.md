### 问题总结

1. javax.net.ssl.SSLHandshakeException
- JDK的问题 jdk1.8.0_261\jre\lib\security\java.security 文件 删掉SSLv3就是允许SSL调用 留下RC4, DES, MD5withRSA这三个

