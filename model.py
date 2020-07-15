from sklearn import svm
from sklearn.svm import SVR
import numpy as np
import pandas as pd
import sys

data = pd.read_csv(sys.argv[1],delimiter=',', header = None)
y=data[0].to_numpy()
del data[0]
X=data.to_numpy()
regressor = SVR(kernel='linear', epsilon=0.1,C=1.5 )
regressor.fit(X,y)
new=np.insert(np.ravel(regressor.coef_, order='C'),0,regressor.intercept_[0])
str=np.array_str(new).replace('\n', '')

print(str)
