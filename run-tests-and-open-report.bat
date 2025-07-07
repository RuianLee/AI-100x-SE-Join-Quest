@echo off
echo 正在執行 Cucumber 測試...
mvn clean test

echo 正在準備報表...
copy target\cucumber-html-reports target\cucumber-report.html

echo 正在開啟測試報表...
start target\cucumber-report.html

echo 測試完成！報表已在瀏覽器中開啟。
pause 