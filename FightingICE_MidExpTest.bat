﻿rem batファイルサンプル

setlocal ENABLEDELAYEDEXPANSION

rem 実行したいAIの数-1
set FIGHT_AI_NUM=39

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
set FIGHT_AI[20]=PMMctsAIexpHLPA1
set FIGHT_AI[21]=PMMctsAIexpHLPA12
set FIGHT_AI[22]=PMMctsAIexpHLPA2
set FIGHT_AI[23]=PMMctsAIexpHLPA22
set FIGHT_AI[24]=PMMctsAIexpHLPA3
set FIGHT_AI[25]=PMMctsAIexpHLPA32
set FIGHT_AI[26]=PMMctsAIexpHLPA4
set FIGHT_AI[27]=PMMctsAIexpHLPA42
set FIGHT_AI[28]=PMMctsAIexpHLPA5
set FIGHT_AI[29]=PMMctsAIexpHLPA52
set FIGHT_AI[30]=PMMctsAIexpHLLeafPA1
set FIGHT_AI[31]=PMMctsAIexpHLLeafPA12
set FIGHT_AI[32]=PMMctsAIexpHLLeafPA2
set FIGHT_AI[33]=PMMctsAIexpHLLeafPA22
set FIGHT_AI[34]=PMMctsAIexpHLLeafPA3
set FIGHT_AI[35]=PMMctsAIexpHLLeafPA32
set FIGHT_AI[36]=PMMctsAIexpHLLeafPA4
set FIGHT_AI[37]=PMMctsAIexpHLLeafPA42
set FIGHT_AI[38]=PMMctsAIexpHLLeafPA5
set FIGHT_AI[39]=PMMctsAIexpHLLeafPA52

rem 使用キャラクター
set CHARACTER=ZEN

rem 総当たり
for /l %%i in (0,1,!FIGHT_AI_NUM!) do (
		java -cp FightingICE_expMidTest.jar;./lib/lwjgl/*;./lib/natives/windows/*;./lib/*;  Main --pmmode --pmai !FIGHT_AI[%%i]! --c1 !CHARACTER! --c2 !CHARACTER! --fastmode --mute -n 2 --limithp 400 400
)

rem TIMEOUT /T -1
endlocal

exit