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
public abstract class AbstractPresenter<V extends View> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);

    private final Descriptor descriptor;

    private final V view;

    private HistoryPolicy historyPolicy = HistoryPolicy.NONE;

    private HistoryProvider<? extends AbstractHistory> historyProvider;

    private AbstractHistory history;

    public AbstractPresenter(V view) {
        this.descriptor = createDescriptor();
        this.view = view;
        if (this.view instanceof AbstractView<?>) {
            ((AbstractView<?>) this.view).setPresenter(this);
        }
    }

    /**
     * Initializes both the component.
     */
    public final void initialize() {
        try {
            if (descriptor.getState() != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the view - " + descriptor.getState().name());
            }
            // pre-initialization
            preInitialize();
            // initialization
            descriptor.setState(ComponentState.INITIALIZING);
            this.view.initialize();
            if (this.history != null) {
                restoreHistory();
            }
            descriptor.setState(ComponentState.INITIALIZED);
            logger.debug("{} Initialized view", getDescriptor().getLogPrefix());
            // post-initialization
            postInitialize();
        } catch (Exception ex) {
            logger.error("{} Error initializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    /**
     * Deinitializes both the component.
     */
    public final void deinitialize() {
        try {
            var descriptor = getDescriptor();
            if (descriptor.getState() != ComponentState.INITIALIZED) {
                throw new IllegalStateException("Unexpected state of the view - " + descriptor.getState().name());
            }
            // pre-deinitialization
            preDeinitialize();
            // deinitialization
            descriptor.setState(ComponentState.DEINITIALIZING);
            if (this.history != null) {
                saveHistory();
            }
            this.view.deinitialize();
            descriptor.setState(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized view", getDescriptor().getLogPrefix());
            // post-deinitialization
            postDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    /**
     * Returns the descriptor.
     *
     * @return
     */
    public Descriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Returns the history policy.
     *
     * @return
     */
    public HistoryPolicy getHistoryPolicy() {
        return historyPolicy;
    }

    /**
     * Sets the history policy to the specified value.
     *
     * @param policy the history policy to set.
     */
    public void setHistoryPolicy(HistoryPolicy policy) {
        this.historyPolicy = policy;
    }

    /**
     * Returns the view.
     *
     * @return
     */
    public V getView() {
        return view;
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

    protected void setHistoryProvider(HistoryProvider<? extends AbstractHistory> historyProvider) {
        this.historyProvider = historyProvider;
    }

    /**
     * Returns the history of the View.
     * @return
     */
    protected AbstractHistory getHistory() {
        return history;
    }

    protected final void restoreHistory() {
        var policy = getHistoryPolicy();
        logger.debug("{} History policy during restore: {}", getDescriptor().getLogPrefix(), policy);
        if (policy != NONE) {
            if (history.isFresh()) {
                logger.debug("{} History is fresh. Skipping restoration", getDescriptor().getLogPrefix());
            } else {
                switch (policy) {
                    case DATA:
                        restoreData();
                        break;
                    case APPEARANCE:
                        restoreAppearance();
                        break;
                    case ALL:
                        restoreData();
                        restoreAppearance();
                    break;
                    default:
                        throw new AssertionError();
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
            case DATA:
                saveData();
                break;
            case APPEARANCE:
                saveAppearance();
                break;
            case ALL:
                saveData();
                saveAppearance();
                break;
            case NONE:
                break;
            default:
                throw new AssertionError();
        }
    }

    /**
     * Method copies all data from view to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#DATA}.
     *
     */
    protected void saveData() {
        getHistory().setFresh(false);
    }

    /**
     * Method copies all data from view to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#APPEARANCE}.
     *
     */
    protected void saveAppearance() {
        getHistory().setFresh(false);
    }

    protected abstract Descriptor createDescriptor();

    void prepareHistory() {
        if (this.historyProvider != null) {
            this.history = this.historyProvider.provide();
            this.historyProvider = null;
        }
    }
}
