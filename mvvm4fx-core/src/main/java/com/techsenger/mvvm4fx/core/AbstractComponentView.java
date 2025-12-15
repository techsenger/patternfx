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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractComponentView<T extends AbstractComponentViewModel> implements ComponentView<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponentView.class);

    private final T viewModel;

    private AbstractComponent<?> component;

    public AbstractComponentView(T viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public T getViewModel() {
        return viewModel;
    }

    @Override
    public AbstractComponent<?> getComponent() {
        return component;
    }

    /**
     * Initializes view.
     */
    protected void initialize() {
        build();
        bind();
        addListeners();
        addHandlers();
    }

    /**
     * Deinitializes view.
     */
    protected void deinitialize() {
        removeHandlers();
        removeListeners();
        unbind();
        unbuild();
    }

    /**
     * Builds view.
     */
    protected void build() { }

    /**
     * Binds view to viewModel etc.
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
     * @param viewModel
     */
    protected void removeHandlers() { }

    /**
     * Removes listeners.
     *
     * @param viewModel
     */
    protected void removeListeners() { }

    /**
     * Unbinds view from viewModel etc.
     *
     * @param viewModel
     */
    protected void unbind() { }

    /**
     * Unbuilds view.
     * @param viewModel
     */
    protected void unbuild() { }

    void setComponent(AbstractComponent<?> component) {
        this.component = component;
    }
}
