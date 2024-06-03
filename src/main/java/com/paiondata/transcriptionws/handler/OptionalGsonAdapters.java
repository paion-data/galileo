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
package com.paiondata.transcriptionws.handler;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * This class provides custom Gson adapters for handling serialization and deserialization
 * of Java's {@link Optional} type. Gson, by default, does not provide native support for
 * serializing and deserializing {@link Optional} objects. These adapters bridge that gap,
 * enabling seamless integration of Optional types within JSON payloads processed by Gson.
 *
 * <p>{@link OptionalSerializer} serializes an Optional into a JSON element. If the Optional
 * contains a value, it serializes that value using the provided Gson context. If the Optional
 * is empty, it outputs a JSON null value.</p>
 *
 * <p>{@link OptionalDeserializer} deserializes a JSON element back into an Optional. It checks
 * if the JSON element is null; if so, it returns an empty Optional. If the element contains a value,
 * it deserializes that value into the type specified by the Optional's generic parameter using
 * the Gson deserialization context.</p>
 *
 * <p>To use these adapters with Gson, they must be registered with a {@link GsonBuilder} instance
 * before building the Gson object.</p>
 *
 * Example usage:
 * <pre>
 * Gson gson = new GsonBuilder()
 *         .registerTypeAdapter(Optional.class, new OptionalSerializer())
 *         .registerTypeAdapter(Optional.class, new OptionalDeserializer())
 *         .create();
 * </pre>
 */
public class OptionalGsonAdapters {

    /**
     * Custom serializer for Java's Optional type.
     * <p>
     * Converts an Optional value to a JSON representation. If the Optional is present, serializes its value;
     * otherwise, outputs a JSON null.

     * @param <T> The generic type of the value contained within the Optional.
     */
    public static class OptionalSerializer<T> implements JsonSerializer<Optional<T>> {
        @Override
        public JsonElement serialize(final Optional<T> src,
                                     final Type typeOfSrc,
                                     final JsonSerializationContext context) {
            return src.map(context::serialize).orElse(JsonNull.INSTANCE);
        }
    }

    /**
     * Custom deserializer for Java's Optional type.
     * <p>
     * Parses a JSON element into an Optional value. If the JSON represents a null value, returns an empty Optional;
     * otherwise, deserializes the JSON into the generic type contained within the Optional.

     * @param <T> The generic type of the value to be contained within the Optional.
     */
    public static class OptionalDeserializer<T> implements JsonDeserializer<Optional<T>> {
        @Override
        public Optional<T> deserialize(final JsonElement json,
                                       final Type typeOfT,
                                       final JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonNull()) {
                return Optional.empty();
            } else {
                return Optional.of(context.deserialize(json,
                        ((ParameterizedType) typeOfT).getActualTypeArguments()[0]));
            }
        }
    }
}
