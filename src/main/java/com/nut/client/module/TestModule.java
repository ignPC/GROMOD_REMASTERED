package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;

@Component
public class TestModule {

    @AutoInit
    public TestModule(Module module) {
        System.out.println("successful init: " + module);
    }
}
