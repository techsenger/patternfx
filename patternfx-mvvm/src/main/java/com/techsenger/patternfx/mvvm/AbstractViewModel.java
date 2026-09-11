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
     * Returns the history of the view.
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
     * Applies default values for the view model's persistent state.
     * <p>
     * This method is invoked during initialization when no history is available or the available history is new.
     * Implementations should assign meaningful default values to all state that will be persisted in history.
     */
    protected void applyPersistentState() { }

    /**
     * Restores the view model's persistent state from history.
     * <p>
     * This method is invoked during initialization when a non-new history is available, in place of
     * {@link #applyPersistentState()}.
     */
    protected void restorePersistentState() { }

    /**
     * Saves the view model's persistent state into history.
     * <p>
     * This method is invoked during deinitialization when history is available. Restoring and persisting the
     * {@link ComponentHistory} instance itself is the responsibility of a history manager, not of this method.
     */
    protected void savePersistentState() { }

    /**
     * Creates this view model's descriptor. Defaults to the concrete class's simple name with a trailing
     * "ViewModel" stripped, if present &mdash; the descriptor identifies the whole component (view included,
     * e.g. in log messages the view itself emits), not just its view model half.
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
     * Resolves the view model's persistent state by either restoring it from history or applying default values when
     * necessary.
     * <p>
     * The view model state is divided into two categories:
     * <ul>
     *     <li><b>Persistent state</b> — participates in the history mechanism and can be restored or saved across
     *     component lifecycles.</li>
     *     <li><b>Transient state</b> — does not participate in the history mechanism and exists only at runtime.</li>
     * </ul>
     * <p>
     * This method operates exclusively on the <b>persistent state</b>. It does not initialize or modify transient data.
     * If history is unavailable or new, defaults are applied via {@link #applyPersistentState()}; otherwise, the state
     * is restored via {@link #restorePersistentState()}.
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
     * Saves the current persistent state of the view model into its history, provided that history is available.
     * <p>
     * The view model state is conceptually divided into two categories:
     * <ul>
     *     <li><b>Persistent state</b> — data that is stored in and restored from history
     *     (e.g., user input, UI state, configuration).</li>
     *     <li><b>Transient state</b> — runtime-only data that is not persisted and
     *     exists only for the duration of the component's lifecycle.</li>
     * </ul>
     * <p>
     * This method operates exclusively on the <b>persistent state</b>. Transient state is not affected and must be
     * managed independently.
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
