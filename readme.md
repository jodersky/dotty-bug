# Steps to reproduce the bug

- dotc:

  ```
  dotc macroman/src/*.scala
  ```


- alternatively, this project is also set up to use mill:

  ```
  mill clean && mill macroman.compile
  ```

  (the clean is important, as partially compiling the project does not trigger the bug)





- happens only if toplevel is top-level (i.e. not in an object)


Simultanous macro expansion in top-level functions and class cause collisions

If a macro is expanded by a top-level function and a class within that package,
and if the macro is defined in the same project, a name collision appears.

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

- the second expansion site, `macroman.scala`:

  ```
  def toplevel: Unit = Macro.expand()
  ```


Expanding a macro *that is defined within the same project* 

package$ is already defined as package object package

//When compiling 
