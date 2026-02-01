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
public abstract class AbstractFxView<T extends Presenter<?>> extends AbstractView<T> implements FxView<T> {

    @Override
    public Descriptor getDescriptor() {
        return getPresenter().getDescriptor();
    }

    @Override
    protected void initialize() {
        build();
        bind();
        addListeners();
        addHandlers();
    }

    @Override
    protected void deinitialize() {
        removeHandlers();
        removeListeners();
        unbind();
        unbuild();
    }

    /**
     * Builds the view.
     */
    protected void build() { }

    /**
     * Makes all bindings.
     */
    protected void bind() { }

    /**
     * Initializes listeners to different properties etc.
     */
    protected void addListeners() { }

    /**
     * Initializes handlers for mouse, keyboard etc events.
     */
    protected void addHandlers() { }

    /**
     * Removes handlers.
     *
     */
    protected void removeHandlers() { }

    /**
     * Removes listeners.
     *
     */
    protected void removeListeners() { }

    /**
     * Unbinds existing bindings.
     *
     */
    protected void unbind() { }

    /**
     * Unbuilds the view.
     */
    protected void unbuild() { }
}
