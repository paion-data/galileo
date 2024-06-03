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
package com.paiondata.transcriptionws.service;

import com.paiondata.transcriptionws.domain.entity.ResponseData;

import java.io.IOException;

/**
 * AstraiosService.
 */
public interface AstraiosService {

    /**
     * Get information by caseId.
     * @param doctorId doctorId.
     * @param caseId caseId.
     * @return ResponseData.Root.
     * @throws IOException IOException.
     */
    ResponseData.Root getInformation(String doctorId, String caseId) throws IOException;

    /**
     * Upload text.
     * @param doctorId doctorId.
     * @param caseId caseId.
     * @param transcribedText transcribedText.
     * @return boolean.
     * @throws IOException IOException.
     */
    boolean uploadText(String doctorId, String caseId, String transcribedText) throws IOException;
}
