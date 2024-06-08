/*
 * Copyright 2024 Paion Data
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
package com.paiondata.transcriptionws.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a generic response container for API operations.
 * <p>
 * This class encapsulates the status code, message, and data for a successful or failed response.
 * It provides static methods for creating success and failure responses with optional data and messages.
 *
 * @param <T> The type of the response data.
 */
@Schema(description = "A generic response container for API operations")
public class Result<T> extends BaseEntity {
    /**
     * Constant for a successful status code.
     */
    public static final int SUCCESS = 200;
    /**
     * Constant for a failed status code.
     */
    public static final int FAIL = 500;
    private static final String DEFAULT_SUCCESS_MESSAGE = "Operation succeeded";
    private static final String DEFAULT_FAIL_MESSAGE = "Operation failed";

    @Schema(description = "The status code of the response")
    private int code;
    @Schema(description = "A message describing the response status")
    private String msg;
    @Schema(description = "The data associated with the response")
    private T data;

    /**
     * Creates a default success response without any data.
     *
     * @param <T> The type of the response data.
     *
     * @return A new success response instance.
     */
    public static <T> Result<T> ok() {
        return restResult(null, SUCCESS, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * Creates a success response with the specified data.
     *
     * @param data The data to include in the response.
     * @param <T> The type of the response data.
     *
     * @return A new success response instance.
     */
    public static <T> Result<T> ok(final T data) {
        return restResult(data, SUCCESS, DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * Creates a success response with the specified data and message.
     *
     * @param data The data to include in the response.
     * @param msg A custom success message.
     * @param <T> The type of the response data.
     *
     * @return A new success response instance.
     */
    public static <T> Result<T> ok(final T data, final String msg) {
        return restResult(data, SUCCESS, msg);
    }

    /**
     * Creates a default failure response without any data.
     *
     * @param <T> The type of the response data.
     *
     * @return A new failure response instance.
     */
    public static <T> Result<T> fail() {
        return restResult(null, FAIL, DEFAULT_FAIL_MESSAGE);
    }

    /**
     * Creates a failure response with the specified message.
     *
     * @param msg A custom failure message.
     * @param <T> The type of the response data.
     *
     * @return A new failure response instance.
     */
    public static <T> Result<T> fail(final String msg) {
        return restResult(null, FAIL, msg);
    }

    /**
     * Creates a failure response with the specified data.
     *
     * @param data The data to include in the response.
     * @param <T> The type of the response data.
     *
     * @return A new failure response instance.
     */
    public static <T> Result<T> fail(final T data) {
        return restResult(data, FAIL, DEFAULT_FAIL_MESSAGE);
    }

    /**
     * Creates a failure response with the specified data and message.
     *
     * @param data The data to include in the response.
     * @param msg A custom failure message.
     * @param <T> The type of the response data.
     *
     * @return A new failure response instance.
     */
    public static <T> Result<T> fail(final T data, final String msg) {
        return restResult(data, FAIL, msg);
    }

    /**
     * Creates a failure response with the specified code and message.
     *
     * @param code The status code for the failure.
     * @param msg A custom failure message.
     * @param <T> The type of the response data.
     *
     * @return A new failure response instance.
     */
    public static <T> Result<T> fail(final int code, final String msg) {
        return restResult(null, code, msg);
    }

    /**
     * Generates a result object with the given data, code, and message.
     *
     * @param data The data to include in the response.
     * @param code The status code for the response.
     * @param msg A message describing the response status.
     * @param <T> The type of the response data.
     *
     * @return A new result instance.
     */
    private static <T> Result<T> restResult(final T data, final int code, final String msg) {
        final Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    /**
     * Gets the status code of the response.
     *
     * @return The status code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the status code of the response.
     *
     * @param code The status code.
     */
    public void setCode(final int code) {
        this.code = code;
    }

    /**
     * Gets the message associated with the response.
     *
     * @return The message.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the message associated with the response.
     *
     * @param msg The message.
     */
    public void setMsg(final String msg) {
        this.msg = msg;
    }

    /**
     * Gets the data included in the response.
     *
     * @return The data.
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data included in the response.
     *
     * @param data The data.
     */
    public void setData(final T data) {
        this.data = data;
    }

    /**
     * Checks if the response indicates an error.
     *
     * @param ret The result to check.
     * @param <T> The type of the response data.
     *
     * @return True if the response is a failure, false otherwise.
     */
    public static <T> Boolean isError(final Result<T> ret) {
        return !isSuccess(ret);
    }

    /**
     * Checks if the response indicates a success.
     *
     * @param ret The result to check.
     * @param <T> The type of the response data.
     *
     * @return True if the response is a success, false otherwise.
     */
    public static <T> Boolean isSuccess(final Result<T> ret) {
        return SUCCESS == ret.getCode();
    }
}
