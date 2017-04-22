package xyz.jcdc.iss.model;

/**
 * Created by jcdc on 4/22/17.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.jcdc.iss.Variables;

public class ISS {

    public interface ISSPositionListener {
        void onStartTracking();
        void onISSPositionReceived(ISS iss);
    }

    @SerializedName("iss_position")
    @Expose
    private ISSPosition issPosition;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;

    public ISSPosition getIssPosition() {
        return issPosition;
    }

    public void setIssPosition(ISSPosition issPosition) {
        this.issPosition = issPosition;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public static ISS getISS() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Variables.URL)
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();

        return new Gson().fromJson(json, ISS.class);
    }

}
