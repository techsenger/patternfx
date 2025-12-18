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

import static com.techsenger.patternfx.core.HistoryPolicy.ALL;
import static com.techsenger.patternfx.core.HistoryPolicy.APPEARANCE;
import static com.techsenger.patternfx.core.HistoryPolicy.DATA;
import static com.techsenger.patternfx.core.HistoryPolicy.NONE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractComponentViewModel<T extends ComponentMediator> implements ComponentViewModel<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentViewModel.class);

    private T mediator;

    public T getMediator() {
        return this.mediator;
    }

    protected void restoreHistory() {
        var policy = getMediator().getHistoryPolicy();
        logger.debug("{} History policy during restore: {}", getMediator().getLogPrefix(), policy);
        ComponentHistory localHistory = null;
        if (policy != NONE) {
            localHistory = getMediator().getHistory();
            if (localHistory.isFresh()) {
                logger.debug("{} History is fresh. Skipping restoration", getMediator().getLogPrefix());
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
    }

    protected void saveHistory() {
        var policy = getMediator().getHistoryPolicy();
        logger.debug("{} History policy during save: {}", getMediator().getLogPrefix(), policy);
        switch (policy) {
            case DATA:
                preHistorySave();
                ((ComponentHistory) getMediator().getHistory()).saveData(this);
                break;
            case APPEARANCE:
                preHistorySave();
                ((ComponentHistory) getMediator().getHistory()).saveAppearance(this);
                break;
            case ALL:
                preHistorySave();
                var h = (ComponentHistory) getMediator().getHistory();
                h.saveData(this);
                h.saveAppearance(this);
                break;
            case NONE:
                break;
            default:
                throw new AssertionError();
        }
    }

    // todo: remove
    protected void postHistoryRestore() { }

    protected void preHistorySave() { }

    /**
     * Initializes the view model.
     */
    protected void initialize() { }

    /**
     * Deinitializes the view model.
     */
    protected void deinitialize() { }

    protected void setMediator(ComponentMediator mediator) {
        this.mediator = (T) mediator;
    }
}
