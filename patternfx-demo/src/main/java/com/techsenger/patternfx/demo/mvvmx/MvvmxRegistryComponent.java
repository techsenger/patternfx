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

package com.techsenger.patternfx.demo.mvvmx;

import com.techsenger.patternfx.core.Name;
import com.techsenger.patternfx.demo.DemoNames;
import com.techsenger.patternfx.mvvmx.AbstractParentComponent;
import com.techsenger.patternfx.mvvmx.BindingUtils;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 *
 * @author Pavel Castornii
 */
public class MvvmxRegistryComponent extends AbstractParentComponent<MvvmxRegistryView> {

    private final class Mediator extends AbstractParentComponent.Mediator implements MvvmxRegistryMediator {

        private final MvvmxRegistryComponent component = MvvmxRegistryComponent.this;

        private final ReadOnlyObjectWrapper<MvvmxReportViewModel> report = new ReadOnlyObjectWrapper<>();

        Mediator() {
            BindingUtils.bind(report, component.report, c -> c.getView().getViewModel());
        }

        @Override
        public void openDialog(MvvmxDialogViewModel vm) {
            var dialogV = new MvvmxDialogView(vm, getView().getStage());
            var dialogC = new MvvmxDialogComponent(dialogV);
            dialogC.initialize();
            dialogC.getView().getDialog().showAndWait();
            dialogC.deinitialize();
        }

        @Override
        public ReadOnlyObjectProperty<MvvmxReportViewModel> reportProperty() {
            return report.getReadOnlyProperty();
        }

        @Override
        public MvvmxReportViewModel getReport() {
            return report.get();
        }

        @Override
        public void addReport(MvvmxReportViewModel reportVm) {
            var reportV = new MvvmxReportView(reportVm);
            var reportC = new MvvmxReportComponent(reportV);
            component.getModifiableChildren().add(reportC);
            component.setReport(reportC);
            reportC.initialize();
            getView().addReport(reportV);
        }

        @Override
        public void removeReport() {
            var reportC = component.getReport();
            getView().removeReport();
            reportC.deinitialize();
            component.getModifiableChildren().remove(reportC);
            component.setReport(null);
        }
    }

    private final ReadOnlyObjectWrapper<MvvmxReportComponent> report = new ReadOnlyObjectWrapper<>();

    public MvvmxRegistryComponent(MvvmxRegistryView view) {
        super(view);
    }

    @Override
    public Name getName() {
        return DemoNames.PERSON_REGISTRY;
    }

    public MvvmxReportComponent getReport() {
        return report.get();
    }

    public ReadOnlyObjectProperty<MvvmxReportComponent> reportProperty() {
        return report.getReadOnlyProperty();
    }

    @Override
    protected Mediator createMediator() {
        return new Mediator();
    }

    private void setReport(MvvmxReportComponent value) {
        report.set(value);
    }
}
