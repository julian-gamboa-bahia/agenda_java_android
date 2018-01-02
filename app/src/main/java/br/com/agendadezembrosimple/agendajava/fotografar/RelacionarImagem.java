package br.com.agendadezembrosimple.agendajava.fotografar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;

import br.com.agendadezembrosimple.agendajava.R;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class RelacionarImagem extends AppCompatActivity {

    final int RESULT_LOAD_IMG = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_ANTIGO = 116;
    private static final String IMAGE_DIRECTORY_NAME = "FotosAgendaJava";

    private Uri fileUri; // file url to store image/video
    private Uri photoURI; // file url to store image/video

    String mCurrentPhotoPath;

    String nome_dataIMAGEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relacionar_imagem);
    }


// Dezemebro22

    public void foto_desde_camara(View v) {

        Random rand = new Random();
        int n = rand.nextInt(1000000); // Gives n such that 0 <= n < 20

        String agora = new Time(System.currentTimeMillis()).toString();

        Date d = new Date();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", d); // Thursday
        String day = (String) DateFormat.format("dd", d); // 20
        String monthString = (String) DateFormat.format("MMM", d); // Jun
        String year = (String) DateFormat.format("yyyy", d); // 2013


        nome_dataIMAGEM = dayOfTheWeek + "_" + day + "_" + monthString + "_" + year +"_" + agora + "_" + n;

        Toast.makeText(getBaseContext(),"Será grabado como \n"+nome_dataIMAGEM,Toast.LENGTH_LONG).show();


        if (Build.VERSION.SDK_INT >= 25) {
            dispatchTakePictureIntent(v);
        }
        else
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE_ANTIGO);
        }
    }
//Dezembro22
    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFileNOVO();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
    //Uri photoURI = FileProvider.getUriForFile(this,"br.com.exampleprovider.exampleprovider.fileprovider",photoFile); //br.com.exampleprovider.exampleprovider.fileprovider
                if (this.getApplicationContext().getPackageName() != null) {
                    Log.d("Dezembro22", "\n" + this.getApplicationContext().getPackageName() + ".fileprovider");
                }

                //Uri photoURI = FileProvider.getUriForFile(this, "br.com.supermercado.super_mercado.fileprovider", photoFile);
                //Uri
                photoURI = FileProvider.getUriForFile(this, "br.com.supermercado.super_mercado.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);// REQUEST_TAKE_PHOTO);
            }
        }
    }
//Dezembro22
    private File createImageFileNOVO() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
//Dezembro22
    public Uri getOutputMediaFileUri(int type) {
    //createImageFile());
        if (Build.VERSION.SDK_INT >= 25) {
            Uri photoURI = FileProvider.getUriForFile(getBaseContext(), getBaseContext().getApplicationContext().getPackageName() + "br.com.supermercado.super_mercado.provider", getOutputMediaFile(type));
            return photoURI;
        }
        else
        {
            return Uri.fromFile(getOutputMediaFile(type));
        }

    }

///////////////////////////////////////////////////////Camara


//Dezembro22
        /*
 * returning image / video
 */
private static File getOutputMediaFile(int type) {

    // External sdcard location
    File mediaStorageDir = new File(
            Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            IMAGE_DIRECTORY_NAME);

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists()) {
        if (!mediaStorageDir.mkdirs()) {

            return null;
        }
    }

    // Create a media file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(new Date());
    File mediaFile;
    if (type == MEDIA_TYPE_IMAGE) {
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
    } else if (type == MEDIA_TYPE_VIDEO) {
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
    } else {
        return null;
    }

    return mediaFile;
}

//Dezembro 22
//Muitos destes códigos debem estar colocados numa Bibloioteca Propria.

    //Setembro14
    public byte[] ajeitar_tamanho() {
            /*Julho 01*/

        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        ImageView image_profissional = (ImageView) findViewById(R.id.image);

        try {
            bitmap = ((BitmapDrawable) image_profissional.getDrawable()).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
            Log.d("julho", "-----------------" + width);
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));

        // Apply the scaled bitmap
        image_profissional.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) image_profissional.getLayoutParams();
        params.width = width;
        params.height = height;
        image_profissional.setLayoutParams(params);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //selectedImage.compress(Bitmap.CompressFormat.JPEG,32, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 32, outputStream);
        byte[] dataIMAGEM = outputStream.toByteArray();

        return dataIMAGEM;

        //Log.d("dataIMAGEM", "tamanhos " + dataIMAGEM.length);
    }


//Dezembro 22  Numerico
private int dpToPx(int dp) {
    float density = getApplicationContext().getResources().getDisplayMetrics().density;
    return Math.round((float) dp * density);
}


    //Dezembro 22
//Gestor de resultados:
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//Antigo, parece muito com o moderno

    if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_ANTIGO) {
        if (resultCode == RESULT_OK) {
            Log.d("Dezembro22", "\nCAMERA_CAPTURE_IMAGE_REQUEST_CODE");
            previewCapturedImage();
            //ajeitar_tamanho();

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "Cancelando Captura da Foto?? ", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Problemas Técnicos", Toast.LENGTH_SHORT)
                    .show();
        }
    }
// Novo, A partir das APIs 25
    if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
        if (resultCode == RESULT_OK) {
            Log.d("Setembro14", "\nCAMERA_CAPTURE_IMAGE_REQUEST_CODE");
            previewCapturedImage();
            ajeitar_tamanho();

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }
//En caso de carregar a imagem desde arquivo
    if (requestCode == RESULT_LOAD_IMG) {
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ImageView image = (ImageView) findViewById(R.id.image);
                image.setImageBitmap(selectedImage);
                ajeitar_tamanho();
            } catch (FileNotFoundException e) {                e.printStackTrace();            }
        }
    }

}
//Dezembro22
        /*
    * Com API25
    */
private void previewCapturedImage() {
    try {
        // hide video preview

        ImageView image_profissional = (ImageView) findViewById(R.id.image);
        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        Bitmap bitmap =null;


        if (Build.VERSION.SDK_INT >= 19) {
            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        }
        else
        {
            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
        }
        //final Bitmap bitmap = BitmapFactory.decodeFile("br.com.supermercado.super_mercado.fileprovider"+photoURI.getPath(),options);

        if(bitmap==null)
        {
            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
        }

        if(bitmap==null)
        {
            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
        }



        Log.d("Setembro22","\n"+bitmap.getByteCount());
        image_profissional.setImageBitmap(bitmap);
    } catch (NullPointerException e) {
        e.printStackTrace();
    }
}

/////////////
}
