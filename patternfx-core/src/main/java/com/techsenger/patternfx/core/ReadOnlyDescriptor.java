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

import java.util.UUID;

/**
 * Represents the internal metadata and platform-level state of a component. The descriptor serves as a technical
 * identity card, containing all platform-related information while completely separating it from business data.
 *
 * <p>The descriptor is a pure data container - it exposes component metadata through simple accessor methods but
 * contains no business logic or behavior. All behavioral aspects (history management, component interactions)
 * are handled separately by the component's ViewModel and other behavioral contracts.
 *
 * @author Pavel Castornii
 */
public interface ReadOnlyDescriptor {

    /**
     * Returns the logical name of the component. Multiple component instances can share the same {@link ComponentName}.
     *
     * @return the component name
     */
    Name getName();

    /**
     * Returns the unique {@link UUID} of this component instance.
     *
     * @return the component instance UUID
     */
    UUID getUuid();

    /**
     * Returns a human-readable identifier for this component instance. The identifier combines the component
     * name with the last 32 bits of its UUID, represented as 8 hexadecimal characters.
     *
     * <p>Example: {@code TextEditorTab#A0A0A0A0}</p>
     *
     * @return a full name identifying this component instance
     */
    String getFullName();

    /**
     * Returns a formatted prefix string with full name followed by a separator to be used in log messages. Example:
     * {@code [DockLayout#A0A0A0A0]}.
     *
     * @return the prefix with full name followed by a separator for log messages
     */
    String getLogPrefix();

    /**
     * Returns the state of the component.
     * @return
     */
    State getState();

    /**
     * Returns the current {@link ComponentGroup}.
     *
     * @return the current component group
     */
    Group getGroup();
}
