from fastai.vision import *

path = 'images_ecoli/'
# Load the model
learn = cnn_learner(data, models.resnet18, metrics=accuracy)
learn = learn.load('best511')

# Load the data and predict Operon-pairs
data = ImageDataBunch.from_folder(path, test='test_operons/')
output = open('operon_pegs.txt', 'w')
c = 0
for i in data.test_ds:
	p = learn.predict(i[0])
	filename = str(data.test_ds.items[c]).split('/')[-1]
	fname = filename.split('_')[0]
	if str(p[0]) == 'Operons':
		output.write(fname + '\n')
	c += 1
output.close()

# Load the data and predict non-Operon-pairs
data = ImageDataBunch.from_folder(path, test='test_noperons/')
output = open('noperon_pegs.txt', 'w')
c = 0
for i in data.test_ds:
	p = learn.predict(i[0])
	filename = str(data.test_ds.items[c]).split('/')[-1]
	fname = filename.split('_')[0]
	if str(p[0]) == 'Noperons':
		output.write(fname + '\n')
	c += 1
output.close()
