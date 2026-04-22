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
            if (this.history != null) {
                restoreHistory();
            }
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
            if (this.history != null) {
                saveHistory();
            }
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

    protected final void restoreHistory() {
        var policy = getHistoryPolicy();
        logger.debug("{} History policy during restore: {}", getDescriptor().getLogPrefix(), policy);
        if (policy != NONE && history != null) {
            if (history.isNew()) {
                logger.debug("{} History is new. Skipping restoration", getDescriptor().getLogPrefix());
            } else {
                switch (policy) {
                    case DATA -> restoreData();
                    case APPEARANCE -> restoreAppearance();
                    case ALL -> {
                        restoreData();
                        restoreAppearance();
                    }
                    default -> throw new AssertionError();
                }
            }
        }
    }

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

    protected final void saveHistory() {
        var policy = getHistoryPolicy();
        logger.debug("{} History policy during save: {}", getDescriptor().getLogPrefix(), policy);
        switch (policy) {
            case DATA -> saveData();
            case APPEARANCE -> saveAppearance();
            case ALL -> {
                saveData();
                saveAppearance();
            }
            case NONE -> { }
            default -> throw new AssertionError();
        }
    }

    /**
     * Method copies all data from view to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#DATA}.
     *
     */
    protected void saveData() {
        if (this.history != null) {
            this.history.setNew(false);
        }
    }

    /**
     * Method copies all data from view to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#APPEARANCE}.
     *
     */
    protected void saveAppearance() {
        if (this.history != null) {
            this.history.setNew(false);
        }
    }

    protected abstract Descriptor createDescriptor();

    void prepareHistory() {
        if (this.historyProvider != null) {
            this.history = this.historyProvider.provide();
            this.historyProvider = null;
        }
    }
}
