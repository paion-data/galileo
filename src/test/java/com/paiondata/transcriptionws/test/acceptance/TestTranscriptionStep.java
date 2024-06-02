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
package com.paiondata.transcriptionws.test.acceptance;

import static io.restassured.RestAssured.given;

import com.paiondata.transcriptionws.service.AITranscriptionService;
import com.paiondata.transcriptionws.service.MinervaService;

import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Test step definitions for the transcription service.
 */
@CucumberContextConfiguration
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestTranscriptionStep extends AbstractStepDefinitions {

    private static final String FILE_NAME = "testFile.mp3";
    @Autowired
    private MinervaService minervaService;
    @Autowired
    private AITranscriptionService aiTranscriptionService;

    private byte[] audioFile;
    private String fileId;
    private String transcribedText;

    /**
     * Uploads the test file to the Minerva service.
     */
    @Before
    public void uploadFile() {
        final File file = new File(Paths.get("src", "test", "resources", "audioFile", FILE_NAME).toString());
        final RequestSpecification requestSpec = given()
                .header("accept", "*/*")
                .contentType(String.valueOf(ContentType.MULTIPART_FORM_DATA));

        final Response response = requestSpec
                .multiPart("file", file, FILE_NAME)
                .when()
                .post("/file/upload");

        response.then()
                .statusCode(201);

        this.fileId = response.getBody().jsonPath().getString("fileId");
    }

    /**
     * Downloads the audio file from the Minerva service.
     * @throws IOException if the audio file cannot be downloaded.
     */
    @Given("we download the audio file")
    public void weDownloadTheAudioFile() throws IOException {
        this.audioFile = minervaService.downloadFile(this.fileId);
    }

    /**
     * Transcribes the audio file.
     * @throws IOException if the audio file cannot be downloaded
     */
    @When("we upload the audio file to the transcription service")
    public void weUploadTheAudioFileToTheTranscriptionService() throws IOException {
        this.transcribedText = aiTranscriptionService.getTranscription(this.audioFile);
    }

    /**
     * Checks if the transcribed text is not empty.
     * @param text the expected transcribed text.
     */
    @Then("the transcribed text should be {string}")
    public void theTranscribedTextShouldBe(final String text) {
        Assert.hasText(text, this.transcribedText);
    }
}
