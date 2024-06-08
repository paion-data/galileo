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
package com.paiondata.transcriptionws.service;

import okhttp3.MediaType;
import okhttp3.Response;

import java.io.IOException;

/**
 * Provides methods to perform various HTTP operations using OkHttp client.
 */
public interface OkHttpClientService {

    /**
     * Downloads a file from the specified URL.
     *
     * @param url The URL from which the file will be downloaded.
     *
     * @return The server's response to the file download request.
     *
     * @throws IOException If an I/O error occurs during the network operation.
     */
    Response fileDownloadClient(String url) throws IOException;

    /**
     * Sends a multipart request to get transcription data.
     *
     * @param url      The URL to send the transcription request to.
     * @param fileName The name of the audio file being sent.
     * @param mediaType The media type of the audio file.
     * @param file The binary content of the audio file.
     *
     * @return The server's response containing transcription details.
     *
     * @throws IOException If an I/O error occurs during the network operation.
     */
    Response getTranscriptionClient(String url, String fileName, MediaType mediaType, byte[] file) throws IOException;

    /**
     * Executes a GraphQL query against a specified endpoint.
     *
     * @param url          The GraphQL endpoint URL.
     * @param payloadTemplate A template string for the GraphQL query payload.
     * @param args         Variable number of arguments to be formatted into the query payload.
     *
     * @return The server's response to the GraphQL query.
     *
     * @throws IOException If an I/O error occurs during the network operation.
     */
    Response bulidGraphQLClient(String url,
                                String payloadTemplate,
                                Object... args) throws IOException;
}
