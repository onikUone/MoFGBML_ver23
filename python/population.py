#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import shutil
import datetime
import random

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import AutoMinorLocator

import glob
from PIL import Image


# =============================================================================
# getAveragePopulation()
#   beforeTrialPath: trial00~が配置されているパス
#   afterTrialPathWithFile: trial00/以下のパス（対象ファイル名を含む）
#   want: 縦軸として欲しい値のkey
#   REPEAT: CV繰り返し数
#   CV: fold数
#   CRITERIA: プロットする試行数の基準（CRITERIA以上の試行数で獲得されたルール数についてプロットする）
#   MAXRULE: ルール数の最大値（横軸の最大値）
# =============================================================================
def getAveragePopulation(beforeTrialPath, afterTrialPathWithFile, want, REPEAT, CV, CRITERIA, MAXRULE):
    SEP = os.sep
    
    # Count how many trials does it obtain an individual having the ruleNum
    countTrial = {str(i+1):0 for i in range(MAXRULE)}
    # 
    values = {str(i+1):[] for i in range(MAXRULE)}
    
    ## 全試行を走査
    for rr in range(REPEAT):
        for cc in range(CV):
            trial = "trial" +str(rr)+str(cc)
    
            fileName = beforeTrialPath + SEP + trial + SEP + afterTrialPathWithFile
            df = pd.read_csv(fileName)
            # non-dominated
            df = df[df['rank'] == 0]
            
            # 本試行に各ルール数を獲得しているかどうかを判定
            for i in range(MAXRULE):
                ruleNum = i+1
                if any(df['ruleNum'].isin([ruleNum])):
                    countTrial[str(ruleNum)] += 1
            
            # 各ルール数のリストに値を追加していく
            for p in range(len(df)):
                individual = df.iloc[p]
                ruleNum = individual['ruleNum']
                value = individual[want]
                values[str(ruleNum)].append(value)
    
    # プロット用の(x, y)を生成
    x, y = [], []
    for key in list(values.keys()):
        if(countTrial[key] >= CRITERIA):
            ruleNum = int(key)
            x.append(ruleNum)
            
            array = np.array(values[key])
            y.append(array.mean())
            
    return x, y
# =============================================================================

# =============================================================================
# figSetting:
# =============================================================================
def figSetting(xMin, xMax, yMin, yMax):
    fig, axes = plt.subplots(1, 1, figsize=(7, 7))
    
    # X ticks (= Number of rule)
    xH = 10
    xticks = [i for i in range(xMin, xMax+xH, xH)]
    if xticks[0] == 0:
        xticks[0] = 1
    xMin = xticks[0]
    xMax = xticks[len(xticks)-1]
    axes.set_xlim(xMin-2, xMax+2)
    axes.set_xticks(xticks)
    
    # Y ticks
    yH = 5
    yMin = (int)(yMin / yH)
    yMin = yMin * yH
    if yMin < 0: yMin = 0
    yMax = (int)((yMax + yH)/yH)
    yMax = yMax * yH  
    if yMax > 100: yMax = 100
    
    yH = 10
    yticks = [i for i in range(yMin, yMax+yH, yH)]
    yMin = yticks[0]
    yMax = yticks[-1]
    axes.set_ylim(yMin-2, yMax+2)
    axes.set_yticks(yticks)
    
    axes.grid(linewidth=0.4)
    axes.yaxis.set_minor_locator(AutoMinorLocator(3))
    axes.tick_params(which = 'major', length = 8, color='black', labelsize=25)
    axes.tick_params(which = 'minor', length = 5, color='black', labelsize=25)
    
    return fig, axes
# =============================================================================
    
# =============================================================================
# makeFigure():
#   fig: 
#   axes: 
#   x: 
#   y: 
#   figureSetting:
# =============================================================================
def scatterAlgorithm(fig, axes, x, y, figureSetting): 
    marker = figureSetting['marker']
    color = figureSetting['color']
    size = figureSetting['size']
    label = figureSetting['algorithm']
    linewidths = 1
    edgecolors = 'black'
    alpha = 0.8
    
    axes.scatter(x, y,s=size, marker=marker, color=color, label=label, linewidths=linewidths, edgecolors=edgecolors, alpha=alpha)
    
    return fig, axes
# =============================================================================

# =============================================================================
# makeFigureSettings():
#   上から順にプロットされる
# =============================================================================
def makeFigureSettings():
    figureSettings = []
    
    figureSettings.append({"algorithm":"MOP13", "marker":"o", "color":"green", "size":200})
    figureSettings.append({"algorithm":"MOP4",  "marker":"o", "color":"dodgerblue", "size":200})
    figureSettings.append({"algorithm":"MOP1",  "marker":"o", "color":"tomato", "size":200})

    return figureSettings
# =============================================================================

# =============================================================================
# mainGIF():
# =============================================================================
def mainGIF(REPEAT, CV, CRITERIA, MAXRULE, DATASET, WANT):
    SEP = os.sep

    genMin = 0
    genMax = 5000
    interval = 100    
    generations = [i for i in range(genMin, genMax+interval, interval)]
    
    xMin = 0
    xMax = MAXRULE
    yMin = 0
    yMax = 100
    fig, axes = figSetting(xMin, xMax, yMin, yMax)

    figureSettings = makeFigureSettings()
    
    # 1. 仮ディレクトリを作成（念の為、"現在日時_乱数"を付与する）
    tmpDir = "tmp_" + datetime.datetime.now().strftime('%Y%m%d') + '_' + str(random.randint(0, 100000000))
    os.makedirs(tmpDir, exist_ok=True)
    
    # 2. 各世代の図を仮ディレクトリに出力
    print('making Materials', end='')
    for generation in generations:
        print('.', end='')
        fig, axes = figSetting(xMin, xMax, yMin, yMax)
        
        for figureSetting in figureSettings:
            algorithm = figureSetting["algorithm"]
# *****************************************************************************
            beforeTrialPath = DATASET + "_" + algorithm
            afterTrialPathWithFile = "population"+SEP+"individual" + SEP + "gen"+str(generation)+".csv"
# *****************************************************************************          
            x, y = getAveragePopulation(beforeTrialPath, afterTrialPathWithFile, WANT, REPEAT, CV, CRITERIA, MAXRULE)
            fig, axes = scatterAlgorithm(fig, axes, x, y, figureSetting)        
        figName = tmpDir + SEP + 'gen'+str(generation).zfill(10)+'.png'
        fig.savefig(figName)
    print()
        
    # 3. 各世代の図をGIF化
    print('making GIF')
    frames = []
    images = glob.glob(tmpDir + SEP + '*.png')
    images.sort()
    
    for image in images:
        new_frame = Image.open(image)
        frames.append(new_frame)
    duration = 100
    loop = 1
    gifName = DATASET+'_'+WANT + '.gif'
    frames[0].save(gifName, format='GIF', append_images=frames[1:], save_all=True, optimize=False, duration=duration, loop=loop)
        
    # 4. 仮ディレクトリを削除
    shutil.rmtree(tmpDir)
        
    print('finished.')
    return
# =============================================================================

# =============================================================================
# mainPopulation():
# =============================================================================
def mainPopulation(REPEAT, CV, CRITERIA, MAXRULE, DATASET, WANT, generation):
    SEP = os.sep
    
    xMin = 0
    xMax = MAXRULE
    yMin = 0
    yMax = 100
    fig, axes = figSetting(xMin, xMax, yMin, yMax)

    figureSettings = makeFigureSettings()
# =============================================================================
    generation = "5000"
# =============================================================================
    
    fig, axes = figSetting(xMin, xMax, yMin, yMax)
    
    for figureSetting in figureSettings:
        algorithm = figureSetting["algorithm"]
# *****************************************************************************
        beforeTrialPath = DATASET + "_" + algorithm
        afterTrialPathWithFile = "population"+SEP+"individual" + SEP + "gen"+str(generation)+".csv"
# *****************************************************************************           
        x, y = getAveragePopulation(beforeTrialPath, afterTrialPathWithFile, WANT, REPEAT, CV, CRITERIA, MAXRULE)
        fig, axes = scatterAlgorithm(fig, axes, x, y, figureSetting)   
    figType = ".png"
# *****************************************************************************
    figName = DATASET + "_" + WANT + "_" + "gen"+generation+figType
# *****************************************************************************
    fig.savefig(figName)
        
    print('finished.')
    return
# =============================================================================

# =============================================================================
REPEAT = 3
CV = 10
CRITERIA = 16
MAXRULE = 60

DATASET = "phoneme"
WANT = "Dtst"

mainGIF(REPEAT, CV, CRITERIA, MAXRULE, DATASET, WANT)
    
#generation = "5000"
#mainPopulation(REPEAT, CV, CRITERIA, MAXRULE, DATASET, WANT, generation)




    
    
    
    
    
    
    
    
    
    
    
    
    


