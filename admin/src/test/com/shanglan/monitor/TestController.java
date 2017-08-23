package com.shanglan.monitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by cuishiying on 2017/7/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring-context.xml"})
public class TestController {


    @Test
    public void test() throws Exception{
//        int j = ByteBuffer.wrap("62".getBytes()).order(ByteOrder.BIG_ENDIAN).getInt();
//        System.out.println(j);
    }


}
