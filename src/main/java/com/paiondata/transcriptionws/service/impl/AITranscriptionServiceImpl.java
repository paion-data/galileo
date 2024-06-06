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

import com.paiondata.transcriptionws.config.WebServiceConfig;
import com.paiondata.transcriptionws.service.AITranscriptionService;
import com.paiondata.transcriptionws.service.OkHttpClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

/**
 * The default implementation of {@link AITranscriptionService}.
 */
@Service
public class AITranscriptionServiceImpl implements AITranscriptionService {
    private static final int TIME_OUT = 150;
    private static final String DEFAULT_AUDIO_FILENAME = "trans";
    private static final MediaType AUDIO_MEDIA_TYPE = MediaType.parse("audio/*");
    private static final Logger LOG = LoggerFactory.getLogger(AITranscriptionServiceImpl.class);
    private final String url;

    @Autowired
    private OkHttpClientService clientService;

    /**
     * The constructor.
     * @param webServiceConfig The configuration of the web service
     */
    @Autowired
    public AITranscriptionServiceImpl(final WebServiceConfig webServiceConfig) {
        this.url = webServiceConfig.getAITranscriptionServiceUrl();
    }

    /**
     * Returns the transcription of an specified audio file.
     * <p>
     * The format of the audio file must be either .WAV or .MP3
     *
     * @param fileBytes  The byte array of the provided audio file contents
     *
     * @return the transcribed text of the audio file in one {@code String}
     *
     * @throws IOException if the {@code fileBytes} fails to be transcribed
     */
    @Override
    public String getTranscription(final byte[] fileBytes) throws IOException {
        try (Response response = clientService
                .getTranscriptionClient(url, DEFAULT_AUDIO_FILENAME, AUDIO_MEDIA_TYPE, fileBytes)) {
            if (!response.isSuccessful()) {
                final String message = String.format("Failed to create transcription request: %s", response.message());
                LOG.error(message);
                throw new IOException(message);
            }

            return Objects.requireNonNull(response.body()).string();
        }
    }
}
