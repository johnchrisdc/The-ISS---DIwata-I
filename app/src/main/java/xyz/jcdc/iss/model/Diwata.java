package xyz.jcdc.iss.model;

/**
 * Created by jcdc on 4/22/17.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.jcdc.iss.Variables;

public class Diwata {

    public interface DiwataPositionListener {
        void onStartTracking();
        void onDiwataPositionReceived(Diwata diwata);
    }

    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private Properties properties;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static Diwata getDiwata() throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Variables.URL_DIWATA)
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();

        return new Gson().fromJson(json, Diwata.class);
    }

}