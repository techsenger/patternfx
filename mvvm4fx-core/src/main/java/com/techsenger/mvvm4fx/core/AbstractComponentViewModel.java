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

import static com.techsenger.mvvm4fx.core.HistoryPolicy.ALL;
import static com.techsenger.mvvm4fx.core.HistoryPolicy.APPEARANCE;
import static com.techsenger.mvvm4fx.core.HistoryPolicy.DATA;
import static com.techsenger.mvvm4fx.core.HistoryPolicy.NONE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractComponentViewModel implements ComponentViewModel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentViewModel.class);

    private final ComponentDescriptor descriptor;

    private HistoryProvider historyProvider;

    private ComponentHistory<?> history;

    private ComponentViewModel.Composer composer;

    public AbstractComponentViewModel() {
        this.descriptor = createDescriptor();
        this.descriptor.stateProperty().addListener((ov, oldV, newV) -> {
            var policy = this.descriptor.getHistoryPolicy();
            if (this.descriptor.getState() == ComponentState.INITIALIZING) {
                logger.debug("{} History policy on initializing: {}", this.descriptor.getLogPrefix(), policy);
                ComponentHistory localHistory = null;
                if (policy != NONE) {
                    localHistory = getOrRequestHistory();
                    if (localHistory.isFresh()) {
                        logger.debug("{} History is fresh. Skipping restoration", this.descriptor.getLogPrefix());
                    } else {
                        switch (policy) {
                            case DATA:
                                localHistory.restoreData(this);
                                postHistoryRestore();
                                break;
                            case APPEARANCE:
                                localHistory.restoreAppearance(this);
                                postHistoryRestore();
                                break;
                            case ALL:
                                localHistory.restoreData(this);
                                localHistory.restoreAppearance(this);
                                postHistoryRestore();
                            break;
                            default:
                                throw new AssertionError();
                        }
                    }
                }
            } else if (this.descriptor.getState() == ComponentState.DEINITIALIZED) {
                logger.debug("{} History policy on deinitializing: {}", this.descriptor.getLogPrefix(), policy);
                //The data and the appearance are saved to the history during the deinitialization of the component,
                //not while the component is running, as this feature is rarely needed but significantly complicates
                //the code.
                switch (policy) {
                    case DATA:
                        preHistorySave();
                        getOrRequestHistory().saveData(this);
                        break;
                    case APPEARANCE:
                        preHistorySave();
                        getOrRequestHistory().saveAppearance(this);
                        break;
                    case ALL:
                        preHistorySave();
                        var h = getOrRequestHistory();
                        h.saveData(this);
                        h.saveAppearance(this);
                        break;
                    case NONE:
                        break;
                    default:
                        throw new AssertionError();
                }
            }
        });
    }

    @Override
    public ComponentDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public HistoryProvider getHistoryProvider() {
        return historyProvider;
    }

    public void setHistoryProvider(HistoryProvider historyProvider) {
        this.historyProvider = historyProvider;
    }

    public Composer getComposer() {
        return composer;
    }

    public void setComposer(Composer composer) {
        this.composer = composer;
    }

    protected void postHistoryRestore() {

    }

    protected void preHistorySave() {

    }

    protected abstract ComponentDescriptor createDescriptor();

    private ComponentHistory getOrRequestHistory() {
        if (this.history == null) {
            if (this.historyProvider == null) {
                throw new NullPointerException("No history provider");
            }
            this.history = this.historyProvider.provide();
        }
        return this.history;
    }
}
