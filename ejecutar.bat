@echo off
REM
REM
cd /d "%~dp0"
if exist bin rmdir /s /q bin

REM
javac -encoding UTF-8 -d bin -sourcepath src src\conexionbd\Main.java
if errorlevel 1 (
    echo.
    echo Error al compilar. Revise los mensajes de arriba.
    pause
    exit /b 1
)

java -cp "bin;lib\*" conexionbd.Main
if errorlevel 1 pause
