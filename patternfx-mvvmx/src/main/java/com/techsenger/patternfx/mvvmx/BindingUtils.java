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

package com.techsenger.patternfx.mvvmx;

import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;

/**
 *
 * @author Pavel Castornii
 */
public final class BindingUtils {


    /**
     * Binds a {@link ComponentViewModel} property to a {@link Component} property.
     *
     * <p>The view-model value is derived from the current component using the provided mapper and is updated
     * whenever the component changes. If the component is {@code null}, the view-model value is {@code null}.
     *
     * @param <V> the view-model type
     * @param <T> the component type
     * @param viewModel the target view-model property to bind
     * @param component the source component property
     * @param mapper a function that maps the component to its view-model
     */
    public static <V extends ComponentViewModel<?>, T extends Component<?>> void bind(ObjectProperty<V> viewModel,
            ObjectProperty<T> component, Function<T, V> mapper) {
            viewModel.bind(Bindings.createObjectBinding(() ->
                    component.get() == null ? null : mapper.apply(component.get()), component));
    }

    private BindingUtils() {
        // empty
    }
}
