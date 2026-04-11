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

/**
 *
 * @author Pavel Castornii
 */
public interface ParentComposer extends ComposerBase {

    /**
     * Composes the static structure of this component by creating and adding its default child components. This
     * method is intended for static composition, where child components are created once during component
     * initialization and remain present for the entire component lifecycle.
     *
     * <p>This method is automatically invoked during the {@code postInitialize} phase of the component lifecycle,
     * after the main initialization has completed but before the component becomes fully operational. This ensures
     * that all child components are properly initialized and integrated into the component tree before the parent
     * component starts its normal operation.
     *
     */
    void compose();
}
