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


package$ is already defined as package object package

//When compiling 
