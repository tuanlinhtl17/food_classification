import numpy as np
from tensorflow.keras.preprocessing import image
from tensorflow.keras.models import load_model
import tensorflow as tf
from keras import backend as K

K.clear_session()

# get image from Nodejs
import sys
# Takes first name and last name via command
# line arguments and then display them
image_path = sys.argv[1]

# predict image

model = load_model('best_model.hdf5')

food_list = [
  'apple_pie',
  'beef_carpaccio',
  'carrot_cake',
  'donuts',
  'hot_dog',
  'samosa',
  'pho',
  'pizza',
  'omelette', 
  'sushi',
  'baklava',
  'beet_salad',
  'cup_cakes',
  'bibimbap',
  'pad_thai'
]

img = image.load_img(image_path, target_size=(224, 224))
img = image.img_to_array(img)
img = np.expand_dims(img, axis=0)
img /= 255.

pred = model.predict(img)
index = np.argmax(pred)
food_list.sort()
pred_value = food_list[index]

print(pred_value)
