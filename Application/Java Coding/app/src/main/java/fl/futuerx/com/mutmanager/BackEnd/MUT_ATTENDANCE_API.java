package fl.futuerx.com.mutmanager.BackEnd;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fl.futuerx.com.mutmanager.Helpers.IOUtils;
import fl.futuerx.com.mutmanager.Models.Students;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MUT_ATTENDANCE_API {

    public static final int DEFAULT_LIMIT  = 20;
    public static final int STATUS_WAITING = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_DONE    = 3;
    private static final String TAG = "MUT_ATTENDANCE_API";
    public static final String MUT_ATTENDANCE_SERVER_PRODUCTION = "http://cre8maniagames.com/mutAttendance_server/api/";
    private final String STC_AUTHENTICATION_SALT = "@##^A%SC(__^%";
//    private API_ACTION      action;
    private boolean      isActiveSession;
    private String urlToCall;
    private Retrofit     retrofit;

    private static MUT_ATTENDANCE_API instance = null;

    public MUT_ATTENDANCE_API() {
        buildUrl();
    }

    public static MUT_ATTENDANCE_API getInstance()
    {
        if(instance == null) {
            instance = new MUT_ATTENDANCE_API();
        }
        return instance;
    }
    private void buildUrl()
    {
        if(true) {
            urlToCall = MUT_ATTENDANCE_SERVER_PRODUCTION;
        }
    }

    private Gson getRealmGsonInstance() {
        return new GsonBuilder()
                             .setDateFormat("yyyy-MM-dd HH:mm:ss")
                             .registerTypeAdapter(Integer.class,  new JsonDeserializer<Integer>()
                             {
                                 @Override
                                 public Integer deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
                                 {
                                     try {
                                         String text = json.getAsJsonPrimitive().getAsString();
                                         Integer value = text == null || text.length() == 0 ? 0 : Integer.parseInt(text);
                                         return value;
                                     } catch (Exception ex) {
                                        return 0;
                                     }
                                 }
                             })
                            .registerTypeAdapter(Boolean.class,  new JsonDeserializer<Boolean>()
                            {
                                @Override
                                public Boolean deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
                                {
                                    try {
                                        String text = json.getAsJsonPrimitive().getAsString();
                                        return text != null && (text.equals("1") || text.toLowerCase().equals("true"));
                                    } catch (Exception ex) {
                                        return false;
                                    }
                                }
                            })
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();

    }

    private Retrofit getClient() {
        try {
            OkHttpClient client = new OkHttpClient()
                                          .newBuilder()
                                          .readTimeout(60 * 1 , TimeUnit.SECONDS)
                                          .connectTimeout(60 * 1, TimeUnit.SECONDS)
                                          .addInterceptor(new Interceptor() {

                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    long t1 = System.nanoTime();
                    String requestLog = String.format("Sending request %s on %s%n%s",
                            request.url(), chain.connection(), request.headers());
//                    Log.d("Request ",String.format("Sending request %s on %s %n %s", request.url(), chain.connection(), request.headers()));
                    if(request.method().compareToIgnoreCase("post")==0){
                        requestLog ="\n"+requestLog+"\n"+bodyToString(request);
                    }
                    Log.d("TAG","request"+"\n"+requestLog);

                    okhttp3.Response response = chain.proceed(request);
                    long t2 = System.nanoTime();
                    String phpScript = response.request().url().toString().substring(response.request().url().toString().lastIndexOf("/"));
                    String responseLog = String.format(Locale.ENGLISH
                            , "Received response for %s in %.1fms %n %s"
                            , phpScript
                            , (t2 - t1) / 1e6d
                            , "");//response.headers()
//
                    String bodyString = response.body().string();
//
                    Log.e("TAG","response"+"\n"+responseLog+"\n"+bodyString);

                    return response.newBuilder()
                                   .body(ResponseBody.create(response.body().contentType(), bodyString))
                                   .build();
//                    okhttp3.Response response = null;
//                    try {
//                        response = chain.proceed(chain.request());
//                        Log.e("okhttp3.Response chain", chain.toString());
//                        Log.e("okhttp3.Response Msg", response.message());
//                        Log.e("okhttp3.Response Bdy", response.body().string());
//                    } catch(IOException e) {
//                        e.printStackTrace();
//                        Log.e("ERROR RETROFIT", e.getMessage());
//                    }
//                    return response;
                }
            }).build();

            retrofit = new Retrofit.Builder()
                                        .baseUrl(urlToCall)
                                        .addConverterFactory(GsonConverterFactory.create(getRealmGsonInstance()))
                                        .client(client)
                                        .build();
        } catch(Exception e) {
            Log.e("ERROR RETROFIT", e.getMessage());
            e.printStackTrace();
        }
        return retrofit;
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
    public API_INTERFACE getApiInstance()
    {
        return getClient().create(API_INTERFACE.class);
    }

    public Call<apiResponseObject> submitOrderMulti(Students student) {

        if(student != null)
        {
//            // TODO compress image
            RequestBody id         = RequestBody.create(MediaType.parse("multipart/form-data"), ""+student.id);
            RequestBody name      = RequestBody.create(MediaType.parse("multipart/form-data"), ""+student.name);
            RequestBody phone        = RequestBody.create(MediaType.parse("multipart/form-data"), ""+student.phone);
            RequestBody email   = RequestBody.create(MediaType.parse("multipart/form-data"), student.email);
            RequestBody tag_id= RequestBody.create(MediaType.parse("multipart/form-data"), student.tag_id);

            // create RequestBody instance from file
            // MultipartBody.Part is used to send also the actual file name
            File image = new File(student.image);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), image);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", IOUtils.getInstance().getFileName(student.image), requestFile);
//            List<MultipartBody.Part> images = new ArrayList<>();
//            if(student.image != null){
//                int i = 0;
//                for(String img : student.image){
//                    File image = new File(img);
//                    if(image.exists()){
//                        Log.d(TAG, "requestUpload: image " + (i++) + "  " + img);
//                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), image);
//                        images.add(MultipartBody.Part.createFormData("image[]", image.getName(), requestFile));
//                    }
//                }
//            }
//
            return getApiInstance().submitOrderAllPart(
                    id
                    , name
                    , phone
                    , email
                    , tag_id
                    , body);
        }
        return null;
    }

    /**
     *
     * @return Auth Salt based on version or other certain criteria
     */
    public String getAuthSalt() {

        return STC_AUTHENTICATION_SALT;
    }
}