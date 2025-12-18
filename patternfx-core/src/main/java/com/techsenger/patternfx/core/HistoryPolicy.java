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

/**
 * Enum representing different history policies, which define what aspects of the component should be historized
 * (saved) or restored (loaded).
 *
 * @author Pavel Castornii
 */
public enum HistoryPolicy {

    /**
     * Represents the history policy for user input (e.g., text data).
     */
    DATA,

    /**
     * Represents the history policy for appearance-related settings (e.g., dialog width, positioning).
     */
    APPEARANCE,

    /**
     * Represents the history policy for all states, including both DATA and APPEARANCE.
     */
    ALL,

    /**
     * Represents the history policy where no state is historized.
     */
    NONE
}
