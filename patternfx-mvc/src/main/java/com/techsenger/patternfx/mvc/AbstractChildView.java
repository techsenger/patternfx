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

package com.techsenger.patternfx.mvc;

import com.techsenger.annotations.Nullable;
import com.techsenger.patternfx.core.ParentPort;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractChildView<C extends ChildController<?>>
        extends AbstractParentView<C> implements ChildView<C> {

    public class Composer extends AbstractParentView<C>.Composer implements ChildView.Composer {

        private final ReadOnlyObjectWrapper<ParentView<?>> parent = new ReadOnlyObjectWrapper<>();

        @Override
        public ReadOnlyObjectProperty<ParentView<?>> parentProperty() {
            return this.parent.getReadOnlyProperty();
        }

        @Override
        public @Nullable ParentView<?> getParent() {
            return this.parent.get();
        }

        @Override
        public void setParent(@Nullable ParentView<?> parent) {
            this.parent.set(parent);
        }

        @Override
        public @Nullable ParentPort getParentPort() {
            var parent = getParent();
            if (parent == null) {
                return null;
            }
            return parent.getController();
        }
    }

    @Override
    public Composer getComposer() {
        return (Composer) super.getComposer();
    }

    @Override
    protected Composer createComposer() {
        return new AbstractChildView<C>.Composer();
    }
}
