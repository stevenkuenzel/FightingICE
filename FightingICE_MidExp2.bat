﻿rem batファイルサンプル

setlocal ENABLEDELAYEDEXPANSION

rem 実行したいAIの数-1
set FIGHT_AI_NUM=19

rem 実行したいAIの名前
set FIGHT_AI[0]=PMMctsAIexpHLPA1
set FIGHT_AI[1]=PMMctsAIexpHLPA12
set FIGHT_AI[2]=PMMctsAIexpHLPA2
set FIGHT_AI[3]=PMMctsAIexpHLPA22
set FIGHT_AI[4]=PMMctsAIexpHLPA3
set FIGHT_AI[5]=PMMctsAIexpHLPA32
set FIGHT_AI[6]=PMMctsAIexpHLPA4
set FIGHT_AI[7]=PMMctsAIexpHLPA42
set FIGHT_AI[8]=PMMctsAIexpHLPA5
set FIGHT_AI[9]=PMMctsAIexpHLPA52
set FIGHT_AI[0]=PMMctsAIexpHLLeafPA1
set FIGHT_AI[1]=PMMctsAIexpHLLeafPA12
set FIGHT_AI[2]=PMMctsAIexpHLLeafPA2
set FIGHT_AI[3]=PMMctsAIexpHLLeafPA22
set FIGHT_AI[4]=PMMctsAIexpHLLeafPA3
set FIGHT_AI[5]=PMMctsAIexpHLLeafPA32
set FIGHT_AI[6]=PMMctsAIexpHLLeafPA4
set FIGHT_AI[7]=PMMctsAIexpHLLeafPA42
set FIGHT_AI[8]=PMMctsAIexpHLLeafPA5
set FIGHT_AI[9]=PMMctsAIexpHLLeafPA52

rem 使用キャラクター
set CHARACTER=ZEN

rem 総当たり
for /l %%i in (0,1,!FIGHT_AI_NUM!) do (
		java -cp FightingICE_expMid.jar;./lib/lwjgl/*;./lib/natives/windows/*;./lib/*;  Main --pmmode --pmai !FIGHT_AI[%%i]! --c1 !CHARACTER! --c2 !CHARACTER! --fastmode --mute -n 100 --limithp 400 400
)

rem TIMEOUT /T -1
endlocal

exit