# OperonHunter

* The directory **images_ecoli/** contains images used to train a neural network and test its performance on the E. coli genome. 
* The directory **images_bsubtilis/** contains images used to train a neural network and test its performance on the B. subtilis genome. 
* The directory **genomes/** contains the genome files used in our method. 
* The directory **strings/** contains string scores used in our method. 


<h1> Neural Network Training and Testing </h1> 

We use the Fastai library to train our model. The script **train.py** demonstrates an example training a *ResNet18* on the images_ecoli/ dataset. It saves the best model observed during training. 

The script **test.py** loads one of the previously saved models and tests it on images found in the **test/** directory under the **images_ecoli/** directory. It outputs two files: **operon_pegs.txt** and **noperon_pegs.txt** representing operon pairs and non-operon pairs respectively. Each line in the output can be thought of as a gene pair comprising the listed gene peg and its immediate successor in that genome. 

One pre-trained model is provided in the **models/** directory under the **images_ecoli/** directory. 

Hyper-parameter fine tuning was performed manually. 

<h1> Image Generation </h1> 

The following steps were performed to generate the images:

1. Call the Compare Region Viewer service provided by PATRIC. An example command is:
    curl --max-time 300 --data-binary '{\"method\": \"SEED.compare\_regions\_for\_peg\", \"params\": [\"$peg\", 5000, 20, \"pgfam\", \"representative+reference\"], \"id\": 1}'         https://p3.theseed.org/services/compare\_region"
    Where $peg is the query gene of interest. Repeat this call for all genes/pegs of interest and place all the output jsons in one folder (let's call it **input\_jsons**). 
1. Run the program **JsonToCoordinates.py** with **input\_jsons** as input to parse the JSON files into a different format to be used by the image generating software. The resulting file will be **oc.txt**, which is the input to **CoordsToJpg.java**. 
    This script needs to be in the same directory as **genomes/** and **strings/** 
1. Compile and run the Java program **CoordsToJpg.java** which will convert the coordinate file into images. 
1. Split your images into the appropriate classes. An example folder has been provided (**images_ecoli/**) which contains a training and testing directory and the images that can be used to train a model and test it on the E. coli genome. 

<hr/>

To cite this work, please use:

Assaf, R., Xia, F. & Stevens, R. Detecting operons in bacterial genomes via visual representation learning. Sci Rep 11, 2124 (2021). https://doi.org/10.1038/s41598-021-81169-9

