#get image from Nodejs
import sys 
# Takes first name and last name via command 
# line arguments and then display them 
image_path = sys.argv[1]

#predict image
from keras.models import load_model
from keras.preprocessing import image
import numpy as np

classifier = load_model('best_model.hdf5')

img = image.load_img(image_path, target_size=(64,64))

img = image.img_to_array(img)
img = np.expand_dims(img, axis=0)
result = classifier.predict(img)
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

print(result)
