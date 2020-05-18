import numpy as np
from tensorflow.keras.preprocessing import image
from tensorflow.keras.models import load_model
import tensorflow as tf
from keras import backend as K
from keras.models import model_from_json
import os

with open('best_model.json', 'r') as json_file:
    loaded_json = json_file.read()
model = model_from_json(loaded_json)
model.load_weights('best_model_weight.h5')

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
img_width, img_height = 224, 224

def predict_percent(model, folder_path):
    true_predict = 0
    number_of_images = 0
    for img in os.listdir(folder_path):
        class_name = img.split(' (')[0]
        img = os.path.join(folder_path, img)
        img = image.load_img(img, target_size=(img_width, img_height))
        img = image.img_to_array(img)
        img = np.expand_dims(img, axis=0)
        img /= 255.
        number_of_images += 1

        pred = model.predict(img)
        index = np.argmax(pred)
        food_list.sort()
        pred_value = food_list[index]

        if pred_value == class_name:
            true_predict += 1
    print(true_predict/number_of_images)


folder_path = './validate/'
predict_percent(model, folder_path)

