package com.st.httpktx;

/**
 * code bt St on 2020/4/9
 */
public interface IResponse {
    /**
     * 返回请求的状态
     * @return
     */
   int getResponseCode();
    /**
     * 返回真正的实体类,用String接收
     * @return
     */
   String getResponseData();
}
