# Tiny-FFmpeg-Android
![](https://api.bintray.com/packages/likehide/maven/tiny-android-ffmpeg/images/download.svg)

This is a tiny FFmpeg project fork from  [formatBCE](https://github.com/formatBCE/FFmpeg-Android).
Execute FFmpeg commands with ease in your Android project.

[中文说明](./README_ZH.md)

## Features
- Smaller package, only 21kb in size
- Support for most CPU architectures (including 64-bit)
-- armv7
-- armv7-neon
-- armv8
-- x86
-- x86_64
-- arm64-v8a
- The developer or user can choose to download the binary suitable for their device.
- Because the library itself does not contain binaries, you can bypass 64-bit detection on Google Play.

## Usage
### Getting Started
Include the dependency
```gradle 
implementation 'com.equationl.ffmpeg:tiny-android-ffmpeg:1.1.8'
 ```
_Replace the 1.1.8 to latest version_
### Start using
#### 1.Get Rady

Remember to check if the device is supported before use and if the binary exists and is available.
1.Check for support：
```java 
ffmpeg.isSupported()
``` 
2.Check if binary file exist
```java 
ffmpeg.isFFmpegExist()
``` 
3.If not exist, you need download by yourself
4.Apply the download file to project
```java 
ffmpeg.setFFmpegFile(downFile);
``` 
5.The callback of apply file
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

#### 2.Using
In this sample code we will run the ffmpeg -version command.
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

Stop (or Quit) the FFmpeg process
If you want to stop the running FFmpeg process, simply call `.sendQuitSignal()` on the `FFtask` that is running:

```java
FFmpeg ffmpeg = FFmpeg.getInstance(context);
FFtask ffTask = ffmpeg.execute( ... )

ffTask.sendQuitSignal();
```

_NOTE: This will result in `onFailure` being called instead of `onSuccess`._



#### 3.Sample
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
## Thanks
This project fork from [formatBCE](https://github.com/formatBCE/FFmpeg-Android)
formatBCE fork from [bravobit](https://github.com/bravobit/FFmpeg-Android)
bravobit fork from[WritingMinds](https://github.com/WritingMinds/ffmpeg-android-java)
