package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import tk.hackspace.MusemMobileApp.items.Item;
import tk.hackspace.MusemMobileApp.network.URLFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioContent.OnAudioFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioContent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioContent extends Fragment {

    private static final String ARG_ITEM_ID = "param1";
    private static final long HALF_SECOND = 500;
    private Item item;
    private SeekBar seekBar;
    private TextView currentTrackTime;
    private TextView currentTrackLength;
    private TextView currentTrackName;
    private MediaPlayer mediaPlayer;
    private ImageView playPauseButton;
    private Timer timer;
    private boolean isPlayerInterrupted = false;

    public AudioContent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _item Parameter 1.
     * @return A new instance of fragment AudioContent.
     */
    public static AudioContent newInstance(Item _item) {
        AudioContent fragment = new AudioContent();
        fragment.item = _item;
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, _item.get_id());
        fragment.setArguments(args);
        return fragment;
    }

    public static String secToTiming(int timeSec) {
        String seconds = (timeSec % 60) < 10 ? "0" + String.valueOf(timeSec % 60) : String.valueOf(timeSec % 60);
        return timeSec / 60 + ":" + seconds;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_content, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timer = new Timer();

        ((TextView) getView().findViewById(R.id.audioItemName)).setText(item.getItemName());
        playPauseButton = ((ImageView) getView().findViewById(R.id.play_pause_button));
        playPauseButton.setActivated(false);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        playPauseButton.setImageResource(R.drawable.play_button_t);
                        mediaPlayer.pause();
                        isPlayerInterrupted = false;
                    } else {
                        playPauseButton.setImageResource(R.drawable.pause_button_t);
                        mediaPlayer.start();
                        isPlayerInterrupted = true;

                    }
                }
            }
        });

        currentTrackName = ((TextView) getView().findViewById(R.id.current_track_name));
        currentTrackLength = ((TextView) getView().findViewById(R.id.current_track_length));
        currentTrackTime = ((TextView) getView().findViewById(R.id.current_track_time));

        seekBar = ((SeekBar) getView().findViewById(R.id.player_seek_bar));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        ListView listView = (ListView) getView().findViewById(R.id.audio_file_list_view);
        AudioFileAdapter adapter = new AudioFileAdapter(item, getView().getContext());

        if (adapter.getCount() == 0) {
            currentTrackName.setText(R.string.msg_no_audio);
        } else
            try {
                setCurrentTrack(0);
            } catch (IOException e) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT);
                e.printStackTrace();
            }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    setCurrentTrack(i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setCurrentTrack(int number) throws IOException {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
            timer = new Timer();
            timer.schedule(new UpdateTimerTask(), HALF_SECOND);
        }
        mediaPlayer = new MediaPlayer();

        Context applicationContext = getActivity().getApplicationContext();

        String url = URLFactory.getVideoURL(item.get_id(), item.getAudioFiles().get(number).getFilename(), applicationContext);

        mediaPlayer.setDataSource(applicationContext, Uri.parse(url));

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setProgress(0);
                        seekBar.setMax(mediaPlayer.getDuration());
                        currentTrackLength.setText(secToTiming(mediaPlayer.getDuration() / 1000));
                    }
                });
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playPauseButton.setImageResource(R.drawable.play_button_t);
            }
        });
        playPauseButton.setImageResource(R.drawable.play_button_t);
        currentTrackName.setText(item.getAudioFiles().get(number).getShortName());
        int timeSec = item.getAudioFiles().get(number).getTimeSec();
        currentTrackLength.setText(secToTiming(timeSec));
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAudioFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null && isPlayerInterrupted) {
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause_button_t);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (timer != null) {
            timer.cancel();
            timer = null;

        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAudioFragmentInteractionListener {
    }

    private class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            Activity activity = getActivity();
            if (activity != null && seekBar != null && currentTrackTime != null && timer != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //null pointer 1 times come
                        timer.purge();
                        currentTrackTime.setText(secToTiming(mediaPlayer.getCurrentPosition() / 1000));
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        timer.schedule(new UpdateTimerTask(), HALF_SECOND);
                    }
                });
            }
        }
    }
}
