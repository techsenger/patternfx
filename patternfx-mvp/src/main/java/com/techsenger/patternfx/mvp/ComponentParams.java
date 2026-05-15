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
import com.techsenger.patternfx.core.HistoryPolicy;
import com.techsenger.patternfx.core.HistoryProvider;
import java.util.Objects;

/**
 *
 * @author Pavel Castornii
 */
public class ComponentParams {

    private HistoryPolicy historyPolicy = HistoryPolicy.NONE;

    private @Nullable HistoryProvider<? extends ComponentHistory> historyProvider;

    public HistoryPolicy getHistoryPolicy() {
        return historyPolicy;
    }

    public void setHistoryPolicy(HistoryPolicy historyPolicy) {
        this.historyPolicy = historyPolicy;
    }

    public @Nullable HistoryProvider<? extends ComponentHistory> getHistoryProvider() {
        return historyProvider;
    }

    public void setHistoryProvider(@Nullable HistoryProvider<? extends ComponentHistory> historyProvider) {
        this.historyProvider = historyProvider;
    }

    protected void validate() {
        Objects.requireNonNull(historyPolicy);
    }
}
