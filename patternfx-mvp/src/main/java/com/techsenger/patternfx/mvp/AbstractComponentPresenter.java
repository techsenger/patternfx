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

package com.techsenger.patternfx.mvp;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.core.HistoryPolicy;
import static com.techsenger.patternfx.core.HistoryPolicy.ALL;
import static com.techsenger.patternfx.core.HistoryPolicy.APPEARANCE;
import static com.techsenger.patternfx.core.HistoryPolicy.DATA;
import static com.techsenger.patternfx.core.HistoryPolicy.NONE;
import com.techsenger.patternfx.core.HistoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractComponentPresenter<V extends View> extends AbstractPresenter<V>
        implements ComponentPresenter<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentPresenter.class);

    private final Descriptor descriptor;

    private HistoryPolicy historyPolicy = HistoryPolicy.NONE;

    private @Nullable HistoryProvider<? extends AbstractComponentHistory> historyProvider;

    private @Nullable AbstractComponentHistory history;

    public AbstractComponentPresenter(V view) {
        super(view);
        this.descriptor = createDescriptor();
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
                ((AbstractComponentView<?>) getView()).initialize();
            }
            applyOrRestoreHistory();
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
            saveHistory();
            if (getView() instanceof AbstractView<?>) {
                ((AbstractComponentView<?>) getView()).deinitialize();
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
    public Descriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public HistoryPolicy getHistoryPolicy() {
        return historyPolicy;
    }

    @Override
    public void setHistoryPolicy(HistoryPolicy policy) {
        this.historyPolicy = policy;
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

    protected void setHistoryProvider(@Nullable HistoryProvider<? extends AbstractComponentHistory> historyProvider) {
        this.historyProvider = historyProvider;
    }

    /**
     * Returns the history of the ComponentView.
     */
    protected @Nullable AbstractComponentHistory getHistory() {
        return history;
    }

    /**
     * Applies default values for the component's persistent data.
     * <p>
     * This method is invoked when no previously persisted data is available or when the current {@link HistoryPolicy}
     * does not include {@code DATA}. Implementations should assign meaningful default values to all data that
     * participates in the history mechanism.
     */
    protected void applyData() { }

    /**
     * Applies default values for the component's persistent appearance state.
     * <p>
     * This method is invoked when no previously persisted appearance state is available or when the current
     * {@link HistoryPolicy} does not include {@code APPEARANCE}. Implementations should initialize all
     * appearance-related state that is managed through the history mechanism.
     */
    protected void applyAppearance() { }

    /**
     * Method copies all data from history to view. This method is called at the beginning of initialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#DATA}.
     *
     */
    protected void restoreData() { }

    /**
     * Method copies all appearance information from history to view. This method is called at the beginning
     * of initialization when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#APPEARANCE}.
     *
     */
    protected void restoreAppearance() { }

    /**
     * Method copies all data from view to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#DATA}.
     *
     */
    protected void saveData() { }

    /**
     * Method copies all data from view to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#APPEARANCE}.
     *
     */
    protected void saveAppearance() { }

    protected abstract Descriptor createDescriptor();

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
     * <p>
     * Behavior depends on the {@link HistoryPolicy} and the state of the history:
     * <ul>
     *     <li>If history is new or unavailable, default values are applied via {@link #applyData()} and
     *     {@link #applyAppearance()}.</li>
     *     <li>If history exists, the state is selectively restored via {@link #restoreData()} and/or
     *     {@link #restoreAppearance()}, while missing parts are filled with defaults.</li>
     * </ul>
     */
    private void applyOrRestoreHistory() {
        logger.debug("{} History policy during initialization: {}", getDescriptor().getLogPrefix(), historyPolicy);
        if (historyPolicy == NONE || history == null || history.isNew()) {
            applyData();
            applyAppearance();
            logger.debug("{} Data and appearance set to defaults. Reason: {}", getDescriptor().getLogPrefix(),
                    historyPolicy == NONE ? "policy is NONE" : history == null ? "history is null" : "history is new");
        } else {
            switch (historyPolicy) {
                case DATA -> {
                    restoreData();
                    applyAppearance();
                    logger.debug("{} Data restored from history, appearance set to defaults",
                            getDescriptor().getLogPrefix());
                }
                case APPEARANCE -> {
                    applyData();
                    restoreAppearance();
                    logger.debug("{} Data set to defaults, appearance restored from history",
                            getDescriptor().getLogPrefix());
                }
                case ALL -> {
                    restoreData();
                    restoreAppearance();
                    logger.debug("{} Data and appearance restored from history", getDescriptor().getLogPrefix());
                }
                default -> throw new AssertionError();
            }
        }
    }

    /**
     * Saves the current persistent state of the component into its history.
     * <p>
     * The component state is conceptually divided into two categories:
     * <ul>
     *     <li><b>Persistent state</b> — data that is stored in and restored from history
     *     (e.g., user input, UI state, configuration).</li>
     *     <li><b>Transient state</b> — runtime-only data that is not persisted and
     *     exists only for the duration of the component's lifecycle.</li>
     * </ul>
     * <p>
     * This method operates exclusively on the <b>persistent state</b>.  Transient state is not affected and must be
     * managed independently.
     * <p>
     * Depending on the {@link HistoryPolicy}, this method delegates to {@link #saveData()} and/or
     * {@link #saveAppearance()}.
     */
    private void saveHistory() {
        if (this.history == null) {
            return;
        }
        logger.debug("{} History policy during deinitialization: {}", getDescriptor().getLogPrefix(), historyPolicy);
        switch (historyPolicy) {
            case DATA -> {
                saveData();
                this.history.setNew(false);
            }
            case APPEARANCE -> {
                saveAppearance();
                this.history.setNew(false);
            }
            case ALL -> {
                saveData();
                saveAppearance();
                this.history.setNew(false);
            }
            case NONE -> { }
            default -> throw new AssertionError();
        }
    }
}
