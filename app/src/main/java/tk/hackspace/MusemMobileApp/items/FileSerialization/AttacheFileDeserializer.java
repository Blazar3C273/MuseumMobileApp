package tk.hackspace.MusemMobileApp.items.FileSerialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import tk.hackspace.MusemMobileApp.items.AttacheFile;
import tk.hackspace.MusemMobileApp.items.AudioFile;
import tk.hackspace.MusemMobileApp.items.PictureFile;
import tk.hackspace.MusemMobileApp.items.VideoFile;

/**
 * Created by Tolik on 30.11.2014.
 */
public class AttacheFileDeserializer implements JsonDeserializer<AttacheFile> {
    @Override
    public AttacheFile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        AttacheFile result;
        JsonObject object = (JsonObject) jsonElement;
        String attachment_type = object.get(JsonFormatStrings.ATTACHMENT_TYPE_FIELD).getAsString();
        switch (attachment_type) {
            case JsonFormatStrings.ATTACHMENT_TYPE_AUDIO:
                result = new AudioFile(null);
                result.setAttachmentType(JsonFormatStrings.ATTACHMENT_TYPE_AUDIO);
                ((AudioFile) result).setTimeSec(object.get("timeSec").getAsInt());
                break;
            case JsonFormatStrings.ATTACHMENT_TYPE_PICTURE:
                result = new PictureFile();
                result.setAttachmentType(JsonFormatStrings.ATTACHMENT_TYPE_PICTURE);
                ((PictureFile) result).setHeight(object.get("height").getAsInt());
                ((PictureFile) result).setWidth(object.get("width").getAsInt());
                break;
            case JsonFormatStrings.ATTACHMENT_TYPE_VIDEO:
                result = new VideoFile(null);
                result.setAttachmentType(JsonFormatStrings.ATTACHMENT_TYPE_VIDEO);
                ((VideoFile) result).setTimeSec(object.get("timeSec").getAsInt());
                break;
            default: {
                throw new JsonParseException("Attachment parse error. Attachment in not Audio or Video or Picture or json with syntax error");
            }
        }
        result.setDescription(object.get("description").getAsString());
        result.setFilename(object.get("file_name").getAsString());
        result.setId(object.get("id").getAsString());
        result.setShortName(object.get("short_name").getAsString());

        return result;
    }
}
