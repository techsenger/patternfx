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

package com.techsenger.patternfx.mvvm;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.HistoryPolicy;
import static com.techsenger.patternfx.core.HistoryPolicy.ALL;
import static com.techsenger.patternfx.core.HistoryPolicy.APPEARANCE;
import static com.techsenger.patternfx.core.HistoryPolicy.DATA;
import static com.techsenger.patternfx.core.HistoryPolicy.NONE;
import com.techsenger.patternfx.core.HistoryProvider;
import com.techsenger.toolkit.fx.value.ObservableSource;
import com.techsenger.toolkit.fx.value.SimpleObservableSource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private final ObjectProperty<HistoryPolicy> historyPolicy = new SimpleObjectProperty<>(HistoryPolicy.NONE);

    private @Nullable HistoryProvider<? extends AbstractHistory> historyProvider;

    private @Nullable AbstractHistory history;

    public AbstractViewModel() {
        this.descriptor = createDescriptor();
    }

    @Override
    public Descriptor getDescriptor() {
        return descriptor;
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
    public void requestDeinitialize() {
        requestDeinitialize.next(null);
    }

    protected void setHistoryProvider(@Nullable HistoryProvider<? extends AbstractHistory> historyProvider) {
        this.historyProvider = historyProvider;
    }

    /**
     * Returns the history of the view.
     */
    protected @Nullable AbstractHistory getHistory() {
        return history;
    }

    /**
     * Initializes the view model.
     */
    protected void initialize() {
        if (this.history != null) {
            restoreHistory();
        }
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
     * Method copies all data from history to view model. This method is called at the beginning of initialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#DATA}.
     */
    protected void restoreData() { }

    /**
     * Method copies all appearance information from history to view model. This method is called at the beginning
     * of initialization when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#APPEARANCE}.
     */
    protected void restoreAppearance() { }

    /**
     * Deinitializes the view model.
     */
    protected void deinitialize() {
        if (this.history != null) {
            saveHistory();
        }
    }

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
     * Method copies all data from view model to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#DATA}.
     */
    protected void saveData() {
        if (this.history != null) {
            this.history.setNew(false);
        }
    }

    /**
     * Method copies all data from view model to history. This method is called at the beginning of deinitialization
     * when the policy is {@link HistoryPolicy#ALL} or {@link HistoryPolicy#APPEARANCE}.
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

    ObservableSource<Void> getRequestDeinitialize() {
        return requestDeinitialize;
    }
}
