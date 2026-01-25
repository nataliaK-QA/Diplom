 ##### Тестовый стенд (Workstation):
	CPU: AMD Ryzen 9 7845HX with Radeon Graphics  (3.00 GHz)
	RAM: 64 GB
	GPU: NVIDIA GeForce GTX 1060 6GB
	OS: Windows 10 Pro
	Установить Allute 2 версии.

 #### Среда
 	** Android Studio Narwhal 3 Feature Drop | 2025.1.3
	** gradle jdk 17
	** Android jdk 29

 ##### Эмулятор (Device):
	Pixel 4 (API 29), 4 Cores, 6GB Data Partition.

 ##### Подготовка эмулятора
	1. Подключить wi-fi на эмуляторе.
	2. Отключить анимацию на устройстве.

 ##### Запуск тестов
 	###### В ручном режиме:
 		1. ./gradlew clean connectedCheck - запуск тестов
 		2. Сохранить отчеты из устройства в папку allure-result:
 			- в ручную (2 x shift + Device Explorer + пройти по пути до /data/data/ru.iteco.fmhandroid/files + 					сохранить папку files в корень проекта)
 			- в терминале (adb shell "run-as ru.iteco.fmhandroid cp -r files/allure-results /sdcard/
 					Download/") + (adb pull /sdcard/Download/allure-results/. ./allure-results) (для эмулятора)
 		3. ./allure-result - для формирования отчета
 			
 	###### В автоматическом режиме: 
 		1. в powershell ".\run_tests_and_report.ps1"
 			
 	###### В полуавтоматическом режиме:
 		1.  в powershell "./gradlew clean connectedCheck" - запуск тестов
 		2.  в powershell ".\get_allure_report.ps1" -  выгрузка отчета. 		

##### Чтобы запустить тесты без функции обновления страницы в пределах одной страницы скриптом в идеальной(без шума новостей) среде: 
	1. Подготовка окружения (Android Studio 2026)
 		Откройте проект в Android Studio.
 		Запустите эмулятор с Android 10.
		В нижней панели откройте App Inspection -> Network Inspector.

	2. Настройка сетевой заглушки (Network Rule)
		Чтобы правило точно перехватило запрос с параметрами, настройте его следующим образом:

		Во вкладке Rules создайте правило:

		Host: students.netoservices.ru

		Path: /qamid-diplom-backend/news

		Query: pages=1&publishDate=false (или используйте .*, если параметры меняются)

		Method: GET

		В секции Response:
		Status Code: 200

		Body: установите флаг Replace entire body.
		Текст заглушки:
		json
		{
  		"pages": 1,
  		"elements": []
		}
		
		Нажмите Save и убедитесь, что чекбокс правила активен.
	
	3. Запустите скрипт.
	
	4. Когда в Network Inspector станет доступен процесс ru.iteco.fmhandroid выберите его(должна появится галочка), но до запуска тестов. Окно Network Inspector должно оставаться открытым до окончания тестирования.
	