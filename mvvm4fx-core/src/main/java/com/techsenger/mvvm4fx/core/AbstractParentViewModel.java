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

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractParentViewModel extends AbstractComponentViewModel implements ParentViewModel {

    private final ObservableList<ChildViewModel> modifiableChildren = FXCollections.observableArrayList();

    private final ObservableList<ChildViewModel> children =
            FXCollections.unmodifiableObservableList(modifiableChildren);

    public AbstractParentViewModel() {
        super();
    }

    @Override
    public ObservableList<ChildViewModel> getChildren() {
        return children;
    }

    @Override
    public SubtreeIterator<ParentViewModel> depthFirstIterator() {
        return new AbstractDepthFirstIterator<ParentViewModel>(this) {

            @Override
            List<ParentViewModel> getChildren(ParentViewModel parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public SubtreeIterator<ParentViewModel> breadthFirstIterator() {
        return new AbstractBreadthFirstIterator<ParentViewModel>(this) {

            @Override
            List<ParentViewModel> getChildren(ParentViewModel parent) {
                return (List) parent.getChildren();
            }
        };
    }

    @Override
    public ParentMediator getMediator() {
        return (ParentMediator) super.getMediator();
    }

    ObservableList<ChildViewModel> getModifiableChildren() {
        return modifiableChildren;
    }
}
