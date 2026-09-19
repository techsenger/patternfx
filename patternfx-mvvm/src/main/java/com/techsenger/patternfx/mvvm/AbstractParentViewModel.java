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

import com.google.errorprone.annotations.concurrent.LazyInit;
import com.techsenger.patternfx.core.ComponentParams;
import com.techsenger.toolkit.fx.value.ObservableSource;
import com.techsenger.toolkit.fx.value.SimpleObservableSource;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentViewModel<C extends Composer> extends AbstractViewModel
        implements ParentViewModel<C> {

    @LazyInit
    private C composer;

    private final ObservableSource<Void> deinitializeTree = new SimpleObservableSource<>();

    public AbstractParentViewModel(ComponentParams params) {
        super(params);
    }

    @Override
    public ComposerAccess getComposerAccess() {
        return getComposer();
    }

    @Override
    public C getComposer() {
        return this.composer;
    }

    @Override
    public void deinitializeTree() {
        deinitializeTree.next(null);
    }

    @SuppressWarnings("unchecked")
    protected void setComposer(Composer composer) {
        this.composer = (C) composer;
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();
        getComposer().compose();
    }

    ObservableSource<Void> deinitializeTreeSource() {
        return deinitializeTree;
    }
}
