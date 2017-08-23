package com.shanglan.pulongwan.thread;

import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.interf.OnHandleDataListener;
import com.shanglan.pulongwan.utils.DecodeUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by cuishiying on 2017/7/25.
 */
public class Queue {
    private  int initCapacity = 1024000;
    private  List<Byte> receiveQueues = new ArrayList<>(initCapacity);
    private  List<Byte> handleQueues = new ArrayList<>(initCapacity);


    /**
     * 存数
     * @param buf
     * @throws InterruptedException
     */
    public  void put(byte[] buf) throws InterruptedException {
        if(buf.length+receiveQueues.size()>initCapacity-1024){
            receiveQueues.clear();
            handleQueues.clear();
        }
        int count = buf.length;
        for (int i=0;i<count;i++){
            receiveQueues.add(buf[i]);
        }
    }

    /**
     * 如果BlockQueue没有空间,则调用此方法的线程被阻断,直到BlockingQueue里面有空间再继续.
     * offer:有返回值
     * put：wu返回值
     * @param packet
     */
    public  void put(DatagramPacket packet){
        try {
            put(packet.getData());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取数
     * @return
     * @throws InterruptedException
     */
    public byte pop() throws InterruptedException {
        return handleQueues.remove(0);
    }

    /**
     * 批量取出
     * @return
     */
    public void pullData(){
        try{
            for(int i=0;i<handleQueues.size()&&i+5<handleQueues.size();i++){
                String i0 = DecodeUtils.byte2HexString(handleQueues.get(i));
                String i1 = DecodeUtils.byte2HexString(handleQueues.get(i+1));
                String i2 = DecodeUtils.byte2HexString(handleQueues.get(i+2));
                String i3 = DecodeUtils.byte2HexString(handleQueues.get(i+3));
                String i4 = DecodeUtils.byte2HexString(handleQueues.get(i+4));
                String i5 = DecodeUtils.byte2HexString(handleQueues.get(i+5));
                if(i<500&&StringUtils.equals(i0,"eb")&&StringUtils.equals(i2,"eb")&&StringUtils.equals(i4,"eb")&&StringUtils.equals(i1,"90")&&StringUtils.equals(i3,"90")&&StringUtils.equals(i5,"90")){
                    if(i+8<handleQueues.size()){
                        String i6 = DecodeUtils.byte2HexString(handleQueues.get(i+6));//控制字
                        String i7 = DecodeUtils.byte2HexString(handleQueues.get(i+7));//帧类别
                        String i8 = DecodeUtils.byte2HexString(handleQueues.get(i+8));//信息字数十六进制表示-----10
                        int count = DecodeUtils.byte2Hex(handleQueues.get(i + 8));//信息字数十进制表示-----16

                        //下个同步字
                        if(i+6*(count+3)<=handleQueues.size()){
                            String i00 = DecodeUtils.byte2HexString(handleQueues.get(i+6*(count+2)));
                            String i11 = DecodeUtils.byte2HexString(handleQueues.get(i+6*(count+2)+1));
                            String i22 = DecodeUtils.byte2HexString(handleQueues.get(i+6*(count+2)+2));
                            String i33 = DecodeUtils.byte2HexString(handleQueues.get(i+6*(count+2)+3));
                            String i44 = DecodeUtils.byte2HexString(handleQueues.get(i+6*(count+2)+4));
                            String i55 = DecodeUtils.byte2HexString(handleQueues.get(i+6*(count+2)+5));
                            if(StringUtils.equals(i00,"eb")&&StringUtils.equals(i22,"eb")&&StringUtils.equals(i44,"eb")&&StringUtils.equals(i11,"90")&&StringUtils.equals(i33,"90")&&StringUtils.equals(i55,"90")){
                                //是下一个同步字,继续
                                List<Byte> tempList = handleQueues.subList(i + 12, i + 12 + 6 * count);
                                for(int j=0;j<tempList.size();){
                                    List<Field> fields = DecodeUtils.byte2Json(tempList.get(j), tempList.get(j + 1), tempList.get(j + 2), tempList.get(j + 3), tempList.get(j + 4), tempList.get(j + 5));
                                    //单独发布每次完整的数据
                                    if(null!=onHandlerData&&null!=fields&&fields.size()>0){
                                        onHandlerData.handleData(fields);
                                    }
                                    j+=6;
                                }
                                i = i+6*(count+2)-1;

                            }else{
                                //不是下一个同步字,校验失败
                                handleQueues.clear();
                                receiveQueues.clear();
                            }
                        }else{
                            //数据不完整,不再继续取数
                            setHandleQueues(handleQueues.subList(i, handleQueues.size()));
                        }
                    }else{
                        //数据不完整，不再继续取数
                        setHandleQueues(handleQueues.subList(i, handleQueues.size()));
//                        handleQueues.clear();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 复制数据
     * @return
     * @throws InterruptedException
     */
    public  boolean copyData() throws InterruptedException {
        int tempSize = receiveQueues.size();
        if(handleQueues.size()+receiveQueues.size()>initCapacity-1000){
            handleQueues.clear();
            receiveQueues.clear();
        }
        handleQueues.addAll(receiveQueues);
        setReceiveQueues(receiveQueues.subList(tempSize,receiveQueues.size()));
        return true;
    }

    private OnHandleDataListener onHandlerData = null;
    public  void setOnHandlerData(OnHandleDataListener l) {
        this.onHandlerData = l;
    }



    public void setReceiveQueues(List<Byte> list) {
        receiveQueues = new ArrayList<>(initCapacity);
        receiveQueues.addAll(list);
    }


    public void setHandleQueues(List<Byte> list) {
        handleQueues = new ArrayList<>(initCapacity);
        handleQueues.addAll(list);
    }
}
