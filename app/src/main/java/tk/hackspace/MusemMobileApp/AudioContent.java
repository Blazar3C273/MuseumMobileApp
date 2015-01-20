package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tk.hackspace.MusemMobileApp.items.Item;


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
    private String itemID;
    private Item item;
    private OnAudioFragmentInteractionListener mListener;

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

    public AudioContent() {
        // Required empty public constructor
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
        ListView listView = (ListView) getView().findViewById(R.id.audio_file_list_view);
        final AudioFileAdapter adapter = new AudioFileAdapter(item, getView().getContext());
        listView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAudioFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAudioFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
