#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import pandas as pd
import numpy as np

# =============================================================================
# getAllTrialValues:
# =============================================================================
def getBestIndividuallValues(beforeTrialPath, afterTrialPathWithFile, CRITERIA, WANT, REPEAT, CV):
    SEP = os.sep
    
    byWhat = CRITERIA[0]
    order = CRITERIA[1]
    
    values = []
    for rr in range(REPEAT):
        for cc in range(CV):
            trial = "trial" + str(rr)+str(cc)
            fileName = beforeTrialPath + SEP + trial + SEP + afterTrialPathWithFile
            df = pd.read_csv(fileName)
            # non-dominated
            df = df[df['rank'] == 0]
            best = df.sort_values(by=byWhat, ascending=order).iloc[0]
            value = best[WANT]
            values.append(value)
    array = np.array(values)
    return array
# =============================================================================

# =============================================================================
# getSeriesFromSetting
# =============================================================================
def getSeriesFromSetting(dataset, columns, beforeTrialPath, afterTrialPathWithFile, CRITERIA, WANT, REPEAT, CV):
    dic = {}
    for key in columns:
        array = getBestIndividuallValues(beforeTrialPath, afterTrialPathWithFile, CRITERIA, WANT, REPEAT, CV)
        dic[key] = array.mean()
    series = pd.Series(dic, name=dataset)
    return series
# =============================================================================

# =============================================================================
# makeSetting:
#   settingsに追加した設定が列として出力される
#   出力されるcsvの表の列を追加したい場合は、settings.append()の行を追加して、各項目の設定を指定する
# =============================================================================
def makeSettings():
    settings = []
    
    settings.append({"algorithm":"MOP1",  "want":"Dtra" })
    settings.append({"algorithm":"MOP4",  "want":"Dtra" })
    settings.append({"algorithm":"MOP13", "want":"Dtra" })
    
    settings.append({"algorithm":"MOP1",  "want":"Dsubtra" })
    settings.append({"algorithm":"MOP4",  "want":"Dsubtra" })
    settings.append({"algorithm":"MOP13", "want":"Dsubtra" })
    
    settings.append({"algorithm":"MOP1",  "want":"Dvalid" })
    settings.append({"algorithm":"MOP4",  "want":"Dvalid" })
    settings.append({"algorithm":"MOP13", "want":"Dvalid" })
    
    settings.append({"algorithm":"MOP1",  "want":"Dtst" })
    settings.append({"algorithm":"MOP4",  "want":"Dtst" })
    settings.append({"algorithm":"MOP13", "want":"Dtst" })
    
    settings.append({"algorithm":"MOP1",  "want":"ruleNum" })
    settings.append({"algorithm":"MOP4",  "want":"ruleNum" })
    settings.append({"algorithm":"MOP13", "want":"ruleNum" })
    
    columns = []
    for setting in settings:
        mop = setting["algorithm"]
        want = setting["want"]
        columns.append(want+"_"+mop)
    return settings, columns
# =============================================================================

def makeTable(DATASETs, GENERATION, CRITERIA, REPEAT, CV):
    SEP = os.sep
    
    # make DataFrame (setting is not used at this phase)
    setting, columns = makeSettings()
    df = pd.DataFrame(columns=columns)
    
    for dataset in DATASETs:
        print(dataset, end='')
        # 1. makeSetting
        settings, columns = makeSettings()
        # 2. getSeries of dataset
        dic = {}
        for setting, key in zip(settings, columns):
            print('.', end='')
            mop = setting["algorithm"]
            want = setting["want"]
# *****************************************************************************
            beforeTrialPath = dataset + "_" + mop
            afterTrialPathWithFile = "population"+SEP+"individual"+SEP+ "gen"+GENERATION+".csv"
# *****************************************************************************
            array = getBestIndividuallValues(beforeTrialPath, afterTrialPathWithFile, CRITERIA, want, REPEAT, CV)
            dic[key] = array.mean()
        print()
        series = pd.Series(dic, name=dataset)
        # 3. append sereis of dataset into DataFrame
        df = df.append(series)
        
        # 4. save table
        fileName = "criteria-"+CRITERIA[0] + "_" + "gen-"+GENERATION + ".csv"
        df.to_csv(fileName)
    return df
    
# =============================================================================
# main():
# =============================================================================
def main(GENERATION, CRITERIA):
    REPEAT = 3
    CV = 10
    
    DATASETs = []
    DATASETs.append("phoneme")
    
    makeTable(DATASETs, GENERATION, CRITERIA, REPEAT, CV)    
    return
# =============================================================================

GENERATION = "5000"

#CRITERIA = ["Dtra", True]
CRITERIA = ["Dvalid", True]

main(GENERATION, CRITERIA)
    
    
