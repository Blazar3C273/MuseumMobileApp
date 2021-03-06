/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tk.hackspace.MusemMobileApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import tk.hackspace.MusemMobileApp.network.NetworkingFunctions;
import tk.hackspace.MusemMobileApp.network.URLFactory;
import tk.hackspace.MusemMobileApp.result.ResultHandler;
import tk.hackspace.MusemMobileApp.result.ResultHandlerFactory;


public class CaptureActivity extends DecoderActivity {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet.of(ResultMetadataType.ISSUE_NUMBER, ResultMetadataType.SUGGESTED_PRICE,
            ResultMetadataType.ERROR_CORRECTION_LEVEL, ResultMetadataType.POSSIBLE_COUNTRY);

    private TextView statusView = null;
    private View resultView = null;
    private boolean inScanMode = false;
    private int clickCounter;



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        clickCounter = 0;
        setContentView(R.layout.capture);
        NetworkingFunctions.initNetworkingFunctions(this);
        resultView = findViewById(R.id.result_view);
        statusView = (TextView) findViewById(R.id.status_view);
        inScanMode = false;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inScanMode)
                finish();
            else
                onResume();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        drawResultPoints(barcode, rawResult);

        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        handleDecodeInternally(rawResult, resultHandler, barcode);
    }

    protected void showScanner() {
        inScanMode = true;
        resultView.setVisibility(View.GONE);
        statusView.setText(R.string.msg_default_status);
        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    protected void showResults() {
        inScanMode = false;
        statusView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.GONE);
        resultView.setVisibility(View.VISIBLE);
    }

    // Put up our own UI for how to handle the decodBarcodeFormated contents.
    private void handleDecodeInternally(final Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        onPause();
        showResults();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.query_sending_progress_bar);
        final Intent intent = new Intent(this, ContentBrowserActivity.class);
        Log.i(TAG, "scanning result: " + rawResult.getText());
        if (rawResult.getText().equals(getString(R.string.feedback_qr_code)))
            startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));

        else if (NetworkingFunctions.getInstance().isServerAvailable()) {
            ResponseHandlerInterface responceHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressBar.setEnabled(false);
                    intent.putExtra("item", response.toString());
                    startActivity(intent);
                    onResume();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getApplicationContext(), R.string.msg_code_not_found, Toast.LENGTH_LONG).show();
                }
            };
            NetworkingFunctions.getInstance().getAsyncHttpClient().get(URLFactory.getItemURL(rawResult.getText(), this), responceHandler);

        } else Toast.makeText(this, R.string.msg_server_unavalble, Toast.LENGTH_LONG).show();
        onResume();
    }
    public void onTextClick(View v){
        clickCounter++;
        Toast.makeText(this,String.valueOf(clickCounter),Toast.LENGTH_SHORT).show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                clickCounter =0;
            }
        },2000);
        if(clickCounter==5){
            clickCounter = 0;
            startActivity(new Intent(getApplicationContext(),HiddenActivity.class));
        }
    }
}
