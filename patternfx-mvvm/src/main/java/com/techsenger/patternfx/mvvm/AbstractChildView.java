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

import com.techsenger.annotations.Nullable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractChildView<VM extends AbstractChildViewModel<?>>
        extends AbstractParentView<VM> implements ChildView<VM> {

    private final ReadOnlyObjectWrapper<ParentView<?>> parent = new ReadOnlyObjectWrapper<>();

    public AbstractChildView(VM viewModel) {
        super(viewModel);
        parent.addListener((ov, oldV, newV) -> {
            if (newV != null) {
                viewModel.setParent(newV.getViewModel());
            } else {
                viewModel.setParent(null);
            }
        });
    }

    @Override
    public ReadOnlyObjectProperty<ParentView<?>> parentProperty() {
        return this.parent.getReadOnlyProperty();
    }

    @Override
    public @Nullable ParentView<?> getParent() {
        return this.parent.get();
    }

    @Override
    public <T extends ParentView<?>> @Nullable T getParent(Class<T> parentClass) {
        var parent = getParent();
        if (parent != null) {
            return parentClass.cast(parent);
        } else {
            return null;
        }
    }

    @Override
    public void setParent(@Nullable ParentView<?> parent) {
        this.parent.set(parent);
    }
}
