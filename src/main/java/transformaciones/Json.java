package transformaciones;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class Json implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
