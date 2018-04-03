package com.gjzg.singleton;


import com.google.gson.Gson;

public class SingleGson {

    private static Gson gson;

    public static Gson getInstance() {
        if (gson == null) {
            synchronized (SingleGson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}

