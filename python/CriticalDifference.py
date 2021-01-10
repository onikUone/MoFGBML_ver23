#!/usr/bin/env python3
# -*- coding: utf-8 -*-


import Orange 
import matplotlib.pyplot as plt
import random
import pandas as pd
import numpy as np
import scipy.stats as stats

random.seed(0)
names = ["algorithm1", "algorithm2", "algorithm3"]
trial = 30

'''
# データ作成
必要なデータ構造：name, dic
name = ["algorithm1", "algorithm2", ..., "algorithmN"]
dic = {"algorithm1":[trial00, trial01, trial02, ..., trial29],
       "algorithm2":[trial00, trial01, trial02, ..., trial29],
       ...,
       "algorithmN":[trial00, trial01, trial02, ..., trial29]
       }
'''
dic = {}
for name in names:
    dic[name] = []
    for i in range(trial):
        dic[name].append(random.random())

'''
# Friedman-test
まずはフリードマン検定で多群間に有意差が存在するかどうかを検定する
有意差が存在する場合、*付きでp_valueが出力される
上のdicをnumpy.arrayに変換している
'''
array = []
for name in names:
    array.append(dic[name])
array = np.array(array)

ranks = {}
for name in names:
    ranks[name] = []
level = 0.05
statistic, p_value = stats.friedmanchisquare(*(array[i,:] for i in range(array.shape[0])))
if(p_value<level):
    print('p-value = '+str(p_value)+'*')
else:
    print('p-value = '+str(p_value))

'''
# Critical Difference
各アルゴリズムの平均ランクを計算する → avranks = []
平均ランクよりCDを求めてプロットする
ascending = True:最小化, False:最大化
'''
ascending=False
for i in range(trial):
    l = {}
    for name in names:
        l[name] = dic[name][i]
    series = pd.Series(l)
    for name in names:
        ranks[name].append(series.rank(ascending=ascending)[name])
df = pd.DataFrame(ranks)
avranks = []
for key in list(ranks.keys()):
    avranks.append(df[key].mean())
    
#names = ["1", "2", "3", "4", "5"]
#avranks = [1.89, 2.00, 3.85, 3.2, 4.0]

'''
引数:
    n: 平均ランクを計算した試行数
    alpha: 有意水準 "0.05", "0.1"
    test: "nemenyi", "bonferroni-dunn"
'''
cd = Orange.evaluation.compute_CD(avranks, n=trial, alpha="0.05", test="nemenyi") #tested on 30 datasets 
print("cd=",cd)
'''
引数:
    cdmethod: namesの内どのアルゴリズムを中心に描画するか
    filename: 図の出力パス
    reverse: False:左がrank1, True:右がrank1
'''
Orange.evaluation.graph_ranks(avranks, names, cd=cd, width=5, textspace=1.5)
plt.show();






