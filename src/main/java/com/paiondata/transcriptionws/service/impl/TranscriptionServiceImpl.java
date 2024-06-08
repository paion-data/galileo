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

import com.paiondata.transcriptionws.common.constant.MessageConstant;
import com.paiondata.transcriptionws.common.domain.entity.ResponseData;
import com.paiondata.transcriptionws.common.exception.InformationNotFoundException;
import com.paiondata.transcriptionws.service.TranscriptionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The default implementation of {@link TranscriptionService}.
 */
@Service
public class TranscriptionServiceImpl implements TranscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(TranscriptionServiceImpl.class);
    private static final String DOCTOR_NOT_FOUND_MSG = MessageConstant.DOCTOR_NOT_FOUND;
    private static final String CASE_NOT_FOUND_MSG = MessageConstant.CASE_NOT_FOUND;
    private static final String FILEID_NOT_FOUND_MSG = MessageConstant.AUDIO_FILE_NOT_FOUND;

    /**
     * Extracts the fileId from the response data.
     *
     * @param responseData The response data object.
     *
     * @return The extracted fileId.
     * @throws InformationNotFoundException when either the doctor or case is not found.
     */
    @Override
    public String extractFileId(final ResponseData.Root responseData) throws InformationNotFoundException {
        final ResponseData.Doctor doctor = getDoctor(responseData);
        final ResponseData.CaseNode cases = getFirstCaseNode(doctor);
        return getAudioFileId(cases);
    }

    /**
     * Retrieves the Doctor object from the response data.
     *
     * @param responseData The root response data object containing the doctor information.
     *
     * @return The Doctor object extracted from the response data.
     *
     * @throws InformationNotFoundException if the doctor information is not found in the response data.
     */
    private ResponseData.Doctor getDoctor(final ResponseData.Root responseData) throws InformationNotFoundException {
        final ResponseData.Doctor doctor = responseData.getData().getDoctor();
        if (doctor == null || doctor.getEdges().isEmpty()) {
            LOG.error(DOCTOR_NOT_FOUND_MSG);
            throw new InformationNotFoundException(DOCTOR_NOT_FOUND_MSG);
        }
        return doctor;
    }

    /**
     * Retrieves the first CaseNode from the specified Doctor object.
     *
     * @param doctor The Doctor object from which to extract the first case.
     *
     * @return The first CaseNode found in the Doctor's cases.
     *
     * @throws InformationNotFoundException if no cases are found for the given Doctor.
     */
    private ResponseData.CaseNode getFirstCaseNode(final ResponseData.Doctor doctor)
            throws InformationNotFoundException {
        final List<ResponseData.CaseEdge> caseEdges = doctor.getEdges().get(0).getNode().getCases().get().getEdges();
        if (caseEdges.isEmpty()) {
            LOG.error(CASE_NOT_FOUND_MSG);
            throw new InformationNotFoundException(CASE_NOT_FOUND_MSG);
        }
        return caseEdges.get(0).getNode();
    }

    /**
     * Extracts the file ID from the given CaseNode object.
     *
     * @param caseNode The CaseNode from which to extract the file ID.
     *
     * @return The file ID associated with the audio file in the CaseNode.
     *
     * @throws InformationNotFoundException if the file ID is not found or empty.
     */
    private String getAudioFileId(final ResponseData.CaseNode caseNode) {
        final String fileId = caseNode.getAudio().get().getEdges().get(0).getNode().getFileId();
        if (fileId.isEmpty()) {
            LOG.error(FILEID_NOT_FOUND_MSG);
            throw new InformationNotFoundException(FILEID_NOT_FOUND_MSG);
        }
        return fileId;
    }
}
