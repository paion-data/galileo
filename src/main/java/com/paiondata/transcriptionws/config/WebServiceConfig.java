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
package com.paiondata.transcriptionws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configures URLs for various web services crucial to the application's functionality.
 */
@Configuration
public class WebServiceConfig {
    @Value("${service.astraios.url}")
    private String astraiosServiceUrl;

    @Value("${service.minerva.url}")
    private String minervaServiceUrl;

    @Value("${service.aiTranscription.url}")
    private String aiTranscriptionServiceUrl;

    /**
     * Retrieves the base URL for Astraios service interactions.
     *
     * @return The configured URL for Astraios service.
     */
    public String getAstraiosServiceUrl() {
        return astraiosServiceUrl;
    }

    /**
     * Obtains the base URL for accessing Minerva service endpoints.
     *
     * @return The configured URL for Minerva service.
     */
    public String getMinervaServiceUrl() {
        return minervaServiceUrl;
    }

    /**
     * Returns the endpoint URL for AI-driven transcription services.
     *
     * @return The URL set for AI Transcription service.
     */
    public String getAITranscriptionServiceUrl() {
        return aiTranscriptionServiceUrl;
    }
}
