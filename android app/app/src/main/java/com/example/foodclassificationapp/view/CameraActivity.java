package com.example.foodclassificationapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.foodclassificationapp.util.SingleUploadBroadcastReceiver;
import com.example.foodclassificationapp.util.Constant;
import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.view.main.MainActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class CameraActivity extends AppCompatActivity implements SingleUploadBroadcastReceiver.Delegate {

    private static final String TAG = "AndroidUploadService";
    private static final String IMAGE_DIRECTORY_NAME = "FoodImages";
    private SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();

    private Uri fileUri;

    private static final Map<String, String> keyFood = new HashMap<>();

    static {
        keyFood.put("apple_pie", "Apple Pie");
        keyFood.put("beef_carpaccio", "Beef Carpaccio");
        keyFood.put("carrot_cake", "Carrot Cake");
        keyFood.put("donuts", "Donuts");
        keyFood.put("hot_dog", "Hot Dog");
        keyFood.put("samosa", "Samosas");
        keyFood.put("pho", "Pho");
        keyFood.put("pizza", "Pizza");
        keyFood.put("omelette", "Omelet");
        keyFood.put("sushi", "Sushi");
        keyFood.put("baklava", "Baklava");
        keyFood.put("beet_salad", "Beet Salad");
        keyFood.put("cup_cakes", "Cup Cakes");
        keyFood.put("bibimbap", "Bibimbap");
        keyFood.put("pad_thai", "Pad Thai");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_info);
        captureImage();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, Constant.CAMERA_IMAGE_REQUEST_CODE);
    }

    private Uri getOutputMediaFileUri() {
        Log.i(TAG, "getOutputMediaFileUri()");
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                    + IMAGE_DIRECTORY_NAME + " directory");
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart(final Context context) {

        //getting the actual path of the image
        String path = fileUri.getPath();

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Constant.CLASSIFY_URL)
                    .addFileToUpload(path, "upload")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CAMERA_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // classify image
                uploadMultipart(getApplicationContext());
            } else if (resultCode == RESULT_CANCELED) {
                // cancel
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
                onBackPressed();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    @Override
    public void onProgress(final int progress) {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Classifying...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(16);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        @SuppressLint("HandlerLeak") final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(1); // Incremented By Value 1
            }
        };
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (progress < progressDialog.getMax()) {
                        Thread.sleep(150);
                        handle.sendMessage(handle.obtainMessage());
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    Log.d("onProgress", e.toString());
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        // do nothing
    }

    @Override
    public void onError(Exception exception) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Error!");
        builder.setMessage("Have an error while classify food. Please retry!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                uploadMultipart(getApplicationContext());
            }
        });
        builder.setNegativeButton("Other Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                captureImage();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        try {
            JSONObject response = new JSONObject(new String(serverResponseBody));
            Intent intent = new Intent(this, FruitInfoActivity.class);

            String foodName = keyFood.get(response.getString(Constant.NAME));
            String path = fileUri.getPath();
            intent.putExtra(Constant.FOOD_CAMERA, foodName);
            intent.putExtra("imgPath", path);
            startActivity(intent);
        } catch (JSONException e) {
            Log.d("CameraActivity", e.toString());
        }
    }

    @Override
    public void onCancelled() {
        // do nothing
    }
}
