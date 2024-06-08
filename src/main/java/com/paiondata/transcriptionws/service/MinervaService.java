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

import java.io.IOException;

/**
 * Minerva Service.
 */
public interface MinervaService {
    /**
     * Retrieves a file from Minerva based on its unique identifier.
     * <p>
     * Fetches the binary content of a file from the Minerva system using the provided file ID.
     *
     * @param fileId The unique identifier of the file to be downloaded.
     *
     * @return The file content as a byte array.
     *
     * @throws IOException If an input/output error occurs during the file retrieval process.
     */
    byte[] downloadFile(String fileId) throws IOException;
}
