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

import com.paiondata.transcriptionws.service.AITranscriptionService;

import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * AITranscriptionServiceImpl.
 */
@Service
public class AITranscriptionServiceImpl implements AITranscriptionService {

    private static final int TIME_OUT = 150;
    private static final String DEFAULT_AUDIO_FILENAME = "trans";
    private static final MediaType AUDIO_MEDIA_TYPE = MediaType.parse("audio/*");
    private static final String URL = "http://localhost:5000/model1";
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .callTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();

    /**
     * Get the transcription of the given audio file.
     * @param fileBytes the audio file.
     * @return the transcription.
     * @throws IOException thrown if the transcription request fails.
     */
    @Override
    public String getTranscription(final byte[] fileBytes) throws IOException {
        final RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio", DEFAULT_AUDIO_FILENAME, RequestBody.create(AUDIO_MEDIA_TYPE, fileBytes))
                .build();

        final Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Transcription request failed with status code: " + response.code());
            }

            return Objects.requireNonNull(response.body()).string();
        }
    }
}
