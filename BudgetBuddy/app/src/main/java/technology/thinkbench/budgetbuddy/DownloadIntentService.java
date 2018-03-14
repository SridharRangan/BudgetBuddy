package technology.thinkbench.budgetbuddy;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.net.MalformedURLException;

public class DownloadIntentService extends IntentService {

    private static final String TAG = DownloadIntentService.class.getSimpleName();

    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String URL_EXTRA = "url";
    public static final String RESULT_EXTRA = "url";

    public static final int RESULT_CODE = 0;
    public static final int INVALID_URL_CODE = 1;
    public static final int ERROR_CODE = 2;

    public DownloadIntentService() {
        super(TAG);

        // make one and re-use, in the case where more than one intent is queued
        //parser = new IllustrativeRSSParser();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        String args = intent.getStringExtra(URL_EXTRA);
        Log.d("DEBUG", "Right before try/catch");
        try {
            try {
                Log.d("DEBUG", "Right before REST");
                String response = RESTimp.sendGet(args);
                Log.d("DEBUG", "Right after REST");
                Log.d("DEBUG", response);
                Intent result = new Intent();
                result.putExtra(RESULT_EXTRA, response);
                Log.d("DEBUG", "Right before reply.send");
                reply.send(this, RESULT_CODE, result);
            }catch (MalformedURLException exc) {
                reply.send(INVALID_URL_CODE);
            } catch (Exception exc) {
                // could do better by treating the different sax/xml exceptions individually
                reply.send(ERROR_CODE);
            }
        }catch (PendingIntent.CanceledException exc) {
            Log.i("WHOOPS", "reply cancelled", exc);
        }
    }
}