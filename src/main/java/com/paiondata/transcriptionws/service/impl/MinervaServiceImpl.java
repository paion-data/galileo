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

import com.paiondata.transcriptionws.service.MinervaService;

import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * MinervaServiceImpl.
 */
@Service
public class MinervaServiceImpl implements MinervaService {

    private static final String BASE_URL = "http://localhost:8080/v1/file/download?fileId=";
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .build();

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
        final String completeUrl = BASE_URL + fileId;
        final Request request = new Request.Builder()
                .url(completeUrl)
                .addHeader("Accept", "*/*")
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response.message());
            }

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            response.body().byteStream().transferTo(outputStream);
            return outputStream.toByteArray();
        }
    }
}
