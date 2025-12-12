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
public abstract class AbstractParentComponent<T extends AbstractParentView<?>> extends AbstractComponent<T>
        implements ParentComponent<T> {

    protected abstract class Mediator extends AbstractComponent.Mediator implements ParentMediator {

        private final ListSynchronizer childrenSynchronizer;

        private final ObservableList<ChildViewModel> modifiableChildren = FXCollections.observableArrayList();

        private final ObservableList<ChildViewModel> children =
                FXCollections.unmodifiableObservableList(modifiableChildren);

        public Mediator() {
            var outerChildren = AbstractParentComponent.this.modifiableChildren;
            childrenSynchronizer = new ListSynchronizer<ChildComponent<?>, ChildViewModel>(outerChildren,
                    modifiableChildren, (v) -> v.getView().getViewModel());
        }

        @Override
        public ObservableList<ChildViewModel> getChildren() {
            return children;
        }


        @Override
        public SubtreeIterator<ParentViewModel> depthFirstIterator() {
            return new AbstractDepthFirstIterator<ParentViewModel>(getView().getViewModel()) {

                @Override
                List<ParentViewModel> getChildren(ParentViewModel parent) {
                    return (List) parent.getMediator().getChildren();
                }
            };
        }

        @Override
        public SubtreeIterator<ParentViewModel> breadthFirstIterator() {
            return new AbstractBreadthFirstIterator<ParentViewModel>(getView().getViewModel()) {

                @Override
                List<ParentViewModel> getChildren(ParentViewModel parent) {
                    return (List) parent.getMediator().getChildren();
                }
            };
        }
    }

    private final ObservableList<ChildComponent<?>> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildComponent<?>> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    public AbstractParentComponent(T view) {
        super(view);
        modifiableChildren.addListener((ListChangeListener<ChildComponent<?>>) (change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().map(e -> (AbstractChildComponent<?>) e)
                            .forEach(e -> e.setParent(this));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().stream().map(e -> (AbstractChildComponent<?>) e)
                            .forEach(e -> e.setParent(null));
                }
            }
        });
    }

    @Override
    public SubtreeIterator<ParentComponent<?>> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentComponent<?>>(this) {

            @Override
            List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public SubtreeIterator<ParentComponent<?>> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentComponent<?>>(this) {

            @Override
            List<ParentComponent<?>> getChildren(ParentComponent<?> parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public ObservableList<ChildComponent<?>> getChildren() {
        return this.children;
    }

    @Override
    protected abstract AbstractParentComponent.Mediator createMediator();

    protected ObservableList<ChildComponent<?>> getModifiableChildren() {
        return modifiableChildren;
    }
}
