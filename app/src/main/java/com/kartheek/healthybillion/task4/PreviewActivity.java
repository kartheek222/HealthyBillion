package com.kartheek.healthybillion.task4;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kartheek.healthybillion.R;
import com.kartheek.healthybillion.utils.ResponseUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PreviewActivity extends AppCompatActivity {
    ImageView imageView;
    private Bitmap btmSource;
    private File sourceFile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initToolbar();
        String filePath = getIntent().getStringExtra("filePath");
        sourceFile = new File(filePath);
        imageView = (ImageView) findViewById(R.id.ivPreview);
        btmSource = toGrayscale(filePath);
        imageView.setImageBitmap(btmSource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Preview Activity");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        File destFile = new File(ResponseUtilities.getInstance().getParentDir(this), sourceFile.getName());
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            new SaveTask(destFile, btmSource) {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    intiDialog();
                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog.dismiss();
                    btmSource.recycle();
                    setResult(RESULT_OK);
                    finish();
                }
            }.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap toGrayscale(String filePath) {
        int width, height;
        Bitmap bmpOriginal = BitmapFactory.decodeFile(filePath);
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    private void intiDialog() {
        // Initializing the progress dialog.
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setMessage("Saving ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    private static class SaveTask extends AsyncTask<Void, Void, Void> {

        private File parendFile;
        private Bitmap btmSource;

        public SaveTask(File parendFile, Bitmap btmSource) {
            this.parendFile = parendFile;
            this.btmSource = btmSource;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                btmSource.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(parendFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
