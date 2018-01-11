##测试部分

FTP服务器：Serv-U

E:\src\Serv-U\ServUAdmin.exe

FTP测试客户端：FileZilla


##需开发模块

矿压监测    D:\location\rockpressure
安全监测    D:\location\safemonitor
人员定位    D:\location\personlocation


##矿压监测方案说明：
(数据源文件见dev.txt)

10.38.8.201
user:shanglan 911
FTPService: d:\location


open 139.129.227.31	//打开连接
user  shanglan 123	//帐号密码



1、客户机上传监测文件到FTP服务器，     

2、本程序监听文件并解析，

3、发布数据到Mqtt服务器，

4、前端订阅Mqtt数据

5、客户需求半小时采集一次数据



##矿压监测具体设计：

一级主题：矿压监测、安全监测、人员定位

二级主题：矿压监测/传感器编号——————————————该传感器所有数值json

例：矿压监测/001--------------{key:value}

前端根据topic找到对应行，然后js操作渲染该行的所有值



##铺龙湾矿压监测脚本

ftpup.bat:

    @echo off
    color 0f
    title FTP人员上传脚本请勿关闭
    
    :START
    @echo 开始上传文件
    @echo %date% %time%
    ftp   -d  -i -n -s:"D:\shanglan\Ftp.ini"
    @echo 上传完成
    @echo %date% %time%
    
    del /f /s /q  D:\dev.txt	//删除本地已经上传的文件
    
    :END
    
    ping 127.0.0.1 -n 10>nul
    
    GOTO START
    
    @echo %date% %time%
    
    
Ftp.ini:    
    
    open 10.38.8.201	//打开连接
    user  shanglan 911	//帐号密码
    bin
    mput  D:\dev.txt	//要上传的文件
    close
    bye



人员定位IP：10.38.129.7

安全监测IP：10.38.129.5



每次维护服务器需：

开启双击热备
启动apollo
启动nginx
启动FTP
