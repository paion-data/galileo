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

import com.paiondata.transcriptionws.common.domain.entity.ResponseData;

/**
 * Service interface for handling transcription operations.
 */
public interface TranscriptionService {

    /**
     * Extracts the fileId from the response data.
     *
     * @param responseData The response data object.
     *
     * @return The extracted fileId.
     */
    String extractFileId(ResponseData.Root responseData);
}
