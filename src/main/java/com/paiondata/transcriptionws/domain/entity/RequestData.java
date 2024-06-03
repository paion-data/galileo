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
package com.paiondata.transcriptionws.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * RequestData.
 */
@Schema(description = "request data")
public class RequestData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "doctor's ID")
    private String doctorId;
    @Schema(description = "case's ID")
    private String caseId;

    /**
     * Get doctorId.
     * @return doctorId.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Set doctorId.
     * @param doctorId doctorId.
     */
    public void setDoctorId(final String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Get caseId.
     * @return caseId.
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * Set caseId.
     * @param caseId caseId.
     */
    public void setCaseId(final String caseId) {
        this.caseId = caseId;
    }
}
