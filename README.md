# Perceptron-Image-Analysis
Design and Implementation of a perceptron to utilize machine learning for image analysis.

# Motivation
- To combine learning (perceptrons) with clustering (agglomerative) similarity measures. This program trains perceptrons based on a training set of (depth) images, and then defines a distance measure based on those perceptrons.
It will then use that distance measure to cluster a test set of (depth) images.
- Familiarize myself with another style of documentation in JavaDocs.
- Make use of gradle as a build tool
- Utilize JUnit testing, and evaluate code coverage using jacoco

# Definition: Perceptron
- Perceptron - The simplest type of artificial neural network, perceptrons act as a binary classifier which maps multiple inputs into a single output value.
- Perceptrons will take in n inputs ($x_{1}$, $x_{2}$, ..., $x_{n}$) and qualify them with n weights ($w_{1}$, $w_{2}$, $w_{n}$), often include a bias (b).
- The perceptron will then calculate the weighted sum of inputs + bias as $\sum$ $w_{i}$ $x_{i}$ + $b$

# Task 
This program expects three inputs. The first is the training set, expressed as a file of depth image file names. The second is the test set, also expressed as a file of (usually different) depth image names.  The third argument is K, the number of clusters for the program to make. It is assumed that K is an integer greater than zero.
# Methodology
- First, use the training images (i.e. the images listed in the first argument file) to train perceptrons. This will train one perceptron for every class label in the training set. In other words, if all the training samples are from class1 or class2, you will train 2 perceptrons, one for each class, and so forth.
- Each perceptron will be trained for 100 epochs.
- Once it has trained the perceptrons, your program will cluster the images in the test set into K clusters using these perceptrons. In particular, assume that the training data has N classes, and that therefore your program has trained N perceptrons. Let $y_{n,i}$ be the score returned by perceptron n on test image i. Then the similarity between two test images i and j is:

$sim(i,j) = \sum_{n=1}^{N} (\frac{1}{(y_{n,i} - y_{n,j})^2})$

- Using this similarity measure, this program uses agglomerative clustering to cluster the test images into K clusters.

# Results
I developed this incrementally, beginning with basics and building upon them with each iteration. I used gradle to build and manage packages, and Git / Github for version control. The development pipeline was as such:
```text
Create Histograms
From image data files
     │
     ▼
Normalize Histograms &
  Compare
     │
     ▼
Create Normalized Histograms
From image files & compare
     │
     ▼
Compare sets of images,
Finding the closest match
     │
     ▼
Cluster similar images
using Agglomerative Clustering
     │
     ▼
Introduce different comparison
methods (NormHist, NormHist4, and
InvSquareDiff)
     │
     ▼
Add image categorization based
on description field to
comparison consideration &
cluster evaluation
     │
     ▼
Create Perceptron and practice
training them
     │
     ▼
Train Perceptrons on set of images, then
use them to cluster input images by
comparison and distance measures
```
The final product achieved:
- 95% testing coverage (line, branch, and path)
- Full JavaDoc documentation
- Perceptron classes with learning capabilities, used to calculate distance measures from training set
- Minimal run-time

The next steps are to link this into other types of images and external APIs. I plan to use an open database such as NASA's to use a set of images to train, and then find the most similar images from the database and effectively "sort" the images. I could also see this used as a tool for custom "slideshow" mechanics such as with desktop images.
