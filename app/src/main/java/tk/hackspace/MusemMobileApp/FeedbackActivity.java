package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;

import tk.hackspace.MusemMobileApp.network.NetworkingFunctions;
import tk.hackspace.MusemMobileApp.network.URLFactory;


public class FeedbackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get("item_id") != null) {
            findViewById(R.id.ratingBar).setVisibility(View.VISIBLE);
        }
    }

    public void onSubmitButtonClick(View view) {
        //send feedback on server
        JSONObject jsonObject = new JSONObject();
        String id = new Date().toString() + "+" + getApplicationContext().getApplicationInfo().uid;
        id = id.replace(' ', '_');

        String text = String.valueOf(((EditText) findViewById(R.id.feedbackText)).getText());
        String name = String.valueOf(((EditText) findViewById(R.id.feedbackVisitorName)).getText());
        String contact = String.valueOf(((EditText) findViewById(R.id.feedbackVisitorContactText)).getText());
        try {
            jsonObject.put("_id", id);
            jsonObject.put("is_viewed",false);
            jsonObject.put("date",new Date().toString());

            if (text != null && !text.equals("")) {
                jsonObject.put("feedback_text", text);
            }
            if (name != null && !name.equals("")) {
                jsonObject.put("visitor_name", name);
            }
            if (contact != null && !contact.equals("")) {
                jsonObject.put("visitor_contact_text", contact);
            }
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            if (ratingBar.getVisibility() == View.VISIBLE) {
                jsonObject.put("item_id", getIntent().getExtras().get("item_id"));
                jsonObject.put("rating", ratingBar.getRating());
                jsonObject.put("item_name", getIntent().getExtras().get("item_name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            HttpEntity stringEntity = new StringEntity(jsonObject.toString(), Charset.defaultCharset().displayName());
            NetworkingFunctions.getInstance().getAsyncHttpClient().put(this, URLFactory.getFeedbackURL(getApplicationContext(), id), stringEntity, "text/plain; charset=UTF-8", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(getApplicationContext(), R.string.msg_feedback_succsess_msg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = "";
                    try {
                        response = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(responseBody))).readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e(FeedbackActivity.class.getCanonicalName(), response, error);
                    Toast.makeText(getApplicationContext(), R.string.msg_feedback_fail_msg, Toast.LENGTH_LONG).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        finish();
    }
}
