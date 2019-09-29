package com.equationl.ffmpeg;

import java.io.IOException;

public interface FFdownloadFileInterface {
    /**
     * Call when need download ffmpeg
     *
     * @param prefix the prefix of ffmpeg file, also ffmpeg's cpu arch type
     * @param fFbinaryInterface FFbinaryInterface
     * */
    void startDownload(String prefix, FFbinaryInterface fFbinaryInterface);

    /**
     * Call when copy file to application storage fail
     *
     * */
    void copyFileFail(IOException e);

    /**
     * call when all work done, include download file, copy file, check file.
    * */
    void allFinish();

    /**
     * call when check file fail*/
    void verifyFileFail();
}
