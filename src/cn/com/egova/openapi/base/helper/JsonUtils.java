package cn.com.egova.openapi.base.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {
    static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    public static <T> T fromJson(String jsonStr, Class<T> type) {
        try {
            return gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        try {
            return gson.fromJson(jsonStr, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object src) {
        try {
            return gson.toJson(src);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object src, boolean serializeNulls) {
        try {
            if (serializeNulls) {
                return gson.toJson(src);
            } else {
                return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(src);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param json
     * @param clazz
     * @return
     */

    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {

        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();

        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();

        for (JsonObject jsonObject : jsonObjects) {

            arrayList.add(gson.fromJson(jsonObject, clazz));

        }

        return arrayList;

    }
}
