package com.hongtao.live.net;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hongtao.live.LiveApplication;
import com.hongtao.live.UserManager;
import com.hongtao.live.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created 2019/7/22.
 *
 * @author HongTao
 */
public class LoginInterceptor implements Interceptor {

    private static final String TAG = "LoginInterceptor";

    private static final String CONTENT_STATUS = "status";
    private static final String CONTENT_OFFLINE = "offline";
    private static final String CONTENT_DATA = "data";

    private static final String HEADER_TOKEN = "token";

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取 Http 请求
        Request request = chain.request();
        Request newRequest = request;
        if (urlNeedToken(request.url())) {
            if (!UserManager.getInstance().getToken().equals("")) {
                newRequest = addTokenHeader(request, UserManager.getInstance().getToken());
            } else {
                LoginActivity.start(LiveApplication.getContext());
                Toast.makeText(LiveApplication.getContext(), "未登录，请先登录。", Toast.LENGTH_SHORT).show();
                return chain.proceed(newRequest);
            }
        }
        // 进行 Http 请求并获取返回结果
        Response response = chain.proceed(newRequest);
        // 获取返回结果
        ResponseBody responseBody = response.body();
        String responseString = responseBody.string();
        Log.d(TAG, "intercept: " + responseString);

        String data = handleResponse(responseString);
        Log.d(TAG, "intercept: data = " + data);
        if (CONTENT_OFFLINE.equals(data)) {
            Looper.prepare();
            UserManager.getInstance().offline();
            LoginActivity.start(LiveApplication.getContext());
            Toast.makeText(LiveApplication.getContext(), "登录过期，请重新登录。", Toast.LENGTH_SHORT).show();
            return response.newBuilder().body(responseBody.create(responseBody.contentType(), "")).build();
        }
        // 重新构建 responseBody
        ResponseBody newResponseBody = responseBody.create(responseBody.contentType(), data);
        return response.newBuilder().body(newResponseBody).build();
    }

    private String handleResponse(String responseString) {
        String result = CONTENT_OFFLINE;

        try {
            JSONObject resultJsonObject = new JSONObject(responseString);
            int status = resultJsonObject.getInt(CONTENT_STATUS);
            if (status == com.hongtao.live.module.Response.CODE_SUCCESS) {
                Object object = new JSONTokener(resultJsonObject.get(CONTENT_DATA).toString()).nextValue();
                if (object instanceof  JSONObject) {
                    result = resultJsonObject.getJSONObject(CONTENT_DATA).toString();
                } else if (object instanceof JSONArray) {
                    result = resultJsonObject.getJSONArray(CONTENT_DATA).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Request addTokenHeader(Request request, String token) {
        Request.Builder builder = request.newBuilder();
        builder.addHeader(HEADER_TOKEN, token);
        return builder.build();
    }

    private boolean urlNeedToken(HttpUrl url) {
        return !url.toString().contains("loginAction") && !url.toString().contains("getRooms");
    }
}
