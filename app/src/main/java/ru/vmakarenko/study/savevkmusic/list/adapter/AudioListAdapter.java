package ru.vmakarenko.study.savevkmusic.list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.list.AudioItem;

public class AudioListAdapter<T> extends ArrayAdapter<AudioItem> {

    private Context context;
    private boolean useList = true;
    private LayoutInflater mInflater;

    public AudioListAdapter(Context context, List items) {
        super(context, R.layout.audio_list_item_layout, items);
    }

    public AudioListAdapter(Context context, int audio_list_item_layout, int song_title, List<AudioItem> audioList) {
        super(context, audio_list_item_layout, song_title, audioList);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.audio_list_item_layout, parent, false);
        } else {
            view = convertView;
        }

        AudioItem item = getItem(position);
        ((TextView) view.findViewById(R.id.song_author)).setText(item.getAuthor());
        ((TextView) view.findViewById(R.id.song_title)).setText(item.getTitle());

        return view;
    }

}