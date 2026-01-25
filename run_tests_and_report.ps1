# --- Step 0: Cleanup Local and Shared Folders ---
Write-Host "--- Step 0: Cleaning old results ---" -ForegroundColor Yellow
if (Test-Path ".\allure-results") {
    Remove-Item -Recurse -Force .\allure-results\*
} else {
    New-Item -ItemType Directory -Path ".\allure-results" -Force
}
# Очистка общей папки на телефоне
adb shell "rm -rf /sdcard/Download/allure-results"

# --- Step 1: Build and Install ---
Write-Host "--- Step 1: Build and Install ---" -ForegroundColor Cyan
./gradlew installDebug installDebugAndroidTest

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build Failed!" -ForegroundColor Red
    exit
}

# --- Step 2: Run Tests ---
Write-Host "--- Step 2: Running Instrumentation Tests ---" -ForegroundColor Cyan
# Запуск тестов (приложение НЕ удалится автоматически после завершения)
adb shell am instrument -w -e debug false ru.iteco.fmhandroid.test/androidx.test.runner.AndroidJUnitRunner

# --- Step 3: Pull Results from Device ---
Write-Host "--- Step 3: Pulling results to PC ---" -ForegroundColor Cyan
# Копируем из защищенной папки в общую
adb shell "run-as ru.iteco.fmhandroid cp -r files/allure-results /sdcard/Download/"
# Скачиваем на компьютер
adb pull /sdcard/Download/allure-results/. ./allure-results

# --- Step 4: Final Device Cleanup (App Uninstallation) ---
Write-Host "--- Step 4: Uninstalling app and test APK ---" -ForegroundColor Yellow
# Теперь файлы уже на компьютере, приложение можно удалять
adb uninstall ru.iteco.fmhandroid
adb uninstall ru.iteco.fmhandroid.test
# Удаляем временную папку с SD-карты
adb shell "rm -rf /sdcard/Download/allure-results"

# --- Step 5: Open Allure Report ---
Write-Host "--- Step 5: Starting Allure Server (Press Ctrl+C to stop) ---" -ForegroundColor Green
allure serve allure-results
