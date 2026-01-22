@echo off
REM 🎨 PLANETA EXPLORER - BLENDER QUICK LAUNCHER
REM Sin dependencias del PATH - Busca Blender automáticamente

setlocal enabledelayedexpansion

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║          🎨 PLANETA EXPLORER - BLENDER LAUNCHER           ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

REM Obtener ruta actual
set SCRIPT_DIR=%~dp0
set SCRIPT_PATH=%SCRIPT_DIR%blender_creature_importer.py

echo 📁 Ruta del script: %SCRIPT_PATH%

REM Buscar Blender en rutas comunes (actualizado para Blender 4.1+)
echo.
echo 🔍 Buscando Blender instalado...
echo.

setlocal enabledelayedexpansion

REM Array de rutas a buscar
set "PATHS[0]=C:\Program Files\Blender Foundation\Blender 4.1\blender.exe"
set "PATHS[1]=C:\Program Files\Blender Foundation\Blender 4.0\blender.exe"
set "PATHS[2]=C:\Program Files (x86)\Blender Foundation\Blender\blender.exe"
set "PATHS[3]=%PROGRAMFILES%\Blender Foundation\Blender 4.1\blender.exe"
set "PATHS[4]=%PROGRAMFILES(X86)%\Blender Foundation\Blender\blender.exe"
set "PATHS[5]=%APPDATA%\..\Local\Programs\Blender Foundation\Blender 4.1\blender.exe"

set FOUND=0

for /l %%i in (0,1,5) do (
    if exist "!PATHS[%%i]!" (
        set BLENDER_EXE=!PATHS[%%i]!
        set FOUND=1
        echo ✅ Encontrado en: !BLENDER_EXE!
        goto :launch
    )
)

:launch
if %FOUND% equ 0 (
    echo ❌ Blender NO encontrado en rutas estándar
    echo.
    echo 💾 Rutas buscadas:
    echo    1. C:\Program Files\Blender Foundation\Blender 4.1
    echo    2. C:\Program Files\Blender Foundation\Blender 4.0
    echo    3. C:\Program Files (x86)\Blender Foundation\Blender
    echo    4. %%PROGRAMFILES%%\...
    echo    5. %%APPDATA%%\...
    echo.
    echo 📦 Soluciones:
    echo    A) Descarga Blender: https://www.blender.org/download/
    echo    B) Instálalo en: C:\Program Files\Blender Foundation\Blender 4.1\
    echo    C) O abre manualmente y copia blender_creature_importer.py en Scripting
    echo.
    pause
    exit /b 1
)

echo.
echo 🚀 Lanzando Blender con tu criatura...
echo.
echo 💡 Cuando se abra Blender:
echo    1. Verás la consola Python en la parte inferior
echo    2. El script correrá automáticamente
echo    3. Busca mensajes como: ✅ Criatura importada: Creature_G1-A0-M0
echo    4. Press Z y select "Rendered" para ver con iluminación
echo.

REM Ejecutar Blender con el script
start "" "!BLENDER_EXE!" --python "!SCRIPT_PATH!"

echo.
echo ✅ Blender iniciado
echo   Si Blender no se abre, verifica la instalación
echo.
timeout /t 3
