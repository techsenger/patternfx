# Techsenger MVVM4FX

Techsenger MVVM4FX is a compact, practical framework for building JavaFX applications with the MVVM pattern.
It provides practical solutions to the most challenging problems of MVVM, including dynamic component composition,
lifecycle management, and component state ownership. The framework supplies all necessary interfaces and base class
implementations for creating components, which serve as the fundamental units of the MVVM pattern. Examples of
components include tabs, dialog windows, toolbars, image viewers, help pages, and more.

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
    * [Component Tree](#component-tree)
    * [Imperative Component Management](#component-imperative)
    * [Component Code Example](#component-code)
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

MVVM4FX reimagines the Model–View–ViewModel pattern as a component-based framework designed around clarity, modularity,
and the KISS principle for building complex, dynamic JavaFX applications. It addresses the most fundamental limitation
of classical MVVM — dynamic component composition and lifecycle management — by introducing an explicit, imperative
component layer responsible for the creation, ownership, and lifetime of components. Each `Component` exists as a
self-contained architectural unit composed of a `ComponentView`, `ComponentViewModel`, and `ComponentMediator`,
optionally augmented with `ComponentHistory`.

The framework enforces a strict separation of responsibilities:
- `Component` defines the identity and manages lifecycle, initialization/deinitialization, and composition of child
components;
- `ComponentView` defines the visual structure and behavior;
- `ComponentViewModel` encapsulates logic and state;
- `ComponentMediator` provides a controlled interaction channel between the `ComponentViewModel` and the `Component`;
- `ComponentHistory` preserves continuity across sessions.

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

A component always consists of at least four classes: a `Component`, a `ComponentView`, a `ComponentViewModel`
and `ComponentMediator`. A natural question might arise: why is there no `Model` in the component, given that
the pattern is called MVVM?

Firstly, a component is a building block for constructing a user interface, which might not be related to the
application's business logic at all. Secondly, the `Model` exists independently of the UI and should have no knowledge
of the component's existence. Thirdly, MVVM is fundamentally about the separation of responsibilities rather than
the mandatory presence of all three layers in every element. In other words, a component does not violate MVVM
principles simply because it lacks a `Model`; it remains compliant as long as the `View` and `ViewModel` maintain a
clear separation of concerns and communicate exclusively through data binding and observable properties.

The `ComponentView` and `ComponentViewModel` classes correspond to the `View` and `ViewModel` in the MVVM pattern and
are relatively straightforward. The `Component` and `ComponentMediator` classes, on the other hand, address the
aspects that MVVM does not cover and are therefore more complex, which is why they are explained in detail below.

The `Component` forms a very thin, structural layer of a higher order than the `View`, which allows it to add child
components to its `View`. A `Component` always operates strictly at the component level and deliberately does not take
initiative. Its sole responsibility is to perform operations requested by its clients—either directly or via the
`Mediator`. For example, it can create a child component and place it in its `View`, but only when the `ViewModel`
commands it to do so through the `Mediator`. Since the `Component` has the greatest capabilities, it is important to
remember that its responsibilities are very limited, to prevent the `Component` from turning into a God object and
violating MVVM responsibility principles.

The `Component` is responsible for:

1. Initializing and deinitializing the component.
2. Providing component data and related objects that directly belong to the component:
   - Structural data (parent/children references);
   - Persistence data (component history);
   - Configuration data (component settings);
   - Lifecycle data (component state);
   - Metadata (component ID, type, version, etc.).
3. Creating, initializing, adding to the component tree, removing from the component tree, and deinitializing
   child components (those that reside directly inside this component). It can also add or remove child components in
   its `View`.
4. Creating, initializing, and passing derived components to other components for further management
   (e.g., dialogs, tabs, system notifications).

The `ComponentMediator` is the interface that the `ViewModel` uses to interact with the `Component`. This interface
is needed for two reasons: first, it allows the `ViewModel` to be tested independently; second, it allows the
`ViewModel` to use the `Component` without knowing the View, since the Component has knowledge of the View.

The `ComponentMediator` is implemented as a non-static inner class within the `Component`, which allows it to work with
both the `View` and the `ViewModel` without violating MVVM principles.

Working with a `Component` and `ComponentMediator` is one of the most challenging parts of using the platform for the
following reasons:

1. MVVM Gap. MVVM does not specify how child and derived components should be created, how their lifecycle should be
managed, or how they should be composed.
2. Architectural Conflict. According to MVVM, the `ViewModel` must not know about the `View`, yet the `ViewModel` may
need to initiate the creation of new components (for example, opening a dialog) and their composition — which is
impossible without interacting with the `View`.
3. Implementation Complexity. Due to the two-layer structure of a component (`View` and `ViewModel`), each of them requires
its own version of a `Component`, which doubles the complexity of the problem. In addition, naming becomes difficult,
since names like `FooViewComponent` and `FooViewModelComponent` are hardly convenient to work with.
4. Inheritance Challenges. Supporting component inheritance, where hierarchies of all classes of inherited components
must be created: `ChildView` extends `ParentView`, `ChildViewModel` extends `ParentViewModel`, `ChildComponent` extends
`ParentComponent` etc.

Advantages of this approach:

* Strict Separation. Using a `Component` together with a `Mediator` enforces a clear separation of layers according to
MVVM and simplifies testing.
* Clean Architecture. The `Component` centralizes all logic related to managing child components, keeping the
`View` and `ViewModel` free from responsibilities that do not belong to them.
* MVVM Compliance. The `Mediator` interface defines how a `ViewModel` can initiate the addition or removal of a
component without violating MVVM principles.

In addition to the four classes, a component may include a `ComponentHistory`. The `ComponentHistory` enables the
preservation of the component’s state across its lifecycle. Data exchange occurs exclusively between the
`ComponentViewModel` and the `ComponentHistory`. When the component’s state transitions to `INITIALIZING`, data is
restored from the `ComponentHistory` to the `ComponentViewModel`. Conversely, when the state transitions to
`DEINITIALIZED`, data from the `ComponentViewModel` is saved back to the `ComponentHistory`. The volume of state
information that is restored and persisted is defined by the `HistoryPolicy` enum.

### Component Lifecycle<a name="component-lifecycle"></a>

Each component features `Component#initialize()` and `Component#deinitialize()` methods,
which initialize and deinitialize all the parts of the component, respectively, updating its state.

In the default implementation during initialization, the component first enters the pre-initialization phase, where
the `ComponentMediator` is created, attached to the `ViewModel`, and the component’s history is restored. After that,
the main initialization phase begins, during which the `ViewModel` and `View` perform their own internal initialization.
Once both parts are initialized, the component completes the process with a post-initialization phase that can be used
for any additional logic specific to the component.

Deinitialization follows the same structure in reverse. It begins with a pre-deinitialization phase, then proceeds to
the main deinitialization of the `View` and `ViewModel` (reverse order), and finishes with a post-deinitialization
phase. By default, the component saves its history at this final stage.

Both AbstractComponentView and AbstractComponentViewModel provide protected initialize() and deinitialize() methods
that are automatically invoked during the lifecycle, allowing each part to perform its own work without breaking
the architectural boundaries. The optional pre and post hooks in `AbstractComponent` give developers additional
flexibility to extend the lifecycle while preserving its structure. This design keeps the component's behavior
predictable, transparent, and easy to customize.

The default implementation of the `AbstractComponentView#initialize()` and `AbstractComponentView#deinitialize()` methods
is split into four protected methods that perform the core `View` operations. These protected methods may be overridden
and are responsible for the following:

- building/unbuilding
- binding/unbinding
- adding/removing listeners
- adding/removing handlers

It is important to note that these protected methods should not be considered the only place for performing such tasks
(e.g., adding or removing handlers) within the `View`; rather, they represent one part of the
initialization/deinitialization process. Thus, such tasks may also be performed in other methods.

A component has five distinct states (see `ComponentState`):

| **State**          | **Description** |
|--------------------|-----------------|
| **CREATING**       | The component is being constructed. The `ComponentViewModel`, `ComponentView`, and `Component` objects exist, but initialization has not yet begun. This is the earliest detectable phase of the lifecycle. |
| **INITIALIZING**   | The component is undergoing initialization. Its `ComponentViewModel`, `ComponentView`, and other internal parts are being initialized. |
| **INITIALIZED**    | The component has been fully initialized. The component, its view, and its view-model are active, bound, and synchronized, and the component is ready for use. |
| **DEINITIALIZING** | The component is undergoing deinitialization. Its `ComponentView`, `ComponentViewModel`, and other internal parts are being deinitialized. |
| **DEINITIALIZED**  | The component has been completely deinitialized. All resources have been released and cleanup has been performed. This is the terminal state of the lifecycle. |

### Component Tree <a name="component-tree"></a>

Components in MVVM4FX form a hierarchical structure, called the component tree that can change dynamically. This tree
represents the logical composition of the application and is independent of the JavaFX node tree, which is
responsible only for rendering.

Each `Component` may have a parent component and multiple child components. Together, they form a directed,
acyclic structure that reflects ownership, lifecycle management, and state boundaries rather than visual layout.

The component tree must not be confused with the JavaFX scene graph. The JavaFX node tree describes how UI elements
are rendered and laid out on screen. The component tree describes how application functionality is structured,
initialized, composed, and disposed. These two hierarchies serve different purposes and are intentionally decoupled.

The component tree is built according to the Unidirectional Hierarchy Rule (UHR). This rule establishes a strict
hierarchical order by explicitly prohibiting circular parent-child relationships, meaning a component cannot be both a
direct or indirect parent and child of another component. The UHR is designed to maintain a clear, acyclic structure,
which prevents logical conflicts and ensures predictable behavior. Importantly, this rule does not restrict child
components from directly accessing or communicating with their parents; it solely forbids cyclical dependencies that
would compromise the architectural integrity of the hierarchy.

It is important to note that the component layer is intentionally designed to be thin. A `Component` must not contain
business logic, presentation logic, or state manipulation beyond what is required for lifecycle management and
structural composition. Its responsibility is limited to coordinating initialization and deinitialization, managing
parent–child relationships, and defining ownership boundaries between components.

Keeping the component layer thin prevents it from becoming a God object and ensures that application logic remains
properly distributed between the View and the ViewModel. This constraint is essential for preserving architectural
clarity, testability, and long-term maintainability.

### Imperative Component Management<a name="component-imperative"></a>

There are two main approaches to managing UI components: declarative and imperative. Each has its own strengths and
weaknesses.

MVVM4FX adopts the imperative approach. In this approach, components are explicitly created, initialized, added to
the component tree, and deinitialized by the developer. This choice leads to the following characteristics:

Strengths:
- Clear ownership and responsibility boundaries for components.
- Predictable and transparent initialization and deinitialization order.
- Full control over component lifecycle and composition.
- Natural support for dynamic UI scenarios (e.g., tabs, dialogs, docking layouts).
- Reliable state persistence and restoration via component history.
- Strict separation of concerns between `Component`, `ComponentView`, and `ComponentViewModel`.

Weaknesses:
- Requires boilerplate code (though it is limited because components are typically large blocks such as editors,
tabs, dialogs, or search panels).
- Higher initial learning curve for developers new to the framework.
- Careful design discipline needed to prevent overly complex or "God" components.

This approach ensures that MVVM4FX components behave predictably, remain testable, and can support complex,
long-living, dynamic UI applications.

### Component Code Example<a name="component-code"></a>

This example demonstrates the creation of a Foo component that dynamically adds a child Bar component.

`ComponentMediator` interface:

```java

public interface FooMediator extends ChildMediator {

    void addBar(BarViewModel bar);
}
```

`ComponentViewModel` class:

```java
public class FooViewModel extends AbstractChildViewModel<FooMediator> {

    public FooViewModel() {
        ...
    }

    public void doSomething() {
        var bar = new BarViewModel();
        ... // set up the bar
        getMediator().addBar(bar);
    }

    ...
}
```

`ComponentView` class:

```java
public class FooView extends AbstractChildView<FooViewModel, FooComponent> {

    public FooView(FooViewModel viewModel) {
        ...
    }

    protected void initialize() {
        logger.debug("{} View is initializing", getComponent().getLogPrefix());
    }
    ...
}
```

`Component` class:

```java
public class FooComponent extends AbstractChildComponent<FooView> {

    protected class Mediator extends AbstractChildComponent.Mediator implements FooMediator {

        @Override
        public void addBar(BarViewModel vm) {
            var v = new BarView(vm);
            var c = new BarComponent(v);
            c.initialize();
            addChild(c);
            getView.addSomewhere(v); // adding bar view into foo view
        }
    }

    public FooComponent(FooView view) {
        ...
    }

    ...

    @Override
    protected FooMediator createMediator() {
        return new FooComponent.Mediator(); // the mediator is created at the beginning of initialization
    }
}
```

This code demonstrates how to create the foo component instance:

```java
var viewModel = new FooViewModel();
var view = new FooView(viewModel);
var component = new FooComponent(view);
component.initialize();
... // use the component
component.deinitialize();
```

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

This project is available on Maven Central:

```
<dependency>
    <groupId>com.techsenger.mvvm4fx</groupId>
    <artifactId>mvvm4fx-core</artifactId>
    <version>${mvvm4fx.version}</version>
</dependency>
```

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
