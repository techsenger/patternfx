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

package com.techsenger.patternfx.mvvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractView<T extends AbstractViewModel> implements View<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractView.class);

    private final T viewModel;

    public AbstractView(T viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public T getViewModel() {
        return viewModel;
    }

    @Override
    public final void initialize() {
        try {
            viewModel.beginInitialize();
            build();
            bind();
            addListeners();
            addHandlers();
            viewModel.endInitialize();
        } catch (Exception ex) {
            logger.error("{} Error initializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    @Override
    public final void deinitialize() {
        try {
            viewModel.beginDeinitialize();
            removeHandlers();
            removeListeners();
            unbind();
            unbuild();
            viewModel.endDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    /**
     * Builds the view.
     */
    protected void build() { }

    /**
     * Binds the view to the view model, etc.
     */
    protected void bind() { }

    /**
     * Initializes listeners to different properties etc.
     */
    protected void addListeners() {
        getViewModel().getRequestDeinitialize().addListener((newV) -> deinitialize());
    }

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
     * Unbinds the view from the view model, etc.
     */
    protected void unbind() { }

    /**
     * Unbuilds the view.
     */
    protected void unbuild() { }

    /**
     * Returns the {@link Descriptor} of this view &mdash; a shortcut that delegates to the underlying view model,
     * sparing subclasses a direct reference to it.
     */
    protected Descriptor getDescriptor() {
        return this.viewModel.getDescriptor();
    }
}
