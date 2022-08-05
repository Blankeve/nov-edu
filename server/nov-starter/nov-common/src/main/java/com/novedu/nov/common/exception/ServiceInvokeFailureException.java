package com.novedu.nov.common.exception;

public class ServiceInvokeFailureException extends RuntimeException {

    public ServiceInvokeFailureException(String message) {
        super(message);
    }

    public ServiceInvokeFailureException() {
        super("网络出差了，请稍后再试");
    }
}
