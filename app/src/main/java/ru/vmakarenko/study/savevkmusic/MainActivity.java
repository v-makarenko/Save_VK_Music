package ru.vmakarenko.study.savevkmusic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vk.sdk.VKSdk;

import ru.vmakarenko.study.savevkmusic.adapter.DrawerItemCustomAdapter;
import ru.vmakarenko.study.savevkmusic.fragment.AboutAppFragment;
import ru.vmakarenko.study.savevkmusic.fragment.AudioListFragment;
import ru.vmakarenko.study.savevkmusic.fragment.FriendsListFragment;
import ru.vmakarenko.study.savevkmusic.fragment.nested.FindAudioOuterFragment;
import ru.vmakarenko.study.savevkmusic.model.ObjectDrawerItem;

public class MainActivity extends AppCompatActivity {
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FragmentsAvailable mode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        if (mode != null) {
            switch (mode) {
                case AUDIO:
                    menu.findItem(R.id.download_all).setVisible(true);
                    break;
                case FRIENDS:
                    menu.findItem(R.id.download_all).setVisible(false);
                    break;
            }
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                //add the function to perform here
                FragmentManager fm = getFragmentManager();
                AboutAppFragment dialogFragment = new AboutAppFragment();
                dialogFragment.show(fm, dialogFragment.tag());
                return (true);
            case R.id.download_all:
                //add the function to perform here
                ((AudioListFragment) getFragmentManager().findFragmentByTag(AudioListFragment.TAG)).downloadAllReq();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.nav_drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        ObjectDrawerItem[] drawerItems = new ObjectDrawerItem[3];

        drawerItems[0] = new ObjectDrawerItem(R.drawable.ic_my_audio, getResources().getString(R.string.my_audio));
        drawerItems[1] = new ObjectDrawerItem(R.drawable.ic_friends_audio, getResources().getString(R.string.frieds_audio));
        drawerItems[2] = new ObjectDrawerItem(R.drawable.ic_outer_audio, getResources().getString(R.string.external_audio));

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }

            private void selectItem(int position) {

                switch (position) {
                    case 0:
                        startAudioFragment(null);
                        break;
                    case 1:
                        startFriendFragment();
                        break;
                    case 2:
                        startFindAudioFragment();
                        break;

                    default:
                        break;
                }


                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });

    }


    public void setFragmentMode(FragmentsAvailable mode) {
        this.mode = mode;
    }

    public enum FragmentsAvailable {
        AUDIO, FRIENDS
    }

    public void startFriendFragment() {
        Fragment fragment = new FriendsListFragment();
        startFragment(fragment);

    }

    public void startAudioFragment(String id) {
        Fragment fragment;
        if (id == null) {
            fragment = new AudioListFragment();
        } else {
            fragment = new AudioListFragment().setUserId(id);
        }
        startFragment(fragment);

    }


    private void startFindAudioFragment() {
        startFragment(new FindAudioOuterFragment());
    }


    public void startFragment(Fragment fragment) {
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(MainActivity.this, "audio", "friends");
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, fragment.getTag()).commit();

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
}