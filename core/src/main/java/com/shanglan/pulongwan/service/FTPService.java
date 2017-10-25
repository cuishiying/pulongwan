package com.shanglan.pulongwan.service;

import com.google.gson.Gson;
import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.interf.OnPublishRockPressureListener;
import com.shanglan.pulongwan.mqtt.ServerMQTT;
import com.shanglan.pulongwan.utils.BaseUtils;
import com.shanglan.pulongwan.utils.MqttUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class FTPService {

    String filePath = "/Users/cuishiying/2017/04/bk/src/oa/矿压/";
    String fileName = "dev.txt";


    /**
     * 监听文件的增删，项目部署后由AutoService自动启动执行
     * @throws Exception
     */
    public AjaxResponse monitorFile()throws Exception{
        monitorFile(filePath);
        return AjaxResponse.success();
    }
    /**
     * 监听文件的增删
     * @throws Exception
     */
    public AjaxResponse monitorFile(String filePath) throws Exception {

        File dir = new File(filePath);
        // 轮询间隔 1 秒
        long interval = TimeUnit.SECONDS.toMillis(1);
        IOFileFilter fileFileter = FileFilterUtils.and(FileFilterUtils.fileFileFilter(),FileFilterUtils.suffixFileFilter(".txt"));

        //观察者
        FileAlterationObserver observer = new FileAlterationObserver(dir,fileFileter);
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                super.onFileCreate(file);
                System.out.println(file.getName()+"==onFileCreate");
                try {
                    //如果是监测的数据文件则解析
                    if(StringUtils.equals(file.getName(),fileName)){
                        handleData(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval,observer);
        //开始监听
        monitor.start();

        return AjaxResponse.success();
    }

    /**
     * 解析矿压数据并发布到Apollo
     * @param file
     * @throws IOException
     */
    public void handleData(File file) throws IOException {
        List<RockPressure> list = new ArrayList();
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));

        String message;
        String[] ss;
        RockPressure r = null;
        String time = br.readLine().split(",")[1];
        while ((message = br.readLine()) != null) {
            ss = message.split(",");
            String sensorCode = ss[0].substring(4, 7);//传感器编号
            if(map.containsKey(sensorCode)){
                String name = ss[0].substring(7, 9);//名称
                String value = ss[7];//压力值
                if(name.equals("P1")){
                    map.put("oneValue",value);//1通道测量值
                }else if(name.equals("P2")){
                    map.put("twoValue",value);//2通道测量值
                }
            }else{
                String mainPoint = ss[0].substring(0, 2);//主站
                String subPoint = ss[0].substring(2, 4);//分站
                String name = ss[0].substring(7, 9);//名称
                String sensorRegion = ss[1].substring(ss[1].indexOf("支架")+2,ss[1].lastIndexOf("支架")+2);//传感器安装位置
                String unit = ss[3];//单位
                String value = ss[7];//压力值
                map.put("mainPoint",mainPoint);
                map.put("subPoint",subPoint);
                map.put("sensorCode",sensorCode);
                map.put("sensorRegion",sensorRegion);
                map.put("unit",unit);
                if(StringUtils.equals(name,"P1")){
                    map.put("oneValue",value);
                }else if(StringUtils.equals(name,"P2")){
                    map.put("twoValue",value);
                }
                map.put("time",time);
            }
            if(!StringUtils.isEmpty(map.get("oneValue"))&&!StringUtils.isEmpty(map.get("twoValue"))){
                RockPressure rockPressure = new RockPressure(map);
                list.add(rockPressure);
                map.clear();
            }
        }
        br.close();
//        FileUtils.deleteQuietly(file);//删除文件
        MqttUtils.publishRockPressure(list);
    }

    /**
     * 初始化页面，数据由Apollo渲染
     * @return
     */
    public List<RockPressure> initRockPressureData(){
        List<RockPressure> list = new ArrayList<>();
        RockPressure rockPressure = null;
        for(int i=1;i<=16;i++){
            rockPressure = new RockPressure();
            rockPressure.setSensorCode(i>9?"0"+i:"00"+i);
            list.add(rockPressure);
        }
        return list;
    }

}
