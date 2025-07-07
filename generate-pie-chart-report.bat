@echo off
chcp 65001 >nul
echo ===================================================
echo 🎯 正在生成帶有圓餅圖的專業測試報告...
echo ===================================================
echo.

echo [1/4] 清理舊的報告...
call mvn clean

echo.
echo [2/4] 編譯專案...
call mvn compile test-compile

echo.
echo [3/4] 執行測試並生成 JSON 報告...
call mvn test

echo.
echo [4/4] 生成帶圓餅圖的 HTML 報告...
call mvn cucumber-reporting:generate

echo.
echo ===================================================
echo 📊 報告生成完成！正在開啟報告...
echo ===================================================

REM 嘗試不同的報告位置
if exist "target\cucumber-html-reports\overview-features.html" (
    echo ✅ 找到主報告文件，正在開啟...
    start "" "target\cucumber-html-reports\overview-features.html"
) else if exist "target\cucumber-html-reports\index.html" (
    echo ✅ 找到索引報告文件，正在開啟...
    start "" "target\cucumber-html-reports\index.html"
) else if exist "target\cucumber-html-reports" (
    echo ✅ 找到報告目錄，正在開啟...
    start "" "target\cucumber-html-reports"
) else if exist "target\cucumber-reports" (
    echo ⚠️  找到基本報告目錄，正在開啟...
    start "" "target\cucumber-reports"
) else (
    echo ❌ 未找到報告文件，檢查錯誤信息
    dir target /s | findstr cucumber
)

echo.
echo 📍 報告應該位於以下位置之一：
echo    - target\cucumber-html-reports\overview-features.html
echo    - target\cucumber-html-reports\index.html
echo    - target\cucumber-reports\
echo.
pause 