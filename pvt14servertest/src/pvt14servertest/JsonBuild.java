package pvt14servertest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Project: genomizer-Server
 * Package: pvt14servertest
 * User: c08esn
 * Date: 5/19/14
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonBuild {

    private JsonObject json = new JsonObject();

    public JsonBuild property(String prop, String name){
        json.addProperty(prop,name);
        return this;
    }

    public JsonBuild property(String prop, boolean a){
        json.addProperty(prop,a);
        return this;
    }

    public JsonBuild add(String prop, JsonArray ja){
        json.add(prop, ja );
        return this;
    }





    public JsonObject build(){
        return json;
    }



}
