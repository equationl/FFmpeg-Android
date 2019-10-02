package com.equationl.ffmpeg;

import java.io.IOException;

public interface FFcheckFileListener {
    /**
     * Call when need download ffmpeg
     *
     * @param prefix the prefix of ffmpeg file, also ffmpeg's cpu arch type
     * @param fFbinaryInterface FFbinaryInterface
     * */
    //void startDownload(String prefix, FFbinaryInterface fFbinaryInterface);

    /**
     * Call when copy file to application storage fail
     *
     * */
    void onCopyFileFail(IOException e);

    /**
     * call when all work done, include download file, copy file, check file.
    * */
    void onAllFinish();

    /**
     * call when check file fail*/
    void onVerifyFileFail();
}
