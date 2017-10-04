@echo off

set CLASSPATH_OLD=%CLASSPATH%
set CLASSPATH=%CLASSPATH%;.\bin
set CLASSPATH=%CLASSPATH%;.\lib\jgraph.jar
set CLASSPATH=%CLASSPATH%;.\lib\jetm.jar

start javaw -classpath "%CLASSPATH%" -Xmx1G -Dsun.java2d.noddraw=true LTL2Automaton_MainFrame

set CLASSPATH=%CLASSPATH_OLD%
set CLASSPATH_OLD=
