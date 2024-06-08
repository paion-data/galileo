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

import java.io.IOException;

/**
 * AI Transcription Service.
 */
public interface AITranscriptionService {
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
    String getTranscription(byte[] fileBytes) throws IOException;
}
