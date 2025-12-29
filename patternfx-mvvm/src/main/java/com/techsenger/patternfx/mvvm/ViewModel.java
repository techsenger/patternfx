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

import com.techsenger.patternfx.core.HistoryPolicy;
import javafx.beans.property.ObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public interface ViewModel {

    /**
     * Returns the descriptor of the View.
     *
     * @return
     */
    Descriptor getDescriptor();

    /**
     * Returns the property for the history policy.
     *
     * @return
     */
    ObjectProperty<HistoryPolicy> historyPolicyProperty();

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
     * Requests the deinitialization of the View.
     */
    void requestDeinitialize();
}
