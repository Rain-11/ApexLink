package com.crazy.rain.dubboService;

import com.crazy.rain.model.entity.User;

public interface UserInterface {

    User verifyUserAccessKey(String secretId);

    boolean deductionAmount();

}
