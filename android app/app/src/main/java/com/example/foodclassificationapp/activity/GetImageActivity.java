package com.example.foodclassificationapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.foodclassificationapp.service.SingleUploadBroadcastReceiver;
import com.example.foodclassificationapp.constant.Constant;
import com.example.foodclassificationapp.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


public class GetImageActivity extends AppCompatActivity implements SingleUploadBroadcastReceiver.Delegate {

    private static final String TAG = "AndroidUploadService";
    private static final String IMAGE_DIRECTORY_NAME = "FoodImages";
    private SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();

    private Uri fileUri;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
//        Log.i(TAG, "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.CAMERA_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // classify image
                uploadMultipart(getApplicationContext());
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
                captureImage();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        String documentId = cursor.getString(0);
        documentId = documentId.substring(documentId.lastIndexOf(':') + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{documentId}, null);
        Objects.requireNonNull(cursor).moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
//        System.out.println(path);
        return path;
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Classifying Food");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(2); // Incremented By Value 2
            }
        };
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (progress < progressDialog.getMax()) {
                        Thread.sleep(200);
                        handle.sendMessage(handle.obtainMessage());
                        if (progress == progressDialog.getMax()) {
                            progressDialog.dismiss();
                        }
                    }
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

            Toast.makeText(getApplicationContext(), "Classify Successfully",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), FruitInfoActivity.class);

            String foodName = response.getString("name");
            intent.putExtra("foodCamera", foodName);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled() {
        // do nothing
    }
}
