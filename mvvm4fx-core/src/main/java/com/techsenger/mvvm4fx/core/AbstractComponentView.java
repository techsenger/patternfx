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

    private ComponentComposer<?> composer;

    public AbstractComponentView(T viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public T getViewModel() {
        return viewModel;
    }

    /**
     * Initializes view.
     */
    @Override
    public final void initialize() {
        var descriptor = this.viewModel.getDescriptor();
        try {
            if (descriptor.stateWrapper().get() != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the component");
            }
            preInitialize(viewModel);
            descriptor.stateWrapper().set(ComponentState.INITIALIZING);
            if (this.composer != null) {
                this.composer.initialize();
            }
            viewModel.initialize();
            build(viewModel);
            bind(viewModel);
            addListeners(viewModel);
            addHandlers(viewModel);
            descriptor.stateWrapper().set(ComponentState.INITIALIZED);
            logger.debug("{} Initialized component", descriptor.getLogPrefix());
            postInitialize(viewModel);
        } catch (Exception ex) {
            logger.error("{} Error initializing", descriptor.getLogPrefix(), ex);
        }
    }

    /**
     * Deinitializes view.
     */
    @Override
    public final void deinitialize() {
        var descriptor = this.viewModel.getDescriptor();
        try {
            if (descriptor.stateWrapper().get() != ComponentState.INITIALIZED) {
                throw new IllegalStateException("Unexpected state of the component");
            }
            preDeinitialize(viewModel);
            descriptor.stateWrapper().set(ComponentState.DEINITIALIZING);
            removeHandlers(viewModel);
            removeListeners(viewModel);
            unbind(viewModel);
            unbuild(viewModel);
            viewModel.deinitialize();
            if (this.composer != null) {
                this.composer.deinitialize();
            }
            descriptor.stateWrapper().set(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized component", descriptor.getLogPrefix());
            postDeinitialize(viewModel);
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", descriptor.getLogPrefix(), ex);
        }
    }

    @Override
    public ComponentComposer<?> getComposer() {
        return composer;
    }

    /**
     * Creates a new {@link ComponentComposer} instance for this component.
     *
     * <p>This method is invoked during the component's pre-initialization phase and allows subclasses to provide
     * a custom composer implementation.
     *
     * <p>The default implementation returns {@code null}, meaning the component does not create a composer by default.
     *
     * @return a newly created {@link ComponentComposer}, or {@code null} if none is required
     */
    protected ComponentComposer<?> createComposer() {
        return null;
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
     * The first method called in initialization.
     */
    protected void preInitialize(T viewModel) {
        this.composer = createComposer();
        if (this.composer != null) {
            viewModel.setMediator(this.composer.createMediator());
        }
    }

    /**
     * Builds view.
     */
    protected void build(T viewModel) {

    }

    /**
     * Binds view to viewModel etc.
     */
    protected void bind(T viewModel) {

    }

    /**
     * Initializes listeners to different properties etc.
     */
    protected void addListeners(T viewModel) {

    }

    /**
     * Initializes handlers for mouse, keyboard etc events.
     */
    protected void addHandlers(T viewModel) {

    }

    /**
     * The last method called in initialization.
     */
    protected void postInitialize(T viewModel) {

    }

    /**
     * The first method called in deinitialization.
     */
    protected void preDeinitialize(T viewModel) {

    }

    /**
     * Removes handlers.
     *
     * @param viewModel
     */
    protected void removeHandlers(T viewModel) {

    }

    /**
     * Removes listeners.
     *
     * @param viewModel
     */
    protected void removeListeners(T viewModel) {

    }

    /**
     * Unbinds view from viewModel etc.
     *
     * @param viewModel
     */
    protected void unbind(T viewModel) {

    }

    /**
     * Unbuilds view.
     * @param viewModel
     */
    protected void unbuild(T viewModel) {

    }

    /**
     * The last method called in deinitialization.
     */
    protected void postDeinitialize(T viewModel) {

    }
}
