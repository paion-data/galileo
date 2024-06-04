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

import com.paiondata.transcriptionws.config.WebServiceConfig;
import com.paiondata.transcriptionws.service.MinervaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * MinervaServiceImpl.
 */
@Service
public class MinervaServiceImpl implements MinervaService {
    private static final Logger LOG = LoggerFactory.getLogger(MinervaServiceImpl.class);
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .build();
    private final String url;

    /**
     * The constructor for MinervaServiceImpl.
     * @param webServiceConfig The configuration for the web service.
     */
    @Autowired
    public MinervaServiceImpl(final WebServiceConfig webServiceConfig) {
        this.url = webServiceConfig.getMinervaServiceUrl();
    }

    /**
     * Retrieves a file from Minerva based on its unique identifier.
     * <p>
     * Fetches the binary content of a file from the Minerva system using the provided file ID.

     * @param fileId The unique identifier of the file to be downloaded.

     * @return The file content as a byte array.
     *
     * @throws IOException If an input/output error occurs during the file retrieval process.
     */
    @Override
    public byte[] downloadFile(final String fileId) throws IOException {
        final String completeUrl = url + fileId;
        final Request request = new Request.Builder()
                .url(completeUrl)
                .addHeader("Accept", "*/*")
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                final String message = String.format("Failed to download file: %s", response.message());
                LOG.error(message);
                throw new IOException(message);
            }

            //The ByteArrayOutputStream does not need to be closed manually
            //but be careful to close resources properly when using other types of streams
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            response.body().byteStream().transferTo(outputStream);
            return outputStream.toByteArray();
        }
    }
}
