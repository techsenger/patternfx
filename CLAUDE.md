# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PatternFX is a compact, component-oriented, modular framework providing architectural pattern templates
(MVC, MVP, and MVVM) for building JavaFX applications. Each template is a complete implementation of one pattern,
including lifecycle management, dynamic composition, and component history — not just a conceptual guideline.
Mixing templates within a single application is unsupported by design. Full conceptual documentation (pattern
comparisons, when to create a component, complete worked examples) lives in `README.md` — read it before making
non-trivial design changes to `patternfx-mvc`, `patternfx-mvp`, or `patternfx-mvvm`, since the class
responsibilities are only fully explained there.

## Language

Everything in the project is written in English — README, documentation, Javadoc, code comments, commit
messages, etc. Always — regardless of what language the conversation with the assistant happens in.

## Scope of work

It is strictly forbidden to operate outside the project directory. This applies absolutely, to every kind
of operation — including read-only ones like `find` or search/grep — not just edits.

The only exception: the developer may explicitly name a directory outside the project directory for a
specific task. In that case, work must stay confined to the project directory plus exactly the
directory/directories the developer named — nothing else. Proactively suggesting or using any other
outside directory on your own is strictly forbidden.

If some source you need isn't in an allowed directory, ask the developer where to find it — never search
the rest of the machine for it on your own.

## Module Layout

Reactor build with five Maven modules (Java 25 / JavaFX 25, uses the Java module system):

- **patternfx-core** (`com.techsenger.patternfx.core`) — pattern-agnostic building blocks shared by all
  templates: `AbstractDescriptor`/`ObservableDescriptor` (component metadata/state, kept separate from
  business data), `ComponentState` (CREATING → INITIALIZING → INITIALIZED → DEINITIALIZING → DEINITIALIZED),
  `AbstractComponentHistory`/`HistoryProvider` (lazy, per-component state persistence),
  `ComponentName`/`ComponentGroup`, and `TreeIterator` (breadth/depth-first traversal of the component tree).
- **patternfx-mvc** (`com.techsenger.patternfx.mvc`, depends on core) — the MVC template. Controller owns the
  `Descriptor` and lifecycle, exactly like MVP's Presenter, but is expected to hold a direct reference to the
  concrete `View` and manipulate it imperatively rather than through a passive interface — so, unlike MVP, there
  is no separate toolkit-agnostic `View` layer: `View`/`ParentView`/`ChildView` are JavaFX-based from the start.
  `Composer` centralizes child/derived component creation, same as in the other templates, but since the
  `Controller` already depends on the concrete `View`, a component's `Composer` doesn't need its own dedicated
  interface either — it can be a plain concrete inner class. Type hierarchy: `Controller` → `ParentController`
  (+ `ParentPort`) → `ChildController` (+ `ChildPort`), with `AbstractController` → `AbstractParentController` →
  `AbstractChildController` as the base implementations, mirrored on the view side by `View` → `ParentView` →
  `ChildView`, implemented by `AbstractView` → `AbstractParentView` → `AbstractChildView`. `Controller` instances
  are never shared directly between components — cross-component communication goes through a `Port`.
- **patternfx-mvp** (`com.techsenger.patternfx.mvp`, depends on core) — the MVP template. Presenter owns the
  `Descriptor` and lifecycle; `View`/`FxView` are passive; `Composer` centralizes child/derived component
  creation. Type hierarchy: `Presenter` → `ParentPresenter` (+ `ParentPort`) → `ChildPresenter` (+ `ChildPort`),
  with `AbstractPresenter` → `AbstractParentPresenter` → `AbstractChildPresenter` as the base implementations.
  Mirrored on the view side: `View` → `FxView` → `ParentFxView` → `ChildFxView`, implemented by
  `AbstractView` → `AbstractFxView` → `AbstractParentFxView` → `AbstractChildFxView`. `Presenter` instances are
  never shared directly between components — cross-component communication goes through a `Port`.
- **patternfx-mvvm** (`com.techsenger.patternfx.mvvm`, depends on core) — the MVVM template. `ViewModel` owns
  the `Descriptor`; `View` drives the lifecycle and binds declaratively to `ViewModel` state (see
  `BindingUtils`); `Composer` again centralizes composition. Hierarchy: `ViewModel` → `ParentViewModel` →
  `ChildViewModel`, with `AbstractViewModel` → `AbstractParentViewModel` → `AbstractChildViewModel`. Views:
  `View` → `ParentView` → `ChildView`, with `AbstractView` → `AbstractParentView` → `AbstractChildView`.
- **patternfx-demo** (`com.techsenger.patternfx.demo`, depends on core + mvc + mvp + mvvm) — runnable JavaFX app
  demonstrating all three templates side by side (`demo.mvc`, `demo.mvp`, and `demo.mvvm` packages), each with an
  equivalent Registry/Report/Dialog component set built from the same `demo.model`
  (Person/PersonService/PersonValidator). Entry point is `Demo`, which delegates to `MvcRunner`, `MvpRunner`, or
  `MvvmRunner`.

In all templates, base `Component`/`Controller`/`Presenter`/`ViewModel` types are for isolated windows/dialogs
with no tree participation; `Parent*` types add child-management; `Child*` types add a parent reference and full
composition-tree integration. All templates enforce the Unidirectional Hierarchy Rule: no circular parent/child
relationships in the component tree (which is a logical ownership tree, independent of the JavaFX scene graph).

Initialization/deinitialization in both templates follows a fixed three-phase template method:
`preInitialize()`/`preDeinitialize()` (overridable) → fixed core phase (build/unbuild, bind/unbind, add/remove
listeners, add/remove handlers) → `postInitialize()`/`postDeinitialize()` (overridable).

Null-safety is enforced at compile time: every package is `@NullMarked` (from `com.techsenger.annotations`),
and the build fails (`-Werror`) on NullAway violations.

## Commands

Full build — compiles, runs unit + integration tests, and checkstyle (from repo root):
```
mvn clean install
```

Unit tests only, no checkstyle/integration tests:
```
mvn clean install -P unit-tests
```

Run a single test class or method (surefire):
```
mvn -pl patternfx-core test -Dtest=SomeTestClass
mvn -pl patternfx-core test -Dtest=SomeTestClass#someMethod
```
(swap `patternfx-core` for `patternfx-mvc`/`patternfx-mvp`/`patternfx-mvvm`/`patternfx-demo` as needed; `-pl` targets a
single module in this multi-module reactor.)

Checkstyle only:
```
mvn checkstyle:check
```

Run the demo app (from `patternfx-demo/`; debugger settings live in that module's `pom.xml`):
```
cd patternfx-demo
mvn javafx:run
```

Note: there are currently no test sources under any module's `src/test/java` — `src/test` directories exist
but are empty.

## Build Configuration Notes

- The parent POM (`maven-root`, an external artifact resolved from Maven Central / the project's Repsy
  snapshot repo) supplies the checkstyle config, compiler flags, and profiles (`default`, `unit-tests`,
  `integration-tests`, `release`, `snapshot`) — it is not part of this repo.
  javac is invoked with `-Werror` and the ErrorProne + NullAway annotation processors; compilation fails on
  raw-type/unchecked warnings and on nullability violations.
- `patternfx-demo` is excluded from publishing (`publishing.plugin.exclusions`) since it's example code, not
  a library artifact.

## Architecture: ViewModel state

The **ViewModel is the source of truth** for a component's state, exposed as JavaFX properties that the View
binds to directly. Every ViewModel property falls into one of three shapes:

1. **Read and written from outside, fully owned by the ViewModel** (nothing else can change it
independently): a plain `Property`, exposed as three public methods — `xProperty()`, `getX()`, `setX()`:

```java
// ViewModel
private final ObjectProperty<Foo> foo = new SimpleObjectProperty<>();

public ObjectProperty<Foo> fooProperty() {
    return this.foo;
}

public Foo getFoo() {
    return this.foo.get();
}

public void setFoo(Foo foo) {
    this.foo.set(foo);
}
```

2. **Read only from outside, fully owned by the ViewModel:** a `ReadOnlyObjectWrapper` backing field,
exposed as two public methods — `xProperty()`, `getX()`:

```java
// ViewModel
private final ReadOnlyObjectWrapper<Foo> foo = new ReadOnlyObjectWrapper<>();

public ReadOnlyObjectProperty<Foo> fooProperty() {
    return this.foo.getReadOnlyProperty();
}

public Foo getFoo() {
    return this.foo.get();
}

ReadOnlyObjectWrapper<Foo> fooWrapper() {
    return this.foo;
}

```

3. **Read only from outside, with a request setter — for state a View or the platform owns, not the
ViewModel (the `wrapper`/`source` naming pattern, its setter marked `@RequestSetter`).** Some ViewModel
state isn't decided by the ViewModel itself — it's decided by the real widget or the OS (a window's
width/height/x/y, maximized/minimized, a `TableView`'s selection) — and the *requested* value and the
*actual* value can legitimately diverge (the platform clamps, ignores, or rejects the request). This is why
real JavaFX's own `Window#widthProperty()` is `ReadOnlyDoubleProperty` with a separate best-effort
`setWidth(double)`, not a plain `DoubleProperty`. For this kind of state:

- Expose it as a `ReadOnly*Property` on the Port/ViewModel — never a plain writable `Property`. A writable
  property lets a caller write an optimistic value straight in; if the platform then silently rejects the
  request (no compensating change event fires, because nothing about the real state actually changed), that
  optimistic value sits there being wrong forever — a "lying property," which is worse than the
  two-way-binding reentrancy problem it might look like it's avoiding.
- Back it with a `ReadOnly*Wrapper` field, and add a package-private `xWrapper()` accessor — documented as a
  framework contract ("written directly by the View... direct invocation by user code results in undefined
  behavior") — that the paired View `.bind()`s straight to the real widget/`Stage` property. When there is no
  independent widget truth to bind to at all (confirmed only asynchronously, e.g. by a manager after it
  finishes an operation), the View instead writes the wrapper directly, once, at the point the real outcome
  is known.
- Give the public setter (`setWidth`, `setSelectedItem`, ...) a matching package-private `xSource()`
  accessor returning an `ObservableSource<T>`. The setter only calls `xSource.next(value)` — it never touches
  the wrapper itself. The View subscribes to `xSource()` and performs the real widget/platform call; the
  outcome flows back solely through the wrapper. This keeps exactly one writer per property in each
  direction, so there's no reentrancy/cycle to guard against and no way to bypass whatever side effect the
  request needs to trigger. Annotate the setter itself with `@RequestSetter`, marking it as best-effort rather
  than a guaranteed state change.
- Don't reach for `wrapper`/`source` for state the ViewModel fully owns itself, even if its setter carries a
  side effect or validation (a derived flag to update, a check that throws) — as long as the setter is the
  *only* way in, a plain `Property` is correct and a `ReadOnly` wrapper only makes the API harder to use for
  no safety benefit. Reserve `wrapper`/`source` for state a second, independent party (the View, the
  platform) can also change out from under the ViewModel.
- **Naming applies uniformly, with or without a paired wrapper.** A command source with no matching
  `ReadOnly*Wrapper` at all (e.g. a ViewModel unconditionally telling the View "re-render this now," with no
  independent "actual" value to diverge from) is still named `<action>Source`, with a same-named accessor
  `<action>Source()` — never `request<Verb>`/`getRequest<Verb>()`.

```java
// ViewModel
private final ReadOnlyObjectWrapper<Item> selectedItem = new ReadOnlyObjectWrapper<>();

private final ObservableSource<Item> selectedItemSource = new SimpleObservableSource<>();

public ReadOnlyObjectProperty<Item> selectedItemProperty() {
    return this.selectedItem.getReadOnlyProperty();
}

public Item getSelectedItem() {
    return this.selectedItem.get();
}

@RequestSetter
public void setSelectedItem(Item item) {
    this.selectedItemSource.next(item);
}

ReadOnlyObjectWrapper<Item> selectedItemWrapper() {
    return this.selectedItem;
}

ObservableSource<Item> selectedItemSource() {
    return this.selectedItemSource;
}
```

```java
// View
viewModel.selectedItemWrapper().bind(this.table.getSelectionModel().selectedItemProperty());
viewModel.selectedItemSource().addListener((item) -> {
    if (item == null) {
        this.table.getSelectionModel().clearSelection();
    } else {
        this.table.getSelectionModel().select(item);
    }
});
```

4. **Collections fully owned by the ViewModel:** an `ObservableList`/`ObservableSet`/`ObservableMap` follows
the same read-only-from-outside idea without needing a wrapper at all: keep a private modifiable collection,
expose an unmodifiable view over it to the View (`FXCollections.unmodifiableObservableList(...)`) — callers
observe structural changes directly — and, if a subclass needs to mutate the collection itself, expose the
modifiable collection to subclasses through a `protected` accessor rather than widening the public one.

## Nullability

Types use `com.techsenger.annotations.Nullable`/`@Unmodifiable`; NullAway is enforced at compile time
(`OnlyNullMarked` mode) — only annotate/guard packages that are explicitly null-marked, don't add blanket
null checks elsewhere.

## Member ordering

Not caught by Checkstyle (no `DeclarationOrder` module in the config) — must be applied by hand on every
edit, not just when writing new files.

Within a class/interface, members are sorted by three nested keys, each answering a different question and
breaking ties in the one before it:

1. **Scope — static vs. instance.** Does this member belong to the class or to the object? All static
   members form one block, placed entirely before all instance members. This is absolute — e.g. a
   `private static` method is placed above a `public` constructor, because scope outranks visibility.
2. **Role — types → fields → (constructors →) methods.** This is a dependency order, not an arbitrary
   bucket: nested types define the vocabulary that fields are declared with, fields hold the state that
   methods/constructors operate on. So within the static block: nested static types → static fields →
   static methods. Within the instance block: nested instance types → instance fields → constructors →
   instance methods.
3. **Visibility — `public` → `protected` → package-private → `private`.** Within one role (e.g. "instance
   methods"), the public contract comes before implementation detail.

So the full sequence in one class is: static nested types (public→private) → static fields
(public→private) → static methods (public→private) → nested instance types (public→private) → instance
fields (public→private) → constructors (public→private) → instance methods (public→private).

**Field grouping, within one role+visibility bucket.** The three criteria above leave ties: in a `Port` or
`ViewModel` interface/class, `getX()`/`isX()`, `setX()`, and `xProperty()` for the same field are normally
all `public` instance methods, so nothing above orders them relative to each other or to the next field's
trio. **When they share the same access modifier, group them by field** instead of batching all getters,
then all setters, then all property accessors as three separate blocks — `getX()` → `setX()` (if present) →
`xProperty()` (if present), then move on to the next field's trio:

```java
double getWidth();

ReadOnlyDoubleProperty widthProperty();

double getHeight();

ReadOnlyDoubleProperty heightProperty();
```

not

```java
double getWidth();

double getHeight();

ReadOnlyDoubleProperty widthProperty();

ReadOnlyDoubleProperty heightProperty();
```

Field order otherwise follows whichever order is already established (typically the backing fields'
declaration order in the concrete `XxxViewModel`).

If the field's methods don't share one access modifier — e.g. a `wrapper()`/`source()` pair is
package-private while `getX()`/`xProperty()` are public (see the `wrapper`/`source` pattern above), or a
setter is `protected` while its getter is `public` — visibility still wins: keep each method in its own
visibility block, don't pull a lower-visibility method up next to a public one just to keep the trio
together. The package-private `xWrapper()`/`xSource()` pair still groups with each other, just in the
package-private block further down the class, not next to the public `getX()`/`xProperty()`.

## Naming convention

A `Map`-typed field/parameter/local/getter/setter is named `<values>By<key>` (e.g. `Map<FileType, Boolean>
selectionsByType`, `getSelectionsByType()`/`setSelectionsByType(...)`), not `<key><values>` (e.g.
`typeSelections`). The `by`-form reads directly as "which value, keyed by which type of key" at the
declaration site, without having to look at the generic type arguments to tell which side is the key.

## Javadoc

Document the contract — what the member does and why a caller would use it — never how it's implemented.
If a sentence just narrates the method body in prose (the steps it performs, the fields it touches along
the way), that's implementation detail the reader can already see by opening the method; cut it. A reader
deciding whether/how to call the member should get what they need without opening its body: what it
returns or does, plus any non-obvious constraint, side effect, or precondition — not a walkthrough.

Target 120-240 characters for the main description (the summary sentence plus any `<p>` continuation)
only — this range does not apply to `@param`/`@return`/`@throws`/`@see`/`{@link}` tags at all. Under 120
characters a javadoc rarely earns its place over just reading the signature; over 240 it has usually
drifted into narrating implementation or into a multi-paragraph essay — shorten it, or split the method
instead of padding its doc further. Avoid `{@link}`/cross-references to other classes or methods where
possible, especially to types from a different module/dependency (patternfx, shellfx, toolkit) — those
references rot silently when the referenced API changes and are harder to notice/fix than one in the same
file.

Accessor methods (get, set, is, xxxProperty) — should not have Javadoc unless they provide information
that is not obvious from the method name and type.

Tags follow a different, simpler rule: keep each tag's description as short as it can be while still
saying what it needs to; the only hard limit on it is the 120-char line length itself (wrap per the
indentation rule below if one line isn't enough). There is no minimum length for a tag.

**Tag coverage.** On `public`/`protected` methods, document every element that appears in the signature —
each `@param`, every checked exception via `@throws`, and so on — as tersely as the 120-char line allows;
a missing tag reads as an oversight on API surface other code depends on. Add `@return` only when the
return value carries semantics beyond what the method name and return type already say — nullability, a
sentinel/special value, which object state it reflects, resource ownership, mutability, a specific format,
or a constraint on the value. Skip `@return` when it would just restate the method name and type (e.g.
`getName()` returning `String` needs no `@return the name`).

On package-private/private methods, add `@param`/`@throws`/etc. only when that specific tag is actually
worth calling out (a non-obvious constraint, a surprising exception) — omitting the routine ones is fine.

Each `@param`, `@return`, `@throws`, and similar tag starts on a new line. The first line holds the tag,
the parameter name (if any), and the start of the description. When the description doesn't fit the
120-char line limit, continue it on the following line(s) using a **fixed 4-space indent relative to the
leading `*`**, not aligned under the start of the description — a fixed indent stays correct regardless of
how long the tag/parameter name is, so it never needs re-indenting when a name changes length:

```java
/**
 * Resolves a file.
 *
 * @param path controls whether the operation should be performed in a
 *     lightweight mode. If {@code false}, additional validation is performed.
 * @param destination specifies the destination used to resolve relative
 *     paths and determine where the resulting files should be stored.
 * @return the resolved file.
 * @throws IOException if the file cannot be resolved.
 */
```

Do not write javadoc on an overriding (`@Override`) method — rule of thumb: the comment belongs only on
the interface method or the parent class method being overridden, not duplicated on every override.

## Code style (Checkstyle)

Checkstyle runs on every `mvn package`/`install` (see Commands above) via the `com.techsenger.checkstyle.config`
artifact (Sun checks-derived, `severity=error`) — a violation fails the build, not just a lint warning. Treat
every rule below as binding when writing or editing Java. `module-info.java` files are exempt from all of it.
Run just this check with `mvn checkstyle:check`, or skip it entirely with `-Dcheckstyle.plugin.skip=true`
or `-P unit-tests`/`-P integration-tests`.

- **Layout**: 120-char line limit, no tabs, no trailing whitespace, file must end with a newline, exactly
  one blank line between the license header and the `package` declaration, no more than 5000 lines/file.
- **Methods**: max 250 non-empty lines, max 8 parameters — both signal you should split the method/introduce
  a parameter object rather than push past them.
- **Imports**: no star imports, no unused imports, no redundant imports.
- **Naming**: standard Java conventions — `PascalCase` types, `camelCase` methods/fields/params/locals,
  `UPPER_SNAKE_CASE` for non-private constants (private constants are exempt, so `private static final` in
  `camelCase`/mixed case is fine).
- **Braces & blocks**: braces required on every `if`/`for`/`while`/etc. (no single-statement bodies without
  `{}`), no empty blocks, no nested blocks, standard left/right-curly placement.
- **Whitespace**: standard spacing around operators/generics/casts/parens (`GenericWhitespace`, `ParenPad`,
  `TypecastParenPad`, `WhitespaceAround`, `WhitespaceAfter`, `NoWhitespaceBefore`/`After`, `OperatorWrap`).
- **Coding**: no empty statements, `equals`/`hashCode` always overridden together, no assignments inside
  expressions (`InnerAssignment`), one variable declaration per statement (no `int a, b;`), simplify boolean
  expressions/returns (`return x == y;` not `if (x == y) return true; else return false;`).
- **Class design**: a class with only private constructors must be `final`; utility classes (only static
  members) must have a private constructor (matches the existing `private Foo() { // empty }` pattern
  already used throughout, e.g. `NavigatorFileIconProvider`); fields should be `private` with accessors
  (`VisibilityModifier`), not exposed directly.
- **Misc**: array brackets on the type, not the variable (`String[] args`, not `String args[]`); long
  literals use uppercase `L` (`100L`, not `100l`).
- **No `TODO` comments** (`TodoComment` module) — since severity is `error`, a matching comment fails the
  build. Don't add new ones; open a tracked issue or just do the work instead.
