package ru.vmakarenko.study.savevkmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.model.FriendItem;

public class FriendsListAdapter extends ArrayAdapter<FriendItem> implements Filterable{

    private Context context;
    private boolean useList = true;
    private LayoutInflater mInflater;

    public FriendsListAdapter(Context context, int resource) {
        super(context, resource);
    }
    public FriendsListAdapter(Context context, int audio_list_item_layout, int itemId, List<FriendItem> friendList) {
        super(context, audio_list_item_layout, itemId, friendList);
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
            view = mInflater.inflate(R.layout.friends_list_item, parent, false);
        } else {
            view = convertView;
        }

        FriendItem user = getItem(position);
        ((TextView)view.findViewById(R.id.friend_surname_name))
                .setText(user.last_name + " " + user.first_name);
//        view.findViewById(R.id.friend_image)

        return view;
    }

}