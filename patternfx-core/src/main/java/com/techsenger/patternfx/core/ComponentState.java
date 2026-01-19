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
 * Represents the lifecycle state of a component within the framework.
 *
 * @author Pavel Castornii
 */
public enum ComponentState {

    /**
     * The component is currently being created.
     *
     * <p>During this phase, the component objects are being constructed, but the initialization process has not
     * yet begun. This is the earliest detectable phase of the component’s lifecycle.
     */
    CREATING,

    /**
     * The component is in the process of initialization.
     */
    INITIALIZING,

    /**
     * The component has been fully initialized and is ready for use.
     */
    INITIALIZED,

    /**
     * The component is in the process of deinitialization.
     */
    DEINITIALIZING,

    /**
     * The component has been completely deinitialized and can no longer be used.
     *
     * <p>At this point, the component, its view, and its elements have released all resources and performed all
     * necessary cleanup. This is the terminal state of the component lifecycle.
     */
    DEINITIALIZED
}


