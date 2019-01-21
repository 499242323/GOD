@echo off 

:HELP
echo 帮助：
echo   如果您第一次下载代码，请先下载jar包，执行命令：ivy 或者 0
echo   jar包下载完成后，首先编译代码主框架，执行命令：main 或者 1
echo   退出，执行命令：end 或者 -1
echo   帮助，执行命令：help 或者 10

:EXE
set /p var="请输入命令："
if %VAR%==ivy goto IVY
if %VAR%==0 goto IVY
if %VAR%==main goto MAIN
if %VAR%==1 goto MAIN
if %VAR%==help goto HELP
if %VAR%==10 goto HELP
goto PLUGIN

:IVY 
echo 开始下载jar包......
call ant -f build.xml ivy-retrieve
goto EXE

:MAIN 
echo 开始编译代码框架......
call ant -f build.xml build
goto EXE


:END
