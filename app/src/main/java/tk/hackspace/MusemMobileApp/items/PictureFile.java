package tk.hackspace.MusemMobileApp.items;


import android.graphics.Bitmap;

import tk.hackspace.MusemMobileApp.items.FileSerialization.JsonFormatStrings;

/**
 * Created by Tolik on 29.11.2014.
 */
public class PictureFile extends AttacheFile {
    private int width;
    private int height;

    public int getDpi() {
        return dpi;
    }

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public PictureFile() {
        super(null);
        bitmap = null;
        attachmentType = JsonFormatStrings.ATTACHMENT_TYPE_PICTURE;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    int dpi;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
