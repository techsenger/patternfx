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

package com.techsenger.patternfx.mvp;

import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.core.DefaultComponentName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractPresenter<V extends View> implements Presenter<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPresenter.class);

    private final V view;

    private final ComponentDescriptor descriptor;

    public AbstractPresenter(V view, ComponentParams params) {
        params.validate();
        this.view = view;
        this.descriptor = createDescriptor();
        if (this.view instanceof AbstractView<?>) {
            ((AbstractView<?>) this.view).setPresenter(this);
        }
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public final void initialize() {
        try {
            if (descriptor.getState() != ComponentState.CREATING) {
                throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
            }
            // pre-initialization
            preInitialize();
            // initialization
            descriptor.setState(ComponentState.INITIALIZING);
            if (getView() instanceof AbstractView<?>) {
                ((AbstractView<?>) getView()).initialize();
            }
            descriptor.setState(ComponentState.INITIALIZED);
            logger.debug("{} Initialized the component", getDescriptor().getLogPrefix());
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
                throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
            }
            // pre-deinitialization
            preDeinitialize();
            // deinitialization
            descriptor.setState(ComponentState.DEINITIALIZING);
            if (getView() instanceof AbstractView<?>) {
                ((AbstractView<?>) getView()).deinitialize();
            }
            descriptor.setState(ComponentState.DEINITIALIZED);
            logger.debug("{} Deinitialized the component", getDescriptor().getLogPrefix());
            // post-deinitialization
            postDeinitialize();
        } catch (Exception ex) {
            logger.error("{} Error deinitializing", getDescriptor().getLogPrefix(), ex);
        }
    }

    @Override
    public ComponentDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * The first method called in initialization.
     */
    protected void preInitialize() { }

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
     * Creates this component's descriptor, defaulting to the class's simple name with any trailing "Presenter"
     * suffix removed.
     */
    protected ComponentDescriptor createDescriptor() {
        var simpleName = getClass().getSimpleName();
        var name = simpleName.endsWith("Presenter")
                ? simpleName.substring(0, simpleName.length() - "Presenter".length()) : simpleName;
        return new ComponentDescriptor(new DefaultComponentName(name));
    }
}
