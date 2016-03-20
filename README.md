# OpenCVTeest
OpenCVに慣れるためのandroidプロジェクト

# 開発環境
* Android Studio 1.5.1
* OpenCV for Android 3.1

# コンパイル方法

* Open CVのネイティブファイルは容量の問題でgit hubには上げていない
* OpenCVTest\app\src\main\jniLibs にopenCVのネイティブファイルを入れる必要がある

以下のようなフォルダ構造
jniLibs  
　|- arm64-v8a  
　|- armeabi  
　|- armeabi-v7a  
　|- mips  
　|- mips64  
　|- x86  
　|- x86_64  


* ネイティブファイルはOpenCV for Androidの中のOpenCV-android-sdk\sdk\native\libsに配置されている
