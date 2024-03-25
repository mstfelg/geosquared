<picture>
    <img alt="GeoSquared Logo" src="./DOCS/assets/geo2.svg" height=100px />
</picture>

This is a fork of GeoGebra with aggressive refactoring aimed for Euclidean
Geometry.

Work in progress. Expect things to break.

## Features
1. Extra tool keybindings.
2. Extra geometry functions.
3. Removed unnecessary dependencies and code.
4. XDG base directory support.
5. Plain text/xml `.gsq` format that is easy to version-control.
6. Modular macros.

## Build
```sh
gradle :desktop:run
gradle :desktop:distTar
```
