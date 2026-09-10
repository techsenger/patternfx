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

package com.techsenger.patternfx.mvp;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.core.DefaultComponentName;
import com.techsenger.patternfx.core.HistoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractPresenter<V extends View> implements Presenter<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);

    private final V view;

    private final ComponentDescriptor descriptor;

    private @Nullable HistoryProvider<? extends ComponentHistory> historyProvider;

    private @Nullable ComponentHistory history;

    @SuppressWarnings("unchecked")
    public AbstractPresenter(V view, ComponentParams params) {
        params.validate();
        this.view = view;
        this.descriptor = createDescriptor();
        this.historyProvider = (HistoryProvider<? extends ComponentHistory>) params.getHistoryProvider();
        if (this.view instanceof AbstractView<?>) {
            ((AbstractView<?>) this.view).setPresenter(this);
        }
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public final void initialize() {
        try {
            if (descriptor.getState() != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
            }
            // pre-initialization
            preInitialize();
            // initialization
            descriptor.setState(ComponentState.INITIALIZING);
            if (getView() instanceof AbstractView<?>) {
                ((AbstractView<?>) getView()).initialize();
            }
            applyOrRestorePersistentState();
            descriptor.setState(ComponentState.INITIALIZED);
            logger.debug("{} Initialized the component", getDescriptor().getLogPrefix());
            // post-initialization
            postInitialize();
        } catch (Exception ex) {
            logger.error("{} Error initializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    @Override
    public final void deinitialize() {
        try {
            var descriptor = getDescriptor();
            if (descriptor.getState() != ComponentState.INITIALIZED) {
                throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
            }
            // pre-deinitialization
            preDeinitialize();
            // deinitialization
            descriptor.setState(ComponentState.DEINITIALIZING);
            savePersistentStateToHistory();
            if (getView() instanceof AbstractView<?>) {
                ((AbstractView<?>) getView()).deinitialize();
            }
            descriptor.setState(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized the component", getDescriptor().getLogPrefix());
            // post-deinitialization
            postDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    @Override
    public ComponentDescriptor getDescriptor() {
        return descriptor;
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
     * Returns the history of the ComponentView.
     */
    protected @Nullable ComponentHistory getHistory() {
        return history;
    }

    /**
     * Applies default values for the component's persistent state.
     * <p>
     * This method is invoked during initialization when no history is available or the available history is new.
     * Implementations should assign meaningful default values to all state that will be persisted in history.
     */
    protected void applyPersistentState() { }

    /**
     * Restores the component's persistent state from history.
     * <p>
     * This method is invoked during initialization when a non-new history is available, in place of
     * {@link #applyPersistentState()}.
     */
    protected void restorePersistentState() { }

    /**
     * Saves the component's persistent state into history.
     * <p>
     * This method is invoked during deinitialization when history is available. Restoring and persisting the
     * {@link ComponentHistory} instance itself is the responsibility of a history manager, not of this method.
     */
    protected void savePersistentState() { }

    /**
     * Creates this component's descriptor. Defaults to the concrete class's simple name with a trailing
     * "Presenter" stripped, if present &mdash; the descriptor identifies the whole component (view included,
     * e.g. in log messages the view itself emits), not just its presenter half.
     */
    protected ComponentDescriptor createDescriptor() {
        var simpleName = getClass().getSimpleName();
        var name = simpleName.endsWith("Presenter")
                ? simpleName.substring(0, simpleName.length() - "Presenter".length()) : simpleName;
        return new ComponentDescriptor(new DefaultComponentName(name));
    }

    private void prepareHistory() {
        if (this.historyProvider != null) {
            this.history = this.historyProvider.provide();
            this.historyProvider = null;
        }
    }

    /**
     * Resolves the component's persistent state by either restoring it from history or applying default values when
     * necessary.
     * <p>
     * The component state is divided into two categories:
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
     * Saves the current persistent state of the component into its history, provided that history is available.
     * <p>
     * The component state is conceptually divided into two categories:
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
