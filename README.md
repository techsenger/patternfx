# Techsenger MVVM4FX

Techsenger MVVM4FX is a tiny framework for developing JavaFX applications using the MVVM pattern. It provides all
the necessary interfaces and base class implementations for creating components, which serve as the units of the MVVM
pattern. Examples of components include tabs, dialog windows, toolbars, image viewers, help pages, and more.

As a real example of using this framework, see [TabShell](https://github.com/techsenger/tabshell) project.

## Table of Contents
* [Overview](#overview)
* [Features](#features)
* [MVVM](#mvvm)
    * [What is MVVM?](#what-is-mvvm)
    * [MVVM Advantages](#mvvm-advantages)
* [Component](#component)
    * [What is a Component?](#what-is-component)
    * [Component Structure](#component-structure)
    * [Component Lifecycle](#component-lifecycle)
    * [Component Hierarchy](#component-hierarchy)
    * [Composite Component](#composite-component)
    * [When to Create a Component?](#when-to-create-component)
    * [When not to Create a Component?](#when-not-to-create-component)
* [Requirements](#requirements)
* [Dependencies](#dependencies)
* [Code building](#code-building)
* [Running Demo](#demo)
* [License](#license)
* [Contributing](#contributing)
* [Support Us](#support-us)

## Overview <a name="overview"></a>

MVVM4FX reimagines the `Model`–`View`–`ViewModel` pattern for JavaFX as a component-based, extensible platform designed
around clarity, modularity, and the KISS principle. Each component exists as a self-contained unit composed of a `View`,
`ViewModel` and `Descriptor`, optionally extended with `Composer` and `History`.

The framework enforces a strict separation between presentation, logic, and identity. The `View` defines the visual
structure and behavior; the `ViewModel` encapsulates logic and state; the `Descriptor` holds the component’s technical
identity; the `Composer` is responsible for managing child components and their composition; and the
`History` preserves continuity across sessions.

At its core, MVVM4FX follows the KISS principle – every class, method, and abstraction exists only for a clear reason,
avoiding unnecessary complexity or dependencies. This simplicity is deliberate: it keeps the architecture transparent,
predictable, and easy to extend.

By combining conceptual clarity with structural discipline, MVVM4FX achieves both architectural purity and practical
flexibility — a balance where components remain independent yet fully interoperable. It is not a minimalistic
abstraction but a complete design system for building coherent, maintainable, and intelligent JavaFX applications.

## Features <a name="features"></a>

Key features include:

* Support for the component lifecycle.
* Organization of core tasks within the view.
* Component inheritance.
* Ability to preserve component history.
* Designed without considering FXML support.
* Support for component-level logging.
* Detailed documentation and sample code.

## MVVM <a name="mvvm"></a>

### What is MVVM? <a name="what-is-mvvm"></a>

MVVM (`Model`-`View`-`ViewModel`) is an architectural pattern that divides an application's logic into three main parts:
`Model`, `View`, and `ViewModel`.

`Model` — encapsulates the data and business logic of the application. `Model`s represent an abstraction that stores and
processes the application’s data, including all business logic rules and data validation logic. `Model`s do not interact
with the UI and do not know about `View` or `ViewModel`. Instead, they provide data and perform actions related to the
business logic. `Model` can include:

* Data (for example, entities from a database or objects obtained from external sources).
* Business logic (such as data processing rules, calculations, data manipulation).
* Validation logic (for example, checks that are performed before saving data).

`View` — represents the user interface that displays the data. The `View`'s task is to contain UI elements and bind their
state to the `ViewModel`. `View` is responsible for displaying data and interacting with the user, but it should not
contain logic for managing the state of these elements. Because it is the responsibility of the `ViewModel` to control
this state without knowing about specific controls in the `View`. For example, if the `ViewModel` indicates that a button
should be active or inactive, the `View` will update the control, but the `View` will not manage the logic that determines
when the button should be enabled or disabled.

Besides, the `View` may and should contain logic related to the visual behavior and layout of elements (presentation
logic). This includes calculating positions and sizes, managing component arrangement (e.g., docking or resizing),
handling animations, drag-and-drop operations, or other view-related interactions that depend on specific UI components.

`ViewModel` — manages the state of UI elements without needing to know the implementation details of the user interface.
`ViewModel` can also serve as a layer between the `View` and `Model`, obtaining data from the `Model` and preparing it for
display in the `View`. It can transform the data from the model into a format suitable for UI presentation.

### MVVM Advantages <a name="mvvm-advantages"></a>

* Separation of concerns. MVVM helps to clearly separate the presentation logic (`View`), business logic and data
(`Model`), and interaction logic (`ViewModel`). This simplifies code maintenance and makes it more readable.

* Testability. The `ViewModel` can be tested independently of the user interface (UI) because it is not tied to specific
visual elements. This makes it easy to write unit tests for business logic.

* Two-way data binding. In MVVM, data is automatically synchronized between the `View` and `ViewModel`, which reduces the
amount of code required for managing UI state and simplifies updates.

* Simplification of complex UIs. When an application has complex UIs with dynamic data, MVVM helps make the code more
understandable and structured, easing management of UI element states.

* UI updates without direct manipulation. The `ViewModel` manages updates to the `View` via data binding, avoiding direct
manipulation of UI elements. This makes the code more flexible and scalable.

## Component <a name="component"></a>

### What is a Component? <a name="what-is-component"></a>

A component is a fundamental, self-contained building block of a user interface (UI) that provides a specific
piece of functionality and enables user interaction. A component represents a higher-level abstraction than standard
UI controls, fundamentally distinguished by its compositional nature, which encompasses and organizes multiple
UI controls, its managed lifecycle, and its capacity to maintain state history. Crucially, while usually components
also encapsulate business logic, this is not a mandatory trait for all, as structural components like layout containers
demonstrate.

### Component Structure <a name="component-structure"></a>

A component always consists of at least two classes: a `ComponentViewModel` and a `ComponentView`. A natural question
might arise: why is there no `Model` in the component, given that the pattern is called MVVM? Firstly, a component
is a building block for constructing a user interface, which might not be related to the application's business logic
at all. Secondly, the `Model` exists independently of the UI and should have no knowledge of the component's existence.
Thirdly, MVVM is fundamentally about the separation of responsibilities rather than the mandatory presence of all
three layers in every element. In other words, a component does not violate MVVM principles simply because it lacks a
`Model`; it remains compliant as long as the `View` and `ViewModel` maintain a clear separation of concerns and
communicate exclusively through data binding and observable properties.

In addition to the `ComponentViewModel` and `ComponentView`, a component always has a `ComponentDescriptor` (which
is provided by the framework and normally does not require custom implementation) and may include three optional
classes: `ComponentHistory`, `ComponentComposer`, `ComponentMediator`.

The `ComponentDescriptor` represents the internal metadata and platform-level state of a component. The descriptor
acts as a technical identity card, containing all framework-related information while keeping it completely separate
from business data. In other words, the purpose of this class is to ensure that internal component data does not mix
with business data within the `ViewModel`.

The `ComponentHistory` enables the preservation of the component’s state across its lifecycle. Data exchange occurs
exclusively between the `ComponentViewModel` and the `ComponentHistory`. When the component’s state transitions to
`INITIALIZING`, data is restored from the `ComponentHistory` to the `ComponentViewModel`. Conversely, when the state
transitions to `DEINITIALIZED`, data from the `ComponentViewModel` is saved back to the `ComponentHistory`. The volume
of state information that is restored and persisted is defined by the `HistoryPolicy` enum.

The `ComponentComposer` is responsible for managing child components and their composition, while the `ComponentMediator`
allows `ComponentViewModel` to interact with the `ComponentComposer` (see [Composite Component](#composite-component)).

### Component Lifecycle <a name="component-lifecycle"></a>

A component has five distinct states (see `ComponentState`):

| **State**          | **Description**                                                                                                                                                                                                                                                                                                                                               |
| ------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Creating**       | The component is currently being created. During this phase, both the `ComponentViewModel` and the `ComponentView` objects are being constructed, but initialization has not yet started.                                                                                                                                                                     |
| **Initializing**   | The component is in the process of initialization. This phase begins when the `ComponentView#initialize()` method is invoked, during which bindings, listeners, and other setup logic are established. When the component transitions to this state, the `ComponentViewModel` restores its state from the `ComponentHistory`.                                 |
| **Initialized**    | The component has been fully initialized and is ready for use. It enters this state upon completion of the `ComponentView#initialize()` method, but before the call to the `AbstractComponentView#postInitialize()` method.                                                                                                                                   |
| **Deinitializing** | The component is in the process of deinitialization. This phase begins when the `ComponentView#deinitialize()` method is invoked, during which bindings are removed, listeners are detached, and cleanup logic is performed.                                                                                                                                  |
| **Deinitialized**  | The component has been completely deinitialized and can no longer be used. It enters this state upon completion of the `ComponentView#deinitialize()` method, but before the call to the `AbstractComponentView#postDeinitialize()` method. When the component transitions to this state, the `ComponentViewModel` saves its state to the `ComponentHistory`. |

Each component features `ComponentView#initialize()` and `ComponentView#deinitialize()` methods, which initialize and
deinitialize the component, respectively, altering its state. The default implementation of these methods in
`AbstractComponentView` is achieved through template methods that handle component building/unbuilding, binding/unbinding,
adding/removing listeners, and adding/removing handlers via corresponding protected methods. It is important to note
that these protected methods should not be considered the exclusive location for performing such tasks (e.g.,
adding/removing handlers) within the component, but rather as part of the initialization/deinitialization process.
Thus, adding/removing handlers may also be performed in other methods of the component.

### Component Hierarchy <a name="component-hierarchy"></a>

Components can act as both parents and children, forming a tree structure that can change dynamically.
The library provides a mechanism for dynamically creating and removing components and includes optional logic
for managing component relationships, leaving their use to the developer's discretion.

The component tree is built according to the Unidirectional Hierarchy Rule (UHR). This rule establishes a strict
hierarchical order by explicitly prohibiting circular parent-child relationships, meaning a component cannot
simultaneously be a direct parent and a direct child of another component. The UHR is designed to maintain a clear,
acyclic structure, which prevents logical conflicts and ensures predictable behavior. Importantly, this rule does not
restrict child components from directly accessing or communicating with their parents; it solely forbids cyclical
dependencies that would compromise the architectural integrity of the hierarchy.

It is crucial to highlight the interaction between components. Consider a parent and a child component as an example.
The parent component's `ComponentViewModel` holds a reference to the child component's `ComponentViewModel` via its
`children` field, while the child component's `ComponentViewModel` holds a reference to the parent component's
`ComponentViewModel` via its `parent` field. Similarly, the parent component's `ComponentView` holds a reference to the
child component's `ComponentView` through its `children` field, and the child component's `ComponentView` holds a
reference to the parent component's `ComponentView` via its `parent` field.

This two-layer linkage establishes a coherent and symmetric relationship between parent and child components at both
the `View` and `ViewModel` layers. The parent and child components are fully aware of each other's existence and state,
enabling direct coordination and communication within the hierarchy while maintaining clear separation of concerns
between the presentation (`View`) and logic (`ViewModel`) layers. This design ensures consistency and synchronization
across the component tree without violating the Unidirectional Hierarchy Rule (UHR), as the relationships are strictly
hierarchical and non-cyclic.

### Composite Component <a name="composite-component"></a>

Components can be either simple or composite. A simple component has no child components. A composite component has
one or more child components. The use of `Composer` and `Mediator` is required only for components that manage children
or dynamically create other components, such as dialogs, panels, or complex containers. Working with a composite
component is one of the most challenging parts of using the platform for the following reasons:

1. MVVM Gap. MVVM does not specify how child components should be created, how their lifecycle should be managed, or
how they should be composed.
2. Architectural Conflict. According to MVVM, the `ViewModel` must not know about the `View`, yet the `ViewModel` may
need to initiate the creation of new components (for example, opening a dialog) and their composition — which is
impossible without interacting with the `View`.
3. Implementation Complexity. Due to the two-layer structure of a component (`View` and `ViewModel`), each of them requires
its own version of a composer, which doubles the complexity of the problem. In addition, naming becomes difficult,
since names like `SomeComponentViewComposer` and `SomeComponentViewModelComposer` are hardly convenient to work with.
4. Inheritance Challenges. Supporting component inheritance, where hierarchies of all classes of inherited components
must be created: `ChildView` extends `ParentView`, `ChildViewModel` extends `ParentViewModel`, `ChildComposer` extends
`ParentComposer` etc.

In MVVM4FX, the solution for working with composite components is implemented using two classes: `Composer` and `Mediator`:

`Mediator`. This is the interface that the `ViewModel` uses to interact with the `Composer`. The need for an
interface is driven by two factors: first, it allows the `ViewModel` to be tested independently of other components;
second, the `Composer` must know about both the `View` and the `ViewModel`, while the `ViewModel` must not know about
the `View`.

```java
public interface FooMediator extends ChildMediator {

    ...
}

public class FooViewModel extends AbstractChildViewModel {

    ...

    @Override
    public FooMediator getMediator() {
        return (FooMediator) super.getMediator();
    }
}
```

`Composer`. This class contains the methods that manage the entire lifecycle of child components, as well as the
methods the `View` uses to interact with the `Composer`. In addition, it defines a non-static inner class that
implements the corresponding `Mediator`.

```java
public class FooComposer extends AbstractChildComposer<FooView> {

    protected class Mediator extends AbstractChildComposer.Mediator implements FooMediator {...}

    ...

    @Override
    protected FooMediator createMediator() {
        return new FooComposer.Mediator();
    }
}

public class FooView extends AbstractChildView<FooViewModel> {

    ...

    @Override
    public FooComposer getComposer() {
        return (FooComposer) super.getComposer();
    }

    @Override
    protected FooComposer createComposer() {
        return new FooComposer(this);
    }
}
```

Composer Creation and Initialization. The `Composer` is created during the component’s pre-initialization phase via
the protected `AbstractComponentView#createComposer()` method. Creating the composer at this stage ensures that both
the `View` and the `ViewModel` are fully constructed, allowing the composer to immediately access and interact with
their properties and methods. The composer is also initialized during the pre-initialization phase, while its
deinitialization takes place in the component’s post-deinitialization phase.

Advantages of this approach:

* Strict Separation. Using a `Composer` together with a `Mediator` enforces a clear separation of layers according to
MVVM and simplifies testing.
* Clean Architecture. The `Composer` centralizes all logic related to managing child components, keeping the
`View` and `ViewModel` free from responsibilities that do not belong to them.
* MVVM Compliance. The `Mediator` interface defines how a `ViewModel` can initiate the addition or removal of a
component without violating MVVM principles.

### When to Create a Component? <a name="when-to-create-component"></a>
* The element has independent testable state or business logic that can exist without a `View`.
* The element has a distinct lifecycle requiring separate initialization/deinitialization, or can be dynamically
added/removed.
* The element is potentially reusable across different contexts (e.g., dialogs, toolbars, multiple editor types).
* Multiple closely related properties form a logical unit - grouping them into a separate component improves
maintainability and reduces parent component complexity.
* The element manages structural composition - it contains child components or forms an independent subtree
(e.g., containers, tabs, panels).
* State persistence is required - the element needs its own `History` to save and restore state between sessions.

### When not to Create a Component? <a name="when-not-to-create-component"></a>
* The element’s `ViewModel` would contain no meaningful behavior or data - making the component redundant.
* The element represents a minor visual part of the interface and does not require its own logic or state.
* The element is simple enough that separating it into its own component would add unnecessary complexity rather
than improving clarity.

## Requirements <a name="requirements"></a>

Java 11+ and JavaFX 19.

## Dependencies <a name="dependencies"></a>

The project will be added to the Maven Central repository in a few days.

## Code Building <a name="code-building"></a>

To build the library use standard Git and Maven commands:

    git clone https://github.com/techsenger/mvvm4fx
    cd mvvm4fx
    mvn clean install

## Running Demo <a name="demo"></a>

To run the demo execute the following commands in the root of the project:

    cd mvvm4fx-demo
    mvn javafx:run

Please note, that debugger settings are in `mvvm4fx-demo/pom.xml` file.

## License <a name="license"></a>

Techsenger MVVM4FX is licensed under the Apache License, Version 2.0.

## Contributing <a name="contributing"></a>

We welcome all contributions. You can help by reporting bugs, suggesting improvements, or submitting pull requests
with fixes and new features. If you have any questions, feel free to reach out — we’ll be happy to assist you.

## Support Us <a name="support-us"></a>

You can support our open-source work through [GitHub Sponsors](https://github.com/sponsors/techsenger).
Your contribution helps us maintain projects, develop new features, and provide ongoing improvements.
Multiple sponsorship tiers are available, each offering different levels of recognition and benefits.
