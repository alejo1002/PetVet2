package com.software.alejo.petvet2;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRActivity extends AppCompatActivity {

    private ImageView qrCodeImageview;
    private String QRcode = "";
    public final static int WIDTH=500;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        if (getIntent().getExtras()!= null) {
            QRcode = getIntent().getExtras().getString("CODIGO_QR");

            if(!QRcode.equals(""))
            {
                createThread();
            }
        }
    }


    private void createThread()
    {
        qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
        showProgressDialog();
        // crea un thread Nuevo para evitar una ANR exception
        Thread t = new Thread(new Runnable() {
            public void run() {

                try {
                    synchronized (this) {
                        //da un tiempo de espera para que se suspendan los demás threads
                        wait(500);

                        //corre sobre el thread the la interfaz gráfica
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcode);
                                    qrCodeImageview.setImageBitmap(bitmap);
                                    hideProgressDialog();

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block
                            } // end of run method
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //ejecuta el thread creado
        t.start();
    }

    // Metodo que codifica el QR y lo retorna como bitmap
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // excepción si el formato no es soportado
            return null;
        }
        //crea la matriz de pixels
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                //trae los colores negro y blanco para los pixels
                pixels[offset + x] = result.get(x, y) ? ContextCompat.getColor(this, R.color.black)
                        :ContextCompat.getColor(this, R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
