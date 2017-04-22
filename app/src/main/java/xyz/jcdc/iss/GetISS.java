package xyz.jcdc.iss;

import android.os.AsyncTask;

import xyz.jcdc.iss.model.ISS;

/**
 * Created by jcdc on 4/22/17.
 */

public class GetISS extends AsyncTask <String, String, ISS> {

    ISS.ISSPositionListener issPositionListener;

    public GetISS(ISS.ISSPositionListener issPositionListener) {
        this.issPositionListener = issPositionListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        issPositionListener.onStartTracking();
    }

    @Override
    protected ISS doInBackground(String... strings) {
        try {
            return ISS.getISS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ISS iss) {
        super.onPostExecute(iss);
        issPositionListener.onISSPositionReceived(iss);
    }
}
