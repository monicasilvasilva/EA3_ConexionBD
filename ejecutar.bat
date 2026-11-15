@echo off
REM Compila y ejecuta la aplicacion. El driver JDBC (.jar) debe estar en la carpeta lib\
cd /d "%~dp0"
if exist bin rmdir /s /q bin
dir /s /b src\*.java > fuentes.txt
javac -encoding UTF-8 -d bin @fuentes.txt
del fuentes.txt
java -cp "bin;lib\*" conexionbd.Main
