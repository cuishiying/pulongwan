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

/**
 * Created by cuishiying on 2017/7/25.
 */
@Component
@Scope
public class NettyThread extends Thread {

    private Lock lock;
    private byte[] req = null;
    private Queue queue;

    public NettyThread(){}

    public NettyThread(Lock lock) {
        this.lock = lock;
    }
    public NettyThread(Lock lock, Queue queue) {
        this.lock = lock;
        this.queue = queue;
    }

    /**
     * 循环接收UDP数
     * 数据存入队列1
     * 判断队列2操作标志
     * true：队列1数据复制到队列2，设置队列2标志false
     */
    @Override
    public void run() {
        System.out.println("====ReceiveThread_start===");
        nettyUdpServerRun(Constance.getSocket_port());

    }

    /**
     * netty接收udp数据
     * @param port
     */
    public void nettyUdpServerRun(int port){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new UdpServerHandler());

            b.bind(port).sync().channel().closeFuture().await();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }
    }

    class UdpServerHandler extends SimpleChannelInboundHandler<io.netty.channel.socket.DatagramPacket>{

        @Override
        protected void messageReceived(ChannelHandlerContext channelHandlerContext, io.netty.channel.socket.DatagramPacket datagramPacket) throws Exception {
            ByteBuf content = datagramPacket.content();
            req = new byte[content.readableBytes()];
            content.readBytes(req);
            queue.put(req);
            if(lock.isLock()){
                queue.copyData();
                lock.setLock(false);

            }
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
            ctx.close();
            cause.printStackTrace();
        }
    }
}
