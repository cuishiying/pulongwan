package com.shanglan.pulongwan.thread;

import com.shanglan.pulongwan.config.Constance;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.*;

/**
 * Created by cuishiying on 2017/7/25.
 */
@Component
@Scope
public class ReceiveThread extends Thread {

    private Lock lock;
    private byte[] req = null;
    private Queue queue;

    private DatagramSocket socket = null;
    private DatagramPacket packet = null;
    DatagramSocket sendsocket;

    public ReceiveThread(){}

    public ReceiveThread(Lock lock) {
        this.lock = lock;
    }
    public ReceiveThread(Lock lock,Queue queue) {
        this.lock = lock;
        this.queue = queue;
        try {
            socket = new DatagramSocket(Constance.getSocket_port());
            sendsocket = new DatagramSocket();

            byte[] buf = new byte[60];
            packet = new DatagramPacket(buf, buf.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环接收UDP数
     * 数据存入队列1
     * 判断队列2操作标志
     * true：队列1数据复制到队列2，设置队列2标志false
     */
    @Override
    public void run() {
        try{
            while (true){
                socket.receive(packet);
//                send(packet);
                queue.put(packet);
                if(lock.isLock()){
                    queue.copyData();
                    lock.setLock(false);

                }else{
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!Constance.isReceiveDataFlat()){
                    Thread.interrupted();
                    break;
                }
            }
        }catch (Exception e){
            Thread.interrupted();
            e.printStackTrace();
        }

    }


    /**
     * 测试数据
     * @param packet
     * @throws Exception
     */
    public void send(DatagramPacket packet) throws Exception {
        byte[] buf = packet.getData();
        DatagramPacket packet1 = new DatagramPacket(buf, packet.getLength(), InetAddress.getByName("139.129.227.31"), 10000);
        sendsocket.send(packet1);
    }
}
