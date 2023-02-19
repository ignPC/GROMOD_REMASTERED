package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;

@Component
public class Module {

    @AutoInit
    public Module() {
    }

    private final String moduleName = "module";
}
