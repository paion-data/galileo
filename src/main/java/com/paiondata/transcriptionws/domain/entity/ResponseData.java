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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * ResponseData.
 */
public class ResponseData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Root.
     */
    public static class Root {
        @JsonProperty("data")
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(final Data data) {
            this.data = data;
        }
    }

    /**
     * Data.
     */
    public static class Data {
        @JsonProperty("doctor")
        private Doctor doctor;

        public Doctor getDoctor() {
            return doctor;
        }

        public void setDoctor(final Doctor doctor) {
            this.doctor = doctor;
        }
    }

    /**
     * Doctor.
     */
    public static class Doctor {
        @JsonProperty("edges")
        private List<Edge> edges;

        public List<Edge> getEdges() {
            return edges;
        }

        public void setEdges(final List<Edge> edges) {
            this.edges = edges;
        }
    }

    /**
     * Edge.
     */
    public static class Edge {
        @JsonProperty("node")
        private Node node;

        public Node getNode() {
            return node;
        }

        public void setNode(final Node node) {
            this.node = node;
        }
    }

    /**
     * Node.
     */
    public static class Node {
        @JsonProperty("id")
        private String id;

        @JsonProperty("cases")
        private Optional<Cases> cases = Optional.empty();

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public Optional<Cases> getCases() {
            return cases;
        }

        public void setCases(final Optional<Cases> cases) {
            this.cases = cases;
        }
    }

    /**
     * Cases.
     */
    public static class Cases {
        @JsonProperty("edges")
        private List<CaseEdge> edges;

        public List<CaseEdge> getEdges() {
            return edges;
        }

        public void setEdges(final List<CaseEdge> edges) {
            this.edges = edges;
        }
    }

    /**
     * CaseEdge.
     */
    public static class CaseEdge {
        @JsonProperty("node")
        private CaseNode node;

        public CaseNode getNode() {
            return node;
        }

        public void setNode(final CaseNode node) {
            this.node = node;
        }
    }

    /**
     * CaseNode.
     */
    public static class CaseNode {
        @JsonProperty("id")
        private String id;

        @JsonProperty("audio")
        private Optional<Audio> audio = Optional.empty();

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public Optional<Audio> getAudio() {
            return audio;
        }

        public void setAudio(final Optional<Audio> audio) {
            this.audio = audio;
        }
    }

    /**
     * Audio.
     */
    public static class Audio {
        @JsonProperty("edges")
        private List<AudioEdge> edges;

        public List<AudioEdge> getEdges() {
            return edges;
        }

        public void setEdges(final List<AudioEdge> edges) {
            this.edges = edges;
        }
    }

    /**
     * AudioEdge.
     */
    public static class AudioEdge {
        @JsonProperty("node")
        private AudioNode node;

        public AudioNode getNode() {
            return node;
        }

        public void setNode(final AudioNode node) {
            this.node = node;
        }
    }

    /**
     * AudioNode.
     */
    public static class AudioNode {
        @JsonProperty("id")
        private String id;
        @JsonProperty("fileId")
        private String fileId;

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(final String fileId) {
            this.fileId = fileId;
        }
    }

    /**
     * ResponseData.
     */
    public ResponseData() {
    }
}
