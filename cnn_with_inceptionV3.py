# Importing the Keras libraries and packages
from keras.models import Sequential, Model
from keras.layers import Convolution2D
from keras.layers import GlobalAveragePooling2D
from keras.layers import Dense
from keras.layers import Dropout
from keras.callbacks import ModelCheckpoint, EarlyStopping, ReduceLROnPlateau
from keras.regularizers import l2
from keras.applications.inception_v3 import InceptionV3
from keras.optimizers import SGD

# picking 15 food items and generating separate data folders for the same
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
final_output_dim = len(food_list)

# config variable for training
train_data_dir = './dataset/train'
test_data_dir = './dataset/test'

img_width, img_height = 224, 224
nb_train_samples = 12000
nb_test_samples = 3000
batch_size = 64

# create model
def create_model():
  base_model = InceptionV3(include_top=False, weights='imagenet', input_shape = (img_width,img_height,3))
  model = base_model.output

  model = Convolution2D(filters = 32, kernel_size = (3,3), strides = 2, padding = 'Same', activation ='relu', input_shape = (img_width,img_height,3), kernel_initializer='he_normal')(model)
  model = Dropout(0.2)(model)

  model = Convolution2D(filters = 64, kernel_size = (3,3),padding = 'Same', activation ='relu',kernel_initializer='he_normal')(model)
  model = Dropout(0.2)(model)

  model = GlobalAveragePooling2D()(model)
  model = Dropout(0.4)(model)
  
  model = Dense(final_output_dim, activation = "softmax",kernel_initializer='he_normal',kernel_regularizer=l2())(model)
  model = Model(input=base_model.input, output=model)
  return model

#callbacks
checkpointer = ModelCheckpoint(filepath='model.hdf5', monitor='val_accuracy', verbose=2, save_best_only=True, save_weights_only=False)
earlystopping = EarlyStopping(monitor='val_loss', patience=5, mode='max')

model = create_model()
model.compile(optimizer = SGD(lr=0.01), loss = "categorical_crossentropy", metrics=["accuracy"])

# training model
from keras.preprocessing.image import ImageDataGenerator

train_datagen = ImageDataGenerator(featurewise_center=False,
                 samplewise_center=False,
                 featurewise_std_normalization=False,
                 samplewise_std_normalization=False,
                 zca_whitening=False,
                 rotation_range=10,
                 width_shift_range=0.05,
                 height_shift_range=0.05,
                 shear_range=0.1,
                 zoom_range=0.2,
                 channel_shift_range=0.,
                 fill_mode='nearest',
                 cval=0.,
                 horizontal_flip=True,
                 vertical_flip=False,
                 rescale=1/255)
#rescale to [0-1], add zoom range of 0.2x, width and height shift and horizontal flip

train_generator = train_datagen.flow_from_directory(
        train_data_dir,
        target_size=(img_width,img_width),       # resize images to (224,224) to increase the training speed and efficiency
        batch_size=batch_size)

test_datagen = ImageDataGenerator(rescale=1./255)    # rescale to [0-1] for testing set

test_generator = test_datagen.flow_from_directory(
        test_data_dir,
        target_size=(img_width,img_height),
        batch_size=batch_size)

history = model.fit_generator(
    train_generator,
    steps_per_epoch=nb_train_samples/batch_size,
    validation_data=test_generator,
    validation_steps=nb_test_samples/batch_size, 
    epochs=50,
    callbacks=[checkpointer, earlystopping]
)
