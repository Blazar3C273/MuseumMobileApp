package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.app.Fragment;
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

    // TODO: Rename and change types of parameters
    public static TextcontentFragment newInstance(Item _item) {
        TextcontentFragment fragment = new TextcontentFragment();
        Bundle args = new Bundle();
        fragment.item = _item;
        args.putString(ITEM_ID, fragment.item.get_id());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TextcontentFragment() {
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
        if (item.getPictureFiles() != null) {
            for (final PictureFile pictureFile : item.getPictureFiles()) {
                ImageLoadingListener listner = new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, final View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        Log.i(TAG, "picture from uri " + imageUri + " is loaded");
                        pictureFile.setBitmap(loadedImage);
                        Html.ImageGetter imageGetter = new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String s) {
                                Drawable drawable = null;
                                for (final PictureFile file : item.getPictureFiles()) {
                                    if (file.getFilename().equals(s) && file.getBitmap() != null) {
                                        Rect bounds = new Rect(0, 0, file.getBitmap().getWidth(), file.getBitmap().getHeight());
                                        drawable = new BitmapDrawable(file.getBitmap());
                                        drawable.setBounds(bounds);
                                    }
                                }
                                return drawable;
                            }
                        };
                        Spanned spanned = Html.fromHtml(item.getText(), imageGetter, null);

                        ((TextView) getView().findViewById(R.id.descriptionTextWiew)).setText(spanned);
                        ((TextView) getView().findViewById(R.id.descriptionTextWiew)).setMovementMethod(LinkMovementMethod.getInstance());
                    }
                };
                ImageSize size = new ImageSize(pictureFile.getWidth(), pictureFile.getHeight());
                ImageLoader.getInstance().loadImage(URLFactory.getImageURL(item.get_id(), pictureFile.getFilename(), getActivity()), size, listner);
            }
        } else {
            Spanned spanned = Html.fromHtml(item.getText(), null, null);
            ((TextView) getView().findViewById(R.id.descriptionTextWiew)).setText(spanned);
        }
        ((TextView) getView().findViewById(R.id.simpleItemName)).setText(item.getItemName());
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
