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

package com.techsenger.mvvm4fx.core;

import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

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
public class ComponentDescriptor {

    private static String logDelimiter = " :";

    public static String getLogDelimiter() {
        return logDelimiter;
    }

    public static void setLogDelimiter(String logDelimiter) {
        ComponentDescriptor.logDelimiter = logDelimiter;
    }

    private final ComponentName name;

    private final UUID uuid;

    private final String fullName;

    private final String logPrefix;

    private final ReadOnlyObjectWrapper<ComponentState> state =
            new ReadOnlyObjectWrapper<>(ComponentState.CREATING);

    private final ObjectProperty<HistoryPolicy> historyPolicy = new SimpleObjectProperty<>(HistoryPolicy.NONE);

    private final ObjectProperty<ComponentGroup> group = new SimpleObjectProperty<>();

    public ComponentDescriptor(ComponentName name) {
        this(name, UUID.randomUUID());
    }

    public ComponentDescriptor(ComponentName name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        long least32bits = uuid.getLeastSignificantBits() & 0xFFFFFFFFL;
        String shortUuid = String.format("%08X", least32bits);
        this.fullName = name.getText() + "@" + shortUuid;
        this.logPrefix = resolveLogPrefix(fullName);
    }

    /**
     * Returns the logical name of the component. Multiple component instances can share the same {@link ComponentName}.
     *
     * @return the component name
     */
    public ComponentName getName() {
        return name;
    }

    /**
     * Returns the unique {@link UUID} of this component instance.
     *
     * @return the component instance UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns a human-readable identifier that combines the component type and its UUID. For example:
     * {@code TextEditorTab#A0A0A0A0}.
     *
     * @return the full name of this component instance
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns a formatted prefix string with full name followed by a separator to be used in log messages. Example:
     * {@code DockLayout#A0A0A0A0 - }.
     *
     * @return the prefix with full name followed by a separator for log messages
     */
    public String getLogPrefix() {
        return logPrefix;
    }

    /**
     * Returns the state of the component.
     * @return
     */
    public ComponentState getState() {
        return this.state.get();
    }

    /**
     * Returns the state property of the component.
     *
     * @return
     */
    public ReadOnlyObjectProperty<ComponentState> stateProperty() {
        return state.getReadOnlyProperty();
    }

    /**
     * Returns the property for the history policy.
     *
     * @return
     */
    public ObjectProperty<HistoryPolicy> historyPolicyProperty() {
        return historyPolicy;
    }

    /**
     * Returns the history policy.
     *
     * @return
     */
    public HistoryPolicy getHistoryPolicy() {
        return historyPolicy.get();
    }

    /**
     * Sets the history policy to the specified value.
     *
     * @param policy the history policy to set.
     */
    public void setHistoryPolicy(HistoryPolicy policy) {
        historyPolicy.set(policy);
    }

    /**
     * Returns the current {@link ComponentGroup}.
     *
     * @return the current component group
     */
    public ComponentGroup getGroup() {
       return group.get();
    }

    /**
     * Sets a new {@link ComponentGroup}.
     *
     * @param value the new component group to set
     */
    public void setGroup(ComponentGroup value) {
       group.set(value);
    }

    /**
     * Returns the {@link ObjectProperty} for the component group.
     *
     * @return the property representing the component group
     */
    public ObjectProperty<ComponentGroup> groupProperty() {
       return group;
    }

    protected String resolveLogPrefix(String fullName) {
        return fullName + logDelimiter;
    }

    ReadOnlyObjectWrapper<ComponentState> stateWrapper() {
        return state;
    }
}
