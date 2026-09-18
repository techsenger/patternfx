/*
 * Copyright 2024-2026 Pavel Castornii.
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

package com.techsenger.patternfx.mvvm;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.core.DefaultComponentName;
import com.techsenger.patternfx.core.HistoryProvider;
import com.techsenger.toolkit.fx.value.ObservableSource;
import com.techsenger.toolkit.fx.value.SimpleObservableSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractViewModel implements ViewModel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractViewModel.class);

    private final Descriptor descriptor;

    private final ObservableSource<Void> requestDeinitialize = new SimpleObservableSource<>();

    private @Nullable HistoryProvider<? extends ComponentHistory> historyProvider;

    private @Nullable ComponentHistory history;

    @SuppressWarnings("unchecked")
    public AbstractViewModel(ComponentParams params) {
        params.validate();
        this.descriptor = createDescriptor();
        this.historyProvider = (HistoryProvider<? extends ComponentHistory>) params.getHistoryProvider();
    }

    @Override
    public Descriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public void requestDeinitialize() {
        requestDeinitialize.next(null);
    }

    /**
     * Returns the history of this component, or {@code null} before it has been prepared.
     */
    protected @Nullable ComponentHistory getHistory() {
        return history;
    }

    /**
     * The first method called in initialization.
     */
    protected void preInitialize() {
        prepareHistory();
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
    protected void postDeinitialize() { }

    /**
     * Applies default values for this component's persistent state; invoked when no history is available or it
     * is new.
     */
    protected void applyPersistentState() { }

    /**
     * Restores this component's persistent state from history; invoked instead of {@link #applyPersistentState()}
     * when non-new history is available.
     */
    protected void restorePersistentState() { }

    /**
     * Saves this component's persistent state into history; invoked during deinitialization whenever history is
     * available.
     */
    protected void savePersistentState() { }

    /**
     * Creates this component's descriptor, defaulting to the class's simple name with any trailing "ViewModel"
     * suffix removed.
     */
    protected Descriptor createDescriptor() {
        var simpleName = getClass().getSimpleName();
        var name = simpleName.endsWith("ViewModel")
                ? simpleName.substring(0, simpleName.length() - "ViewModel".length()) : simpleName;
        return new Descriptor(new DefaultComponentName(name));
    }

    final void beginInitialize() {
        if (descriptor.getState() != ComponentState.CREATING) {
            throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
        }
        preInitialize();
        descriptor.setState(ComponentState.INITIALIZING);
    }

    final void endInitialize() {
        applyOrRestorePersistentState();
        descriptor.setState(ComponentState.INITIALIZED);
        logger.debug("{} Initialized the component", getDescriptor().getLogPrefix());
        postInitialize();
    }

    final void beginDeinitialize() {
        if (descriptor.getState() != ComponentState.INITIALIZED) {
            throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
        }
        preDeinitialize();
        descriptor.setState(ComponentState.DEINITIALIZING);
        savePersistentStateToHistory();
    }

    final void endDeinitialize() {
        descriptor.setState(ComponentState.DEINITIALIZED);
        logger.debug("{} Deinitialized the component", getDescriptor().getLogPrefix());
        postDeinitialize();
    }

    ObservableSource<Void> getRequestDeinitialize() {
        return requestDeinitialize;
    }

    private void prepareHistory() {
        if (this.historyProvider != null) {
            this.history = this.historyProvider.provide();
            this.historyProvider = null;
        }
    }

    /**
     * Applies default persistent state, or restores it from history, depending on whether history is missing or
     * new.
     */
    private void applyOrRestorePersistentState() {
        if (history == null || history.isNew()) {
            applyPersistentState();
            logger.debug("{} Persistent state set to defaults. Reason: {}", getDescriptor().getLogPrefix(),
                    history == null ? "history is null" : "history is new");
        } else {
            restorePersistentState();
            logger.debug("{} Persistent state restored from history", getDescriptor().getLogPrefix());
        }
    }

    /**
     * Saves the current persistent state to history and marks it no longer new, when history is available.
     */
    private void savePersistentStateToHistory() {
        if (this.history == null) {
            return;
        }
        savePersistentState();
        this.history.setNew(false);
        logger.debug("{} Persistent state saved to history", getDescriptor().getLogPrefix());
    }
}
