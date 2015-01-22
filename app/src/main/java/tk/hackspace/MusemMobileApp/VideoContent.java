package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import tk.hackspace.MusemMobileApp.items.Item;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoContent.OnVideoFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoContent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoContent extends Fragment {
    private Item item;

    public VideoContent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _item Parameter 1.
     * @return A new instance of fragment VideoContent.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoContent newInstance(Item _item) {
        VideoContent fragment = new VideoContent();
        fragment.item = _item;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView = (ListView) getView().findViewById(R.id.video_list_view);
        VideoItemAdapter adapter = new VideoItemAdapter(getView().getContext(), item);
        listView.setAdapter(adapter);
        ((TextView) getView().findViewById(R.id.item_name)).setText(item.getItemName());
        if (adapter.getCount() == 0) {
            ((TextView) getView().findViewById(R.id.no_video_label)).setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onStart() {
        // getActivity().setTitle(getString(R.string.video_content_veiw_title));
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_content, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVideoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnVideoFragmentInteractionListener {
    }

}
