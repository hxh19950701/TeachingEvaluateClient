package com.hxh19950701.teachingevaluateclient.interfaces;

public interface ManagerInitializeListener {
    void onSuccess(boolean fromCache);
    void onFailure(Exception initException, Exception updateException);
}
