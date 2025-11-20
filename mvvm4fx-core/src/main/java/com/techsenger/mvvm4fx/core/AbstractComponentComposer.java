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

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractComponentComposer<T extends ComponentView<?>> implements ComponentComposer<T> {

    protected abstract class ViewModelComposer implements ComponentViewModel.Composer {

    }

    private final T view;

    private final ComponentViewModel.Composer viewModelComposer;

    public AbstractComponentComposer(T view) {
        this.view = view;
        this.viewModelComposer = createViewModelComposer();
    }

    @Override
    public ParentViewModel.Composer getViewModelComposer() {
        return viewModelComposer;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void deinitialize() {

    }

    protected final T getView() {
        return view;
    }

    protected abstract ComponentViewModel.Composer createViewModelComposer();
}
