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
 * Optional Gson Adapters.
 */
public class OptionalGsonAdapters {

    /**
     * Optional Serializer.
     * @param <T> type of the optional.
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
     * Optional Deserializer.
     * @param <T> type of the optional.
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
