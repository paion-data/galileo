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
package com.paiondata.transcriptionws.service.impl;

import static java.util.Objects.requireNonNull;

import com.paiondata.transcriptionws.service.OkHttpClientService;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * The default implementation of {@link OkHttpClientService}.
 */
@Service
public class OkHttpClientServiceImpl implements OkHttpClientService {

    private static final int TIME_OUT = 250;
    private static final String JSON = "application/json";
    private static final String APPLICATION_WILDCARD = "*/*";
    private static final String CONTENT_TYPE_JSON = "Content-Type";
    private static final String ACCEPT_HEADER = "Accept";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse(JSON);

    OkHttpClient client = new OkHttpClient.Builder()
            .build();

    /**
     * Downloads a file from the specified URL.
     *
     * @param url The URL from which the file will be downloaded.
     *
     * @return The server's response to the file download request.
     *
     * @throws IOException If an I/O error occurs during the network operation.
     */
    @Override
    public Response fileDownloadClient(final String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(ACCEPT_HEADER, APPLICATION_WILDCARD)
                .build();

        return this.client.newCall(request).execute();
    }

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
    @Override
    public Response getTranscriptionClient(final String url, final String fileName,
                                           final MediaType mediaType, final byte[] file) throws IOException {
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", fileName, RequestBody.create(mediaType, file))
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        final OkHttpClient longClient = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .callTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();

        return longClient.newCall(request).execute();
    }

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
    @Override
    public Response bulidGraphQLClient(final String url,
                                       final String payloadTemplate,
                                       final Object... args) throws IOException {
        final String requestBody = toQueryString(String.format(payload(payloadTemplate), args));
        final RequestBody body = RequestBody.create(requestBody, JSON_MEDIA_TYPE);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader(CONTENT_TYPE_JSON, JSON)
                .addHeader(ACCEPT_HEADER, JSON)
                .build();

        return this.client.newCall(request).execute();
    }

    /**
     * Loads a resource file, under "payload" resource directory, as a {@code String} object given that resource file
     * name.
     * <p>
     * All new line characters ("\n") will be removed as well.
     *
     * @param resourceName  The specified resource file name
     *
     * @return the resource file content as a single {@code String}
     *
     * @throws NullPointerException if {@code resourceName} is {@code null}
     * @throws IllegalStateException if an I/O error occurs reading from the resource file stream
     * @throws IllegalArgumentException  if resource path is not formatted strictly according to RFC2396 and cannot be
     * converted to a URI.
     */
    @NotNull
    private String payload(final @NotNull String resourceName) {
        return toResource("payload", resourceName)
                .replace("\n", "")
                // Escape the double quotes and backslash in the input string
                .replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Loads a resource file content as a {@code String} object according to a provided resource path.
     * <p>
     * The resource path is defined by two components:
     * <ol>
     *     <li> a relative path under "resource" folder
     *     <li> the name of the resource file
     * </ol>
     * For example, when we would like to read
     * "src/test/resources/payload/metadata/multiple-fields-metadata-request.json", then the relative path is
     * "payload/metadata" and the name of the resource file is "multiple-fields-metadata-request.json"
     *
     * @param resourceDirPath  The relative path under "resource" folder
     * @param resourceFilename  The specified resource file name
     *
     * @return the resource file content as a single {@code String}
     *
     * @throws NullPointerException if {@code resourceFilename} is {@code null}
     * @throws IllegalStateException if an I/O error occurs reading from the resource file stream
     * @throws IllegalArgumentException  if resource path is not formatted strictly according to RFC2396 and cannot be
     * converted to a URI.
     */
    @NotNull
    private String toResource(final @NotNull String resourceDirPath, final @NotNull String resourceFilename) {
        requireNonNull(resourceDirPath);
        requireNonNull(resourceFilename);

        final String resource = String.format(
                "%s/%s",
                resourceDirPath.endsWith("/")
                        ? resourceDirPath.substring(0, resourceDirPath.length() - 1)
                        : resourceDirPath,
                resourceFilename
        );

        try {
            return new String(
                    Files.readAllBytes(
                            Paths.get(
                                    requireNonNull(
                                            this.getClass()
                                                    .getClassLoader()
                                                    .getResource(resource)
                                    )
                                            .toURI()
                            )
                    )
            );
        } catch (final IOException exception) {
            final String message = String.format("Error reading file stream from '%s'", resource);
            throw new IllegalStateException(message, exception);
        } catch (final URISyntaxException exception) {
            final String message = String.format("'%s' is not a valid URI fragment", resource);
            throw new IllegalArgumentException(message, exception);
        }
    }
    /**
     * Converts a GraphQL query to a JSON string.
     *
     * @param query GraphQL query.
     *
     * @return String representation of the query.
     */
    private String toQueryString(final String query) {
        return String.format("{\"query\": \"%s\"}", query);
    }
}
