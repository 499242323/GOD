@echo off 

:HELP
echo ������
echo   �������һ�����ش��룬��������jar����ִ�����ivy ���� 0
echo   jar��������ɺ����ȱ����������ܣ�ִ�����main ���� 1
echo   �˳���ִ�����end ���� -1
echo   ������ִ�����help ���� 10

:EXE
set /p var="���������"
if %VAR%==ivy goto IVY
if %VAR%==0 goto IVY
if %VAR%==main goto MAIN
if %VAR%==1 goto MAIN
if %VAR%==help goto HELP
if %VAR%==10 goto HELP
goto PLUGIN

:IVY 
echo ��ʼ����jar��......
call ant -f build.xml ivy-retrieve
goto EXE

:MAIN 
echo ��ʼ���������......
call ant -f build.xml build
goto EXE


:END
