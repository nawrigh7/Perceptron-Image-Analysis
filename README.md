# Perceptron-Image-Analysis
Design and Implementation of a perceptron to utilize machine learning for image analysis.

# Motivation
To combine learning (perceptrons) with clustering (agglomerative) similarity measures. This program trains perceptrons based on a training set of (depth) images, and then defines a distance measure based on those perceptrons.
It will then use that distance measure to cluster a test set of (depth) images. 

# Task 
This program expects three inputs. The first is the training set, expressed as a file of depth image file names. The second is the test set, also expressed as a file of (usually different) depth image names.  The third argument is K, the number of clusters for the program to make. It is assumed that K is an integer greater than zero.
# Methodology
- First, use the training images (i.e. the images listed in the first argument file) to train perceptrons. This will train one perceptron for every class label in the training set. In other words, if all the training samples are from class1 or class2, you will train 2 perceptrons, one for each class, and so forth.
- Each perceptron will be trained for 100 epochs.
- Once it has trained the perceptrons, your program will cluster the images in the test set into K clusters using these perceptrons. In particular, assume that the training data has N classes, and that therefore your program has trained N perceptrons. Let $y_{n,i}$ be the score returned by perceptron n on test image i. Then the similarity between two test images i and j is:

$sim(i,j) = \sum_{n=1}^{N} (\frac{1}{(y_{n,i} - y_{n,j})^2})$

- Using this similarity measure, this program uses agglomerative clustering to cluster the test images into K clusters.
