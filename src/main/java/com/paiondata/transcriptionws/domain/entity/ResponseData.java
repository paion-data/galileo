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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Represents a hierarchical structure containing various nested objects for data retrieval.
 */
public class ResponseData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Root object holding the main data.
     */
    public static class Root {
        @JsonProperty("data")
        private Data data;

        /**
         * Retrieves the data object.
         *
         * @return The data object.
         */
        public Data getData() {
            return data;
        }

        /**
         * Sets the data object.
         *
         * @param data The data object to set.
         */
        public void setData(final Data data) {
            this.data = data;
        }
    }

    /**
     * Container for the main data.
     */
    public static class Data {
        @JsonProperty("doctor")
        private Doctor doctor;

        /**
         * Retrieves the Doctor object.
         *
         * @return The Doctor object.
         */
        public Doctor getDoctor() {
            return doctor;
        }

        /**
         * Sets the Doctor object.
         *
         * @param doctor The Doctor object to set.
         */
        public void setDoctor(final Doctor doctor) {
            this.doctor = doctor;
        }
    }

    /**
     * Represents a Doctor entity.
     */
    public static class Doctor {
        @JsonProperty("edges")
        private List<Edge> edges;

        /**
         * Retrieves the list of Edge objects.
         *
         * @return The list of Edge objects.
         */
        public List<Edge> getEdges() {
            return edges;
        }

        /**
         * Sets the list of Edge objects.
         *
         * @param edges The list of Edge objects to set.
         */
        public void setEdges(final List<Edge> edges) {
            this.edges = edges;
        }
    }

    /**
     * Represents a connection edge in the data hierarchy.
     */
    public static class Edge {
        @JsonProperty("node")
        private Node node;

        /**
         * Retrieves the Node object.
         *
         * @return The Node object.
         */
        public Node getNode() {
            return node;
        }

        /**
         * Sets the Node object.
         *
         * @param node The Node object to set.
         */
        public void setNode(final Node node) {
            this.node = node;
        }
    }

    /**
     * Represents a generic node in the data hierarchy.
     */
    public static class Node {
        @JsonProperty("id")
        private String id;

        @JsonProperty("cases")
        private Optional<Cases> cases = Optional.empty();

        /**
         * Retrieves the ID of the node.
         *
         * @return The ID of the node.
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the ID of the node.
         *
         * @param id The ID of the node to set.
         */
        public void setId(final String id) {
            this.id = id;
        }

        /**
         * Retrieves the optional Cases object.
         *
         * @return The optional Cases object.
         */
        public Optional<Cases> getCases() {
            return cases;
        }

        /**
         * Sets the optional Cases object.
         *
         * @param cases The optional Cases object to set.
         */
        public void setCases(final Optional<Cases> cases) {
            this.cases = cases;
        }
    }

    /**
     * Represents a collection of case edges.
     */
    public static class Cases {
        @JsonProperty("edges")
        private List<CaseEdge> edges;

        /**
         * Retrieves the list of CaseEdge objects.
         *
         * @return The list of CaseEdge objects.
         */
        public List<CaseEdge> getEdges() {
            return edges;
        }

        /**
         * Sets the list of CaseEdge objects.
         *
         * @param edges The list of CaseEdge objects to set.
         */
        public void setEdges(final List<CaseEdge> edges) {
            this.edges = edges;
        }
    }

    /**
     * Represents a case edge in the data hierarchy.
     */
    public static class CaseEdge {
        @JsonProperty("node")
        private CaseNode node;

        /**
         * Retrieves the CaseNode object.
         *
         * @return The CaseNode object.
         */
        public CaseNode getNode() {
            return node;
        }

        /**
         * Sets the CaseNode object.
         *
         * @param node The CaseNode object to set.
         */
        public void setNode(final CaseNode node) {
            this.node = node;
        }
    }

    /**
     * Represents a specific case node in the data hierarchy.
     */
    public static class CaseNode {
        @JsonProperty("id")
        private String id;

        @JsonProperty("audio")
        private Optional<Audio> audio = Optional.empty();

        /**
         * Retrieves the ID of the case node.
         *
         * @return The ID of the case node.
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the ID of the case node.
         *
         * @param id The ID of the case node to set.
         */
        public void setId(final String id) {
            this.id = id;
        }

        /**
         * Retrieves the optional Audio object.
         *
         * @return The optional Audio object.
         */
        public Optional<Audio> getAudio() {
            return audio;
        }

        /**
         * Sets the optional Audio object.
         *
         * @param audio The optional Audio object to set.
         */
        public void setAudio(final Optional<Audio> audio) {
            this.audio = audio;
        }
    }

    /**
     * Represents a collection of audio edges.
     */
    public static class Audio {
        @JsonProperty("edges")
        private List<AudioEdge> edges;

        /**
         * Retrieves the list of AudioEdge objects.
         *
         * @return The list of AudioEdge objects.
         */
        public List<AudioEdge> getEdges() {
            return edges;
        }

        /**
         * Sets the list of AudioEdge objects.
         *
         * @param edges The list of AudioEdge objects to set.
         */
        public void setEdges(final List<AudioEdge> edges) {
            this.edges = edges;
        }
    }

    /**
     * Represents an audio edge in the data hierarchy.
     */
    public static class AudioEdge {
        @JsonProperty("node")
        private AudioNode node;

        /**
         * Retrieves the AudioNode object.
         *
         * @return The AudioNode object.
         */
        public AudioNode getNode() {
            return node;
        }

        /**
         * Sets the AudioNode object.
         *
         * @param node The AudioNode object to set.
         */
        public void setNode(final AudioNode node) {
            this.node = node;
        }
    }

    /**
     * Represents a specific audio node in the data hierarchy.
     */
    public static class AudioNode {
        @JsonProperty("id")
        private String id;

        @JsonProperty("fileId")
        private String fileId;

        /**
         * Retrieves the ID of the audio node.
         *
         * @return The ID of the audio node.
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the ID of the audio node.
         *
         * @param id The ID of the audio node to set.
         */
        public void setId(final String id) {
            this.id = id;
        }

        /**
         * Retrieves the file ID associated with the audio node.
         *
         * @return The file ID.
         */
        public String getFileId() {
            return fileId;
        }

        /**
         * Sets the file ID associated with the audio node.
         *
         * @param fileId The file ID to set.
         */
        public void setFileId(final String fileId) {
            this.fileId = fileId;
        }
    }

    /**
     * Constructs an empty ResponseData object.
     */
    public ResponseData() {
    }
}
