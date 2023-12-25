@echo off
rd /q /s Class
del /q /s lab4_2.bat
mkdir Class
javac -cp lib\*; src\Main.java -d Class\

echo cls>lab4_2.bat
echo java -cp Class\;lib\*; Main>>lab4_2.bat
echo @pause>>lab4_2.bat

@pause