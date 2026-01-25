adb shell "run-as ru.iteco.fmhandroid cp -r files/allure-results /sdcard/Download/"
adb pull /sdcard/Download/allure-results/. ./allure-results
allure serve allure-results