package tk.hackspace.MusemMobileApp.items;


import java.io.File;

import tk.hackspace.MusemMobileApp.items.FileSerialization.JsonFormatStrings;

/**
 * Created by Tolik on 29.11.2014.
 */
public class AudioFile extends AttacheFile {
    public AudioFile(File file) {
        super(file);
        setAttachmentType(JsonFormatStrings.ATTACHMENT_TYPE_AUDIO);
    }

    public int getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(int timeSec) {
        this.timeSec = timeSec;
    }

    private int timeSec = 0;
}
