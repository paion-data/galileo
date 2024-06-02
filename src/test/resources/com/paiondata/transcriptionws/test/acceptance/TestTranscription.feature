Feature: Test Transcription

  Scenario: Update an Existing Doctor
    Given we download the audio file
    When we upload the audio file to the transcription service
    Then the transcribed text should be "Hey!"
