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

package com.techsenger.patternfx.core;

import com.techsenger.annotations.Nullable;

/**
 *
 * @author Pavel Castornii
 */
public class ComponentParams {

    private @Nullable HistoryProvider<? extends AbstractComponentHistory> historyProvider;

    public @Nullable HistoryProvider<? extends AbstractComponentHistory> getHistoryProvider() {
        return historyProvider;
    }

    public void setHistoryProvider(@Nullable HistoryProvider<? extends AbstractComponentHistory> historyProvider) {
        this.historyProvider = historyProvider;
    }

    /**
     * Validates the params. Called before the params are used to construct a component. Subclasses adding their
     * own required fields should override this method to validate them.
     *
     * <p>Framework contract: This method is intended to be called exclusively by the component being constructed
     * from these params, which may live in a different module than this class. Direct invocation by user code
     * results in undefined behavior.
     */
    public void validate() { }
}
