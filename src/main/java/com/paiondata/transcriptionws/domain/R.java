/*
 * Copyright Paion Data
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paiondata.transcriptionws.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * the response data.
 * @param <T> the type of the data.
 */
@Schema(description = "the response data")
public class R<T> implements Serializable {
    /**
     * the success code.
     */
    public static final int SUCCESS = 200;
    /**
     * the fail code.
     */
    public static final int FAIL = 500;

    private static final long serialVersionUID = 1L;
    private static final String SUCCESS_MESSAGE = "操作成功";
    private static final String FAIL_MESSAGE = "操作失败";

    @Schema(description = "the status code")
    private int code;
    @Schema(description = "the message")
    private String msg;
    @Schema(description = "the response data")
    private T data;

    /**
     * the success response.
     * @param <T> the type of the data.
     * @return the success response.
     */
    public static <T> R<T> ok() {
        return restResult(null, SUCCESS, SUCCESS_MESSAGE);
    }

    /**
     * the success response.
     * @param data the data.
     * @param <T> the type of the data.
     * @return the success response.
     */
    public static <T> R<T> ok(final T data) {
        return restResult(data, SUCCESS, SUCCESS_MESSAGE);
    }

    /**
     * the success response.
     * @param data the data.
     * @param msg the message.
     * @param <T> the type of the data.
     * @return the success response.
     */
    public static <T> R<T> ok(final T data, final String msg) {
        return restResult(data, SUCCESS, msg);
    }

    /**
     * the fail response.
     * @param <T> the type of the data.
     * @return the fail response.
     */
    public static <T> R<T> fail() {
        return restResult(null, FAIL, FAIL_MESSAGE);
    }

    /**
     * the fail response.
     * @param msg the message.
     * @param <T> the type of the data.
     * @return the fail response.
     */
    public static <T> R<T> fail(final String msg) {
        return restResult(null, FAIL, msg);
    }

    /**
     * the fail response.
     * @param data the data.
     * @param <T> the type of the data.
     * @return the fail response.
     */
    public static <T> R<T> fail(final T data) {
        return restResult(data, FAIL, FAIL_MESSAGE);
    }

    /**
     * the fail response.
     * @param data the data.
     * @param msg the message.
     * @param <T> the type of the data.
     * @return the fail response.
     */
    public static <T> R<T> fail(final T data, final String msg) {
        return restResult(data, FAIL, msg);
    }

    /**
     * the fail response.
     * @param code the code.
     * @param msg the message.
     * @param <T> the type of the data.
     * @return the fail response.
     */
    public static <T> R<T> fail(final int code, final String msg) {
        return restResult(null, code, msg);
    }

    /**
     * rest result.
     * @param data the data.
     * @param code the code.
     * @param msg the message.
     * @param <T> the type of the data.
     * @return the result.
     */
    private static <T> R<T> restResult(final T data, final int code, final String msg) {
        final R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    /**
     * get the code.
     * @return the code.
     */
    public int getCode() {
        return code;
    }

    /**
     * set the code.
     * @param code the code.
     */
    public void setCode(final int code) {
        this.code = code;
    }

    /**
     * get the message.
     * @return the message.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * set the message.
     * @param msg the message.
     */
    public void setMsg(final String msg) {
        this.msg = msg;
    }

    /**
     * get the data.
     * @return the data.
     */
    public T getData() {
        return data;
    }

    /**
     * set the data.
     * @param data the data.
     */
    public void setData(final T data) {
        this.data = data;
    }

    /**
     * is error.
     * @param ret the result.
     * @param <T> the type of the data.
     * @return true if error, false otherwise.
     */
    public static <T> Boolean isError(final R<T> ret) {
        return !isSuccess(ret);
    }

    /**
     * is success.
     * @param ret the result.
     * @param <T> the type of the data.
     * @return true if success, false otherwise.
     */
    public static <T> Boolean isSuccess(final R<T> ret) {
        return R.SUCCESS == ret.getCode();
    }
}
