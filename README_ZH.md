# Tiny-FFmpeg-Android
![](https://api.bintray.com/packages/likehide/maven/tiny-android-ffmpeg/images/download.svg)

这是一个 Fork 自 [formatBCE](https://github.com/formatBCE/FFmpeg-Android) 的 轻量级FFmpeg 库。
使你可以更轻松的在安卓项目中使用 FFmpeg 命令。

## 特性
- 更小的集成包，仅仅只有21kb的大小。
- 支持大多数CPU架构（包括64位）
-- armv7
-- armv7-neon
-- armv8
-- x86
-- x86_64
-- arm64-v8a
- 可由开发者或用户自由选择下载适合使用设备的二进制文件
- 因为库本身没有包含二进制文件，所以可以绕过 Google Play 的64位检测

## 使用方法
### 准备项目
导入项目
```gradle 
implementation 'com.equationl.ffmpeg:tiny-android-ffmpeg:1.1.8'
 ```
_请将版本号替换为上方图片所示的最新版本_
### 开始使用
#### 1.准备工作
使用前切记检查是否支持该设备，并且二进制是否存在且可用。
1.检查是否支持：
```java 
ffmpeg.isSupported()
``` 
2.检查二进制文件是否存在
```java 
ffmpeg.isFFmpegExist()
``` 
3.如果不存在则需要自行下载
4.将下载好的文件应用到库中
```java 
ffmpeg.setFFmpegFile(downFile);
``` 
5.应用二进制文件时的回调
```java 
ffmpeg.setFFcheckFileListener(new FFcheckFileListener() {
            @Override
            public void onCopyFileFail(IOException e) {
                Log.e(e);
            }

            @Override
            public void onAllFinish() {

            }

            @Override
            public void onVerifyFileFail() {
                Log.e("check file fail!");
            }
        });
``` 

#### 2.开始使用
下面的例子将会运行 ffmpeg -version 命令
```java
FFmpeg ffmpeg = FFmpeg.getInstance(context);
  // to execute "ffmpeg -version" command you just need to pass "-version"
ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

    @Override
    public void onStart() {}

    @Override
    public void onProgress(String message) {}

    @Override
    public void onFailure(String message) {}

    @Override
    public void onSuccess(String message) {}

    @Override
    public void onFinish() {}

});
```

停止（或退出） FFmpeg 进程
如果你想要终止运行中的FFmpeg进程，只需要在运行中的  `FFtask` 调用 `.sendQuitSignal()`：
```java
FFmpeg ffmpeg = FFmpeg.getInstance(context);
FFtask ffTask = ffmpeg.execute( ... )

ffTask.sendQuitSignal();
```

_注意：这样做的话，将会调用 `onFailure`  而非`onSuccess`._



#### 3.完整示例
```java 
FFmpeg ffmpeg = FFmpeg.getInstance(this);
ffmpeg.setFFcheckFileListener(new FFcheckFileListener() {
            @Override
            public void onCopyFileFail(IOException e) {
                Log.e(e);
            }

            @Override
            public void onAllFinish() {

            }

            @Override
            public void onVerifyFileFail() {
                Log.e("check file fail!");
            }
        });
if (ffmpeg.isSupported()) {
            //check if support
            if (ffmpeg.isFFmpegExist()) {
                //check if ffmpeg file exist
                //all good, begin use
                runFFmpeg();
            }
            else {
                //begin download ffmpeg
                String prefix = ffmpeg.getPrefix();
                File downFile = downloadFile(prefix);
                //apply download file to application
                ffmpeg.setFFmpegFile(downFile);
                //just for case, check again
                if (ffmpeg.isFFmpegExist()) {
                    //all good, use it
                    runFFmpeg();
                }
            }
        }
        else {
            //well, not support
            Log.e("ffmpeg not support!");
        }
    }
```
runFFmpeg()：
```java
private void runFFmpeg() {
        fFmpeg.execute(new String[]{"-version"}, new ExecuteBinaryResponseHandler() {
            @Override
            public void onSuccess(String message) {
                Timber.d(message);
            }

            @Override
            public void onProgress(String message) {
                Timber.d(message);
            }
        });

    }
```
## 鸣谢
该项目 Fork 自 [formatBCE](https://github.com/formatBCE/FFmpeg-Android)
formatBCE Fork自 [bravobit](https://github.com/bravobit/FFmpeg-Android)
bravobit Fork 自[WritingMinds](https://github.com/WritingMinds/ffmpeg-android-java)
感谢上面各位为开源做出的贡献！
