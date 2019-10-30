# -*- coding: utf-8 -*-
"""
Created on Thu Sep 26 08:03:13 2019

@author: westl
"""
#get image from Nodejs
import sys 
# Takes first name and last name via command 
# line arguments and then display them 
image_path = sys.argv[1]

#predict image
from keras.models import load_model
from keras.preprocessing import image
import numpy as np

classifier = load_model('food_classifier.h5')

# image_path="test_manual/011.jpg"

img = image.load_img(image_path, target_size=(64,64))

img = image.img_to_array(img)
img = np.expand_dims(img, axis=0)
result = classifier.predict(img)
foods = ["Banh mi", "Bun cha", "com", "Dau ran", "Ga luoc", "Nem ran", "Pho bo", "Pho cuon", "Trung ran", "Vit quay", "Xoi xeo"]

print(result)
