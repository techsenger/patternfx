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

import com.techsenger.annotations.Nullable;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractName implements Name {

    private final @Nullable String text;

    public AbstractName(@Nullable String text) {
        this.text = text;
    }

    /**
     * Returns the human-readable name.
     *
     * @return the component name text
     */
    @Override
    public @Nullable String getText() {
        return text;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[text=" + String.valueOf(text) + "]";
    }
}
