/**
 * Created by seibelsabrina on 4/17/17.
 */
public class JSONPair {

    JSON jsonObject;
    String stringVal;

    public JSONPair() {
        jsonObject = null;
        stringVal = "";
    }


    public boolean isObject() {
        if (jsonObject instanceof JSON) {
            return true;
        }
        return false;
    }

    public boolean isString() {
        if (isObject()) {
            return false;
        }
        return true;
    }

    /* Getters and Setters */
    public JSON getJsonObject() {
        return jsonObject;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setJsonObject(JSON jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }
}
