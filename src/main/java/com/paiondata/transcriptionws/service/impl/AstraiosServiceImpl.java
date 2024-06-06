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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paiondata.transcriptionws.config.WebServiceConfig;
import com.paiondata.transcriptionws.domain.entity.ResponseData;
import com.paiondata.transcriptionws.handler.OptionalGsonAdapters;
import com.paiondata.transcriptionws.service.AstraiosService;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * The default implementation of {@link AstraiosService}
 */
@Service
public class AstraiosServiceImpl implements AstraiosService {
    private static final String JSON = "application/json";
    private static final String GET_DOCTOR_CASE_ID_PAYLOAD_PATH = "get-doctor-caseId.json";
    private static final String UPLOAD_QUERY_PAYLOAD_PATH = "upload-query.json";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse(JSON);
    private static final Logger LOG = LoggerFactory.getLogger(AstraiosServiceImpl.class);
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .build();
    private final String serviceUrl;
    private final Gson gson;

    /**
     * The constructor for AstraiosServiceImpl.
     * @param webServiceConfig The configuration for the web service.
     */
    @Autowired
    public AstraiosServiceImpl(final WebServiceConfig webServiceConfig) {
        this.serviceUrl = webServiceConfig.getAstraiosServiceUrl();
        this.gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Optional.class, new OptionalGsonAdapters.OptionalSerializer())
                .registerTypeHierarchyAdapter(Optional.class, new OptionalGsonAdapters.OptionalDeserializer())
                .create();
    }

    /**
     * Retrieves the patient record for a specific case associated with a doctor.
     * <p>
     * Retrieves the patient's medical information related to the given case identifier.
     *
     * @param doctorId The ID of the doctor.
     * @param caseId The ID of the case to fetch the patient record for.
     *
     * @return The requested patient record encapsulated in a ResponseData.Root object.
     *
     * @throws IOException If an issue arises while fetching the patient record.
     */
    @Override
    public ResponseData.Root getDoctorInformationById(final String doctorId, final String caseId) throws IOException {
        final Request request = buildPostRequest(serviceUrl, GET_DOCTOR_CASE_ID_PAYLOAD_PATH, caseId, doctorId);
        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                final String message = String.format("Failed to get doctor information: %s", response.message());
                LOG.error(message);
                throw new IOException(message);
            }

            return gson.fromJson(response.body().string(), ResponseData.Root.class);
        }
    }

    /**
     * Attaches transcribed text to a specific case associated with a doctor.
     * <p>
     * Records the provided text under the given doctor and case identifiers.
     *
     * @param doctorId The ID of the doctor linked to the case.
     * @param caseId The ID of the case where the text should be stored.
     * @param transcribedText The text to be uploaded and associated with the case.
     *
     * @return True if the text upload was successful, false otherwise.
     *
     * @throws IOException If an issue occurs during the upload process.
     */
    @Override
    public boolean uploadTranscribedText(final String doctorId,
                                         final String caseId,
                                         final String transcribedText) throws IOException {
        final Request request =
                buildPostRequest(serviceUrl, UPLOAD_QUERY_PAYLOAD_PATH, doctorId, caseId, transcribedText);
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    /**
     * Constructs a POST request with the specified URL, payload, and arguments.
     * <p>
     * Generates a request body using the provided payload template and arguments, then sets appropriate headers.
     *
     * @param url The endpoint URL for the POST request.
     * @param payloadTemplate The template for the request payload, which will be formatted with the provided arguments.
     * @param args Variable number of arguments to be used in the payload template formatting.
     *
     * @return A fully constructed Request object ready for execution.
     */
    private Request buildPostRequest(final String url,
                                     final String payloadTemplate,
                                     final Object... args) {
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
