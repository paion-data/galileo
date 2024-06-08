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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paiondata.transcriptionws.config.WebServiceConfig;
import com.paiondata.transcriptionws.common.domain.entity.ResponseData;
import com.paiondata.transcriptionws.handler.OptionalGsonAdapters;
import com.paiondata.transcriptionws.service.AstraiosService;
import com.paiondata.transcriptionws.service.OkHttpClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;

/**
 * The default implementation of {@link AstraiosService}.
 */
@Service
public class AstraiosServiceImpl implements AstraiosService {
    private static final String GET_DOCTOR_CASE_ID_PAYLOAD_PATH = "get-doctor-caseId.graphql";
    private static final String UPLOAD_QUERY_PAYLOAD_PATH = "upload-query.graphql";
    private static final Logger LOG = LoggerFactory.getLogger(AstraiosServiceImpl.class);
    private final String serviceUrl;
    private final Gson gson;

    @Autowired
    private OkHttpClientService clientService;

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
        try (Response response = clientService
                .bulidGraphQLClient(serviceUrl, GET_DOCTOR_CASE_ID_PAYLOAD_PATH, doctorId, caseId)) {
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
        try (Response response = clientService
                .bulidGraphQLClient(serviceUrl, UPLOAD_QUERY_PAYLOAD_PATH, doctorId, caseId, transcribedText)) {
            return response.isSuccessful();
        }
    }
}
