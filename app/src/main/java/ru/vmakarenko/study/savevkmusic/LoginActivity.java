package ru.vmakarenko.study.savevkmusic;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;

import org.json.JSONException;

import java.io.InputStream;

import ru.vmakarenko.study.savevkmusic.fragment.SimpleListFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // add list fragment
        SimpleListFragment fragment = new SimpleListFragment();
        getFragmentManager().beginTransaction().
                add(R)fragmentTransaction.add(R.id.list_fragment, fragment);
        fragmentTransaction.add(R.id.detail_fragment, fragment2);
        fragmentTransaction.commit();
    }

    public void onClickLoginVk(View view) {
        VKSdk.login(this, "audio");
    }

    private void reload(){
        VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, VKApiUser.FIELD_PHOTO_50)).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiUser user = null;
                try {
                    user = new VKApiUser().parse(response.json.optJSONArray("response").getJSONObject(0));
                    new DownloadImageTask((ImageView) findViewById(R.id.vk_user_pic_mini))
                            .execute(user.photo_50);
                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "Problem with parsing current user");
                    e.printStackTrace();
                }
                ((EditText) LoginActivity.this.findViewById(R.id.user_name_vk))
                        .setText(user.first_name + " " + user.last_name);
                Log.d(this.getClass().getName(), user.id + " " + user.first_name + " " + user.last_name);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.text_success_auth), Toast.LENGTH_SHORT).show();
                reload();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(LoginActivity.this.getApplicationContext(), getString(R.string.text_you_denied_auth), Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
