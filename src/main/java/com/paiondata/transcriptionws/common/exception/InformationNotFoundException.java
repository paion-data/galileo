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
package com.paiondata.transcriptionws.common.exception;

/**
 * Represents an exception that occurs when requested information is not found.
 */
public class InformationNotFoundException extends BaseException {

    /**
     * Constructs a new InformationNotFoundException with {@code null} as its detail message.
     */
    public InformationNotFoundException() {
    }

    /**
     * Constructs a new InformationNotFoundException with the specified detail message.
     *
     * @param msg The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public InformationNotFoundException(final String msg) {
        super(msg);
    }
}
