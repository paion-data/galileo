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
package com.paiondata.transcriptionws.controller;

import com.paiondata.transcriptionws.domain.R;
import com.paiondata.transcriptionws.domain.entity.FileUploadRequest;
import com.paiondata.transcriptionws.domain.entity.ResponseData;
import com.paiondata.transcriptionws.service.AITranscriptionService;
import com.paiondata.transcriptionws.service.AstraiosService;
import com.paiondata.transcriptionws.service.MinervaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * TranscriptionController.
 */
@RestController
@RequestMapping("/v1")
public class TranscriptionController {

    @Autowired
    private MinervaService minervaService;

    @Autowired
    private AITranscriptionService aiTranscriptionService;

    @Autowired
    private AstraiosService astraiosService;

    /**
     * Transcribe the audio file and upload the transcription text to Astraios.
     * @param fileUploadRequest the request body.
     * @return the response.
     */
    @PostMapping("/transcribe")
    public R<String> getTranscription(@RequestBody final FileUploadRequest fileUploadRequest) {
        final String caseId = fileUploadRequest.getCaseId();

        try {
            final ResponseData.Root responseData = astraiosService.getInformationByCaseId(caseId);
            if (responseData == null || responseData.getData() == null) {
                return R.fail("Invalid caseId or data not found");
            }

            final String doctorId = extractDoctorId(responseData);
            final String fileId = extractFileId(responseData);

            final byte[] audioFile = minervaService.downloadFile(fileId);
            final String transcribedText = aiTranscriptionService.getTranscription(audioFile);

            final boolean uploadSuccess = astraiosService.uploadText(doctorId, caseId, transcribedText);
            return uploadSuccess ? R.ok() : R.fail("Failed to upload transcription text");

        } catch (final IOException e) {
            return R.fail("File processing error: " + e);
        }
    }

    /**
     * Extract the fileId from the response data.
     * @param responseData the response data.
     * @return String.
     */
    private String extractFileId(final ResponseData.Root responseData) {
        return responseData.getData().getDoctor().getEdges().get(0).getNode().getCases().get().getEdges()
                .get(0).getNode().getAudio().get().getEdges().get(0).getNode().getFileId();
    }

    /**
     * Extract the doctorId from the response data.
     * @param responseData the response data.
     * @return String.
     */
    private String extractDoctorId(final ResponseData.Root responseData) {
        return responseData.getData().getDoctor().getEdges().get(0).getNode().getId();
    }
}
