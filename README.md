
### Necesse ExampleMod ported to Kotlin.
See: https://github.com/DrFair/ExampleMod

#### Notes
- ByteBuddy patches that <ins>reassign</ins> `@Advice.Return` parameters WILL NOT work due to Kotlin having
final (`val`) function parameters.
  - **Workaround**: [examples/ExampleMethodPatch.java](src/main/kotlin/examplemod/examples/ExampleMethodPatch.java).
  A Java file can coexist with Kotlin source files just fine,
  it gets compiled and behaves in its package as expected,
  so simply write the patches that need return values in Java.
- Probably other stuff (unlikely unless it deals with patches).
  Loading the mod works fine, and most equipments work.

---

An example mod for Necesse.

Check out the [modding wiki page](https://necessewiki.com/Modding) for more.
