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

package com.techsenger.patternfx.mvc;

import com.google.errorprone.annotations.concurrent.LazyInit;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractView<C extends Controller<?>> implements View<C> {

    @LazyInit
    private C controller;

    @Override
    public C getController() {
        return controller;
    }

    protected void initialize() {
        build();
        bind();
        addListeners();
        addHandlers();
    }

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
     * Binds the view to the controller, etc.
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
     */
    protected void removeHandlers() { }

    /**
     * Removes listeners.
     */
    protected void removeListeners() { }

    /**
     * Unbinds the view from the controller, etc.
     */
    protected void unbind() { }

    /**
     * Unbuilds the view.
     */
    protected void unbuild() { }

    /**
     * Returns the {@link ComponentDescriptor} of this view &mdash; a shortcut that delegates to the underlying
     * controller, sparing subclasses a direct reference to it.
     */
    protected ComponentDescriptor getDescriptor() {
        return getController().getDescriptor();
    }

    @SuppressWarnings("unchecked")
    void setController(Controller<?> controller) {
        this.controller = (C) controller;
    }
}
