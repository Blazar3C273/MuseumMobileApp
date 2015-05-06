package tk.hackspace.MusemMobileApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.json.JSONObject;

import tk.hackspace.MusemMobileApp.network.NetworkingFunctions;
import tk.hackspace.MusemMobileApp.network.URLFactory;


public class HiddenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);
    }

    public void onSubmitQRButton(View v){
        final Intent intent = new Intent(getApplicationContext(),ContentBrowserActivity.class);
        if (NetworkingFunctions.getInstance().isServerAvailable()) {
            ResponseHandlerInterface responceHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    intent.putExtra("item", response.toString());
                    startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getApplicationContext(), R.string.msg_code_not_found, Toast.LENGTH_LONG).show();
                }
            };
            NetworkingFunctions.getInstance().getAsyncHttpClient().get(URLFactory.getItemURL(((EditText)findViewById(R.id.manualQRcode)).getText().toString(), this), responceHandler);

        } else Toast.makeText(this, R.string.msg_server_unavalble, Toast.LENGTH_LONG).show();
    }
}
