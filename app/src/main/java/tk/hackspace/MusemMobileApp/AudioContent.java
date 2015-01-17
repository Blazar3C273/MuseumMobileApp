package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.json.JSONObject;

import tk.hackspace.MusemMobileApp.items.AudioFile;
import tk.hackspace.MusemMobileApp.items.FileSerialization.ItemDeserializer;
import tk.hackspace.MusemMobileApp.items.Item;
import tk.hackspace.MusemMobileApp.network.NetworkingFunctions;
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
    private String itemID;
    private Item item;
    private OnAudioFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ItemId Parameter 1.
     * @return A new instance of fragment AudioContent.
     */
    public static AudioContent newInstance(String ItemId) {
        AudioContent fragment = new AudioContent();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, ItemId);
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
        if (getArguments() != null) {
            final String itemID = getArguments().getString(ARG_ITEM_ID);
            ResponseHandlerInterface responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    item = ItemDeserializer.getTunedForDeserializationGson().fromJson(response.toString(), Item.class);
                    ((TextView) getActivity().findViewById(R.id.audioItemName)).setText(item.getItemName());
                    TextView discriptionText = ((TextView) getActivity().findViewById(R.id.audioDescriptionTextView));
                    if (item.getAudioFiles() == null) {
                        discriptionText.setText(R.string.msg_no_audio);
                    } else
                        for (AudioFile audioFile : item.getAudioFiles()) {
                            discriptionText.setText(discriptionText.getText() + "\n" + audioFile.getDescription());
                        }
                }
            };
            NetworkingFunctions.getInstance().getAsyncHttpClient().get(getActivity(), URLFactory.getItemURL(itemID, getActivity()), responseHandler);
        }
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
