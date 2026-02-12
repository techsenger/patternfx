/*
 * Copyright 2024-2025 Pavel Castornii.
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

package com.techsenger.patternfx.core;

import java.io.Serializable;

/**
 * Represents a serializable snapshot of a component's state. A history object stores only the information that
 * must persist between component sessions and excludes all transient or runtime aspects.
 *
 * @author Pavel Castornii
 */
public interface ComponentHistory extends Serializable {

    /**
     * Returns whether this history instance is new, meaning it was newly created and has not yet been used to
     * save or restore any component state. A new history contains no previously stored data and should not be
     * passed to a component for restoration.
     *
     * <p>Once the component's state has been saved into this history, the flag becomes {@code false}, indicating that
     * the history now holds valid data that can be used to restore the component's state in future instances.
     *
     * @return {@code true} if this history is new and has not yet been used, {@code false} otherwise
     */
    boolean isNew();

    /**
     * Method called before the component is serialized. This can be used to prepare the object's state
     * before saving it in a binary format.
     */
    void preSerialize();

    /**
     * Method called after the component is deserialized. This can be used to restore the object's state
     * after loading it from a binary format.
     */
    void postDeserialize();
}
