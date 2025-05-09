@echo off
REM === Compilation du projet ParcAuto ===
REM Nettoyage du dossier target
if exist target rmdir /s /q target
mkdir target\classes

REM Compilation des sources principales
javac -encoding UTF-8 -cp "lib/*;src/main/java" -d target/classes @sources.txt

REM Copie des ressources (FXML, CSS, images, JS)
xcopy /E /I /Y src/main/resources target/classes

REM Compilation des tests (optionnel)
REM javac -encoding UTF-8 -cp "lib/*;target/classes;src/test/java" -d target/test-classes @test-sources.txt

REM Instructions d'exécution
REM java -cp "lib/*;target/classes" com.miage.parcauto.MainApp

echo.
echo Compilation terminée. Pour exécuter l'application :
echo    java -cp "lib/*;target/classes" com.miage.parcauto.MainApp
echo.
pause
