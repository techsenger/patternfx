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
public abstract class AbstractComponent<T extends AbstractComponentView<?>> implements Component<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComponent.class);

    protected abstract class Mediator implements ComponentMediator {

    }

    private final T view;

    public AbstractComponent(T view) {
        this.view = view;
        this.view.setComponent(this);
    }

    @Override
    public T getView() {
        return view;
    }

    @Override
    public final void initialize() {
        var viewModel = this.view.getViewModel();
        var descriptor = viewModel.getDescriptor();
        try {
            var currentState = descriptor.getState();
            if (currentState != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the component - " + currentState.name());
            }
            // pre-initialization
            preInitialize();
            // initialization
            descriptor.getStateWrapper().set(ComponentState.INITIALIZING);
            viewModel.initialize();
            this.view.initialize();
            descriptor.getStateWrapper().set(ComponentState.INITIALIZED);
            logger.debug("{} Initialized component", descriptor.getLogPrefix());
            // post-initialization
            postInitialize();
        } catch (Exception ex) {
            logger.error("{} Error initializing", descriptor.getLogPrefix(), ex);
        }
    }

    @Override
    public final void deinitialize() {
        var viewModel = this.view.getViewModel();
        var descriptor = viewModel.getDescriptor();
        try {
            var currentState = descriptor.getState();
            if (currentState != ComponentState.INITIALIZED) {
                throw new IllegalStateException("Unexpected state of the component - " + currentState.name());
            }
            // pre-deinitialization
            preDeinitialize();
            // deinitialization
            descriptor.getStateWrapper().set(ComponentState.DEINITIALIZING);
            this.view.deinitialize();
            viewModel.deinitialize();
            descriptor.getStateWrapper().set(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized component", descriptor.getLogPrefix());
            // post-deinitialization
            postDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", descriptor.getLogPrefix(), ex);
        }
    }

    /**
     * The first method called in initialization.
     */
    protected void preInitialize() {
        var mediator = createMediator();
        this.view.getViewModel().setMediator(mediator);
        this.view.getViewModel().restoreHistory();
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
    protected void postDeinitialize() {
        this.view.getViewModel().saveHistory();
    }

    protected abstract AbstractComponent.Mediator createMediator();
}
