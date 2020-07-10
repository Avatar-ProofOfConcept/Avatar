from sklearn import svm
from sklearn.model_selection import GridSearchCV
from sklearn.svm import SVR
import numpy as np
import pandas as pd
data = pd.read_csv('test.csv',delimiter=',', header = None)
y=data[0].to_numpy()
del data[0]
X=data.to_numpy()
print(X)
parameters = {'kernel': ('linear', 'poly', 'rbf'), 'C':[1.5, 10],'gamma': [1e-7, 1e-4],'epsilon':[0.1,0.2,0.5,0.3]}
svr = svm.SVR()
clf = GridSearchCV(svr, parameters)
clf.fit(X,y)
print(clf.best_params_)

