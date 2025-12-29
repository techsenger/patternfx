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

/**
 * Represents a logical name of a component that unites both its {@link ComponentView} and {@link ComponentViewModel}
 * counterparts.
 * <p>A {@code ComponentName} defines the type identity of a component within the platform. It is typically declared
 * in the public API so that other components can reference it, even when the actual implementation classes are
 * located in internal or deeply nested packages.
 *
 * <p>Each component has exactly one {@code ComponentName}, which serves as a symbolic identifier shared between its
 * {@link ComponentView} and {@link ComponentViewModel}. Component names are usually defined as constants within
 * interfaces representing the module's public API.
 *
 * @author Pavel Castornii
 */
public class Name {

    private final String text;

    public Name(String text) {
        this.text = text;
    }

    /**
     * Returns the human-readable name of the component.
     *
     * @return the component name text
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
