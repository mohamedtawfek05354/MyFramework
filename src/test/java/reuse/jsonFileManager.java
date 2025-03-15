package reuse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.invoke.MethodHandles.lookup;
public class jsonFileManager {

    protected static LinkedHashMap <String,Object> data;
    private Type type ;
    private static final Logger log = LogManager.getLogger(lookup().lookupClass());

    /**
     * Initializes the JSON file manager and loads data from the specified JSON file.
     *
     * @param jsonPath the path to the JSON file.
     */
    public jsonFileManager (String jsonPath) {
        initialization();
        if (jsonPath == null || jsonPath.isEmpty()){
            log.warn("The json path is empty, please provide the path");
        }
        try {
            data = new Gson().fromJson(new FileReader(jsonPath), type);
        }
        catch (Exception e)
        {log.warn("The data is empty as the json file is wrong/not provided {}",data );}
    }

    /**
     * Retrieves the value associated with the specified key from the JSON data.
     *
     * @param key the key to search for.
     * @return the corresponding value, or null if the key is not found.
     */
    public Object getValueByKey(String key) {
        try {
            if (data.containsKey(key)) {
                log.info("Sent key is existing in the json: '{}'", key);
                Object value = data.get(key);
                log.info("The value of key '{}' is retrieved: '{}'", key, value);
                return value;
            }
        }
        catch (Exception e) {
            log.warn("The key entered doesn't exist in the Json , the key equals: '{}'", key);
            return null;
        }
        return null;
    }

    /**
     * Retrieves the value if it's map ,so it will be key and value as a LinkedHashMap.
     *
     * @param key the key to search for.
     * @return a LinkedHashMap containing the key-value pair, or null if the key is not found.
     */
    public LinkedHashMap<String, Object> getKeyAndValueByKey(String key) {
        if (key == null || key.isEmpty()) {
            log.warn("The provided key is null or empty.");
            return null;
        }
        if (data.containsKey(key)) {
            log.info("Sent key is existing in the json: {}", key);
            Object value = data.get(key);
            log.debug("Value of the key: {}", value);
            try {
                Map<?, ?> rawMap = (Map<?, ?>) value;
                if (rawMap.isEmpty()) {
                    log.warn("The map is empty: {}", rawMap);
                    return null;
                }
                LinkedHashMap<String, Object> result = new LinkedHashMap<>();
                for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                    result.put(entry.getKey().toString(), entry.getValue());
                }
                log.info("The map is created: {}", result);
                return result;
            }
            catch (Exception e) {
                log.error("The Value isn't map {}", value);
            }
        }
        else {
            log.error("The key entered doesn't exist in the Json: {}", key);
            return null;
        }
        return null;
    }

    /**
     * Retrieves a list of values associated with the specified key if it maps to a list.
     *
     * @param key the key to search for.
     * @return a list of values, or null if the key does not exist or is not a list.
     */
    public List<String> getValueListByKey(String key) {
        if (key == null || key.isEmpty()) {
            log.error("The provided key is null or empty.");
            return null;
        }
        if (data.containsKey(key)) {
            log.info("Sent key is existing in the json: {}", key);
            Object value = data.get(key);
            try {
                List<String> valueList = (List) value;
                log.info("The list of {} are/is retrieved: {}", key, value);
                return valueList;
            }
            catch (Exception e) {
                log.error("The Value isn't list {}", value);
                return null;
            }
        }
        else {
            log.error("The key entered doesn't exist in the Json: {}", key);
            return null;
        }
    }

    /**
     * Retrieves all keys from the JSON data.
     *
     * @return a list of all keys, or null if the data is empty.
     */
    public List<String> getKeys() {
        if (!data.isEmpty()) {
            List<String> keys = new ArrayList<>(data.keySet());
            log.info("The list of keys are/is retrieved: {} ", keys);
            return keys;
        }
        else {
            log.error("The json file don't contain any keys {}", data);
        }
        return null;
    }

    /**
     * Initializes the JSON data structure and defines its type.
     */
    public void initialization() {
        data = null;
        type = new TypeToken<LinkedHashMap<String, Object>>() {}.getType();
    }
}
