Macro definition and expansion in same compilation run can cause name collisions with toplevel definitions

If a macro is expanded by a toplevel function and at another place in the same
package but in a different file, and if the macro is defined in the same
compilation run, a name collision appears.

Consider the following example. Assume these 3 files:

- the macro definition itself, `Macro.scala`:
  
  ```
  object Macro {
    inline def expand(): Unit = ${impl}
    def impl(using scala.quoted.QuoteContext) = '{???}
  }
  ```

- the first expansion site, `Clazz.scala`:

  ```
  class Clazz {
    def foo = Macro.expand()
  }
  ```

- the second expansion site, `toplevel.scala`:

  ```
  def fct: Unit = Macro.expand()
  ```

Compiling all three files at once will produce the following error.

`dotc Macro.scala Clazz.scala toplevel.scala`:

```
-- Error: toplevel.scala:1:0 ---------------------------------------------------
1 |def fct: Unit = Macro.expand()
  |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  |toplevel$package$ is already defined as package object toplevel$package
1 error found
```

Some notes:

- compiling them separately works as intended:
  
  ```
  dotc Macro.scala && dotc Clazz.scala toplevel.scala # OK
  dotc Macro.scala Clazz.scala && dotc toplevel.scala # OK
  dotc Macro.scala toplevel.scala && dotc Clazz.scala # OK
  ```

- `Clazz` and `fct` must be defined in the same package but in different files,
  for this bug to manifest itself.

- the other expansion site doesn't have to be in a class. It can very well be
  another toplevel function in a different file. The issue will manifest itself
  as long as expansions are in different files but in the same package and at
  least one of them is a toplevel definition.
