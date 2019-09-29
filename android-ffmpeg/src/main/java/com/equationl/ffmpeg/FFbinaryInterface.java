package com.equationl.ffmpeg;

import java.io.File;
import java.util.Map;

public interface FFbinaryInterface {

    /**
     * Executes a command
     *
     * @param environmentVars                 Environment variables
     * @param cmd                             command to execute
     * @param ffcommandExecuteResponseHandler {@link FFcommandExecuteResponseHandler}
     * @return the task
     */
    FFtask execute(Map<String, String> environmentVars, String[] cmd, FFcommandExecuteResponseHandler ffcommandExecuteResponseHandler);

    /**
     * Executes a command
     *
     * @param cmd                             command to execute
     * @param ffcommandExecuteResponseHandler {@link FFcommandExecuteResponseHandler}
     * @return the task
     */
    FFtask execute(String[] cmd, FFcommandExecuteResponseHandler ffcommandExecuteResponseHandler);

    /**
     * Executes a command synchronously
     *
     * @param environmentVars                 Environment variables
     * @param cmd                             command to execute
     * @return true, if command executed successfully, false otherwise
     */
    boolean execute(Map<String, String> environmentVars, String[] cmd);

    /**
     * Executes a command synchronously
     *
     * @param cmd                             command to execute
     * @return true, if command executed successfully, false otherwise
     */
    boolean execute(String[] cmd);

    /**
     * Checks if FF binary is supported on this device
     *
     * @return true if FF binary is supported on this device
     */
    boolean isSupported();

    /**
     * Checks if a command with given task is currently running
     *
     * @param task - the task that you want to check
     * @return true if a command is running
     */
    boolean isCommandRunning(FFtask task);

    /**
     * Kill given running process
     *
     * @param task - the task to kill
     * @return true if process is killed successfully
     */
    boolean killRunningProcesses(FFtask task);

    /**
     * Timeout for binary process, should be minimum of 10 seconds
     *
     * @param timeout in milliseconds
     */
    void setTimeout(long timeout);

    /**
     * callback when download success
     *
     * @param file the file of download
     * */
    void FFmpegFileDownloadSuccessCallback(File file);

    /**
    * Checks if FF binary is exist
    * */
    boolean isFFmpegExist();
}
