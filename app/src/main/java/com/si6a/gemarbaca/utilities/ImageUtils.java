package com.si6a.gemarbaca.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ImageUtils {
    public static String convertImageToBase64(Context context, Uri uri) {
        String result = "";
//        try {
//            // Obtain InputStream from Uri
//            InputStream inputStream = context.getContentResolver().openInputStream(uri);
//
//            // Read InputStream and encode to Base64
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            String base64Image = Base64.encodeToString(byteArray, Base64.URL_SAFE | Base64.CRLF);
//
//            // Close InputStream
//            inputStream.close();
//
//            // Use the base64Image as needed
////            result = "data:image/png;base64," + base64Image;
//            result = base64Image;
//
//            return result;
//
//        } catch (IOException e) {
//            // Handle exception
//        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] bytes = stream.toByteArray();
            result = Base64.encodeToString(bytes, Base64.URL_SAFE);

        } catch (IOException e) {
//            throw new RuntimeException(e);
        }
        return result;
    }

//    public static Bitmap convertBase64ToBitmap(String base64Image){
////        byte[] imageByte = Base64.decode("data:image/png;base64," +base64Image, Base64.DEFAULT);
//        byte[] imageByte = Base64.decode(base64Image, Base64.URL_SAFE);
//        return BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
//    }

    public static Uri convertBase64ToUri(Context context, String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.URL_SAFE);
        String fileName = System.currentTimeMillis() + ".png"; // You can change the file name and extension according to your requirements
        File outputFile = new File(context.getCacheDir(), fileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(decodedBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(outputFile);
    }

}
