from fastai.vision import *
from fastai.callbacks import *

r_seed = 42
import os
os.environ['PYTHONHASHSEED']=str(r_seed)
import random
random.seed(r_seed)
from numpy.random import seed
seed(r_seed)
import torch
torch.manual_seed(r_seed)
torch.backends.cudnn.deterministic = True
torch.backends.cudnn.benchmark = False
if torch.cuda.is_available(): torch.cuda.manual_seed_all(r_seed)

path = 'pair_w224/'
tfms = get_transforms(do_flip=True,flip_vert=False,max_rotate=0,max_zoom=1,max_lighting=None,max_warp=None,p_affine=0,p_lighting=0)
data = ImageDataBunch.from_folder(path,ds_tfms=tfms, bs=64, seed=r_seed)
learn = cnn_learner(data, models.resnet18, metrics=accuracy)
learn.unfreeze() # Comment this line when training on the E. coli data
learn.fit_one_cycle(50, max_lr=0.03, callbacks=[SaveModelCallback(learn, every='improvement', monitor='accuracy', name='224_model')])
