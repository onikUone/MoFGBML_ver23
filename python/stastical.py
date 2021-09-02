# =============================================================================
import pandas as pd
from scipy import stats
# =============================================================================

dataName = "CAL500"

objectives = []
objectives.append("ExactMatchError_Dtst")
objectives.append("Fmeasure_Dtst")
objectives.append("HammingLoss_Dtst")

algorithms = []
algorithms.append("CFmean_MOP_SubAcc")
algorithms.append("CFmean_MOP_HL")
algorithms.append("CFmean_MOP_FM")
algorithms.append("CFvector_MOP_SubAcc")
algorithms.append("CFvector_MOP_HL")
algorithms.append("CFvector_MOP_FM")
algorithms.append("old_CFvector_MOP_SubAcc")
algorithms.append("old_CFvector_MOP_HL")
algorithms.append("old_CFvector_MOP_FM")
algorithms.append("BR")
algorithms.append("LP")
algorithms.append("MLC45")
algorithms.append("FDTML")
algorithms.append("MLRBC")

objective = 'ExactMatchError' + '_Dtst'
objective = 'HammingLoss' + '_Dtst'
objective = 'Fmeasure' + '_Dtst'
fileName = dataName + "/" + dataName + "_" + objective + ".csv"
df = pd.read_csv(fileName)

p = 0.05

base = 'CFmean_MOP_FM'

for compare in algorithms:
    result = stats.wilcoxon(df[base], df[compare])
    #result = stats.mannwhitneyu(df[base], df[compare])
    #result = stats.ttest_rel(df[base], df[compare])

    if result.pvalue < p:
        print(compare + ": Different")
    else:
        print(compare + ": Not")



