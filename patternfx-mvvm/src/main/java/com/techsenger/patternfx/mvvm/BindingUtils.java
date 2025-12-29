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

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public final class BindingUtils {

   /**
     * Binds a {@link ViewModel} property to a {@link View} property.
     *
     * <p>The view-model value is derived from the current view by calling {@link View#getViewModel()} and is updated
     * whenever the view changes. If the view is {@code null}, the view-model value will also be {@code null}.
     *
     * @param <VM> the type of the view-model
     * @param <V> the type of the view
     * @param viewModel the target view-model property to bind
     * @param view the source view property
     */
    public static <VM extends ViewModel, V extends View<VM>> void bind(ObjectProperty<VM> viewModel,
            ObjectProperty<V> view) {
        ObjectBinding<VM> binding = Bindings.createObjectBinding(
            () -> {
                V v = view.get();
                return v == null ? null : v.getViewModel();
            },
            view
        );

        viewModel.bind(binding);
    };

    private BindingUtils() {
        // empty
    }
}
