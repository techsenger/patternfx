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

/**
 * Represents the lifecycle state of a component within the framework.
 *
 * @author Pavel Castornii
 */
public enum ComponentState {

    /**
     * The component is currently being created.
     *
     * <p>During this phase, the {@code ComponentViewModel}, {@code ComponentView}, and {@code Component}
     * objects are being constructed, but the initialization process has not yet begun.
     * This is the earliest detectable phase of the component’s lifecycle.
     */
    CREATING,

    /**
     * The component is in the process of initialization.
     *
     * <p>This state indicates that the component is initializing its {@code ComponentViewModel},
     * {@code ComponentView}, and other internal parts.
     */
    INITIALIZING,

    /**
     * The component has been fully initialized and is ready for use.
     *
     * <p>This is the stable operational state in which the component, its view, and its view-model
     * are fully active, bound, and synchronized.
     */
    INITIALIZED,

    /**
     * The component is in the process of deinitialization.
     *
     * <p>This state indicates that the component is deinitializing its {@code ComponentView},
     * {@code ComponentViewModel}, and other internal parts.
     */
    DEINITIALIZING,

    /**
     * The component has been completely deinitialized and can no longer be used.
     *
     * <p>At this point, the component, its view, and its view-model have released all resources and performed all
     * necessary cleanup. This is the terminal state of the component lifecycle.
     */
    DEINITIALIZED
}


