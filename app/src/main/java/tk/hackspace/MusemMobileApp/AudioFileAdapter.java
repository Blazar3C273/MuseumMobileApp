package tk.hackspace.MusemMobileApp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import tk.hackspace.MusemMobileApp.items.AudioFile;
import tk.hackspace.MusemMobileApp.items.Item;
import tk.hackspace.MusemMobileApp.network.URLFactory;

/**
 * Created by Anatoliy on 20.01.2015.
 */
public class AudioFileAdapter extends BaseAdapter {
    public static final long ONE_SECOND = 1000;
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
        return item.getAudioFiles().size();
    }

    @Override
    public Object getItem(int i) {
        return item.getAudioFiles().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View _view, final ViewGroup viewGroup) {
        // используем созданные, но не используемые view
        final View view;

        if (_view == null) {
            view = inflater.inflate(R.layout.audio_file_layout, viewGroup, false);
        } else {
            view = _view;
        }

        final AudioFile audioFile = (AudioFile) getItem(i);
        final Boolean[] mpReadyFlag = {false};
        final MediaPlayer mediaPlayer = new MediaPlayer();
        final SeekBar seekBar = ((SeekBar) view.findViewById(R.id.audio_file_seek_bar));
        final ImageView buttonImageView = (ImageView) view.findViewById(R.id.play_pause_button);
        final TextView timeTextView = (TextView) view.findViewById(R.id.track_time);
        final TextView currentTimeTextView = (TextView) view.findViewById(R.id.current_track_time);

        ((TextView) view.findViewById(R.id.track_name)).setText(audioFile.getShortName());
        ((TextView) view.findViewById(R.id.track_discription)).setText(audioFile.getDescription() + "");

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(context, Uri.parse(URLFactory.getVideoURL(item.get_id(), audioFile.getFilename(), context)));
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mpReadyFlag[0] = true;
                int duration = mediaPlayer.getDuration();

                seekBar.setMax(duration);
                int sec = duration % 60000 / 1000;
                timeTextView.setText(duration / 60000 + ":" + sec);
            }
        });


        buttonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying() && mpReadyFlag[0]) {
                    buttonImageView.setImageResource(R.drawable.play_button_t);
                    mediaPlayer.pause();
                } else {
                    buttonImageView.setImageResource(R.drawable.pause_button_t);
                    mediaPlayer.start();
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // mediaPlayer.seekTo(seekBar.getProgress());
                //mediaPlayer.start();
            }
        });


        mediaPlayer.prepareAsync();
        return view;
    }
}
