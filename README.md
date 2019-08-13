# FFmpeg-Android
This fork contains 64-bit binaries for FFmpeg as well as 32-bit ones.
For original implementation, refer to https://github.com/bravobit/FFmpeg-Android

To include this library to your project, do following (cheers to https://github.com/stanchovy for describing it):

- git clone project into your local computer

- open the project in Android Studio and do Build-> Build Project

- Look for the resulting aar file: android-ffmpeg/build/outputs/aar/android-ffmpeg-release.aar

- Open your project in Android Studio. File -> Project Structure. In the new window, select Modules -> (+) to add a new module. Select the aar file from step 3 and add it.

- Adjust your settings.gradle file include line to include ':android-ffmpeg-release'. For example include ':app' ':android-ffmpeg-release'
Go to your app level build.gradle and add the following line to your dependencies { ... } section: compile project(path: ':android-ffmpeg-release', configuration: 'default')

You can now successfully build your project and add the library to your Android code, e.g. import nl.bravobit.ffmpeg.FFmpeg;


Steps 4-7 are explained in more detail here: https://stackoverflow.com/questions/51141190/how-to-add-and-use-an-aar-in-androidstudio-project

Finally, you can verify the whole 64-bit issue by going to Build -> Generate Signed Bundle / APK and building your APK. Then Select Build -> Analyze APK and then selecting your newly generated APK; you should be able to browse and now see a folder structure with /assets/x86_64/ffmpeg and /assets/arm64-v8a/ffmpeg which indicate that you're now including a 64-bit ffmpeg version in your apk that you will eventually be submitting to the Play Store, complying with the new mandate for 64-bit builds.
