Spigot project that adds netherite gear to piglins in generated bastion structures.
[Spigot thread](https://www.spigotmc.org/threads/piglin-not-spawning-with-armour-in-bastion.599235)

### Paper SLF4J logger settings

To enable SLF4J debugging you need to change Appender's logging level. You
can do it manually for your server's core or copy the `debug.xml` file if
you're using Paper/Purpur.

```xml
<Rewrite name="rewrite2">
    <ExtraClassInfoRewritePolicy />
    <AppenderRef ref="File"/>
    <!-- level="debug" is what you need -->
    <AppenderRef ref="TerminalConsole" level="debug"/>
    <AppenderRef ref="ServerGuiConsole" level="info"/>
</Rewrite>
```

### Build

```bash
# compiles & builds .jar file in build/libs/
gradle jar
```
