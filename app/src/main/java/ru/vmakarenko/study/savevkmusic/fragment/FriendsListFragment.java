package ru.vmakarenko.study.savevkmusic.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.vmakarenko.study.savevkmusic.MainActivity;
import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.adapter.FriendsListAdapter;
import ru.vmakarenko.study.savevkmusic.interfaces.TagFragment;
import ru.vmakarenko.study.savevkmusic.model.AudioItem;
import ru.vmakarenko.study.savevkmusic.model.FriendItem;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class FriendsListFragment extends ListFragment implements TagFragment {
    public final static String TAG = "friend_list_fragment_tag";
@Override
    public String tag(){
        return TAG;
    }
    List<FriendItem> friendsList = new ArrayList<>();
    public List<FriendItem> getFriendsList() {
        return friendsList;
    }

    FriendsListAdapter friendItemAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        friendItemAdapter = new FriendsListAdapter(inflater.getContext(), R.layout.friends_list_item,R.id.friend_surname_name, friendsList);

        /** Setting the list adapter for the ListFragment */
        setListAdapter(friendItemAdapter);

        VKApi.friends()
                .get(VKParameters
                        .from(VKApiConst.FIELDS, "first_name", "last_name", "photo_50"))
                .executeWithListener(
                        new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                try {
                                    friendsList.clear();
                                    Type type = new TypeToken<List<FriendItem>>() {
                                    }.getType();
                                    friendsList.addAll(
                                            (List<FriendItem>) new Gson().fromJson(response.json.getJSONObject("response").getJSONArray("items").toString(), type));

                                    Log.d(this.getClass().getName(), "friendlist loaded");
                                    friendItemAdapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    Log.e(this.getClass().getName(), "Error parsing friends response");
                                    e.printStackTrace();
                                }
                            }
                        }

                );

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ((MainActivity)getActivity()).startAudioFragment(friendsList.get(position).id);
    }
}
