package tk.hackspace.MusemMobileApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tk.hackspace.MusemMobileApp.items.AudioFile;
import tk.hackspace.MusemMobileApp.items.Item;

/**
 * Created by Anatoliy on 20.01.2015.
 */
public class AudioFileAdapter extends BaseAdapter {

    private Item item;
    private Context context;
    private LayoutInflater inflater;

    public AudioFileAdapter(Item item, Context _context) {
        this.item = item;
        this.context = _context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (item == null || item.getAudioFiles() == null) {
            return 0;
        }
        return item.getAudioFiles().size();
    }

    @Override
    public Object getItem(int i) {
        if (item == null || item.getAudioFiles() == null) {
            return null;
        }
        return item.getAudioFiles().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View _view, final ViewGroup viewGroup) {

        final View view;

        if (_view == null) {

            view = inflater.inflate(R.layout.audio_file_layout, viewGroup, false);

        } else {

            view = _view;
        }

        final AudioFile audioFile = (AudioFile) getItem(i);
        final TextView timeTextView = (TextView) view.findViewById(R.id.track_time);

        ((TextView) view.findViewById(R.id.track_name)).setText(audioFile.getShortName());
        ((TextView) view.findViewById(R.id.track_discription)).setText(audioFile.getDescription() + "");
        timeTextView.setText(AudioContent.secToTiming(audioFile.getTimeSec()));

        return view;
    }
}
