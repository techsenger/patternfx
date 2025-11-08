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
 * Represents the lifecycle state of a component within the MVVM4FX framework.
 *
 * <p>Each component passes through several well-defined phases from creation to destruction. These states describe
 * both transitional phases (process-oriented) and stable phases (result-oriented), allowing fine-grained lifecycle
 * control and predictable behavior.
 *
 * <p>The typical lifecycle sequence is as follows:
 * <pre>
 * CREATING → INITIALIZING → INITIALIZED → DEINITIALIZING → DEINITIALIZED
 * </pre>
 *
 * @author Pavel Castornii
 */
public enum ComponentState {

    /**
     * The component is currently being created.
     *
     * <p>During this phase, the {@code ComponentViewModel} and {@code ComponentView} objects are being constructed,
     * but the initialization process has not yet begun. This is the earliest detectable phase of the component’s
     * lifecycle.
     */
    CREATING,

    /**
     * The component is in the process of initialization.
     *
     * <p>This phase begins when the {@code initialize()} method is invoked on the {@code ComponentView}, during which
     * bindings, listeners, and other setup logic are established.
     */
    INITIALIZING,

    /**
     * The component has been fully initialized and is ready for use.
     *
     * <p>This is the stable operational state in which the component’s view and view-model are fully active, bound,
     * and synchronized.
     */
    INITIALIZED,

    /**
     * The component is in the process of deinitialization.
     *
     * <p>This phase begins when the {@code deinitialize()} method is invoked, during which listeners are removed,
     * bindings are cleared, and any necessary cleanup is performed.
     */
    DEINITIALIZING,

    /**
     * The component has been completely deinitialized and can no longer be used.
     *
     * <p>At this point, all bindings, handlers, and resources have been released. This is the terminal state of the
     * component lifecycle.
     */
    DEINITIALIZED
}
