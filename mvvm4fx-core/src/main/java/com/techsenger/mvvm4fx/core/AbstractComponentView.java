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
     * Returns the component descriptor for convenient access. This is a shortcut method that delegates to the
     * underlying ViewModel's descriptor.
     *
     * @return the component descriptor
     */
    protected ComponentDescriptor getDescriptor() {
       return this.viewModel.getDescriptor();
    }

    /**
     * Performs initialization.
     */
    protected void initialize() {
        build(viewModel);
        bind(viewModel);
        addListeners(viewModel);
        addHandlers(viewModel);
    }

    /**
     * Performs deinitialization.
     */
    protected void deinitialize() {
        removeHandlers(viewModel);
        removeListeners(viewModel);
        unbind(viewModel);
        unbuild(viewModel);
    }

    /**
     * Builds view.
     */
    protected void build(T viewModel) { }

    /**
     * Binds view to viewModel etc.
     */
    protected void bind(T viewModel) { }

    /**
     * Initializes listeners to different properties etc.
     */
    protected void addListeners(T viewModel) { }

    /**
     * Initializes handlers for mouse, keyboard etc events.
     */
    protected void addHandlers(T viewModel) { }

    /**
     * Removes handlers.
     *
     * @param viewModel
     */
    protected void removeHandlers(T viewModel) { }

    /**
     * Removes listeners.
     *
     * @param viewModel
     */
    protected void removeListeners(T viewModel) { }

    /**
     * Unbinds view from viewModel etc.
     *
     * @param viewModel
     */
    protected void unbind(T viewModel) { }

    /**
     * Unbuilds view.
     * @param viewModel
     */
    protected void unbuild(T viewModel) { }

    void setComponent(AbstractComponent<?> component) {
        this.component = component;
    }
}
