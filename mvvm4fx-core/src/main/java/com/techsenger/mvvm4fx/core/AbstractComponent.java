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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractComponent<T extends AbstractComponentView<?>> implements Component<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponent.class);

    private static String logDelimiter = " :";

    public static String getLogDelimiter() {
        return logDelimiter;
    }

    public static void setLogDelimiter(String logDelimiter) {
        AbstractComponent.logDelimiter = logDelimiter;
    }

    protected abstract class Mediator implements ComponentMediator {

        @Override
        public ComponentName getName() {
            return AbstractComponent.this.getName();
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
        public ComponentState getState() {
            return state.get();
        }

        @Override
        public ReadOnlyObjectProperty<ComponentState> stateProperty() {
            return state.getReadOnlyProperty();
        }

        @Override
        public ObjectProperty<HistoryPolicy> historyPolicyProperty() {
            return historyPolicy;
        }

        @Override
        public HistoryPolicy getHistoryPolicy() {
            return historyPolicy.get();
        }

        @Override
        public void setHistoryPolicy(HistoryPolicy policy) {
            historyPolicy.set(policy);
        }

        @Override
        public ComponentHistory<?> getHistory() {
            return history;
        }

        @Override
        public ComponentGroup getGroup() {
            return group.get();
        }

        @Override
        public void setGroup(ComponentGroup value) {
            group.set(value);
        }

        @Override
        public ObjectProperty<ComponentGroup> groupProperty() {
            return group;
        }

        @Override
        public void deinitialize() {
            AbstractComponent.this.deinitialize();
        }
    }

    private final T view;

    private final UUID uuid = UUID.randomUUID();

    private final String fullName;

    private final String logPrefix;

    private final ReadOnlyObjectWrapper<ComponentState> state = new ReadOnlyObjectWrapper<>(ComponentState.CREATING);

    private final ObjectProperty<HistoryPolicy> historyPolicy = new SimpleObjectProperty<>(HistoryPolicy.NONE);

    private final ObjectProperty<ComponentGroup> group = new SimpleObjectProperty<>();

    private HistoryProvider historyProvider;

    private ComponentHistory<?> history;

    public AbstractComponent(T view) {
        this.view = view;
        this.view.setComponent(this);
        long least32bits = uuid.getLeastSignificantBits() & 0xFFFFFFFFL;
        String shortUuid = String.format("%08X", least32bits);
        this.fullName = getName().getText() + "@" + shortUuid;
        this.logPrefix = resolveLogPrefix(fullName);
    }

    @Override
    public T getView() {
        return view;
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
    public ComponentState getState() {
        return this.state.get();
    }

    @Override
    public ReadOnlyObjectProperty<ComponentState> stateProperty() {
        return state.getReadOnlyProperty();
    }

    @Override
    public ObjectProperty<HistoryPolicy> historyPolicyProperty() {
        return historyPolicy;
    }

    @Override
    public HistoryPolicy getHistoryPolicy() {
        return historyPolicy.get();
    }

    @Override
    public void setHistoryPolicy(HistoryPolicy policy) {
        historyPolicy.set(policy);
    }

    @Override
    public ComponentHistory<?> getHistory() {
        return history;
    }

    @Override
    public ComponentGroup getGroup() {
       return group.get();
    }

    @Override
    public void setGroup(ComponentGroup value) {
       group.set(value);
    }

    @Override
    public ObjectProperty<ComponentGroup> groupProperty() {
       return group;
    }

    @Override
    public final void initialize() {
        var viewModel = this.view.getViewModel();
        try {
            if (getState() != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the component - " + getState().name());
            }
            // pre-initialization
            preInitialize();
            // initialization
            state.set(ComponentState.INITIALIZING);
            viewModel.initialize();
            this.view.initialize();
            state.set(ComponentState.INITIALIZED);
            logger.debug("{} Initialized component", logPrefix);
            // post-initialization
            postInitialize();
        } catch (Exception ex) {
            logger.error("{} Error initializing", logPrefix, ex);
        }
    }

    @Override
    public final void deinitialize() {
        var viewModel = this.view.getViewModel();
        try {
            if (getState() != ComponentState.INITIALIZED) {
                throw new IllegalStateException("Unexpected state of the component - " + getState().name());
            }
            // pre-deinitialization
            preDeinitialize();
            // deinitialization
            state.set(ComponentState.DEINITIALIZING);
            this.view.deinitialize();
            viewModel.deinitialize();
            state.set(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized component", logPrefix);
            // post-deinitialization
            postDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", logPrefix, ex);
        }
    }

    protected String resolveLogPrefix(String fullName) {
        return fullName + logDelimiter;
    }

    /**
     * The first method called in initialization.
     */
    protected void preInitialize() {
        var mediator = createMediator();
        this.view.getViewModel().setMediator(mediator);
        if (this.historyProvider != null) {
            this.history = this.historyProvider.provide();
        }
        this.view.getViewModel().restoreHistory();
    }

    /**
     * The last method called in initialization.
     */
    protected void postInitialize() { }

    /**
     * The first method called in deinitialization.
     */
    protected void preDeinitialize() { }

    /**
     * The last method called in deinitialization.
     */
    protected void postDeinitialize() {
        if (this.history != null) {
            this.view.getViewModel().saveHistory();
        }
    }

    protected void setHistoryProvider(HistoryProvider historyProvider) {
        this.historyProvider = historyProvider;
    }

    protected abstract AbstractComponent.Mediator createMediator();
}
