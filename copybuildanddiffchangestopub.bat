SET MINECRIFT_SRCROOT=%1
SET MINECRIFTPUB_SRCROOT=%2
SET MCP_VERSION=mcp910-pre1

rmdir /S /Q %MINECRIFTPUB_SRCROOT%\%MCP_VERSION%\src\minecraft
xcopy /E %MINECRIFT_SRCROOT% %MINECRIFTPUB_SRCROOT%\%MCP_VERSION%\src\minecraft\
cd %MINECRIFTPUB_SRCROOT%
call build.bat
call getchanges.bat
