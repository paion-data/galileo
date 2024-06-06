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
package com.paiondata.transcriptionws.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.paiondata.transcriptionws.common.domain.entity.RequestData
import com.paiondata.transcriptionws.common.domain.entity.ResponseData
import com.paiondata.transcriptionws.service.AITranscriptionService
import com.paiondata.transcriptionws.service.AstraiosService
import com.paiondata.transcriptionws.service.MinervaService
import com.paiondata.transcriptionws.service.TranscriptionService

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import spock.lang.Specification

/**
 * Integration tests for the TranscriptionController, focusing on audio transcription and text upload interfaces.
 *
 * This test verifies the flow from receiving a request to transcribe audio, interacting with external services,
 * and uploading the transcribed text back to the system.
 */
@WebMvcTest(TranscriptionController.class)
class TranscriptionControllerITSpec extends Specification {

    private static final String URL = '/v1/transcribe-offline'

    @Autowired
    private MockMvc mockMvc

    @MockBean
    private MinervaService minervaService

    @MockBean
    private AITranscriptionService aiTranscriptionService

    @MockBean
    private AstraiosService astraiosService

    @MockBean
    private TranscriptionService transcriptionServicel

    /**
     * Tests the end-to-end workflow of audio transcription and text upload.
     *
     * @param mockMvc The MockMvc instance for simulating HTTP requests.
     * @param minervaService The mocked MinervaService for dependency injection.
     * @param aiTranscriptionService The mocked AITranscriptionService for dependency injection.
     * @param astraiosService The mocked AstraiosService for dependency injection.
     */
    @Test
    void 'Testing audio transcription and text upload interfaces'() {
        given:
        def requestData = new RequestData('caseId', 'doctorId')
        def responseDataRoot = new ResponseData.Root()
        def audioBytes = new byte[0]
        def transcribedText = 'This is the transcribed text'

        Mockito.when(astraiosService.getDoctorInformationById('doctorId', 'caseId'))
                .thenReturn(responseDataRoot)
        Mockito.when(minervaService.downloadFile('fileId'))
                .thenReturn(audioBytes)
        Mockito.when(aiTranscriptionService.getTranscription(audioBytes))
                .thenReturn(transcribedText)
        Mockito.when(astraiosService.uploadTranscribedText('doctorId', 'caseId', transcribedText))
                .thenReturn(true)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestData)))

        then:
        result.andExpect(MockMvcResultMatchers.status().isOk())
    }
}
