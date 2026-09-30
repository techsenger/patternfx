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

import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.patternfx.core.ComponentState;
import com.techsenger.patternfx.core.DefaultComponentName;
import com.techsenger.toolkit.fx.value.ObservableSource;
import com.techsenger.toolkit.fx.value.SimpleObservableSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractViewModel implements ViewModel {

    private static final Logger logger = LoggerFactory.getLogger(AbstractViewModel.class);

    private final ComponentDescriptor descriptor;

    private final ObservableSource<Void> deinitialize = new SimpleObservableSource<>();

    public AbstractViewModel(ComponentParams params) {
        params.validate();
        this.descriptor = createDescriptor();
    }

    @Override
    public ComponentDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public void deinitialize() {
        deinitialize.next(null);
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
     * Creates this component's descriptor, defaulting to the class's simple name with any trailing "ViewModel"
     * suffix removed.
     */
    protected ComponentDescriptor createDescriptor() {
        var simpleName = getClass().getSimpleName();
        var name = simpleName.endsWith("ViewModel")
                ? simpleName.substring(0, simpleName.length() - "ViewModel".length()) : simpleName;
        return new ComponentDescriptor(new DefaultComponentName(name));
    }

    final void beginInitialize() {
        if (descriptor.getState() != ComponentState.CREATING) {
            throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
        }
        preInitialize();
        descriptor.setState(ComponentState.INITIALIZING);
    }

    final void endInitialize() {
        descriptor.setState(ComponentState.INITIALIZED);
        logger.debug("{} Initialized the component", getDescriptor().getLogPrefix());
        postInitialize();
    }

    final void beginDeinitialize() {
        if (descriptor.getState() != ComponentState.INITIALIZED) {
            throw new IllegalStateException("Unexpected state of the component - " + descriptor.getState().name());
        }
        preDeinitialize();
        descriptor.setState(ComponentState.DEINITIALIZING);
    }

    final void endDeinitialize() {
        descriptor.setState(ComponentState.DEINITIALIZED);
        logger.debug("{} Deinitialized the component", getDescriptor().getLogPrefix());
        postDeinitialize();
    }

    ObservableSource<Void> deinitializeSource() {
        return deinitialize;
    }
}
