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
package com.paiondata.transcriptionws.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Encapsulates the request data with identifiers for a doctor and a case.
 */
@Schema(description = "Request data container including identifiers for a doctor and a case.")
public class RequestData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "The ID of the doctor.")
    private String doctorId;

    @Schema(description = "The ID of the case.")
    private String caseId;

    /**
     * Constructor.
     * @param doctorId the doctor's ID
     *
     * @param caseId the case's ID
     */
    public RequestData(final String doctorId, final String caseId) {
        this.doctorId = doctorId;
        this.caseId = caseId;
    }

    /**
     * Retrieves the doctor's ID.
     *
     * @return The doctor's ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Assigns the doctor's ID.
     *
     * @param doctorId The doctor's ID to assign.
     */
    public void setDoctorId(final String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Retrieves the case's ID.
     *
     * @return The case's ID.
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * Assigns the case's ID.
     *
     * @param caseId The case's ID to assign.
     */
    public void setCaseId(final String caseId) {
        this.caseId = caseId;
    }
}
