# Byow — Coinlight Caverns

![Pixel-art design board](assets/byow-pixel-design.png)

**Byow** is my individual implementation of Berkeley CS 61B's final project, Project 3: Build Your
Own World. It is a playful pixel dungeon where you generate a deterministic world, wander with WASD,
switch a wall-blocked line of sight with `B`, and collect every gold coin.

> **Solo project:** designed and implemented independently by me for Berkeley CS 61B's final Project 3.

## Highlights

- Primary ambition feature: toggleable line of sight with wall occlusion.
- Secondary ambition feature: coin collection, HUD progress, and return-to-menu completion flow.
- Deterministic seeded world generation with save/replay support.
- Pixel-art direction documented in [`docs/design.md`](docs/design.md).

## Controls

`N` new game · `L` load · `Q` quit/save · `WASD` move · `B` toggle sight · `C` enable coin mode.

## Run

Open the `proj3` folder in IntelliJ IDEA with the course's `algs4.jar` dependency configured, then
run `core.Main`. The source layout follows the CS 61B BYOW starter conventions.

## Project structure

```text
assets/                         pixel-art concept board
docs/design.md                  product and technical design
proj3/src/core/                 menus, world generation, gameplay
proj3/src/tileengine/           tiles, renderer, line-of-sight utility
```

This repository is a personal fork of the original CS 61B project and is intended for educational
use.
