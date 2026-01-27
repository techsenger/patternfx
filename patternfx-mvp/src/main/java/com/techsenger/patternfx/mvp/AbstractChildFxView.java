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

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractChildFxView<P extends ChildPresenter<?, ?>>
        extends AbstractParentFxView<P> implements ChildFxView<P> {

    public class Composer extends AbstractParentFxView.Composer implements ChildFxView.Composer {

        private final AbstractChildFxView<?> view = AbstractChildFxView.this;

        @Override
        public ParentPort getParent() {
            var parent = view.getParent();
            if (parent == null) {
                return null;
            }
            return parent.getPresenter().getPort();
        }
    }

    private final ReadOnlyObjectWrapper<ParentFxView<?>> parent = new ReadOnlyObjectWrapper<>();

    @Override
    public ReadOnlyObjectProperty<ParentFxView<?>> parentProperty() {
        return this.parent.getReadOnlyProperty();
    }

    @Override
    public ParentFxView<?> getParent() {
        return this.parent.get();
    }

    @Override
    public <T extends ParentFxView<?>> T getParent(Class<T> parentClass) {
        return (T) getParent();
    }

    @Override
    public void setParent(ParentFxView<?> parent) {
        this.parent.set(parent);
    }

    @Override
    public Composer getComposer() {
        return (Composer) super.getComposer();
    }

    @Override
    protected Composer createComposer() {
        return new AbstractChildFxView.Composer();
    }
}
