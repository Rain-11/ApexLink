package com.crazy.rain.dubboService;

import com.crazy.rain.model.entity.InterfaceInfo;

public interface InterfaceDetailsInterface {

    InterfaceInfo verifyIfItExists(String path, String host, String method);

    boolean increaseNumberCalls(Long interfaceId, Long id);

}
