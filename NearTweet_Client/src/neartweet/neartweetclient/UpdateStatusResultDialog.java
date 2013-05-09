/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neartweet.neartweetclient;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;

@SuppressWarnings("deprecation")
public class UpdateStatusResultDialog extends Dialog {

    private Bundle values;
    private TextView mOutput, mUsefulTip;
    private Button mViewPostButton, mDeletePostButton;
    private Activity activity;
    private Handler mHandler;

    public UpdateStatusResultDialog(Activity activity, String title, Bundle values) {
        super(activity);
        this.activity = activity;
        this.values = values;
        setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        final String postId = values.getString("post_id");

        mViewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Source tag: view_post_tag
                 */
                Utility.mAsyncRunner.request(postId, new WallPostRequestListener());
            }
        });
        mDeletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Source tag: delete_post_tag
                 */
                Utility.mAsyncRunner.request(postId, new Bundle(), "DELETE",
                        new WallPostDeleteListener(), null);
            }
        });
    }

    public class WallPostRequestListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
            try {
                JSONObject json = new JSONObject(response);
                setText(json.toString(2));
            } catch (JSONException e) {
                setText(activity.getString(R.string.exception) + e.getMessage());
            }
        }

        public void onFacebookError(FacebookError error) {
            setText(error.getMessage());
        }

		@Override
		public void onFacebookError(FacebookError arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
    }

    public class WallPostDeleteListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
            if (response.equals("true")) {
                String message = "Wall Post deleted" + "\n";
                message += "Api Response: " + response;
                setText(message);
            } else {
                setText("wall post could not be deleted");
            }
        }

        public void onFacebookError(FacebookError error) {
            setText(error.getMessage());
        }

		@Override
		public void onFacebookError(FacebookError arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
    }

    public void setText(final String txt) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mOutput.setText(txt);
            }
        });
    }
}
