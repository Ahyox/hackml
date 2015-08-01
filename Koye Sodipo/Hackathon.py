import pandas as pd
import numpy as np
from sklearn.feature_extraction import DictVectorizer as DV
from sklearn import ensemble
from sklearn.preprocessing import Imputer
from sklearn.cross_validation import train_test_split


train  = pd.read_csv('C:/Users/Koye/Desktop/Kaggle/HotelsNG/hotelsng.csv')
Y = train['cancelled']

ccolumns = [ 'client_title','made_on_behalf', 'country', 'hotelname','cab_service_req','is_phone_booking','hotel_state',]
cat_train = train[ccolumns]

train = train.drop(['phone', 'email_address','additional_info','ip_address','cancelled','customer_cancelled_by_self' ], axis=1)
train = train.drop(['time','checkin','checkout'],axis=1)
num_train = train.drop(ccolumns, axis=1).as_matrix()

vectorizer = DV(sparse = False)
cat_dict = cat_train.T.to_dict().values()

cat_train_final = vectorizer.fit_transform(cat_dict)


x_train = np.hstack((num_train, cat_train_final))


imp = Imputer(missing_values='NaN', strategy='mean', axis=0)
x_train = imp.fit_transform(x_train)

x_train, x_test, Y_train, Y_test = train_test_split(x_train, Y, test_size=0.10, random_state=42)

rf = ensemble.RandomForestRegressor(n_estimators=100, max_depth=9)
rf.fit(x_train, Y_train)

preds  = rf.predict(x_test)

for cnt in xrange(0,len(preds)):
    preds[cnt] = round(preds[cnt])

compare = []
#assert(len(self.y2)==len(preds))
for cnt in xrange(0,len(preds)):
    compare.append( 1 if preds[cnt] == Y_test[cnt] else 0 )

acc = float(sum(compare)) / float(len(compare))

print "Accuracy on test set: "+str(acc)
