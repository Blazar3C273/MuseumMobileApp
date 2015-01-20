package tk.hackspace.MusemMobileApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tk.hackspace.MusemMobileApp.items.Item;
import tk.hackspace.MusemMobileApp.items.VideoFile;
import tk.hackspace.MusemMobileApp.network.URLFactory;
import tk.hackspace.MusemMobileApp.simple.DemoUtil;
import tk.hackspace.MusemMobileApp.simple.SimplePlayerActivity;

/**
 * Created by Anatoliy on 19.01.2015.
 */
public class VideoItemAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private Item item;

    public VideoItemAdapter(Context ctx, Item _item) {
        this.ctx = ctx;
        this.item = _item;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item.getVideoFiles().size();
    }

    @Override
    public Object getItem(int i) {
        return item.getVideoFiles().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View _view, ViewGroup viewGroup) {
        // используем созданные, но не используемые view
        View view = _view;
        if (view == null) {
            view = lInflater.inflate(R.layout.video_item_layout, viewGroup, false);
        }

        final VideoFile videoFile = (VideoFile) getItem(i);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.video_file_name)).setText(videoFile.getShortName());
        ((TextView) view.findViewById(R.id.video_file_description)).setText(videoFile.getDescription() + "");

//        ((TextView) view.findViewById(R.id.video_file_length)).setText(videoFile.getTimeSec());
        //((ImageView) view.findViewById(R.id.ivImage)).setImageResource();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, SimplePlayerActivity.class);
                intent
                        .setData(Uri.parse(URLFactory.getVideoURL(item.get_id(), videoFile.getFilename(), ctx)))
                        .putExtra(DemoUtil.CONTENT_ID_EXTRA, videoFile.getShortName())
                        .putExtra(DemoUtil.CONTENT_TYPE_EXTRA, DemoUtil.TYPE_OTHER);
                ctx.startActivity(intent);
            }
        });
        return view;
    }

}
