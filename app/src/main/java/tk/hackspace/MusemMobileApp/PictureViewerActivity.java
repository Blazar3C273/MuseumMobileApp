package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import tk.hackspace.MusemMobileApp.network.URLFactory;


public class PictureViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        final ImageViewTouch imageViewTouch = (ImageViewTouch) findViewById(R.id.image);
        String pictureURI = getIntent().getData().getEncodedSchemeSpecificPart();

        String itemID = pictureURI.substring(0, pictureURI.lastIndexOf('/'));
        String pictureFileName = pictureURI.substring(pictureURI.lastIndexOf('/') + 1, pictureURI.length());

        ImageLoadingListener listner = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Matrix matrix = imageViewTouch.getDisplayMatrix();
                imageViewTouch.setImageBitmap(loadedImage, matrix, -1, 5f);
            }
        };

        ImageLoader.getInstance().loadImage(URLFactory.getImageURL(itemID, pictureFileName, this), listner);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
