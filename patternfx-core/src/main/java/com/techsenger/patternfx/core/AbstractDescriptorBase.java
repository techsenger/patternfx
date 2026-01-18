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

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractDescriptorBase implements DescriptorBase {

    private static Function<DescriptorBase, String> logPrefixResolver = (d) -> "[" + d.getFullName() + "]";

    public static Function<DescriptorBase, String> getLogPrefixResolver() {
        return logPrefixResolver;
    }

    public static void setLogPrefixResolver(Function<DescriptorBase, String> logPrefixResolver) {
        Objects.requireNonNull(logPrefixResolver, "logPrefixResolver can't be null");
        AbstractDescriptorBase.logPrefixResolver = logPrefixResolver;
    }

    private final Name name;

    private final UUID uuid;

    private final String fullName;

    private final String logPrefix;

    private final ReadOnlyObjectWrapper<State> state =
            new ReadOnlyObjectWrapper<>(State.CREATING);

    private final ObjectProperty<HistoryPolicy> historyPolicy = new SimpleObjectProperty<>(HistoryPolicy.NONE);

    private final ObjectProperty<Group> group = new SimpleObjectProperty<>();

    public AbstractDescriptorBase(Name name) {
        this(name, UUID.randomUUID());
    }

    public AbstractDescriptorBase(Name name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        long least32bits = uuid.getLeastSignificantBits() & 0xFFFFFFFFL;
        String shortUuid = String.format("%08X", least32bits);
        this.fullName = name.getText() + "@" + shortUuid;
        this.logPrefix = resolveLogPrefix(fullName);
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getLogPrefix() {
        return logPrefix;
    }

    @Override
    public State getState() {
        return this.state.get();
    }

    @Override
    public Group getGroup() {
       return group.get();
    }

    @Override
    public void setGroup(Group value) {
       group.set(value);
    }

    protected String resolveLogPrefix(String fullName) {
        return logPrefixResolver.apply(this);
    }

    protected void setState(State state) {
        this.state.set(state);
    }

    ReadOnlyObjectProperty<State> stateProperty() {
        return state.getReadOnlyProperty();
    }

    ObjectProperty<Group> groupProperty() {
       return group;
    }
}
