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

import com.techsenger.patternfx.core.AbstractBreadthFirstIterator;
import com.techsenger.patternfx.core.AbstractDepthFirstIterator;
import com.techsenger.patternfx.core.TreeIterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentJfxComposer<V extends AbstractParentJfxView<?, ?>> implements ParentComposer {

    private final V view;

    public AbstractParentJfxComposer(V view) {
        this.view = view;
    }

    @Override
    public List<Port> getChildren() {
        return view.getChildren().stream().map(v -> v.getPresenter().getPort()).collect(Collectors.toList());
    }

    @Override
    public TreeIterator<Port> depthFirstIterator() {
        return new AbstractDepthFirstIterator<Port, ParentJfxView<?>>(view) {

            @Override
            protected List<ParentJfxView<?>> getChildren(ParentJfxView<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected Port map(ParentJfxView<?> value) {
                return value.getPresenter().getPort();
            }
        };
    }

    @Override
    public TreeIterator<Port> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<Port, ParentJfxView<?>>(view) {

            @Override
            protected List<ParentJfxView<?>> getChildren(ParentJfxView<?> parent) {
                return (List) parent.getChildren();
            }

            @Override
            protected Port map(ParentJfxView<?> value) {
                return value.getPresenter().getPort();
            }
        };
    }

    @Override
    public String toTreeString() {
        return view.toTreeString();
    }

    @Override
    public String toTreeString(BiConsumer<Port, StringBuilder> appender) {
        return view.toTreeString(depthFirstIterator(), appender);
    }

    protected V getView() {
        return view;
    }
}
