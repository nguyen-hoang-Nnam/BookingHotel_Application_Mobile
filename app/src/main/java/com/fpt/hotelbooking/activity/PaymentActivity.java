package com.fpt.hotelbooking.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fpt.hotelbooking.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PaymentActivity", "onCreate() started");
        setContentView(R.layout.activity_payment);

        // Get the payment link from intent extras
        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);
        String paymentLink = getIntent().getStringExtra("PAYMENT_LINK");
        Log.d("PaymentActivity", "Received Payment Link: " + paymentLink);

        // Generate and display the QR code
        if (paymentLink == null || paymentLink.isEmpty()) {
            Log.e("PaymentActivity", "Payment link is invalid.");
            Toast.makeText(this, "Payment link is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate QR code from the payment link
        Bitmap qrCodeBitmap = generateQRCode(paymentLink);
        if (qrCodeBitmap != null) {
            // Set the QR code bitmap to the ImageView
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } else {
            Log.e("PaymentActivity", "Failed to generate QR code.");
            Toast.makeText(this, "Failed to generate QR code.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap generateQRCode(String url) {
        try {
            // Create QR code writer instance
            QRCodeWriter writer = new QRCodeWriter();
            // Encode the URL into a QR code matrix
            BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 200, 200); // Generate the QR code matrix
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            // Create a bitmap to store the QR code image
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // Create bitmap from matrix

            // Populate the bitmap with pixels from the QR code matrix
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Set each pixel to black or white depending on the bit value
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            // Log the exception and return null if QR generation fails
            e.printStackTrace();
            return null;
        }
    }
}

