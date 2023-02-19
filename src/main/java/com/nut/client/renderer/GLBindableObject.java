package com.nut.client.renderer;

public interface GLBindableObject<T> {

    T bind();

    T unbind();
}
