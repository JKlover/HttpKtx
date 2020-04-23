package com.st.httpktx;

import android.content.Context;

/**
 * code bt St on 2019/3/1 0001.
 */
public  class RequestConfig {
    private Context context;
    private Object postRequestBase;

    public RequestConfig(Builder builder) {
        this.context=builder.context;
        this.postRequestBase=builder.postRequestBase;
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object getPostRequestBase() {
        return postRequestBase;
    }

    public void setPostRequestBase(Object postRequestBase) {
        this.postRequestBase = postRequestBase;
    }

    public static class Builder {
        private Context context;
        private Object postRequestBase;

        private Builder(Context context) {
            this.context = context.getApplicationContext();
        }
        public Builder basePostRequest(Object postRequestBase) {
            this.postRequestBase = postRequestBase;
            return this;
        }
        public RequestConfig build(){
            return new RequestConfig(this);
        }

    }
}
