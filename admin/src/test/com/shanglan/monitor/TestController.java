package com.shanglan.monitor;

import com.shanglan.pulongwan.entity.FTPConf;
import com.shanglan.pulongwan.service.FTPService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;

/**
 * Created by cuishiying on 2017/7/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-context.xml"})
public class TestController {

    @Autowired
    private FTPService ftpService;


    @Test
    public void test() throws Exception{
//        ftpService.addFTPConf("安全监测","/Users/cuishiying/2017/04/bk/src/oa/安全监测/","dev.txt");
        FTPConf ftpConf = ftpService.findFTPConfByName("安全监测");
        System.out.println("ok");
    }


}
