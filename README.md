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
* [Patterns](#patterns)
    * [What is a Model?](#patterns-model)
    * [MVP Pattern](#patterns-mvp)
        * [MVP Pattern Advantages](#patterns-mvp-advantages)
        * [MVP Pattern Disadvantages](#patterns-mvp-disadvantages)
    * [MVVM Pattern](#patterns-mvvm)
        * [MVVM Pattern Advantages](#patterns-mvvm-advantages)
        * [MVVM Pattern Disadvantages](#patterns-mvvm-disadvantages)
    * [MVC vs MVP vs MVVM](#patterns-mvc-mvp-mvvm)
* [Templates](#templates)
    * [Component](#templates-component)
        * [Component Descriptor](#templates-component-descriptor)
        * [Component Lifecycle](#templates-component-lifecycle)
        * [Component Logging](#templates-component-logging)
        * [Component Types](#templates-component-types)
        * [Component Tree](#templates-component-tree)
        * [Component Composer](#templates-component-composer)
        * [Imperative Component Management](#templates-component-management)
        * [Component History](#templates-component-history)
        * [When to Create a Component?](#templates-component-when-to-create)
        * [When Not to Create a Component?](#templates-component-when-not-to-create)
    * [MVP Template](#templates-mvp)
        * [MVP Component Structure](#templates-mvp-structure)
        * [MVP Component Lifecycle](#templates-mvp-lifecycle)
        * [MVP Component Example](#templates-mvp-example)
    * [MVVM Template](#templates-mvvm)
        * [MVVM Component Structure](#templates-mvvm-structure)
        * [MVVM Component Lifecycle](#templates-mvvm-lifecycle)
        * [MVVM Component Example](#templates-mvvm-example)
* [Limitations](#limitations)
* [Requirements](#requirements)
* [Dependencies](#dependencies)
* [Code Building](#code-building)
* [Running Demo](#demo)
* [License](#license)
* [Contributing](#contributing)
* [Support Us](#support-us)

## Overview <a name="overview"></a>

Today, there are various architectural patterns available for developing JavaFX applications, such as MVC, MVP, and MVVM,
which can be chosen depending on their goals and preferences. However, when building real-world applications, developers
often encounter challenges that these patterns do not fully address. These include:

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

## Patterns <a name="patterns"></a>

MVC, MVP, and MVVM are three proven architectural patterns for JavaFX applications, each offering a distinct approach to
separating concerns and managing complexity. The fundamental difference between them lies in where the
presentation (view-related) logic is located and who is responsible for updating the UI. The way each pattern
accesses the `View` follows directly from this decision.

* In MVC, the presentation logic is concentrated in the `Controller`. The `Controller` directly manipulates the
concrete `View`, handling user input and updating UI elements. As a result, the `Controller` holds a direct reference
to the `View`.
* In MVP, the presentation logic is split between the `View` and the `Presenter`. The `View` is responsible for
passive rendering and user event forwarding, while the `Presenter` contains the main coordination logic. The `Presenter`
interacts with the `View` only through a `View` interface, which decouples it from the concrete UI implementation.
* In MVVM, the presentation logic is located in the `View` itself, expressed declaratively through bindings to the
`ViewModel`’s state. The `ViewModel` contains no UI-specific logic and has no reference to the `View`. UI updates are
performed automatically via data binding rather than explicit method calls.

In all patterns, a core principle is maintained: the `View` never directly interacts with the `Model`. All other
characteristics of these patterns—such as testability, use of bindings, and the way UI updates are performed — are
direct consequences.

### What is a Model?<a name="patterns-model"></a>

Across all architectural patterns in PatternFX, the Model represents the application's data and core business logic.
It is an independent, foundational layer that does not depend on data presentation or user interaction mechanisms.

A well-designed Model typically encompasses:

* Domain Data: The application's state and business objects (e.g., User, Order, InventoryItem).
* Business logic (such as data processing rules, calculations, data manipulation).
* Validation logic (for example, checks that are performed before saving data).
* Data Access: Mechanisms for persistence, such as interactions with databases, file systems, or web services.

In PatternFX, it is supposed the Model is reusable and interchangeable. The same `Model` can be used with different
presentation patterns without modification. The fundamental principle is that the `Model` never references the
`View` or presentation logic. It has no awareness of how its data is displayed. This strict isolation makes the
application core robust, easily testable, and independent of the UI layer.

### MVP Pattern <a name="patterns-mvp"></a>

MVP (`Model`-`View`-`Presenter`) is an architectural pattern that separates an application's logic into three main parts:
`Model`, `View`, and `Presenter`.

`View` — represents the user interface and is responsible for rendering UI elements and forwarding user interactions
to the `Presenter`. In MVP, the `View` is intentionally kept passive: it does not contain business logic or decision-making
code. Instead, it exposes a `View` interface that defines what can be displayed or updated. The concrete UI implementation
(JavaFX nodes, layouts, controls) remains hidden behind this interface.

`Presenter` — contains the presentation and interaction logic. It reacts to user events forwarded by the `View`,
coordinates work with the `Model`, and updates the `View` by invoking methods on the `View` interface. The `Presenter`
does not know anything about concrete UI controls; it operates purely on abstractions. This makes the `Presenter`
independent of the UI toolkit and straightforward to test.

`Model` — represents the application’s data and business logic. As in other patterns, the `Model` is completely
independent of the UI and does not reference either the `View` or the `Presenter`.

A key characteristic of MVP is that **all interaction logic flows through the `Presenter`**. The `View` never updates
itself based on internal decisions, and the `Model` never directly influences the UI. This creates an explicit and
predictable control flow:

User Action → View → Presenter → Model → Presenter → View

In JavaFX, MVP aligns naturally with imperative UI updates. Instead of expressing UI behavior through declarative state
and bindings, the `Presenter` can directly instruct the `View` to perform concrete actions, such as focusing controls,
scrolling, opening dialogs, or updating selections.

#### MVP Pattern Advantages <a name="patterns-mvp-advantages"></a>

* Clear separation of responsibilities. The `View` is responsible only for rendering and event forwarding, the
`Presenter` handles interaction logic, and the `Model` encapsulates business rules and data.
* High testability of interaction logic. The `Presenter` can be tested independently by mocking the `View` interface,
allowing verification of complex UI behavior without creating JavaFX components.
* No duplication of UI state. Unlike MVVM, MVP does not require mirroring JavaFX control state in a separate abstraction.
The `Presenter` can directly instruct the `View` to update itself.
* Well suited for interaction-heavy UIs. MVP handles algorithmic and procedural interaction logic naturally, such as
navigation, focus management, incremental search, step-based workflows, and complex editing scenarios.
* Explicit control flow. UI updates occur through explicit method calls, making behavior easier to trace, reason about,
and debug.

#### MVP Pattern Disadvantages <a name="patterns-mvp-disadvantages"></a>

* More verbose interaction code. Because UI updates are performed explicitly, the `Presenter` often contains more
boilerplate compared to declarative approaches.
* Tighter coupling to the `View` interface. Although the `Presenter` does not depend on concrete UI controls, it is still
strongly coupled to the shape of the `View` interface, which must be carefully designed and maintained.
* Limited support for declarative state. MVP does not naturally express UI as a pure projection of state. For
data-driven screens with simple behavior, this may result in more code than necessary.
* Manual synchronization responsibility. The developer must ensure that the `Presenter` keeps the `View` consistent
with the underlying state, as there is no automatic binding mechanism.

### MVVM Pattern <a name="patterns-mvvm"></a>

MVVM (`Model`-`View`-`ViewModel`) is an architectural pattern that divides an application's logic into three main parts:
`Model`, `View`, and `ViewModel`.

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

It is worth noting that there are two common interpretations of what the `ViewModel` should hold. In the classical
definition, rooted in the origins of the pattern, the `ViewModel` is a `Presentation Model` — a wrapper around the
domain model whose primary purpose is to maintain `View` state that does not belong to the domain. In this
interpretation, the `ViewModel` explicitly holds properties like `buttonEnabled` or `isLoading`, mirroring the state
of UI elements in an abstract, UI-toolkit-agnostic form. In the alternative interpretation, the `ViewModel` operates
in business terms — exposing properties like `orderValid` or `documentDirty` — and leaves the mapping to concrete
UI state as the responsibility of the `View`. Both approaches are valid, but they imply different distributions of
responsibility between the `View` and the `ViewModel`.

To simplify working with the `ViewModel`, you can use the [StateFX](https://github.com/techsenger/statefx)
library, which allows creating JavaFX node states through composition using interfaces.

`Model` — represents the application’s data and business logic. As in other patterns, the `Model` is completely
independent of the UI and does not reference either the `View` or the `Presenter`.

#### MVVM Pattern Advantages <a name="patterns-mvvm-advantages"></a>

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

#### MVVM Pattern Disadvantages <a name="patterns-mvvm-disadvantages"></a>

* The need to mirror UI state in the `ViewModel`. JavaFX nodes already contain their own state (properties like selected,
disabled, and text). MVVM requires creating parallel state in the `ViewModel` and synchronizing it through data binding.
This creates redundancy: the same state exists in two places—natively in the `View`'s nodes and explicitly in the
`ViewModel`'s properties. It is important to note that this state must be explicitly exposed in the `ViewModel`
due to MVVM's architecture, and is not required for any other purpose in JavaFX (unlike mobile application development,
where it may be necessary to store state separately from the `View`). To illustrate, imagine you have a
`ToggleButton` in the `View`. Then, in the `ViewModel`, you might have 3 properties (disable, selected, text) and
9 methods (3 property accessors, 3 setters, 3 getters).
* Some changes to the `View` are difficult to propagate through state. This occurs when JavaFX provides only read-only
properties or special methods for performing actions, or when using controls from third-party libraries.

### MVC vs MVP vs MVVM <a name="patterns-mvc-mvp-mvvm"></a>

Each of the discussed patterns has its own strengths and weaknesses, and the choice of architecture should be
driven by the project’s requirements and the developer’s preferences.

MVC provides maximum control and is very simple to implement, but it has a serious drawback — MVC tends to mix
presentation and interaction logic inside the Controller, which often leads to tightly coupled and harder-to-test code.
For this reason, the following analysis will focus only on MVP and MVVM.

MVP and MVVM are similar in that both patterns introduce an explicit representation of UI state outside of the `View`,
unlike MVC. In MVP, the state is stored in the `Presenter`, while in MVVM, the state is stored in the `ViewModel`.
It is worth noting that this characteristic makes both patterns significantly more complex compared to MVC.

MVVM naturally aligns with declarative UI frameworks and state-based rendering models, which makes it particularly well
suited for CRUD-style screens where the UI is a deterministic projection of state. However, this advantage is
limited to scenarios where the UI behavior is state-driven.

When the interaction model becomes algorithm-driven — for example:
- searching and navigating through a document,
- stepping through search results,
- managing focus, selection, scrolling, or cursor movement,
- handling multi-step or temporal interactions,

the logic no longer maps naturally to state. Attempting to express such behavior purely through `ViewModel` state
often results in complex derived properties, numerous listeners, and implicit control flow that is difficult to
reason about and debug.

MVP, on the other hand, allows interaction-heavy and algorithmic logic. So, it remains effective in scenarios where
UI behavior cannot be naturally modeled as state.

From a testability perspective, MVVM has an advantage over MVP because the `ViewModel` is an ideal unit for testing.
In contrast, in MVP you need to mock the `View` in every `Presenter` test, which introduces boilerplate.

Conceptually, MVVM is centered around modeling UI as a projection of state, while MVP models UI behavior as an explicit
sequence of interactions. The more deterministic and state-driven the UI is, the more natural MVVM becomes. The
more procedural and interaction-driven it is, the more natural MVP becomes.

Thus, when choosing between MVP and MVVM, it is also important to consider the nature of the application: MVVM may
be more suitable for primarily data-driven interfaces (e.g., forms and dashboards), while MVP often fits better
for action-driven scenarios (e.g., navigation and complex editing tools).

## Templates <a name="templates"></a>

In PatternFX, a template is a complete, opinionated implementation of an architectural pattern adapted for JavaFX
applications. A template defines:

* The set of architectural roles involved (e.g., `View`, `Presenter`, `ViewModel`).
* The responsibilities and constraints of each role.
* The allowed communication paths between components.
* The component lifecycle and integration with the PatternFX application model

A template is not a conceptual guideline or a loose recommendation. It is a fully functional architectural
infrastructure that provides all required base classes, interfaces, and runtime behavior needed to build a
non-trivial application.

Each template enforces a single architectural pattern consistently across the entire application. Mixing multiple
templates within the same application is explicitly unsupported, as it would break the assumptions and guarantees
made by the template.

By selecting a template, the application commits to a specific structure, interaction model, and lifecycle semantics.
This allows PatternFX to provide predictable behavior, strong architectural boundaries, and consistent composition
rules while still supporting dynamic UI assembly.

### Component <a name="templates-component"></a>

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

#### Component Descriptor<a name="templates-component-descriptor"></a>

The `Descriptor` represents the internal metadata and platform-level state of a component. It acts as a
technical identity card, containing all framework-related information while keeping it completely separate
from business data. In other words, the purpose of this class is to ensure that internal component data does not mix
with business-related data handled by the `Controller`, `Presenter`, `ViewModel` depending on the template.

#### Component Lifecycle<a name="templates-component-lifecycle"></a>

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

#### Component Logging <a name="templates-component-logging"></a>

PatternFX supports component-scoped logging, allowing log messages to be produced in the context of a specific
component instance rather than only at the class or subsystem level. This approach is especially useful in complex and
dynamic applications where multiple instances of the same component type may exist simultaneously (for example, tabs,
dialogs, editors, or background components). Component-scoped logging makes it possible to precisely identify the
exact source of a log message and greatly simplifies debugging and diagnostics.

Each component exposes a log prefix that uniquely identifies its instance. This prefix is provided by the `Descriptor`.

#### Component Types<a name="templates-component-types"></a>

Each template in the framework provides base classes and interfaces for creating three types of components, which
form a hierarchy of inheritance and composition:

* **Base Component** — the fundamental implementation of the selected architectural pattern. This is the simplest and
“purest” component type, providing only minimal functionality: lifecycle management and interaction between the core
elements of the pattern. Base components do not support parent–child relationships and therefore cannot participate
in a tree-like composition. They are intended for isolated, self-contained windows or dialogs.

* **Parent Component** — extends the base component by adding the ability to act as a container for child components.
This type is responsible for creating children, managing their lifecycle, and composing them. It is typically used
for composite screens, forming the root of a component tree.

* **Child Component** — extends the parent component by adding a reference to its parent and full integration into the
composition tree. This is the most powerful and feature-rich component type, capable of participating in complex
scenarios such as being dynamically added to or removed from the hierarchy. It is used to implement reusable,
nested UI building blocks (for example, toolbars, forms, or widgets).

This three-level system allows developers to flexibly choose the appropriate level of component complexity depending
on its role — from a simple isolated dialog (Base Component) to a complex reusable control embedded into the
overall application structure (Child Component).

#### Component Tree <a name="templates-component-tree"></a>

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

#### Component Composer <a name="templates-component-composer"></a>

The `Composer` serves two primary purposes:

1. To externalize and centralize component composition logic in all supported patterns (MVC, MVP, MVVM).
2. To enforce strict separation of layers in patterns where presentation logic must not depend on the `View` (MVP, MVVM).

The `Composer` acts as a dedicated composition layer placed above the `View`. Its purpose is to isolate component
creation and structural composition logic from the primary responsibilities of the architectural pattern in use.

It is important to note that the `Composer` is responsible for adding, managing, and removing two types of components:

1. Child components — those that reside directly inside the current component.
2. Derived components — those that are created by this component but attached elsewhere, such as dialogs, tabs, popups, etc.

In MVC, the `Controller` may access the `View`, so there is no strict architectural restriction preventing it from
creating or composing components directly. However, introducing a `Composer` still provides a significant structural
benefit: it centralizes and isolates component composition logic. This prevents the `View` (and optionally the
`Controller`) from accumulating orchestration responsibilities that do not belong to their primary roles.

In MVP, the `Presenter` must not depend on the `View`. At the same time, it may need to initiate the creation
of new components (for example, opening a dialog) and manage their composition. Since component creation ultimately
involves view-level constructs, doing this directly would violate the separation rules of MVP. The `Composer`
resolves this by acting as an abstraction layer: the `Presenter` interacts only with the `Composer` interface, while
the concrete implementation performs the necessary view-related operations.

In MVVM, the `ViewModel` must not depend on the `View`. However, it may still need to create and compose
components. The `Composer` provides the required abstraction: the `ViewModel` works only with the `Composer`
interface, while the implementation handles view-specific logic. This preserves the separation between presentation
logic and UI constructs.

The `Composer` provides the following advantages:

- Structural Clarity. Establishes a consistent and explicit mechanism for managing child and derived components.
- Separation of Concerns. Keeps `View`, `Controller`, `Presenter`, and `ViewModel` focused on their primary responsibilities.
- Improved Testability. Allows composition logic to be tested independently.
- Architectural Consistency. Provides a uniform composition model across all supported templates.

#### Imperative Component Management<a name="templates-component-management"></a>

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

#### Component History <a name="templates-component-history"></a>

`History` preserves the component’s state across its lifecycle. In the default implementation, the `History`
instance is lazily provided via a `HistoryProvider` that is set before initialization. During the initialization phase,
the provider’s `provide()` method is called to obtain the history. This allows the provider to be overridden in
subclasses without retrieving a history instance, which may be an expensive operation. After the history is obtained,
the provider is cleared (set to null), and the component uses the history. State restoration occurs in the
deinitialization phase. The volume and type of state information that is restored and persisted are determined
by the `HistoryPolicy` enum.

#### When to Create a Component? <a name="templates-component-when-to-create"></a>
* The element has independent testable state or business logic that can exist without a `View`.
* The element has a distinct lifecycle requiring separate initialization/deinitialization, or can be dynamically
added/removed.
* The element is potentially reusable across different contexts (e.g., dialogs, toolbars, multiple editor types).
* Multiple closely related properties form a logical unit - grouping them into a separate component improves
maintainability and reduces parent component complexity.
* The element manages structural composition - it contains child components or forms an independent subtree
(e.g., containers, tabs, panels).
* State persistence is required - the element needs its own `History` to save and restore state between sessions.

#### When not to Create a Component? <a name="templates-component-when-not-to-create"></a>
* The element’s `ViewModel` would contain no meaningful behavior or data - making the component redundant.
* The element represents a minor visual part of the interface and does not require its own logic or state.
* The element is simple enough that separating it into its own component would add unnecessary complexity rather
than improving clarity.

### MVP Template <a name="templates-mvp"></a>

<img width="1209" height="533" alt="PatternFX MVP" src="https://github.com/user-attachments/assets/663115e8-5702-4195-bc88-77e3ea3f4b9f" />

In this template, additional responsibilities are distributed between the `View` and `Presenter`. For responsibilities
that cannot be performed without creating an additional element, a `Composer` is introduced:

| **Task**                                              | **Responsible**             |
|-------------------------------------------------------|-----------------------------|
| Storing component metadata and state                  | `Descriptor` in `Presenter` |
| Managing the component lifecycle                      | `Presenter`                 |
| Creating and removing components                      | `Composer`, View.Composer   |
| Composing and decomposing components                  | `Composer`, View.Composer   |
| Maintaining references to parent and child components | `View` / `Composer`         |
| Representing the node in the "component" tree         | `View`                      |

As you can see the `View` and `Presenter` start to accumulate logic that does not traditionally belong to
them within a classic MVP template. The `View`, which should operate solely at the JavaFX node level, begins to
maintain references to parent and child components, and also holds a `Composer` in its structure. As for the
`Presenter`, it gains a descriptor and handles component initialization.

#### MVP Component Structure <a name="templates-mvp-structure"></a>

A component  consists of the following classes: a `View`, `FxView`, `Presenter`. In addition to them, a component
always has a `Descriptor` (which is provided by the framework and normally does not require custom implementation)
and may include `Composer`, `Port` and a `History` classes.

`Port` is an interface with its implementation supplied by a nested, non-static class in `Presenter`.
It represents an explicit communication channel between presenters.

This interface is introduced to achieve the following objectives:

* To maintain component encapsulation by avoiding direct presenter-to-presenter references.
* To establish a well-defined and strictly controlled interaction boundary between a presenter and its external
environment.

It is important to note that `Presenter` instances should never be shared directly between components. A `Port` is the
only intended mechanism for inter-presenter communication.

A `Presenter` exposes one primary `Port` via `presenter.getPort()`. This `Port` extends the primary `Port`s of all child
`Presenters`, providing a unified access point to the entire component subtree. Additionally, a `Presenter` may define
any number of secondary `Port`s, which can be used, for example, to establish bidirectional communication channels
between components.

#### MVP Component Lifecycle <a name="templates-mvp-lifecycle"></a>

Each `Presenter` provides the `Presenter#initialize()` and `Presenter#deinitialize()` methods, which initialize and
deinitialize all parts of the component respectively, updating its state.

The default implementation of these methods in `AbstractPresenter` is based on the Template Method pattern. Initialization
and deinitialization are divided into three phases.

The first phase consists of invoking the protected methods `preInitialize()` / `preDeinitialize()`, which may be
overridden. The second phase is strictly fixed and performs the core initialization and deinitialization logic.
The third phase consists of invoking the protected methods `postInitialize()` / `postDeinitialize()`, which may also
be overridden.

The second phase is the most important one. During this phase, the `View` is initialized and deinitialized via
calls to the methods `View#initialize()` and `View#deinitialize()`. During the second phase, the `AbstractFxView`
is initialized and deinitialized by invoking four protected methods that perform the core `FxView` operations. These
protected methods may be overridden and are responsible for the following:

- building/unbuilding
- binding/unbinding
- adding/removing listeners
- adding/removing handlers

It is important to note that these protected methods should not be considered the only place for performing such tasks
(e.g., adding or removing handlers) within the `FxView`; rather, they represent one part of the
initialization/deinitialization process. Thus, such tasks may also be performed in other methods.

The `Composer` is created and assigned to the `AbstractParentPresenter` when the `AbstractParentFxView#setPresenter()`
method is called.

#### MVP Component Example<a name="templates-mvp-example"></a>

`Composer` interface:

```java
public interface FooComposer extends ParentComposer {

    void addBar();

    BarPort getBar();
    ...
}
```

`View` interface:

```java
public interface FooView extends ParentView {
   ...
}
```

`Presenter` class:

```java
public class FooPresenter<V extends FooView, C extends FooComposer> extends AbstractParentPresenter<V, C> {

    public FooPresenter(V view) {
        super(view);
    }

    public void onAction() {
        getComposer().addBar();
        // use bar
    }

    ...
}
```

`FxView` class:

```java
public class FooFxView<P extends FooPresenter<?, ?>> extends AbstractParentFxView<P> implements FooView {

    protected class Composer extends AbstractParentFxView<P>.Composer  implements FooComposer {

        @Override
        public void addBar() {
            bar = new BarFxView();
            var p = new BarPresenter(bar);
            p.initialize();
            getModifiableChildren().add(bar);
            someNode.getChildren().add(bar.getNode()); // adding bar view into foo view
        }

        @Override
        public BarPort getBar() {
            if (bar != null) {
                return bar.getPresenter().getPort();
            } else {
                return null;
            }
        }
    }

    private BarFxView bar;

    public FooFxView() {
        ...
    }

    @Override
    public void initialize() {
        super.initialize();
        logger.debug("{} View is initializing", getDescriptor().getLogPrefix());
    }

    @Override
    protected Composer createComposer() {
        return new Composer();
    }
    ...
}
```
This code demonstrates how to create the foo component instance:

```java
var view = new FooFxView<>();
var presenter = new FooPresenter<>(view);
presenter.initialize();
... // use the component
presenter.deinitialize();
```

### MVVM Template <a name="templates-mvvm"></a>

<img width="1211" height="537" alt="PatternFX MVVM" src="https://github.com/user-attachments/assets/72e4465b-dac7-4320-92ed-247f7a062068" />

In this template, additional responsibilities are distributed between the `View` and `ViewModel`. For responsibilities
that cannot be performed without creating an additional element, a `Composer` is introduced:

| **Task**                                              | **Responsible**             |
|-------------------------------------------------------|-----------------------------|
| Storing component metadata and state                  | `Descriptor` in `ViewModel` |
| Managing the component lifecycle                      | `View`                      |
| Creating and removing components                      | `Composer`, View.Composer   |
| Composing and decomposing components                  | `Composer`, View.Composer   |
| Maintaining references to parent and child components | `View` / `ViewModel`        |
| Representing the node in the "component" tree         | `View`                      |

Thus, we can see that the `View` and `ViewModel` start to accumulate logic that does not traditionally belong to
them within a classic MVVM template. The `View`, which should operate solely at the JavaFX node level, begins to
handle component initialization, maintain references to parent and child components, and also holds a `Composer`
in its structure. As for the `ViewModel`, it gains a descriptor and stores references to parent and child components.

#### MVVM Component Structure <a name="templates-mvvm-structure"></a>

A component  consists of the following classes: a `View` and a `ViewModel`. In addition to them, a component
always has a `Descriptor` (which is provided by the framework and normally does not require custom implementation)
and may include a `Composer` and a `History` classes.

#### MVVM Component Lifecycle <a name="templates-mvvm-lifecycle"></a>

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

#### MVVM Component Example<a name="templates-mvvm-example"></a>

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

## Limitations <a name="limitations"></a>

* JavaFX uses strict CSS selectors for `ToolBar` such as `.tool-bar > .container > .button` that only apply styles to
direct children of the toolbar's internal container. When toolbar items are wrapped in another container (HBox, VBox,
etc.), default control styles no longer apply. Therefore, when working with `ToolBar`, it is recommended to create a
single dedicated component without nested components to avoid duplicating styles for toolbar items.

## Requirements <a name="requirements"></a>

Java 11+ and JavaFX 19.

## Dependencies <a name="dependencies"></a>

This project will be available on Maven Central in a few weeks.

For MVP template:

```
<dependency>
    <groupId>com.techsenger.patternfx</groupId>
    <artifactId>patternfx-core</artifactId>
    <version>${patternfx.version}</version>
</dependency>
<dependency>
    <groupId>com.techsenger.patternfx</groupId>
    <artifactId>patternfx-mvp</artifactId>
    <version>${patternfx.version}</version>
</dependency>
```

For MVVM template:

```
<dependency>
    <groupId>com.techsenger.patternfx</groupId>
    <artifactId>patternfx-core</artifactId>
    <version>${patternfx.version}</version>
</dependency>
<dependency>
    <groupId>com.techsenger.patternfx</groupId>
    <artifactId>patternfx-mvvm</artifactId>
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
