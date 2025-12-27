@echo off
echo ========================================
echo    Application Location de Voitures
echo ========================================

echo.
echo 1. Verification de MySQL...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERREUR: MySQL n'est pas installe ou pas dans le PATH
    echo Veuillez installer MySQL et l'ajouter au PATH
    pause
    exit /b 1
)

echo MySQL detecte avec succes!

echo.
echo 2. Creation de la base de donnees...
mysql -u root -p < "db\scripts\create_tables.sql"
if %errorlevel% neq 0 (
    echo ERREUR: Echec de creation de la base de donnees
    pause
    exit /b 1
)

echo Base de donnees creee avec succes!

echo.
echo 3. Insertion des donnees d'exemple...
mysql -u root -p < "db\scripts\sample_data.sql"
if %errorlevel% neq 0 (
    echo ERREUR: Echec d'insertion des donnees
    pause
    exit /b 1
)

echo Donnees d'exemple inserees avec succes!

echo.
echo 4. Compilation du projet...
mvn clean compile
if %errorlevel% neq 0 (
    echo ERREUR: Echec de compilation
    pause
    exit /b 1
)

echo.
echo 5. Demarrage de l'application...
mvn exec:java -Dexec.mainClass="com.location.voitures.Main"

pause