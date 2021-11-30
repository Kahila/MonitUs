package uj.ac.monitus_mobile.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uj.ac.monitus_mobile.R;

/**
 * Service generator class allows resuse
 * to create new interface object for services with API
 */
public class MonitUsServiceGenerator {
    //address of API on localhost listening on port 5000
    private static final String BASE_URL = "http://10.0.2.2:5000";

    /**
     * retrofit instances for sending requests
     * this instance generates an implementation of the
     * RetrofitInterface
     */
    private static Retrofit.Builder builder = new Retrofit.Builder()
                                              .baseUrl(BASE_URL)
                                              .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    //client for logging requests, used in debugging
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                                                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    //generator method allows creation of services
    public static <S> S createService(Class<S> serviceClass){
        if(!httpClient.interceptors().contains(logging)){
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }

}
