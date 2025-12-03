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

import com.techsenger.toolkit.fx.collections.ListSynchronizer;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentView<T extends AbstractParentViewModel> extends AbstractComponentView<T>
        implements ParentView<T> {

    private final ObservableList<ChildView<?>> children = FXCollections.observableArrayList();

    private final ListSynchronizer childrenSynchronizer;

    public AbstractParentView(T viewModel) {
        super(viewModel);
        childrenSynchronizer = new ListSynchronizer<ChildView<?>, ChildViewModel>(children,
                viewModel.getModifiableChildren(), (v) -> v.getViewModel());
    }

    @Override
    public ObservableList<ChildView<?>> getChildren() {
        return children;
    }

    @Override
    public SubtreeIterator<ParentView<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentView<?>>(this) {

            @Override
            List<ParentView<?>> getChildren(ParentView<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public SubtreeIterator<ParentView<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentView<?>>(this) {

            @Override
            List<ParentView<?>> getChildren(ParentView<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public ParentComposer<?> getComposer() {
        return (ParentComposer<?>) super.getComposer();
    }

    @Override
    protected void addListeners(T viewModel) {
        super.addListeners(viewModel);
        children.addListener((ListChangeListener<ChildView<?>>) (change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().map(e -> (AbstractChildView<?>) e)
                            .forEach(e -> e.setParent(this));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().stream().map(e -> (AbstractChildView<?>) e)
                            .forEach(e -> e.setParent(null));
                }
            }
        });
    }
}
