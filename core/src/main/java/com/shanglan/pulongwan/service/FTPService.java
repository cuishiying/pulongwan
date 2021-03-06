package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.dto.PersonLocationQueryDTO;
import com.shanglan.pulongwan.dto.RockPressureQueryDTO;
import com.shanglan.pulongwan.dto.SafeMonitorQueryDTO;
import com.shanglan.pulongwan.entity.FTPConf;
import com.shanglan.pulongwan.entity.PersonLocation;
import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.entity.SafeMonitor;
import com.shanglan.pulongwan.interf.FTPObserver;
import com.shanglan.pulongwan.interf.OnFileCreateListener;
import com.shanglan.pulongwan.repository.FtpConfRepository;
import com.shanglan.pulongwan.repository.PersonLocationRepository;
import com.shanglan.pulongwan.repository.RockPressureRepository;
import com.shanglan.pulongwan.repository.SafeMonitorRepository;
import com.shanglan.pulongwan.utils.MqttUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class FTPService {

    @Autowired
    private RockPressureRepository rockPressureRepository;
    @Autowired
    private SafeMonitorRepository safeMonitorRepository;
    @Autowired
    private PersonLocationRepository personLocationRepository;
    @Autowired
    private FtpConfRepository ftpConfRepository;

    private FTPObserver rockPressObserver;
    private FTPObserver safePressObserver;
    private FTPObserver personPressObserver;

    private List<SafeMonitor> tempList = new ArrayList<>();



//    *******************************FTP服务器配置******************************

    public FTPConf findFTPConfByName(String name){
        FTPConf conf = ftpConfRepository.findByName(name);
        return conf;
    }

    public FTPConf findFTPConfById(Integer id){
        FTPConf conf = ftpConfRepository.findOne(id);
        return conf;
    }

    public AjaxResponse addFTPConf(String name,String ftppath,String monitorfile){
        FTPConf ftpConf = new FTPConf();
        ftpConf.setName(name);
        ftpConf.setFtppath(ftppath);
        ftpConf.setMonitorfile(monitorfile);
        ftpConfRepository.save(ftpConf);
        return AjaxResponse.success();
    }

    public AjaxResponse updateFTPConf(String name,String ftppath,String monitorfile){
        FTPConf ftpConf = findFTPConfByName(name);
        ftpConf.setFtppath(ftppath);
        ftpConf.setMonitorfile(monitorfile);
        return AjaxResponse.success();
    }

    public List<FTPConf> findAllConf(){
        List<FTPConf> list = ftpConfRepository.findAll();
        return list;
    }



    //    *******************************文件监听******************************


    /**
     * 监听矿压文件的增删
     * @throws Exception
     */
    public AjaxResponse startFTP(Integer id) throws Exception {

        FTPConf conf = ftpConfRepository.findOne(id);
        conf.setMonitoring(true);

        if(StringUtils.equals(conf.getName(),"矿压监测")){

            if(rockPressObserver==null){
                rockPressObserver = new FTPObserver(conf.getFtppath(), conf.getMonitorfile(),".txt", new OnFileCreateListener() {
                    @Override
                    public void onFileCreate(File file) {
                        try{
                            handleRockPressureData(file);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            rockPressObserver.start();
            return AjaxResponse.success();

        }else if(StringUtils.equals(conf.getName(),"安全监测")){

            if(safePressObserver==null){
                safePressObserver = new FTPObserver(conf.getFtppath(), conf.getMonitorfile(), ".txt",new OnFileCreateListener() {
                    @Override
                    public void onFileCreate(File file) {
                        try{
                            handleSafeData(file);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            safePressObserver.start();
            return AjaxResponse.success();
        }else if(StringUtils.equals(conf.getName(),"人员定位")){

            if(personPressObserver==null){
                personPressObserver = new FTPObserver(conf.getFtppath(), null,null, new OnFileCreateListener() {
                    @Override
                    public void onFileCreate(File file) {
                        try{
                            handlePersonData(file);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            personPressObserver.start();
            return AjaxResponse.success();
        }else{
            return AjaxResponse.fail();
        }
    }

    /**
     * 停止监听
     * @param id
     * @return
     * @throws Exception
     */
    public AjaxResponse stopFTP(Integer id) throws Exception{
        FTPConf conf = ftpConfRepository.findOne(id);
        conf.setMonitoring(false);
        if(StringUtils.equals(conf.getName(),"矿压监测")){

            if(rockPressObserver!=null){
                rockPressObserver.stop();
            }
            return AjaxResponse.success();

        }else if(StringUtils.equals(conf.getName(),"安全监测")){

            if(safePressObserver!=null){
                safePressObserver.stop();
            }
            return AjaxResponse.success();
        }else if(StringUtils.equals(conf.getName(),"人员定位")){

            if(personPressObserver!=null){
                personPressObserver.stop();
            }
            return AjaxResponse.success();
        }else{
            return AjaxResponse.fail();
        }
    }

    //    *******************************矿压监测******************************

    /**
     * 解析矿压数据并发布到Apollo
     * @param file
     * @throws IOException
     */
    public void handleRockPressureData(File file) throws IOException {
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
                rockPressure.setDelTime(LocalDateTime.now());
                list.add(rockPressure);
                map.clear();
            }
        }
        br.close();
        MqttUtils.publishRockPressure(list);
        saveRockPressureData(list);     //保存数据
//        FileUtils.deleteQuietly(file);
    }

    /**
     * 每半小时保存矿压数据
     * @param list
     */
    public void saveRockPressureData(List<RockPressure> list){
        LocalDateTime now = LocalDateTime.now();
        if(now.getMinute()%30==0){
            rockPressureRepository.save(list);
        }
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
    public List<SafeMonitor> initSafeMonitorData(){
        List<SafeMonitor> list = new ArrayList<>();
        // TODO: 2017/12/13  
        return list;
    }
    public List<PersonLocation> initPersonLocationData(){
        List<PersonLocation> list = new ArrayList<>();
        // TODO: 2017/12/13
        return list;
    }

    /**
     * 矿压监测历史纪录
     * @param queryDTO
     * @param pageable
     * @return
     */
    public Page<RockPressure> findRockPressureData(RockPressureQueryDTO queryDTO, Pageable pageable){
        Specification<RockPressure> specification = getWhereClause(queryDTO);
        return rockPressureRepository.findAll(specification,pageable);
    }
    /**
     * 查询条件
     * @param queryVo
     * @return
     */
    private Specification<RockPressure> getWhereClause(RockPressureQueryDTO queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&&StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("subPoint"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("sensorType"), "%" + queryVo.getKeyword().trim() + "%")));

            }

            //时间
            if (queryVo!=null&&queryVo.getQueryDate() != null) {
                LocalDateTime begin = LocalDateTime.of(queryVo.getQueryDate(), LocalTime.MIN);
                Predicate dateBeginQuery = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("delTime"), begin);
                predicate.add(dateBeginQuery);
            }
            if (queryVo!=null&&queryVo.getQueryDate() != null) {
                LocalDateTime end = LocalDateTime.of(queryVo.getQueryDate(), LocalTime.MAX);
                Predicate dateEndQuery = cb.lessThanOrEqualTo(root.<LocalDateTime>get("delTime"), end);
                predicate.add(dateEndQuery);
            }

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

    //    *******************************安全监测******************************

    public void handleSafeData(File file) throws Exception {
        List<SafeMonitor> list = new ArrayList();

        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));

        String message;
        String[] ss;
        RockPressure r = null;
        if(StringUtils.equals(file.getName(),"dev.txt")){
            if(!tempList.isEmpty()){
                list.addAll(tempList);
                tempList.clear();
                String time = br.readLine().split(",")[1];
                while ((message = br.readLine()) != null) {
                    ss = message.split(",");
                    for (int i=0;i<list.size();i++){
                        if (StringUtils.equals(list.get(i).getSensorCode(),ss[1])){
                            list.get(i).setCodeIndex(ss[0]);
                            list.get(i).setSensorRegion(ss[2]);
                            list.get(i).setName(ss[3]);
                            list.get(i).setSensorType(ss[4]);
                            list.get(i).setUnit(ss[5]);
                            list.get(i).setRangeLower(ss[6]);
                            list.get(i).setRangeHigher(ss[7]);
                            list.get(i).setAlarmLower(ss[8]);
                            list.get(i).setAlarmHigher(ss[9]);
                            list.get(i).setPowerOff(ss[10]);
                            list.get(i).setPowerOn(ss[11]);
                        }
                    }
                }
                br.close();
            }else{
                String time = br.readLine().split(",")[1];
                while ((message = br.readLine()) != null) {
                    ss = message.split(",");
                    String codeIndex = ss[0];
                    String sensorCode = ss[1];
                    String sensorRegion = ss[2];
                    String name = ss[3];
                    String sensorType = ss[4];
                    String unit = ss[5];
                    String rangeLower = ss[6];
                    String rangeHigher = ss[7];
                    String alarmLower = ss[8];
                    String alarmHigher = ss[9];
                    String powerOff = ss[10];
                    String powerOn = ss[11];

                    map.put("codeIndex",codeIndex+"");
                    map.put("sensorCode",sensorCode);
                    map.put("sensorRegion",sensorRegion);
                    map.put("name",name);
                    map.put("sensorType",sensorType);
                    map.put("unit",unit);
                    map.put("rangeLower",rangeLower);
                    map.put("rangeHigher",rangeHigher);
                    map.put("alarmLower",alarmLower);
                    map.put("alarmHigher",alarmHigher);
                    map.put("powerOff",powerOff);
                    map.put("powerOn",powerOn);

                    SafeMonitor safeMonitor = new SafeMonitor(map);
                    list.add(safeMonitor);
                    map.clear();
                }
                br.close();
                tempList.addAll(list);
            }

        }else if(StringUtils.equals(file.getName(),"rtdata.txt")){
            if(!tempList.isEmpty()){
                list.addAll(tempList);
                tempList.clear();
                String time = br.readLine().split(",")[1];
                while ((message = br.readLine()) != null) {
                    ss = message.split(",");
                    for (int i=0;i<list.size();i++){
                        if (StringUtils.equals(list.get(i).getSensorCode(),ss[0])){
                            list.get(i).setSensorValue(ss[1]);
                        }
                    }
                }
                br.close();
            }else{
                String time = br.readLine().split(",")[1];
                while ((message = br.readLine()) != null) {
                    ss = message.split(",");
                    SafeMonitor safeMonitor = new SafeMonitor();
                    safeMonitor.setSensorCode(ss[0]);
                    safeMonitor.setSensorValue(ss[1]);
                    list.add(safeMonitor);
                }
                br.close();
                tempList.addAll(list);
            }

        }
        if(!list.isEmpty()&&tempList.isEmpty()){
            //发布消息
            MqttUtils.publishSafe(list);
            saveSafeData(list);
        }


    }
    public void saveSafeData(List<SafeMonitor> list){
        LocalDateTime now = LocalDateTime.now();
        if(now.getMinute()%30==0){
            safeMonitorRepository.save(list);
        }
    }
    public Page<SafeMonitor> findSafeMonitorData(SafeMonitorQueryDTO queryDTO, Pageable pageable){
        Specification<SafeMonitor> specification = getSMWhereClause(queryDTO);
        return safeMonitorRepository.findAll(specification,pageable);
    }

    private Specification<SafeMonitor> getSMWhereClause(SafeMonitorQueryDTO queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&&StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("sensorCode"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("sensorRegion"), "%" + queryVo.getKeyword().trim() + "%")));

            }

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

    //    *******************************人员定位******************************

    public void handlePersonData(File file) throws Exception {
        List<PersonLocation> list = new ArrayList();
        HashMap<String, String> map = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));

        String message;
        String[] ss;
        RockPressure r = null;
        while ((message = br.readLine()) != null) {
            ss = message.split(";");
            Integer codeIndex = Integer.parseInt(ss[7].replace(ss[0],""));//卡号
            String name = ss[8];//姓名
            String duty = ss[10];//职务
            String groupTeam = ss[11];//班组
            String addr1 = ss[12];//所在区域
            String addr2 = ss[16];//来向区域
            String addr3 = ss[20];//所在区域
            String inboxTime = ss[13];//入站时间
            String ynUnderground = StringUtils.equals(ss[6],"1")?"是":"否";//是否下井
            String undergroundTime = ss[18];//入井时间

            map.put("codeIndex",codeIndex+"");
            map.put("name",name);
            map.put("duty",duty);
            map.put("groupTeam",groupTeam);
            map.put("addr1",addr1);
            map.put("addr2",addr2);
            map.put("addr3",addr3);
            map.put("inboxTime",inboxTime);
            map.put("ynUnderground",ynUnderground);
            map.put("undergroundTime",undergroundTime);
            PersonLocation personLocation = new PersonLocation(map);
            list.add(personLocation);
            map.clear();
        }
        br.close();
        MqttUtils.publishPerson(list);
        savePersonData(list);
        if(personPressObserver!=null){
            FileUtils.cleanDirectory(personPressObserver.getDir());
        }

    }
    public void savePersonData(List<PersonLocation> list){
        LocalDateTime now = LocalDateTime.now();
        if(now.getMinute()%30==0){
            personLocationRepository.save(list);
        }
    }
    public Page<PersonLocation> findPersonLocationData(PersonLocationQueryDTO queryDTO, Pageable pageable){
        Specification<PersonLocation> specification = getPLWhereClause(queryDTO);
        return personLocationRepository.findAll(specification,pageable);
    }

    private Specification<PersonLocation> getPLWhereClause(PersonLocationQueryDTO queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&&StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("name"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("duty"), "%" + queryVo.getKeyword().trim() + "%")));

            }

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

}
