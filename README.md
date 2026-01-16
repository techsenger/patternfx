# Techsenger PatternFX

PatternFX is a compact, practical, component-oriented, and modular framework that provides architectural pattern
templates for building JavaFX applications. Each template is a concrete implementation of a specific architectural
pattern, comes with its own strengths and weaknesses, and users can choose the one that best fits their application.
Using multiple templates within a single application is not supported.

All templates provide a complete implementation required for building complex applications with dynamic composition
and are intended for practical, real-world use. For each template, a demo consisting of three components is provided,
demonstrating its practical usage.

The main feature of PatternFX is its application model, which represents an application as a dynamically modifiable
tree of components where a component is a fundamental, self-contained building block of a user interface that
provides a specific piece of functionality and enables user interaction. This approach enables a clear and consistent
structure, predictable dynamic composition, and controlled lifecycle management.

As a real example of using this framework, see [TabShell](https://github.com/techsenger/tabshell) project.

## Table of Contents
* [Overview](#overview)
* [Features](#features)
* [Component](#component)
    * [Component Lifecycle](#component-lifecycle)
    * [Component Tree](#component-tree)
    * [Imperative Component Management](#component-management)
    * [Component History](#component-history)
    * [Component Logging](#component-logging)
    * [When to Create a Component?](#component-when-to-create)
    * [When Not to Create a Component?](#component-when-not-to-create)
* [MVVM Pattern](#mvvm-pattern)
    * [MVVM Overview](#mvvm-pattern-overview)
    * [MVVM Advantages](#mvvm-pattern-advantages)
    * [MVVM Disadvantages](#mvvm-pattern-disadvantages)
* [MVVM Template](#mvvm-template)
    * [Component Structure](#mvvm-template-structure)
    * [Component Lifecycle](#mvvm-template-lifecycle)
    * [Component Code Example](#mvvm-template-code)
* [MVVMX Template](#mvvmx-template)
    * [Component Structure](#mvvmx-template-structure)
    * [Component Lifecycle](#mvvmx-template-lifecycle)
    * [Component Code Example](#mvvmx-template-code)
* [Requirements](#requirements)
* [Dependencies](#dependencies)
* [Code building](#code-building)
* [Running Demo](#demo)
* [License](#license)
* [Contributing](#contributing)
* [Support Us](#support-us)

## Overview <a name="overview"></a>

Today, there are various architectural patterns available for developing JavaFX applications, which developers can
choose depending on their goals and preferences. At the same time, when building real-world applications, developers
often face challenges that standard patterns do not fully address. These include:

1. Storing metadata for a component.
2. Managing the lifecycle of a component.
3. Dynamically creating and removing components.
4. Dynamically composing and decomposing components.
5. Maintaining references to parent and child components.
6. Component inheritance.
7. Application structure when the application consists of multiple components.
8. Preserving and restoring a component's history.

While these issues may not arise when developing simple applications, they become critical when building complex
applications.

The templates in PatternFX are specifically designed to address these challenges. Each template provides its own
solution to the above problems, with its own advantages and trade-offs — as is well known, there is no silver bullet.
It is up to the developer to choose the solution that best fits their needs.

At its core, PatternFX follows the KISS principle – every class, method, and abstraction exists only for a clear reason,
avoiding unnecessary complexity or dependencies. This simplicity is deliberate: it keeps the architecture transparent,
predictable, and easy to extend.

## Features <a name="features"></a>

Key features include:

* Support for the component lifecycle.
* Models the application as a component tree.
* Provides all necessary methods for working with a component tree.
* Organizes core tasks within the view.
* Supports component inheritance.
* Enables preserving component history.
* Provides component-level logging support.
* Designed without FXML dependency.
* Includes a demo for each template demonstrating its usage.
* Comprehensive documentation.

## Component <a name="component"></a>

PatternFX provides templates for different architectural patterns. In addition, even within a single pattern, multiple
templates may exist, each with its own set of constituent classes. For these reasons, the term `component` is
introduced to describe a higher-level abstraction than standard UI controls, fundamentally distinguished by its
compositional nature, which encompasses and organizes multiple UI controls, its managed lifecycle, and its capacity
to maintain state history.

For example, consider an application that uses the MVC pattern and contains an editor and a search panel that is
dynamically added and removed. In this case, there are two components. The first component includes the classes
`EditorView`, `EditorController`, while the second includes `SearchView`, `SearchController`.

A natural question might arise: why is there no `Model` in the component? Firstly, a component is a building block
for constructing a user interface, which might not be related to the application's business logic at all. Secondly,
the `Model` exists independently of the UI and should have no knowledge of the component's existence.

### Component Lifecycle<a name="component-lifecycle"></a>

The component lifecycle defines the process and order of initialization and deinitialization of a component, as well
as of its child components in the case of a composite component. Violations of the lifecycle may lead to issues such
as failure to restore or persist state, unreleased resources resulting in memory leaks, and incorrect component behavior
(for example, required bindings not being established).

Due to the importance of lifecycle management, all templates provided by the framework define the methods `initialize()`
and `deinitialize()`. These methods serve as the primary mechanisms for controlling the component lifecycle
and its `State`. The internal implementation of these methods is defined by the selected template.

Each component has five distinct states (see `State`):

| **State**          | **Description** |
|--------------------|-----------------|
| **CREATING**       | The component is being constructed; some or all objects exist, but the component has not yet been initialized. |
| **INITIALIZING**   | The component is undergoing initialization. |
| **INITIALIZED**    | The component has been fully initialized and is ready for use. |
| **DEINITIALIZING** | The component is undergoing deinitialization. |
| **DEINITIALIZED**  | The component has been completely deinitialized; all resources have been released and cleanup has been performed. |

### Component Tree <a name="component-tree"></a>

Components in PatternFX form a hierarchical structure, called the component tree that can change dynamically. This tree
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

### Imperative Component Management<a name="component-management"></a>

There are two main approaches to managing UI components: declarative and imperative. Each has its own strengths and
weaknesses.

PatternFX adopts the imperative approach. In this approach, components are explicitly created, initialized, added to
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

This approach ensures that PatternFX components behave predictably, remain testable, and can support complex,
long-living, dynamic UI applications.

### Component History <a name="component-history"></a>

`History` preserves the component’s state across its lifecycle. In the default implementation, the `History`
instance is lazily provided via a `HistoryProvider` that is set before initialization. During the initialization phase,
the provider’s `provide()` method is called to obtain the history. This allows the provider to be overridden in
subclasses without retrieving a history instance, which may be an expensive operation. After the history is obtained,
the provider is cleared (set to null), and the component uses the history. State restoration occurs in the
deinitialization phase. The volume and type of state information that is restored and persisted are determined
by the `HistoryPolicy` enum.

### Component Logging <a name="component-logging"></a>

PatternFX supports component-scoped logging, allowing log messages to be produced in the context of a specific
component instance rather than only at the class or subsystem level. This approach is especially useful in complex and
dynamic applications where multiple instances of the same component type may exist simultaneously (for example, tabs,
dialogs, editors, or background components). Component-scoped logging makes it possible to precisely identify the
exact source of a log message and greatly simplifies debugging and diagnostics.

Each component exposes a log prefix that uniquely identifies its instance. The way this prefix is obtained depends
on the template implementation. The framework also allows customization of the log prefix both at the template level
and for individual component instances.

### When to Create a Component? <a name="component-when-to-create"></a>
* The element has independent testable state or business logic that can exist without a `View`.
* The element has a distinct lifecycle requiring separate initialization/deinitialization, or can be dynamically
added/removed.
* The element is potentially reusable across different contexts (e.g., dialogs, toolbars, multiple editor types).
* Multiple closely related properties form a logical unit - grouping them into a separate component improves
maintainability and reduces parent component complexity.
* The element manages structural composition - it contains child components or forms an independent subtree
(e.g., containers, tabs, panels).
* State persistence is required - the element needs its own `History` to save and restore state between sessions.

### When not to Create a Component? <a name="component-when-not-to-create"></a>
* The element’s `ViewModel` would contain no meaningful behavior or data - making the component redundant.
* The element represents a minor visual part of the interface and does not require its own logic or state.
* The element is simple enough that separating it into its own component would add unnecessary complexity rather
than improving clarity.

## MVVM Pattern <a name="mvvm-pattern"></a>

### MVVM Overview <a name="mvvm-pattern-overview"></a>

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

### MVVM Advantages <a name="mvvm-pattern-advantages"></a>

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

### MVVM Disadvantages <a name="mvvm-pattern-disadvantages"></a>

* The need to mirror UI state in the ViewModel. JavaFX nodes already contain their own state (properties like selected,
disabled, and text). MVVM requires creating parallel state in the ViewModel and synchronizing it through data binding.
This creates redundancy: the same state exists in two places—natively in the View's nodes and explicitly in the
ViewModel's properties. It is important to note that this state must be explicitly exposed in the `ViewModel`
due to MVVM's architecture, and is not required for any other purpose in JavaFX (unlike mobile application development,
where it may be necessary to store state separately from the `View`). To illustrate, imagine you have a
`ToggleButton foo` in the `View`. Then, in the `ViewModel`, you might have:

```java
BooleanProperty fooDisabled = new SimpleBooleanProperty();
StringProperty fooText = new SimpleStringProperty();
BooleanProperty fooSelected = new SimpleBooleanProperty();

+ 3 property accessors, 3 setters, 3 getters
```
* Some changes to the View are difficult to propagate through state. This occurs when JavaFX provides only read-only
properties or special methods for performing actions, or when using controls from third-party libraries.

## MVVM Template <a name="mvvm-template"></a>

<img width="1211" height="537" alt="PatternFX MVVM" src="https://github.com/user-attachments/assets/72e4465b-dac7-4320-92ed-247f7a062068" />

In this template, additional tasks are distributed between the `View` and `ViewModel`. For tasks that cannot be
performed without creating an additional element, a `Composer` is introduced:

| **Task**                                              | **Responsible**             |
|-------------------------------------------------------|-----------------------------|
| Storing component metadata                            | `Descriptor` in `ViewModel` |
| Managing the component lifecycle                      | `View`                      |
| Creating and removing components                      | `Composer`, View.Composer   |
| Composing and decomposing components                  | `Composer`, View.Composer   |
| Maintaining references to parent and child components | `View` / `ViewModel`        |
| Representing the node in the "component" tree         | `View`                      |

Thus, we can see that the `View` and `ViewModel` start to accumulate logic that does not traditionally belong to
them within a classic MVVM template. The `View`, which should operate solely at the JavaFX node level, begins to
handle component initialization, maintain references to parent and child components, and also holds a `Composer`
in its structure. As for the `ViewModel`, it gains a descriptor and stores references to parent and child components.

### Component Structure <a name="mvvm-template-structure"></a>

A component  consists of the following classes: a `View` and a `ViewModel`. In addition to them, a component
always has a `Descriptor` (which is provided by the framework and normally does not require custom implementation)
and may include a `Composer` and a `History` classes.

The `Descriptor` represents the internal metadata and platform-level state of a component. The descriptor acts as a
technical identity card, containing all framework-related information while keeping it completely separate
from business data. In other words, the purpose of this class is to ensure that internal component data does not mix
with business data within the `ViewModel`.

The `Composer` is responsible for:
1. Creating and managing child components (those that will reside directly inside this component).
2. Creating and managing derived components (those that will be provided to another component after creation,
e.g., dialogs, tabs, system notifications, etc.).

The need to create a `Composer` is explained by the fact that, according to MVVM, the `ViewModel` must not know about
the `View`. However, the `ViewModel` may need to initiate the creation of new components (for example, opening a
dialog) and their composition — which is impossible without interacting with the `View`.

This contradiction is resolved as follows: the `ViewModel` works with the `Composer` interface, which knows nothing
about the `View`, while the implementation of this interface is provided in the nested non-static class `View.Composer`.

Advantages of this approach:

* Strict Separation. Using a `Composer` enforces a clear separation of layers according to MVVM and simplifies testing.
* Clean Architecture. The `Composer` centralizes all logic related to managing child components, keeping the
`View` and `ViewModel` free from responsibilities that do not belong to them.

### Component Lifecycle <a name="mvvm-template-lifecycle"></a>

Each `View` provides the `View#initialize()` and `View#deinitialize()` methods, which initialize and deinitialize all
parts of the component respectively, updating its state.

The default implementation of these methods in `AbstractView` is based on the Template Method pattern. Initialization
and deinitialization are divided into three phases.

The first phase consists of invoking the protected methods `preInitialize()` / `preDeinitialize()`, which may be
overridden. The second phase is strictly fixed and performs the core initialization and deinitialization logic.
The third phase consists of invoking the protected methods `postInitialize()` / `postDeinitialize()`, which may also
be overridden.

The second phase is the most important one. During this phase, the `ViewModel` is initialized and deinitialized via
calls to the protected methods `AbstractViewModel#initialize()` and `AbstractViewModel#deinitialize()`, which may be
overridden. Additionally, during the second phase, the `AbstractView` itself is initialized and deinitialized by invoking
four protected methods that perform the core `View` operations. These protected methods may be overridden and are
responsible for the following:

- building/unbuilding
- binding/unbinding
- adding/removing listeners
- adding/removing handlers

It is important to note that these protected methods should not be considered the only place for performing such tasks
(e.g., adding or removing handlers) within the `View`; rather, they represent one part of the
initialization/deinitialization process. Thus, such tasks may also be performed in other methods.

The `Composer` is created and assigned to `AbstractParentViewModel` in the constructor of `AbstractParentView`.

### Component Code Example<a name="mvvm-template-code"></a>

`Composer` interface:

```java
public interface FooComposer extends Composer {

    void addBar(BarViewModel bar);

    ...
}
```

`ViewModel` class:

```java
public class FooViewModel extends AbstractChildViewModel<FooComposer> {

    public FooViewModel() {
        ...
    }

    public void doSomething() {
        var bar = new BarViewModel();
        ... // set up the bar
        getComposer().addBar(bar);
    }

    ...
}
```

`View` class:

```java
public class FooView extends AbstractChildView<FooViewModel> {

    private final class ComposerImpl implements FooComposer {

        @Override
        public void addBar(BarViewModel bar) {
            var v = new BarView(vm);
            v.initialize();
            getModifiableChildren().add(v);
            someNode.getChildren().add(v.getNode()); // adding bar view into foo view
        }
    }

    public FooView(FooViewModel viewModel) {
        ...
    }

    @Override
    protected void initialize() {
        super.initialize();
        logger.debug("{} View is initializing", getDescriptor().getLogPrefix());
    }

    @Override
    protected Composer createComposer() {
        return new ComposerImpl();
    }
    ...
}
```
This code demonstrates how to create the foo component instance:

```java
var viewModel = new FooViewModel();
var view = new FooView(viewModel);
view.initialize();
... // use the component
view.deinitialize();
```

## MVVMX Template <a name="mvvmx-template"></a>

<img width="1217" height="542" alt="PatternFX MVVMX" src="https://github.com/user-attachments/assets/21a63b51-f939-407c-b3ea-03e4f62815d7" />

In this template, additional elements — `Component` and `Mediator` — are introduced to handle all additional tasks.

| **Task**                                              | **Responsible**                    |
|-------------------------------------------------------|------------------------------------|
| Storing component metadata                            | `Component`                        |
| Managing the component lifecycle                      | `Component`                        |
| Creating and removing components                      | `Component` / `Component.Mediator` |
| Composing and decomposing components                  | `Component` / `Component.Mediator` |
| Maintaining references to parent and child components | `Component` / `Component.Mediator` |
| Representing the node in the "component" tree         | `Component`                        |

Thus, in this template, the `View` and `ViewModel` are not burdened with logic that does not belong to them, which is an
advantage. The trade-off lies in the increased complexity of the template structure due to the introduction of the
`Component` element.

### Component Structure <a name="mvvmx-template-structure"></a>

A component, as a rule, consists of the following classes: `Component` (with an inner `Mediator` implementation),
`ComponentView`, `ComponentViewModel`, and `ComponentMediator`.

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

Advantages of this approach:

* Strict Separation. Using a `Component` together with a `Mediator` enforces a clear separation of layers according to
MVVM and simplifies testing. The `Mediator` interface defines how a `ViewModel` can initiate the addition or removal of
a component without violating MVVM principles. It provides a controlled, testable channel for UI composition that
respects the pattern's constraints.
* Clean Architecture. The `Component` centralizes all logic related to managing child components, keeping the
`View` and `ViewModel` free from responsibilities that do not belong to them. This prevents `View` and `ViewModel` from
becoming bloated with lifecycle management or compositional logic. In addition, the `Component` serves as a single
source of truth for child component references. This eliminates duplication where `View` would store child `View`
references and `ViewModel` would store child `ViewModel` references. Instead, the `Component` manages the complete
child graph while exposing only appropriate references to each layer.
* Explicit Component-Level Operations. When `View` or `ViewModel` needs to interact at the component level,
it does so explicitly through `getComponent()` or `getMediator()` calls. This creates clear architectural boundaries
and makes it immediately visible when code crosses from view/view-model concerns into component management concerns.

**Important:** `Component` and `ComponentMediator` are an extension of the MVVM pattern. The MVVM pattern remains the core
of the framework and defines all key rules of operation. Whenever a developer needs functionality beyond standard MVVM,
they access the `getComponent()` and `getMediator()` methods — this immediately signals that the extension is being
used. Following this principle ensures that MVVM principles are never violated and that the framework is used correctly.

### Component Lifecycle <a name="mvvmx-template-lifecycle"></a>

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

The default implementation of the `AbstractComponentView#initialize()` and `AbstractComponentView#deinitialize()`
methods is split into four protected methods that perform the core `View` operations. These protected methods may be
overridden and are responsible for the following:

- building/unbuilding
- binding/unbinding
- adding/removing listeners
- adding/removing handlers

It is important to note that these protected methods should not be considered the only place for performing such tasks
(e.g., adding or removing handlers) within the `View`; rather, they represent one part of the
initialization/deinitialization process. Thus, such tasks may also be performed in other methods.

### Component Code Example<a name="mvvmx-template-code"></a>

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

    @Override
    protected void initialize() {
        super.initialize();
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
            getModifiableChildren().add(c);
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

## Requirements <a name="requirements"></a>

Java 11+ and JavaFX 19.

## Dependencies <a name="dependencies"></a>

This project will be available on Maven Central in a few weeks.

For MVVM template:

```
<dependency>
    <groupId>com.techsenger.patternfx</groupId>
    <artifactId>patternfx-mvvm</artifactId>
    <version>${patternfx.version}</version>
</dependency>

```

For MVVMX template:

```
<dependency>
    <groupId>com.techsenger.patternfx</groupId>
    <artifactId>patternfx-mvvmx</artifactId>
    <version>${patternfx.version}</version>
</dependency>

```

## Code Building <a name="code-building"></a>

To build the library use standard Git and Maven commands:

    git clone https://github.com/techsenger/patternfx
    cd patternfx
    mvn clean install

## Running Demo <a name="demo"></a>

To run the demo execute the following commands in the root of the project:

    cd patternfx-demo
    mvn javafx:run

Please note, that debugger settings are in `patternfx-demo/pom.xml` file.

## License <a name="license"></a>

Techsenger PatternFX is licensed under the Apache License, Version 2.0.

## Contributing <a name="contributing"></a>

We welcome all contributions. You can help by reporting bugs, suggesting improvements, or submitting pull requests
with fixes and new features. If you have any questions, feel free to reach out — we’ll be happy to assist you.

## Support Us <a name="support-us"></a>

You can support our open-source work through [GitHub Sponsors](https://github.com/sponsors/techsenger).
Your contribution helps us maintain projects, develop new features, and provide ongoing improvements.
Multiple sponsorship tiers are available, each offering different levels of recognition and benefits.
