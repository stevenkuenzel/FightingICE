﻿rem batファイルサンプル

setlocal ENABLEDELAYEDEXPANSION

rem 実行したいAIの数-1
set FIGHT_AI_NUM=19

rem 実行したいAIの名前
set FIGHT_AI[0]=PMMctsAIexpPA1
set FIGHT_AI[1]=PMMctsAIexpPA12
set FIGHT_AI[2]=PMMctsAIexpPA2
set FIGHT_AI[3]=PMMctsAIexpPA22
set FIGHT_AI[4]=PMMctsAIexpPA3
set FIGHT_AI[5]=PMMctsAIexpPA32
set FIGHT_AI[6]=PMMctsAIexpPA4
set FIGHT_AI[7]=PMMctsAIexpPA42
set FIGHT_AI[8]=PMMctsAIexpPA5
set FIGHT_AI[9]=PMMctsAIexpPA52
set FIGHT_AI[10]=PMMctsAIexpLeafPA1
set FIGHT_AI[11]=PMMctsAIexpLeafPA12
set FIGHT_AI[12]=PMMctsAIexpLeafPA2
set FIGHT_AI[13]=PMMctsAIexpLeafPA22
set FIGHT_AI[14]=PMMctsAIexpLeafPA3
set FIGHT_AI[15]=PMMctsAIexpLeafPA32
set FIGHT_AI[16]=PMMctsAIexpLeafPA4
set FIGHT_AI[17]=PMMctsAIexpLeafPA42
set FIGHT_AI[18]=PMMctsAIexpLeafPA5
set FIGHT_AI[19]=PMMctsAIexpLeafPA52

rem 使用キャラクター
set CHARACTER=ZEN

rem 総当たり
for /l %%i in (0,1,!FIGHT_AI_NUM!) do (
		java -cp FightingICE_expMid.jar;./lib/lwjgl/*;./lib/natives/windows/*;./lib/*;  Main --pmmode --pmai !FIGHT_AI[%%i]! --c1 !CHARACTER! --c2 !CHARACTER! --fastmode --mute -n 100 --limithp 400 400
)

rem TIMEOUT /T -1
endlocal

exit