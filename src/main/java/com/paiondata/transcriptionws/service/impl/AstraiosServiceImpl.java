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
package com.paiondata.transcriptionws.service.impl;

import static java.util.Objects.requireNonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paiondata.transcriptionws.domain.entity.ResponseData;
import com.paiondata.transcriptionws.handler.OptionalGsonAdapters;
import com.paiondata.transcriptionws.service.AstraiosService;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * AstraiosServiceImpl.
 */
@Service
public class AstraiosServiceImpl implements AstraiosService {

    private static final String JSON = "application/json";
    private static final String URL = "http://localhost:8080/v1/data/doctor";
    private static final String GET_DOCTOR_CASE_ID_PAYLOAD_PATH = "get-doctor-caseId.json";
    private static final String UPLOAD_TEXT_PAYLOAD_PATH = "upload-text.json";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse(JSON);
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .build();
    Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Optional.class, new OptionalGsonAdapters.OptionalSerializer())
            .registerTypeHierarchyAdapter(Optional.class, new OptionalGsonAdapters.OptionalDeserializer())
            .create();

    /**
     * get information by caseId.
     * @param caseId caseId.
     * @return ResponseData.Root.
     * @throws IOException IOException.
     */
    @Override
    public ResponseData.Root getInformationByCaseId(final @NotNull String caseId) throws IOException {
        final Request request = buildPostRequest(URL, GET_DOCTOR_CASE_ID_PAYLOAD_PATH, caseId);
        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return gson.fromJson(response.body().string(), ResponseData.Root.class);
        }
    }

    /**
     * upload text.
     * @param doctorId doctorId.
     * @param caseId caseId.
     * @param transcribedText transcribedText.
     * @return boolean.
     * @throws IOException IOException.
     */
    @Override
    public boolean uploadText(final @NotNull String doctorId,
                              final @NotNull String caseId,
                              final @NotNull String transcribedText) throws IOException {
        final Request request = buildPostRequest(URL, UPLOAD_TEXT_PAYLOAD_PATH, doctorId, caseId, transcribedText);
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * build post request.
     * @param url request url.
     * @param payloadTemplate payload template.
     * @param args args.
     * @return Request.
     */
    private Request buildPostRequest(final @NotNull String url,
                                     final @NotNull String payloadTemplate,
                                     final @NotNull Object... args) {
        final String requestBody = String.format(payload(payloadTemplate), args);
        final RequestBody body = RequestBody.create(requestBody, JSON_MEDIA_TYPE);
        return new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", JSON)
                .addHeader("Accept", JSON)
                .build();
    }

    /**
     * Loads a resource file, under "payload" resource directory, as a {@code String} object given that resource file
     * name.
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
        return toResource("payload", resourceName);
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
}
