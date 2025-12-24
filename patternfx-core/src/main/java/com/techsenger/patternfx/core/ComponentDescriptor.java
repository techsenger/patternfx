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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public interface ComponentDescriptor {

    /**
     * Returns the logical name of the component. Multiple component instances can share the same {@link ComponentName}.
     *
     * @return the component name
     */
    ComponentName getName();

    /**
     * Returns the unique {@link UUID} of this component instance.
     *
     * @return the component instance UUID
     */
    UUID getUuid();

    /**
     * Returns a human-readable identifier that combines the component name and its UUID. For example:
     * {@code TextEditorTab#A0A0A0A0}.
     *
     * @return the full name of this component instance
     */
    String getFullName();

    /**
     * Returns a formatted prefix string with full name followed by a separator to be used in log messages. Example:
     * {@code DockLayout#A0A0A0A0 - }.
     *
     * @return the prefix with full name followed by a separator for log messages
     */
    String getLogPrefix();

    /**
     * Returns the state of the component.
     * @return
     */
    ComponentState getState();

    /**
     * Returns the state property of the component.
     *
     * @return
     */
    ReadOnlyObjectProperty<ComponentState> stateProperty();

    /**
     * Returns the current {@link ComponentGroup}.
     *
     * @return the current component group
     */
    ComponentGroup getGroup();

    /**
     * Sets a new {@link ComponentGroup}.
     *
     * @param value the new component group to set
     */
    void setGroup(ComponentGroup value);

    /**
     * Returns the {@link ObjectProperty} for the component group.
     *
     * @return the property representing the component group
     */
    ObjectProperty<ComponentGroup> groupProperty();
}
