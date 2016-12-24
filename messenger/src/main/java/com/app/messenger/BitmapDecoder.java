//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.util.GlobalUtills;

//------------------------------------------------------------------------------------------------------------------------------
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


//==============================================================================================================================
public class BitmapDecoder
{
    //--------------------------------------------------------------------------------------------------------------------------
    public static Uri getTemporaryUri()
    {
        return Uri.fromFile(createTemporaryFile());
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static Bitmap getFromData(int requestCode, int resultCode, Intent data, ContentResolver contentResolver)
    {
        Bitmap bitmap = null;

        switch (requestCode)
        {
            case GlobalConstant.REQUEST_CODE_22:
                if (resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        File             tempFile     = createTemporaryFile();
                        String           tempFileName = temporaryFilePath();
                        InputStream      inputStream  = contentResolver.openInputStream(data.getData());
                        FileOutputStream outputStream = new FileOutputStream(tempFileName);

                        globalUtills_.copyStream(inputStream, outputStream);

                        outputStream.close();
                        inputStream.close();

                        bitmap = decodeFile(tempFileName);

                        if (tempFile.exists())
                            tempFile.delete();
                    }
                    catch(Exception error)
                    {
                        error.printStackTrace();
                    }
                }

                break;

            case GlobalConstant.REQUEST_CODE_1888:
                try
                {
                    File   tempFile = createTemporaryFile();
                    String filePath = temporaryFilePath();

                    bitmap = decodeFile(filePath);

                    if (tempFile.exists())
                        tempFile.delete();
                }
                catch(RuntimeException error)
                {
                    error.printStackTrace();
                }
                catch(Exception error)
                {
                    error.printStackTrace();
                }

                break;
        }

        return bitmap;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static Bitmap decodeFile(String filePath)
    {
        int scale = getAproptiateScale(filePath);

        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, option);

        try
        {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int           orientation   = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                                        ExifInterface.ORIENTATION_NORMAL);
            Matrix        matrix        = new Matrix();

            final float DEGREE_90  =  90;
            final float DEGREE_180 = 180;
            final float DEGREE_270 = 270;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                matrix.postRotate(DEGREE_90);

            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                matrix.postRotate(DEGREE_180);

            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                matrix.postRotate(DEGREE_270);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch(Throwable error)
        {
            error.printStackTrace();
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        final int JPEG_QUALITY = 100;

        bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outStream);

        return bitmap;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static int getAproptiateScale(String filePath)
    {
        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, option);

        final int REQUIRED_SIZE   = 1024;
              int maximalSideSize = Math.max(option.outWidth, option.outHeight);
              int scale           = 1;

        if (maximalSideSize > REQUIRED_SIZE)
            scale = (int) Math.pow(2, Math.ceil(Math.log((double) maximalSideSize / REQUIRED_SIZE) / LOG_2));

        return scale;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static File createTemporaryFile()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            try
            {
                File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);

                file.createNewFile();

                return file;
            }
            catch(IOException error)
            {
            }
        }

        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static String temporaryFilePath()
    {
        return Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";
    private static final double LOG_2           = Math.log(2);

    //--------------------------------------------------------------------------------------------------------------------------
    private static GlobalUtills globalUtills_ = new GlobalUtills();  // Globals can be static by themselves
}
