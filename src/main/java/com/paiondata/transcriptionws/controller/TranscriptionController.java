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
package com.paiondata.transcriptionws.controller;

import com.paiondata.transcriptionws.domain.Result;
import com.paiondata.transcriptionws.domain.entity.RequestData;
import com.paiondata.transcriptionws.domain.entity.ResponseData;
import com.paiondata.transcriptionws.service.AITranscriptionService;
import com.paiondata.transcriptionws.service.AstraiosService;
import com.paiondata.transcriptionws.service.MinervaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

/**
 * Represents the Transcription Controller responsible for handling audio transcription and text upload to Astraios.
 */
@RestController
@RequestMapping("/v1")
@Tag(name = "Transcription Controller", description = "Handles audio transcription and text upload to Astraios")
public class TranscriptionController {

    @Autowired
    private MinervaService minervaService;

    @Autowired
    private AITranscriptionService aiTranscriptionService;

    @Autowired
    private AstraiosService astraiosService;

    /**
     * Transcribes the audio file and uploads the transcription text to Astraios.
     *
     * @param requestData The request body containing the doctor and case IDs.
     *
     * @return The response indicating success or failure.
     */
    @PostMapping("/transcribe-offline")
    @Operation(summary = "Transcribes audio and uploads transcription text to Astraios")
    public Result<String> getTranscription(@RequestBody final RequestData requestData) {
        final String caseId = requestData.getCaseId();
        final String doctorId = requestData.getDoctorId();

        try {
            final ResponseData.Root responseData = astraiosService.getDoctorInformationById(doctorId, caseId);
            if (responseData == null || responseData.getData() == null) {
                return Result.fail("Invalid caseId or data not found");
            }

            final String fileId = extractFileId(responseData);

            final byte[] audioFile = minervaService.downloadFile(fileId);
            final String transcribedText = aiTranscriptionService.getTranscription(audioFile);

            final boolean uploadSuccess = astraiosService.uploadTranscribedText(doctorId, caseId, transcribedText);
            return uploadSuccess ? Result.ok() : Result.fail("Failed to upload transcription text");

        } catch (final IOException e) {
            return Result.fail("File processing error: " + e);
        }
    }

    /**
     * Extracts the fileId from the response data.
     *
     * @param responseData The response data object.
     *
     * @return The extracted fileId.
     */
    private String extractFileId(final ResponseData.Root responseData) {
        return responseData.getData().getDoctor().getEdges().get(0).getNode().getCases().get().getEdges()
                .get(0).getNode().getAudio().get().getEdges().get(0).getNode().getFileId();
    }
}
