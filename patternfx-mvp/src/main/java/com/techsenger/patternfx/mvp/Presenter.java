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

import com.techsenger.patternfx.core.HistoryPolicy;

/**
 *
 * @author Pavel Castornii
 */
public interface Presenter {

    /**
     * Returns the port.
     * @return
     */
    Port getPort();

    /**
     * Returns the descriptor.
     *
     * @return
     */
    Descriptor getDescriptor();

    /**
     * Returns the history policy.
     *
     * @return
     */
    HistoryPolicy getHistoryPolicy();

    /**
     * Sets the history policy to the specified value.
     *
     * @param policy the history policy to set.
     */
    void setHistoryPolicy(HistoryPolicy policy);

    /**
     * Initializes both the presenter and its associated view.
     */
    void initialize();

    /**
     * Deinitializes both the presenter and its associated view.
     */
    void deinitialize();
}
