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

package com.techsenger.patternfx.mvvm;

import com.techsenger.patternfx.core.ComponentState;
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
            var descriptor = viewModel.getDescriptor();
            if (descriptor.getState() != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the view - " + descriptor.getState().name());
            }
            // pre-initialization
            preInitialize();
            // initialization
            descriptor.setState(ComponentState.INITIALIZING);
            viewModel.initialize();
            build();
            bind();
            addListeners();
            addHandlers();
            descriptor.setState(ComponentState.INITIALIZED);
            logger.debug("{} Initialized view", getDescriptor().getLogPrefix());
            // post-initialization
            postInitialize();
        } catch (Exception ex) {
            logger.error("{} Error initializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    @Override
    public final void deinitialize() {
        try {
            var descriptor = getDescriptor();
            if (descriptor.getState() != ComponentState.INITIALIZED) {
                throw new IllegalStateException("Unexpected state of the view - " + descriptor.getState().name());
            }
            // pre-deinitialization
            preDeinitialize();
            // deinitialization
            descriptor.setState(ComponentState.DEINITIALIZING);
            removeHandlers();
            removeListeners();
            unbind();
            unbuild();
            viewModel.deinitialize();
            descriptor.setState(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized view", getDescriptor().getLogPrefix());
            // post-deinitialization
            postDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    /**
     * The first method called in initialization.
     */
    protected void preInitialize() {
        viewModel.prepareHistory();
    }

    /**
     * The last method called in initialization.
     */
    protected void postInitialize() { }

    /**
     * The first method called in deinitialization.
     */
    protected void preDeinitialize() { }

    /**
     * The last method called in deinitialization.
     */
    protected void postDeinitialize() { }


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
    protected void addListeners() {
        getViewModel().getRequestDeinitialize().addListener((newV) -> deinitialize());
    }

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
     *
     * @param viewModel
     */
    protected void unbuild() { }

    /**
     * Returns the {@link Descriptor} associated with this view.
     *
     * <p>This method is provided as a convenience shortcut that delegates to the underlying view model, allowing
     * subclasses to access the descriptor without directly referencing the view model.
     *
     * @return the view descriptor
     */
    protected Descriptor getDescriptor() {
        return this.viewModel.getDescriptor();
    }
}
