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

    public  jsonFileManager (String jsonPath) {
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

    protected static Object getValueByKey(String key) {
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
    protected static LinkedHashMap<String, Object> getKeyAndValueByKey(String key) {
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
    protected static List <String> getValueListByKey(String key) {
        if (key == null || key.isEmpty()) {
            log.error("The provided key is null or empty.");
            return null;
        }

        if (data.containsKey(key)) {
            log.info("Sent key is existing in the json: {}", key);
            Object value = data.get(key);
            try {
                List <String> valueList = (List) value;
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
    protected static List <String> getKeys (String keyPrefix) {
        List<String> keys = new ArrayList<>();
        if (keyPrefix == null || keyPrefix.isEmpty()) {
            log.warn("The provided keyPrefix is null or empty.");
            return null;
        }
        for (String key : data.keySet()) {
            if (key.toLowerCase().replaceAll("\\s+", "").contains(keyPrefix.toLowerCase().replaceAll("\\s+", ""))) {
                keys.add(key);}
        }
        if (!keys.isEmpty()) {
            log.info("The list of keys containing {} are/is retrieved: {} " ,keyPrefix ,keys);
            return keys;
        } else {
            log.info("The key prefix entered doesn't match any keys in the JSON: {}" ,keyPrefix);
            return null;
        }
    }
    protected static List <String> getKeys () {
        if (!data.isEmpty()) {
            List<String> keys = new ArrayList<>(data.keySet());
            log.info("The list of keys are/is retrieved: {} ", keys);
            return keys;
        }
        else {
            log.error("The json file don't contain any keys {}",data);
        }

        return null;
    }
    protected static List <Object> getValues(String valuePrefix) {
        List<Object> values = new ArrayList<>();
        if (valuePrefix == null || valuePrefix.isEmpty()) {
            log.warn("The provided valuePrefix is null or empty.");
            return null;
        }
        for (Object value : data.values()) {
            if (value != null && value.toString().toLowerCase().replaceAll("\\s+", "")
                    .contains(valuePrefix.toLowerCase().replaceAll("\\s+", ""))) {
                values.add(value);
            }
        }
        if (!values.isEmpty()) {
            log.info("The list of values containing '{}' are retrieved: {}", valuePrefix, values);
            return values;
        } else {
            log.info("The value prefix entered doesn't match any values in the JSON: {}", valuePrefix);
            return null;
        }
    }
    protected static List <Object> getValues () {
        if ( data!=null ) {
            List<Object> values = new ArrayList<>(data.values());
            log.info("The list of values are/is retrieved: {} ", values);
            return values;
        }
        else {
            log.error("The json file don't contain any values {}",data);
        }
        return null;
    }
    protected void initialization() {
        data = null;
        type = new TypeToken<LinkedHashMap<String, Object>>() {}.getType();
    }





}
