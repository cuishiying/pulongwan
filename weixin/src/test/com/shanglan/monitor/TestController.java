package com.shanglan.monitor;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.service.DbService;
import com.shanglan.pulongwan.service.FTPService;
import com.shanglan.pulongwan.utils.DecodeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by cuishiying on 2017/7/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-context.xml"})
public class TestController {

    String filePath = "/Users/cuishiying/2017/04/bk/src/oa/矿压/";
    String fileName = "dev.txt";

    @Autowired
    private FTPService FTPService;
    @Autowired
    private DbService dbService;

    @Test
    public void test0() throws Exception{

        DatagramSocket socket = new DatagramSocket();
        byte[] buf = DecodeUtils.toByteArray("f401");

        //小端模式
        int d = DecodeUtils.decodeHex2Int(buf);
        System.out.println(d);


        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("localhost"), 10000);
//        socket.send(packet);


        while (true){
            socket.send(packet);
            Thread.sleep(1000);
        }
    }




    @Test
    public void test1() throws Exception{
        DatagramSocket socket = new DatagramSocket();
        //小端模式
        byte[] buf = DecodeUtils.toByteArray("f401");

        int i = DecodeUtils.decodeHex2Int(buf);
        System.out.println(i);
    }

    @Test
    public void test2() throws Exception{
//        AjaxResponse ajaxResponse = FTPService.monitorRockPressureFile(filePath,fileName);

    }
    @Test
    public void test3() throws Exception{
        String substring = "0101001P1".substring(0, 2);
        String substring1 = "0101001P1".substring(2, 4);
        String substring2 = "0101001P1".substring(4, 7);
        String substring3 = "0101001P1".substring(7, 9);
        String str = "山西铺龙湾煤业有限公司测区15108综采工作面支架001#支架后柱";
        String substring4 = str.substring(str.indexOf("支架")+2,str.lastIndexOf("支架")+2);
        System.out.println(substring);
        System.out.println(substring1);
        System.out.println(substring2);
        System.out.println(substring3);
        System.out.println(substring4);

    }
    @Test
    public void test4() throws Exception{
        dbService.delAll();
    }

}
