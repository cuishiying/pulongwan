package com.shanglan.pulongwan.interf;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class FTPObserver {
    private File dir;
//    private File initFile;
    private IOFileFilter fileFileter;
    private FileAlterationObserver observer = null;
    private FileAlterationListenerAdaptor adaptor = null;
    private FileAlterationMonitor monitor = null;
    // 轮询间隔 1 秒
    long interval = TimeUnit.SECONDS.toMillis(1);

    public File getDir() {
        return dir;
    }

    public FTPObserver(String filePath, String monitorFileName, String suffix, OnFileCreateListener onFileCreateListener) throws Exception {

        this.dir = new File(filePath);
        if(suffix!=null){
            this.fileFileter = FileFilterUtils.and(FileFilterUtils.fileFileFilter(),FileFilterUtils.suffixFileFilter(suffix));
        }


        adaptor = new FileAlterationListenerAdaptor(){
            @Override
            public void onFileCreate(File file) {
                super.onFileCreate(file);
                System.out.println(file.getName()+"==onFileCreate");
                if(suffix!=null){
                    handleData(file,monitorFileName,onFileCreateListener);
                }else{
                    //人员定位
                    handlePersonData(file,onFileCreateListener);
                }
            }

            @Override
            public void onFileChange(File file) {
                super.onFileChange(file);
                System.out.println(file.getName()+"==onFileChange");
                if(suffix!=null){
                    handleData(file,monitorFileName,onFileCreateListener);
                }else{
                    //人员定位
                    handlePersonData(file,onFileCreateListener);
                }
            }
        };
        if(observer==null){
            if(suffix!=null){
                observer = new FileAlterationObserver(dir,fileFileter);
            }else{
                observer = new FileAlterationObserver(dir);
            }
        }
        //监听器
        monitor = new FileAlterationMonitor(interval);
        monitor.addObserver(observer);
        monitor.start();
    }

    public void handlePersonData(File file,OnFileCreateListener onFileCreateListener){
        try {
            //如果是监测的人员定位数据文件则解析
            if(file.getName().contains("RYSS")){
                if(null!=onFileCreateListener){
                    onFileCreateListener.onFileCreate(file);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleData(File file,String monitorFileName,OnFileCreateListener onFileCreateListener){
        try {
            //如果是监测的矿压数据文件则解析
//            if(StringUtils.equals(file.getName(),monitorFileName)){
//            }
            if(null!=onFileCreateListener){
                onFileCreateListener.onFileCreate(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
//        FileUtils.deleteQuietly(initFile);
        //开始监听
        if(observer!=null&&adaptor!=null){
            observer.addListener(adaptor);
        }
    }

    public void stop(){
        if(observer!=null&&adaptor!=null){
            observer.removeListener(adaptor);
        }
    }
}
