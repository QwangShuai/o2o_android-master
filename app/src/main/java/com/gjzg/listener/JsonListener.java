package com.gjzg.listener;


public interface JsonListener {

    void success(String json);

    void failure(String failure);
}
