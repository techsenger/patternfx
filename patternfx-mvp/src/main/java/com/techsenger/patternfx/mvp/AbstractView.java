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

package com.techsenger.patternfx.mvp;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractView<T extends Presenter<?>> {

    private T presenter;

    /**
     * Returns the presenter.
     * @return
     */
    public T getPresenter() {
        return presenter;
    }

    /**
     * Initializes the View.
     */
    protected void initialize() {
        // empty
    }

    /**
     * Deiinitializes the View.
     */
    protected void deinitialize() {
        // empty
    }

    void setPresenter(Presenter<?> presenter) {
        this.presenter = (T) presenter;
    }
}
