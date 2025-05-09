@echo off
REM === Exécution des tests unitaires JUnit 5 ===
REM Ce script suppose que les classes de test sont déjà compilées dans target/test-classes

setlocal
set CLASSPATH=lib/*;target/classes;target/test-classes

REM Recherche tous les tests JUnit Jupiter
for %%f in (target\test-classes\**\*Test.class) do (
    echo Test trouvé : %%f
)

REM Lancement des tests avec la plateforme JUnit
java -jar lib\junit-platform-console-standalone-1.8.1.jar --class-path %CLASSPATH% --scan-class-path

endlocal
pause
