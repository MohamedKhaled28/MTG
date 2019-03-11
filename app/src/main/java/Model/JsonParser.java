package Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HP on 4/9/2017.
 */

public class JsonParser {
    public static JSONArray parseJson(String context){
        JSONArray array = null;
        try {
            JSONObject jsonObject = new JSONObject(context);

            if (jsonObject.getBoolean("Success")) {

                if (jsonObject.has("item")) {
                    array = new JSONArray(String.valueOf(jsonObject.getJSONArray("item")));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array;
    }
}
