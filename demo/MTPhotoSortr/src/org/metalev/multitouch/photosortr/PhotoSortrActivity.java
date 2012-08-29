/**
 * PhotoSorterActivity.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * Released under the Apache License v2.
 */
package org.metalev.multitouch.photosortr;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoSortrActivity extends Activity {
	
	PhotoSortrView photoSorter;

    private final static String TAG = "PhotoSortrActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.instructions);
		setContentView(R.layout.main);
        photoSorter = (PhotoSortrView)findViewById(R.id.photoSorter);
        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                captureScreen();
            }
        });
	}

    private void captureScreen()
    {
        final int CROP_WIDTH = (int)getResources().getDimension(R.dimen.crop_width);
        final int CROP_HEIGHT = (int)getResources().getDimension(R.dimen.crop_height);

        Log.d("captureScreen", "CROP_WIDTH = " + CROP_WIDTH);
        Log.d("captureScreen", "CROP_HEIGHT = " + CROP_HEIGHT);

        View root = photoSorter; //This will get the outer frames too - photoSorter.getRootView();
        Bitmap bitmap = Bitmap.createBitmap(CROP_WIDTH, CROP_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float dx = -1 * ((float)root.getWidth() / 2.0f - (float)CROP_WIDTH / 2.0f);
        float dy = -1 * ((float)root.getHeight() / 2.0f - (float)CROP_HEIGHT / 2.0f);
        canvas.translate(dx, dy);
        root.draw(canvas);

        File target = new File(Environment.getExternalStorageDirectory(), "screenshot-cropped.jpg");
        Log.d(TAG, "Target path: " + target.getPath());
        try
        {
            FileOutputStream out = new FileOutputStream(target);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Image captured!", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, "Unable to capture screen", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to capture image", e);
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Unable to capture screen", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unable to capture image", e);
        }
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		photoSorter.loadImages(this);
	}


	
	@Override
	protected void onPause() {
		super.onPause();
		photoSorter.unloadImages();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			photoSorter.trackballClicked();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}