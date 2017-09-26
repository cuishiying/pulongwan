@echo off
color 0f
title FTP人员上传脚本请勿关闭

:START
@echo 开始上传文件
@echo %date% %time%
ftp   -d  -i -n -s:"E:\src\ftp_up\Ftp.ini"
@echo 上传完成
@echo %date% %time%

del /f /s /q  E:\location\*	//删除本地已经上传的文件

:END

ping 127.0.0.1 -n 10>nul

GOTO START

@echo %date% %time%