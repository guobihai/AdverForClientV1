package com.smtlibrary.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Desc: 配置信息工具
 * Created by 庞承晖
 * Date: 2018-09-26.
 * Time: 16:26
 */
public class ConfigUtil {

    /**
     * App文件根目录(默认使用包名)
     */
    public static String APP_CACHE_ROOT_DIR_NAME;
    /**
     * 子目录名称
     */
    public static final String EXTERNAL_IMAGE_DIR_NAME = "Image";//图片
    public static final String EXTERNAL_VOICE_DIR_NAME = "Voice";//音频
    public static final String EXTERNAL_VIDEO_DIR_NAME = "Video";//视频
    public static final String EXTERNAL_DEVICE_DIR_NAME = "Device";//设备信息
    public static final String EXTERNAL_MUSIC_DIR_NAME = "Music";//音乐
    public static final String EXTERNAL_LOG_DIR_NAME = "Log";//日志

    /**
     * 获取图片文件(随机)
     *
     * @return
     */
    public static File getImageFileRandom() {
        String fileName = System.currentTimeMillis() + ".jpg";
        return new File(ConfigUtil.getImageDirFile(), fileName);
    }

    /**
     * 获取音频文件(随机)
     *
     * @return
     */
    public static File getVoiceFileRandom() {
        String fileName = System.currentTimeMillis() + ".mp3";
        return new File(ConfigUtil.getVoiceDirFile(), fileName);
    }

    public static File getVoiceFileRandom(String suffix) {
        String fileName = System.currentTimeMillis() + suffix;
        return new File(ConfigUtil.getVoiceDirFile(), fileName);
    }

    /**
     * 获取视频文件(随机)
     *
     * @return
     */
    public static File getVideoFileRandom() {
        String fileName = System.currentTimeMillis() + ".mp4";
        return new File(ConfigUtil.getVideoDirFile(), fileName);
    }

    public static File getLogFile(String fileName) {
        return new File(ConfigUtil.getLogDirFile(), fileName);
    }

    public static File getLogFileAgora() {
        return getLogFile("agora-rtc.log");
    }

    public static File getLogFileAgora1() {
        return getLogFile("agora-rtc_1.log");
    }

    /**
     * 获取图片路径File
     *
     * @return
     */
    public static File getImageDirFile() {
        return getDirPath(getImageDirPath());
    }

    /**
     * 获取音频路径File
     *
     * @return
     */
    public static File getVoiceDirFile() {
        return getDirPath(getVoiceDirPath());
    }

    /**
     * 获取视频路径File
     *
     * @return
     */
    public static File getVideoDirFile() {
        return getDirPath(getVideoDirPath());
    }

    /**
     * 获取日志路径File
     *
     * @return
     */
    public static File getLogDirFile() {
        return getDirPath(getLogDirPath());
    }

    /**
     * 获取音乐路径File
     *
     * @return
     */
    public static File getMusicDirFile() {
        return getDirPath(getMusicDirPath());
    }

    /**
     * 获取设备路径File
     *
     * @return
     */
    public static File getDeviceDirFile() {
        return getDirPath(getDeviceDirPath());
    }

    /**
     * 获取路径File
     *
     * @param path
     * @return
     */
    public static File getDirPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取图片路径
     */
    public static String getImageDirPath() {
        return getCacheRootDirPath() + File.separator + EXTERNAL_IMAGE_DIR_NAME + File.separator;
    }

    /**
     * 获取音频路径
     */
    public static String getVoiceDirPath() {
        return getCacheRootDirPath() + File.separator + EXTERNAL_VOICE_DIR_NAME + File.separator;
    }

    /**
     * 获取视频路径
     */
    public static String getVideoDirPath() {
        return getCacheRootDirPath() + File.separator + EXTERNAL_VIDEO_DIR_NAME + File.separator;
    }

    /**
     * 获取日志路径
     */
    public static String getLogDirPath() {
        return getCacheRootDirPath() + File.separator + EXTERNAL_LOG_DIR_NAME + File.separator;
    }

    /**
     * 获取音乐路径
     */
    public static String getMusicDirPath() {
        return getCacheRootDirPath() + File.separator + EXTERNAL_MUSIC_DIR_NAME + File.separator;
    }

    /**
     * 获取音乐路径
     */
    public static String getDeviceDirPath() {
        return getCacheRootDirPath() + File.separator + EXTERNAL_DEVICE_DIR_NAME + File.separator;
    }

    /**
     * 获取缓存根目录路径
     *
     * @return
     */
    public static String getCacheRootDirPath() {
        String rootPath ="";
        if (!TextUtils.isEmpty(APP_CACHE_ROOT_DIR_NAME)) {
            rootPath = APP_CACHE_ROOT_DIR_NAME;
        } else {
            rootPath = ContextHolder.getContext().getPackageName();
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + rootPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }
}
