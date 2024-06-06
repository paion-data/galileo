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

import com.paiondata.transcriptionws.domain.entity.ResponseData;

import java.io.IOException;

/**
 * AstraiosService.
 */
public interface AstraiosService {

    /**
     * Retrieves the patient record for a specific case associated with a doctor.
     * <p>
     * Retrieves the patient's medical information related to the given case identifier.

     * @param doctorId The ID of the doctor.
     * @param caseId The ID of the case to fetch the patient record for.

     * @return The requested patient record encapsulated in a ResponseData.Root object.
     *
     * @throws IOException If an issue arises while fetching the patient record.
     */
    ResponseData.Root getDoctorInformationById(String doctorId, String caseId) throws IOException;

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
    boolean uploadTranscribedText(String doctorId, String caseId, String transcribedText) throws IOException;
}
