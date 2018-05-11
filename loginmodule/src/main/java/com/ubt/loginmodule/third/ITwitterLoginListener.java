package com.ubt.loginmodule.third;

import twitter4j.auth.AccessToken;

public interface ITwitterLoginListener {
    public void OnLoginComplete(AccessToken accessToken);

}
