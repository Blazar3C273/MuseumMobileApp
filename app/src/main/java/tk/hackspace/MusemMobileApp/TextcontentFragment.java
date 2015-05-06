package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import tk.hackspace.MusemMobileApp.items.Item;
import tk.hackspace.MusemMobileApp.items.PictureFile;
import tk.hackspace.MusemMobileApp.network.URLFactory;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link TextcontentFragment.OnTextFragmentInteractionListener}
 * interface.
 */
public class TextcontentFragment extends Fragment {

    private static final String ITEM_ID = "item";
    private static final String TAG = TextcontentFragment.class.getSimpleName();


    private Item item;

    private OnTextFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TextcontentFragment() {
    }

    // TODO: Rename and change types of parameters
    public static TextcontentFragment newInstance(Item _item) {
        TextcontentFragment fragment = new TextcontentFragment();
        Bundle args = new Bundle();
        fragment.item = _item;
        args.putString(ITEM_ID, fragment.item.get_id());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Change Adapter to display your content

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_content, container, false);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTextFragmentInteractionListener) activity;
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

    /*
    * 1-Загрузить итем
    * 2-распарсить итем
    * 3-подгрузить картинки
    * 4-установить текст
    * 5-установить ImageGetter
    * */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WebView webView = (WebView) getView().findViewById(R.id.webView);
        webView.loadData(getValidHtmlForWebView(item.getText()), "text/html", "UTF-8");
        ((TextView) getView().findViewById(R.id.simpleItemName)).setText(item.getItemName());
        ((Button) getActivity().findViewById(R.id.feedbackButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                intent.putExtra("item_id", item.get_id());
                intent.putExtra("item_name", item.getItemName());
                startActivity(intent);
            }
        });
    }

    private String getValidHtmlForWebView(String text) {
        return text.replace("src=\"","src=\""+URLFactory.getItemURL(item.get_id(),getActivity().getApplicationContext())+"/");
    }

    @Override
    public void onStart() {
        getFragmentManager().beginTransaction().setBreadCrumbShortTitle(getString(R.string.text_content_veiw_title)).commit();
        super.onStart();
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
    public interface OnTextFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
