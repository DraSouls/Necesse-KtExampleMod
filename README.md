
### Necesse ExampleMod ported to Kotlin.
See: https://github.com/DrFair/ExampleMod

#### Notes
- ByteBuddy patches that <ins>reassign</ins> `@Advice.Return` parameters might not work due to Kotlin having
final (`val`) function parameters. It is recommended to write game patches in Java instead.
  - Affected class: [ExampleMethodPatch](src/main/kotlin/examplemod/examples/ExampleMethodPatch.kt)
- Probably other stuff. The features are untested yet, but loading the mod works fine.

---

An example mod for Necesse.

Check out the [modding wiki page](https://necessewiki.com/Modding) for more.
