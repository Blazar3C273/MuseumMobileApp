package tk.hackspace.MusemMobileApp.items;


import java.io.File;

import tk.hackspace.MusemMobileApp.items.FileSerialization.JsonFormatStrings;

/**
 * Created by Tolik on 29.11.2014.
 */
public class VideoFile extends AttacheFile {
    public int getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(int timeSec) {
        this.timeSec = timeSec;
    }

    public VideoFile(File file) {
        super(file);
        attachmentType = JsonFormatStrings.ATTACHMENT_TYPE_VIDEO;
    }

    private int timeSec = 0;
}
